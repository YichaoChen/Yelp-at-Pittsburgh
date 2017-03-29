package edu.pitt.is2140.processing;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.ws.rs.core.Context;


public class StopWordRemover {
	// Remove stopwords
		public String[] stopWordRemover(String []input) throws IOException{
                    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stopword.txt");
                
                    InputStreamReader streamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(streamReader);
                    Set<String> stopWords = new HashSet<String>();
                    List<String> temp = new ArrayList<String>();
                    String[] result;

                    String line = reader.readLine();

                        // Collect stopwords
                    while(line != null){
                       stopWords.add(line);
                       line = reader.readLine(); 
                    }

                    // Check whether the word in the query is a stopword
                    for(int i = 0; i < input.length; i++){
                            input[i] = input[i].toLowerCase();
                            // Filter out the words
                            if(!stopWords.contains(input[i])){
                                    temp.add(input[i]);
                            }
                    }

                    result = new String[temp.size()];

                    for(int i = 0; i < temp.size(); i++){
                            result[i] = temp.get(i);
                    }

                    return result;
                }
}
