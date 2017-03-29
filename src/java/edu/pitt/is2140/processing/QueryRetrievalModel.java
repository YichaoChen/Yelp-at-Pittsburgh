package edu.pitt.is2140.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QueryRetrievalModel {

    protected MyIndexReader indexReader;

    private final int mu = 2000;
    private List<Integer> docLength;
    private int collectionLength = 0;
    private List<String> docno;

    public QueryRetrievalModel(MyIndexReader ixreader) throws IOException {
        indexReader = ixreader;
        int i = 0;
        this.docLength = new ArrayList<Integer>();
        this.docno = new ArrayList<String>();

        while (true) {
            try {
                this.docno.add(indexReader.getDocno(i));
            } catch (Exception e) {
                break;
            }

            try {
                int docLength = indexReader.docLength(i);

                this.docLength.add(new Integer(docLength));
                this.collectionLength += docLength;
            } catch (Exception e) {
                this.docLength.add(new Integer(0));
            }
            i++;
        }
    }

    public List<Integer> getDocLength() {
        return docLength;
    }

    public int getCollectionLength() {
        return collectionLength;
    }

    /**
     * Search for the topic information. The returned results (retrieved
     * documents) should be ranked by the score (from the most relevant to the
     * least). TopN specifies the maximum number of results to be returned.
     *
     * @param aQuery The query to be searched for.
     * @param TopN The maximum number of returned document
     * @return
     */
    public List<Document> retrieveQuery(Query aQuery, int TopN) throws IOException {
        // NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
        // implement your retrieval model here, and for each input query, return the topN retrieved documents
        // sort the docs based on their relevance score, from high to low

        String queryContent = aQuery.GetQueryContent();
        String[] tokenList = queryContent.split("\\W+");
        List<Document> docList = new ArrayList<Document>();

        int count = 0;
        List<int[]> posting = new ArrayList<>();
        for (String token : tokenList) {
            count++;
            int[][] postingList = indexReader.getPostingList(token);
            if (postingList == null) {
                continue;
            }
            Arrays.sort(postingList, new java.util.Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    return Double.compare((double) a[0], (double) b[0]);
                }
            });
            int j = 0;
            for (int i = 0; i < postingList.length; i++) {
                int docId = postingList[i][0];
                if (count == 1) {
                    posting.add(j, new int[tokenList.length + 1]);
                    posting.get(j)[0] = docId;
                    posting.get(j)[count] = postingList[i][1];
                    j++;
                } else {
                    while ((j < posting.size()) && (posting.get(j)[0] < docId)) {
                        j++;
                    }
                    if ((j < posting.size()) && (posting.get(j)[0] == docId)) {
                        posting.get(j)[count] = postingList[i][1];;
                        j++;
                    } else {
                        posting.add(j, new int[tokenList.length + 1]);
                        posting.get(j)[0] = docId;
                        posting.get(j)[count] = postingList[i][1];
                    }
                }
            }
        }

        count = 0;
        for (String token : tokenList) {
            count++;
            long ctf = indexReader.CollectionFreq(token);

            if (ctf != 0) {
                for (int i = 0; i < posting.size(); i++) {
                    int freq = posting.get(i)[count];
                    int docId = posting.get(i)[0];

                    double p = (double) (((double) freq + (double) this.mu * (double) ctf / (double) this.collectionLength) / (double) ((double) this.docLength.get(docId) + (double) this.mu));

                    if (count == 1) {
                        Document thisDoc = new Document("" + docId, this.docno.get(docId), p);

                        thisDoc.freq = new int[tokenList.length];
                        thisDoc.freq[count - 1] = freq;
                        thisDoc.docLength = this.docLength.get(docId);

                        docList.add(thisDoc);
                    } else {
                        Document thisDoc = docList.get(i);
                        thisDoc.freq[count - 1] = freq;

                        thisDoc.setScore(thisDoc.score() * p);
                    }

                }
            }
        }

        Collections.sort(docList, new Comparator<Document>() {
            public int compare(Document D1, Document D2) {
                return Double.compare(D2.score(), D1.score());
            }
        });

        if (TopN < docList.size()) {
            return docList.subList(0, TopN);
        } else {
            return docList;
        }
    }

}
