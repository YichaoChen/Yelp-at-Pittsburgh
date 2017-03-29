package edu.pitt.is2140.processing;

import java.util.ArrayList;

/**
 * This is for INFSCI 2140 in 2015
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	//you can add essential private methods or variables
	private ArrayList<char []> words = new ArrayList<char []>();
	private int counter = 0;
	
	// YOU MUST IMPLEMENT THIS METHOD
	public WordTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		String tokenText = new String(texts);
		String [] tokenWord = tokenText.split("\\W+");
		for(int i=0; i<tokenWord.length; i++)
			words.add(tokenWord[i].toCharArray());
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] nextWord() {
		// read and return the next word of the document
		// or return null if it is the end of the document
		if(counter<words.size())
			return words.get(counter++);
		else
			return null;
	}
	
}
