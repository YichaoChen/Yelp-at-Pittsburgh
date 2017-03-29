package edu.pitt.is2140.processing;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;

public class Query {
    //you can modify this class

    private String queryContent;
    private String synonyms;
    private String originalQuery;

    public Query(String queryContent) throws IOException, JSONException {
        
        originalQuery = queryContent;
        
		
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern   p   =   Pattern.compile(regEx);     
        Matcher   m   =   p.matcher(queryContent);
        String removeSymb = m.replaceAll("").trim();
        //System.out.println(removeSymb);
        String[] pureInput = removeSymb.split(" ");
        StopWordRemover rem = new StopWordRemover();
        String[] afterStopWord = rem.stopWordRemover(pureInput);
        
        
        String[] afterNorm = normalizer(afterStopWord);
        StringBuilder temp1 = new StringBuilder();
        for(String word: afterNorm){
            temp1.append(word).append(" ");
        }
        
        this.queryContent = temp1.toString().trim();
        
        StringBuilder temp2 = new StringBuilder();
        
        for(String word: afterStopWord){
                CollectSynonyms synonyms = new CollectSynonyms(word, "en_US", "giTPbrDj2MyGliE67QxV", "json");
                temp2.append(synonyms.getSynonyms()).append(" ");
        }
        
		
        synonyms = temp2.toString().replaceAll(","," ").trim().replace("  ", " ");
        
    }

    public String GetQueryContent() {
        return queryContent;
    }

    public void SetQueryContent(String content) {
        queryContent = content;
    }
    
    public String getExtendedBeforeStem() {
        return synonyms;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }
    
    
    public static String[] normalizer(String[] input){
            String[] result = new String[input.length];
            Stemmer stemmer = new Stemmer();
            String needsNormalize;

            for(int i = 0; i < input.length; i++){
                    needsNormalize = input[i].toLowerCase();

                    char[] chars = needsNormalize.toCharArray();
                    stemmer.add(chars, chars.length);
                    stemmer.stem();
                    result[i] = stemmer.toString();			
            }
            return result;
    }
}
