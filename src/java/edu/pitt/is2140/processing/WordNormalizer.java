package edu.pitt.is2140.processing;

/**
 * This is for INFSCI 2140 in 2015
 * 
 */
public class WordNormalizer {
	//you can add essential private methods or variables
	
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] lowercase( char[] chars ) {
		//transform the uppercase characters in the word to lowercase
		chars = (new String (chars)).toLowerCase().toCharArray();
		return chars;
	}
	
	public String stem(char[] chars)
	{
		//use the stemmer in Classes package to do the stemming on input word, and return the stemmed word
		String str =  "";
		
		Stemmer wordStemmer = new Stemmer();
		wordStemmer.add(chars, chars.length);
		wordStemmer.stem();
		str = wordStemmer.toString();
		
		return str;
	}
	
}
