package edu.pitt.is2140.processing;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;

import org.json.*;

public class CollectSynonyms {

    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    String word;
    String language;
    String key;
    String output;

    public CollectSynonyms(String word, String language, String key, String output) {
        this.word = word;
        this.language = language;
        this.key = key;
        this.output = output;
    }

    public String getSynonyms() throws JSONException {
        try {
            StringBuffer result = new StringBuffer();
            HashSet removeDuplicate = new HashSet();
            URL serverAddress = new URL(endpoint + "?word=" + URLEncoder.encode(word, "UTF-8") + "&language=" + language + "&key=" + key + "&output=" + output);
            HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200) {
                String line = null;
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + '\n');
                }

                String json_string = sb.toString();
                json_string = json_string;

                JSONObject obj = new JSONObject(json_string);

                JSONArray array_json = obj.getJSONArray("response");

                obj = array_json.getJSONObject(0);

                obj = obj.getJSONObject("list");

                String array_value = (String) obj.get("synonyms");

                array_value = array_value.replace(" (similar term)", "").replace(" (related term)", "");

                String[] array = array_value.split("\\|");

                for (String words : array) {
                    removeDuplicate.add(words);
                }

                Iterator iter = removeDuplicate.iterator();
                while (iter.hasNext()) {
                    result.append(String.valueOf(iter.next())).append(",");
                }
                connection.disconnect();
                return result.toString().trim();
            } else {
                connection.disconnect();
                return "";
            }
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        
        return "";
        
    }

}
