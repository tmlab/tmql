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
package de.topicmapslab.tmql4j.preprocessing.core.transformer;

import java.util.Map;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.core.TMQLTokenizer;

/**
 * Transformer class implementing the substitution of TMQL non-canonical
 * shortcuts by their extended canonical patterns.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NonCanonicalTransformer {

	/**
	 * map containing non-canonical shortcuts to their extended canonical
	 * patterns
	 */
	private static final Map<String, String> shortcuts = HashUtil.getHashMap();

	/**
	 * mapping non-canonical shortcuts to their extended canonical patterns
	 */
	static {
		shortcuts.put("^", ". >> types ==");
		shortcuts.put("\\", "<< atomify << characteristics");
		shortcuts.put("~", "<< indicators");
		shortcuts.put("=", "<< locators");
		shortcuts.put("@", ". >> scope ==");
		shortcuts.put("->", ">> players");
		shortcuts.put("<-", "<< players");
		shortcuts.put("<->", ">> traverse");
		shortcuts.put("!", ">> item");
		shortcuts.put("~~>", ">> reifier");
		shortcuts.put("<~~", "<< reifier");
	}

	/**
	 * private and hidden constructor
	 */
	private NonCanonicalTransformer() {

	}

	/**
	 * Transform the non-canonical query to the canonical grammar level by
	 * substitute the shortcuts with their extended patterns.
	 * 
	 * @param query
	 *            the query to transform
	 * @return the string representation of the canonized query
	 */
	public static final String transform(final String query) {
		/*
		 * split query to string-represented tokens
		 */
		TMQLTokenizer tokenizer = new TMQLTokenizer(query);
		/*
		 * initialize string buffer
		 */
		StringBuffer buffer = new StringBuffer();
		String lastToken = "";
		/*
		 * iterate over all tokens
		 */
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			/*
			 * check if token is a string
			 */
			if (token.matches("\".?\"")) {
				buffer.append(token);
			}
			/*
			 * check if token is shortcut for combination of characteristic and
			 * atomify axis in forward direction
			 */
			else if (token.equalsIgnoreCase("/")) {
				/*
				 * replace / anchor by >> characteristics anchor >> atomify
				 */
				buffer.append(">> characteristics " + tokenizer.nextToken()
						+ " >> atomify ");
			}
			/*
			 * check if token is shortcut of scope filter
			 */
			else if (token.equalsIgnoreCase("@")) {
				/*
				 * check if last token was opening squared bracket
				 */
				if (!lastToken.equalsIgnoreCase("[")) {
					/*
					 * add opening squared bracket
					 */
					buffer.append("[ ");
				}
				/*
				 * replace @ anchor by . >> scope == anchor
				 */
				buffer.append(shortcuts.get(token));
				buffer.append(" ");
				token = tokenizer.nextToken();
				buffer.append(token);
				/*
				 * check if last token was opening squared bracket
				 */
				if (!lastToken.equalsIgnoreCase("[")) {
					/*
					 * add sclosing squared bracket
					 */
					buffer.append(" ]");
				}
			}
			/*
			 * check if current token is known shortcut
			 */
			else if (shortcuts.containsKey(token)) {
				buffer.append(shortcuts.get(token));
			}			
			/*
			 * token isn't known shortcut
			 */
			else {
				buffer.append(token);
			}
			buffer.append(" ");
			lastToken = token;
		}
		return buffer.toString().trim();
	}

}
