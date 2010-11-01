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
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLLexerException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;

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

	private static Set<Character> nonXmlMarger = HashUtil.getHashSet();
	static {
		nonXmlMarger.add('-');
		nonXmlMarger.add('<');
		nonXmlMarger.add('=');
		nonXmlMarger.add('>');
		nonXmlMarger.add(' ');
	}

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
		// tokenize();
		newTokenize();

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

	private final void newTokenize() {
		StringBuilder buffer = new StringBuilder();
		boolean isProtected = false;
		String protectionPattern = "";
		for (int index = 0; index < origin.length(); index++) {
			char c = origin.charAt(index);
			/*
			 * whitespace and not protected
			 */
			if (c == ' ' && !isProtected) {
				if (!buffer.toString().isEmpty()) {
					tokens.add(buffer.toString().trim());
					buffer = new StringBuilder();
				}
				continue;
			}
			/*
			 * check escaping symbol
			 */
			if (c == '\\' && isProtected) {
				/*
				 * next character is missing
				 */
				if (index + 1 >= origin.length()) {
					throw new TMQLLexerException(
							"Invalid character sequence at position '"
									+ index
									+ "'. Expected '\"' or '\\' but no character was found!");
				}
				char next = origin.charAt(index+1);
				/*
				 * only " and \ are allowed after escape symbol
				 */
				if (next == '"' || next == '\\') {
					buffer.append(c);
					buffer.append(next);
					index++;
					continue;
				}
				throw new TMQLLexerException(
						"Invalid character sequence at position '" + index
								+ "'. Expected '\"' or '\\' but '" + next
								+ "' was found!");
			}
			buffer.append(c);
			String currentPattern = buffer.toString();
			/*
			 * is end of protection
			 */
			if (isProtected && currentPattern.endsWith(protectionPattern)) {
				/*
				 * special handling for XML end
				 */
				if (protectionPattern.equalsIgnoreCase(">")) {
					/*
					 * is embed content axis
					 */
					if (origin.length() > index + 1
							&& !nonXmlMarger.contains(origin.charAt(index + 1))
							&& !nonXmlMarger.contains(origin.charAt(index - 1))) {
						continue;
					}
				}
				/*
				 * special handling for data-typed literals
				 */
				else if (protectionPattern.equalsIgnoreCase("\"\"\"")
						|| protectionPattern.equalsIgnoreCase("\"")) {
					if (origin.length() > index + 2
							&& "^^".equalsIgnoreCase(origin.substring(
									index + 1, index + 3))) {
						/*
						 * change protection pattern to whitespace
						 */
						protectionPattern = " ";
						continue;
					}
					/*
					 * if next character is also an quote ignore current existence of quotes
					 */
					else if (origin.length() > index + 1 && origin.charAt(index+1) == '"'){
						continue;
					}
					/*
					 * protected quotes at the beginning of a string
					 */
					else if ( buffer.length() < protectionPattern.length()*2){
						continue;
					}
				}
				if (!buffer.toString().isEmpty()) {
					tokens.add(buffer.toString().trim());
					buffer = new StringBuilder();
				}
				isProtected = false;
				continue;
			}
			/*
			 * is single quoted string
			 */
			if ("'''".equalsIgnoreCase(currentPattern)) {
				if (!buffer.toString().isEmpty()) {
					tokens.add(buffer.toString().trim());
					buffer = new StringBuilder();
				}
			}
			/*
			 * beginning of new protection?
			 */
			if (!isProtected) {
				/*
				 * is triple quoted string
				 */
				if ("\"\"\"".equalsIgnoreCase(currentPattern)) {
					protectionPattern = "\"\"\"";
					isProtected = true;
				}
				/*
				 * could be XML
				 */
				else if ("<".equalsIgnoreCase(currentPattern)) {
					/*
					 * is XML if next char is not <
					 */
					if (origin.length() > index + 1
							&& !nonXmlMarger.contains(origin.charAt(index + 1))) {
						isProtected = true;
						protectionPattern = ">";
					}
				}
				/*
				 * could be simple double quote string
				 */
				else if ("\"".equalsIgnoreCase(currentPattern)) {
					/*
					 * is XML if next char is not "
					 */
					if (origin.length() > index + 1
							&& !"\"".equalsIgnoreCase(String.valueOf(origin
									.charAt(index + 1)))) {
						isProtected = true;
						protectionPattern = "\"";
					}
				}
				/*
				 * could be simple single quote string
				 */
				else if ("'".equalsIgnoreCase(currentPattern)) {
					/*
					 * is XML if next char is not '
					 */
					if (origin.length() > index + 1
							&& !"'".equalsIgnoreCase(String.valueOf(origin
									.charAt(index + 1)))) {
						isProtected = true;
						protectionPattern = "'";
					}
				}
			}
		}
		/*
		 * add last string
		 */
		if (buffer.toString().length() != 0) {
			tokens.add(buffer.toString().trim());
		}
	}

	/**
	 * Internal method to split the origin string into internal token
	 * representation. Tokenizer class split the query at whitespace except of
	 * protected whitespace, like as a part of strings.
	 */
	private final void tokenize() {
		StringBuffer buffer = new StringBuffer();
		char lastChar = origin.charAt(0);
		boolean stringProtection = lastChar == '"';
		boolean xmlProtection = lastChar == '<';
		buffer.append(lastChar);
		for (int index = 1; index < origin.length(); index++) {
			char c = origin.charAt(index);
			if (c == '"') {
				/*
				 * save token """
				 */
				if (lastChar == '"') {
					stringProtection = false;
				}
				/*
				 * is string
				 */
				else {
					stringProtection = !stringProtection;
				}
				buffer.append(c);
			}
			/*
			 * can be XML content
			 */
			else if (!stringProtection && c == '<'
					&& origin.indexOf(">", index) != -1 && lastChar != '<') {
				char next = origin.charAt(index + 1);
				if (next != '<' && next != '=' && next != ' ') {
					xmlProtection = true;
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
			else if (!stringProtection && c == '>' && xmlProtection) {
				xmlProtection = false;
				buffer.append(c);
				tokens.add(buffer.toString());
				buffer = new StringBuffer();
			}
			/*
			 * is space character and not a part of a string
			 */
			else if (c == ' ' && !xmlProtection && !stringProtection) {
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
