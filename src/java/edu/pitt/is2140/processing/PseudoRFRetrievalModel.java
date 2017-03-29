package edu.pitt.is2140.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class PseudoRFRetrievalModel {

	MyIndexReader ixreader;

	private final int mu = 2000;
	private List<Integer> docLength;
	private int collectionLength = 0;
	private List<String> docno;
        public boolean allEmpty = true;
        public int postLen;

	public PseudoRFRetrievalModel(MyIndexReader ixreader) {
		this.ixreader = ixreader;

		int i = 0;
		this.docLength = new ArrayList<Integer>();
		this.docno = new ArrayList<String>();

		while (true) {
			try{
				this.docno.add(ixreader .getDocno(i));
			}catch (Exception e){
				break;
			}
			
			try {
				int docLength = ixreader.docLength(i);

				this.docLength.add(new Integer(docLength));
				this.collectionLength += docLength;
			} catch (Exception e) {
				this.docLength.add(new Integer(0));
			}
			i++;
		}
	}

	/**
	 * Search for the topic with pseudo relevance feedback. The returned results
	 * (retrieved documents) should be ranked by the score (from the most
	 * relevant to the least).
	 * 
	 * @param aQuery
	 *            The query to be searched for.
	 * @param TopN
	 *            The maximum number of returned document
	 * @param TopK
	 *            The count of feedback documents
	 * @param alpha
	 *            parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery(Query aQuery, int TopN, int TopK, double alpha) throws Exception {
		// this method will return the retrieval result of the given Query, and
		// this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK
		// documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's
		// P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine
		// the each query token's original retrieval score P(token|document)
		// with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get
		// the whole query's new score,
		// P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')

		// get P(token|feedback documents)
		HashMap<String, Double> TokenRFScore = GetTokenRFScore(aQuery, TopK);

		List<Document> docList = new ArrayList<Document>();
		
		int count = 0;
		List<int[]> posting = new ArrayList<>();
		for(Entry<String, Double> entry : TokenRFScore.entrySet()){
			String token = entry.getKey();
			count++;
			int[][] postingList = ixreader.getPostingList(token);
                        postLen = postingList.length;
			if(postingList.length == 0){
                            continue;                             
                        }else{
                            allEmpty = false;
                        }
			Arrays.sort(postingList, new java.util.Comparator<int[]>() {
			    public int compare(int[] a, int[] b) {
			        return Double.compare((double) a[0], (double) b[0]);
			    }
			});
			int j = 0;
			for(int i=0; i<postingList.length; i++){
				int docId = postingList[i][0];
				if(count==1){
					posting.add(new int[TokenRFScore.size()+1]);
					posting.get(i)[0] = docId;
					posting.get(i)[count] = postingList[i][1];
				}else {
					while((j<posting.size())&&(posting.get(j)[0]<docId))
						j++;
					if((j<posting.size())&&(posting.get(j)[0]==docId)){
						posting.get(j)[count] = postingList[i][1];;
						j++;
					}else{
						posting.add(j, new int[TokenRFScore.size()+1]);
						posting.get(j)[0] = docId;
						posting.get(j)[count] = postingList[i][1];
					}
				}
			}
		}
                
                if(allEmpty == false){
		
                        count = 0;
                        for (Entry<String, Double> entry : TokenRFScore.entrySet()) {
                                String token = entry.getKey();
                                double score = (double) entry.getValue();
                                count++;
                                long ctf = ixreader.CollectionFreq(token);
                                if (ctf != 0) {
                                        for (int i = 0; i < posting.size(); i++) {
                                                int freq = posting.get(i)[count];
                                                int docId = posting.get(i)[0];
                                                double p = (double) (((double) freq + (double) this.mu * (double) ctf / (double) this.collectionLength)
                                                                / (double) ((double) this.docLength.get(docId) + (double) this.mu));

                                                p = alpha * p + (1 - alpha) * score;

                                                try {
                                                        Document thisDoc = docList.get(i);

                                                        thisDoc.setScore(thisDoc.score() * p);
                                                } catch (IndexOutOfBoundsException e) {
                                                        docList.add(new Document("" + docId, this.docno.get(docId), p));
                                                }
                                        }
                                }
                        }

                        Collections.sort(docList, new Comparator<Document>() {
                                public int compare(Document D1, Document D2) {
                                        return Double.compare(D2.score(), D1.score());
                                }
                        });

                        // sort all retrieved documents from most relevant to least, and return
                        // TopN
                        List<Document> results;
                        if(TopN > docList.size()){
                            results = new ArrayList<Document>(docList.subList(0, docList.size()));
                        }else{
                            results = new ArrayList<Document>(docList.subList(0, TopN));
                        }

                        return results;
                }else{
                    return null;
                }
                
	}

	public HashMap<String, Double> GetTokenRFScore(Query aQuery, int TopK) throws Exception {
		// for each token in the query, you should calculate token's score in
		// feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String, Double> TokenRFScore = new HashMap<String, Double>();

		String queryContent = aQuery.GetQueryContent();
		String[] tokenList = queryContent.split("\\W+");

		QueryRetrievalModel model = new QueryRetrievalModel(this.ixreader);
		List<Document> preresults = model.retrieveQuery(aQuery, TopK);

		int docLength = 0;

		for (Document document : preresults) 
			docLength += document.docLength;
		
		int count = -1;
		for (String token : tokenList) {
			count++;
			int[][] postingList = this.ixreader.getPostingList(token);
			if(postingList!=null){
				int freq = 0;
				long ctf = this.ixreader.CollectionFreq(token);
				
				for(Document document: preresults)
					freq += document.freq[count];
	
				double p = (double) (((double) freq + (double) this.mu * (double) ctf / (double) this.collectionLength)
						/ (double) ((double) docLength + (double) this.mu));
				TokenRFScore.put(token, p);
			}
		}

		return TokenRFScore;
	}
        
        public boolean isPostEmpty(){
            return allEmpty;
        }

}