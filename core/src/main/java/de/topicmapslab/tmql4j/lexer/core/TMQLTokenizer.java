/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.lexer.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * TMQL tokenizer class. String secure tokenizer of TMQL queries, splitting
 * strings by looking for space characters, but protects white spaces as part of
 * string tokens.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLTokenizer {

	/**
	 * the origin string
	 */
	private final String origin;

	/**
	 * the list of tokens
	 */
	private final List<String> tokens;

	/**
	 * the internal iterator of tokens
	 */
	private final Iterator<String> iterator;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param origin
	 *            the internal string for splitting
	 */
	public TMQLTokenizer(String origin) {
		this.origin = origin;
		this.tokens = new LinkedList<String>();

		/*
		 * call method to tokenize
		 */
		tokenize();

		this.iterator = this.tokens.iterator();
	}

	/**
	 * Method checks if another token is available after the current position.
	 * 
	 * @return <code>true</code> if iterator has more tokens, <code>false</code>
	 *         otherwise
	 */
	public boolean hasMoreTokens() {
		return iterator.hasNext();
	}

	/**
	 * Method return the next token after the current position if it is
	 * available
	 * 
	 * @return the next token but never <code>null</code>
	 * @throws NoSuchElementException
	 *             thrown if no element is avaible after current position
	 */
	public String nextToken() throws NoSuchElementException {
		return iterator.next();
	}

	/**
	 * Internal method to split the origin string into internal token
	 * representation. Tokenizer class split the query at whitespace except of
	 * protected whitespace, like as a part of strings.
	 */
	private final void tokenize() {
		StringBuffer buffer = new StringBuffer();
		char lastChar = origin.charAt(0);
		boolean isProtected = lastChar == '"' || lastChar == '<';
		buffer.append(lastChar);
		for (int index = 1; index < origin.length(); index++) {
			char c = origin.charAt(index);
			if (c == '"') {
				/*
				 * save token """
				 */
				if (lastChar == '"') {
					isProtected = false;
				}
				/*
				 * is string
				 */
				else {
					isProtected = !isProtected;
				}
				buffer.append(c);
			}
			/*
			 * can be XML content
			 */
			else if (c == '<' && origin.indexOf(">", index) != -1
					&& lastChar != '<') {
				char next = origin.charAt(index + 1);
				if (next != '<' && next != '=' && next != ' ') {
					isProtected = true;
					if (buffer.toString().length() > 0) {
						tokens.add(buffer.toString());
						buffer = new StringBuffer();
					}
				}
				buffer.append(c);
			}
			/*
			 * is XML content end
			 */
			else if (c == '>' && isProtected) {
				isProtected = false;
				buffer.append(c);
				tokens.add(buffer.toString());
				buffer = new StringBuffer();
			}
			/*
			 * is space character and not a part of a string
			 */
			else if (c == ' ' && !isProtected) {
				if (buffer.toString().length() != 0) {
					tokens.add(buffer.toString());
					buffer = new StringBuffer();
				}
			}
			/*
			 * '{' and '}' are always stand-alone characters
			 */
			else if (c == '{' || c == '}') {
				if (!buffer.toString().isEmpty()) {
					isProtected = false;
					tokens.add(buffer.toString());
					buffer = new StringBuffer();					
				}
				buffer.append(c);
				tokens.add(buffer.toString());
				buffer = new StringBuffer();
			}
			/*
			 * is any char
			 */
			else {
				buffer.append(c);
			}
			/*
			 * store as last char
			 */
			lastChar = c;
		}

		if (buffer.toString().length() != 0) {
			tokens.add(buffer.toString());
		}

	}
}
