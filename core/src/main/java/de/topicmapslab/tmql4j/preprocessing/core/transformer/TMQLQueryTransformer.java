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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.common.context.PrefixHandler;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.lexer.core.TMQLTokenizer;


/**
 * Query transformer class to cleaned query by removing unnecessary white-spaces
 * and add necessary white-spaces.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQueryTransformer {

	private static final String WHITESPACE = " ";

	/**
	 * List of internal system tokens which can isolate without use protection
	 * of other tokens
	 */
	public static List<String> patterns = new LinkedList<String>();
	static {
		patterns.add("~~>");
		patterns.add("<~~");
		patterns.add("<<");
		patterns.add(">>");
		patterns.add("<->");
		patterns.add("<=");
		patterns.add("=~");
		patterns.add(",");
		patterns.add("==");
		patterns.add("--");
		patterns.add("+\\+");
		patterns.add("@");
		patterns.add("!=");
		patterns.add("(");
		patterns.add(")");
		patterns.add("{");
		patterns.add("}");
		patterns.add("[");
		patterns.add("]");
		patterns.add("\\");
		patterns.add("\\.\\.\\.");
	}

	/**
	 * Map of special internal system tokens which can not isolate without use
	 * protection of other tokens.
	 * 
	 * Map contains internal tokens mapping to the protected tokens, for example
	 * the tilde-token '~' can isolate any time except as a part of the tokens
	 * '~~>' , '<~~' or '=~'
	 */
	private static final Map<String, String[]> specials = HashUtil.getHashMap();
	static {
		specials.put("~", new String[] { "~~>", "<~~", "=~" });
		specials.put("=", new String[] { "==", "<=", ">=", "=~" });
		specials.put("->", new String[] { "<->" });
		specials.put("<-", new String[] { "<->" });
		specials.put("<=", new String[] {});
		specials.put("-", new String[] { "<->", "->", "<-", "--" });
		specials.put("+", new String[] { "++" });
		specials.put(":", new String[] {});
		specials.put("//", new String[] {});
		specials.put("/", new String[] { "//", "/>", "</" });
		specials.put("..", new String[] { "..." });
		specials.put("*", new String[] {});
		specials.put("!", new String[] {"!="});
	}

	/**
	 * Internal transformer method to remove unnecessary whitespace or insert
	 * missing once. Method works iterative over all tokens by splitting them. <br />
	 * <br />
	 * First all unprotected tokens would be isolate by inserting whitespace. <br />
	 * <br />
	 * Second all new tokens would be check for valid URIs or XML nodes. If some
	 * URIs or XML found the URI would be isolate from token and the second step
	 * will be repeated for the other tokens. Otherwise the tokens would be
	 * checked for protected tokens.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the string representation of the TMQL Query
	 * @return the transformed string representation of the TMQL Query
	 */
	public static final String transform(final ITMQLRuntime runtime,
			final String query) {
		StringBuffer buffer = new StringBuffer();

		/*
		 * split query to string-represented tokens
		 */
		TMQLTokenizer tokenizer = new TMQLTokenizer(query);
		/*
		 * iterate over all tokens
		 */
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			/*
			 * check if token is longer than two
			 */
			token = clean(runtime, token);
			/*
			 * add transformed token to buffer and insert one whitespace
			 */
			buffer.append(token.trim() + WHITESPACE);
		}

		return buffer.toString().trim();
	}

	/**
	 * Transformation of TMQL tokens <br />
	 * <br />
	 * First all unprotected tokens would be isolate by inserting whitespace. <br />
	 * <br />
	 * Second all new tokens would be check for valid URIs or XML nodes. If some
	 * URIs or XML found the URI would be isolate from token and the second step
	 * will be repeated for the other tokens. Otherwise the tokens would be
	 * checked for protected tokens.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param token
	 *            the token to transform
	 */
	private static String clean(final ITMQLRuntime runtime, final String token) {

		String cleaned = new String(token);

		/*
		 * add necessary white-spaces
		 */
		cleaned = transformPatterns(runtime, cleaned);

		/*
		 * split query to string-represented tokens
		 */
		TMQLTokenizer tokenizer = new TMQLTokenizer(cleaned);
		String tmp = new String();
		/*
		 * iterate over all tokens
		 */
		while (tokenizer.hasMoreTokens()) {
			String t = tokenizer.nextToken();
			/*
			 * the pattern // will be detect as URI but at the beginning they
			 * represent the type-instance-association
			 */
			if (t.startsWith("//")) {
				tmp += "//" + WHITESPACE;
				t = t.substring(2);
			}
			tmp += uriProtectionAlgorithm(runtime, t);
			tmp += WHITESPACE;
		}
		cleaned = tmp;

		return cleaned;
	}

	/**
	 * Internal URI and XML Protection algorithm. Method isolate all URIs and
	 * XML nodes from given token and replace protected tokens by checking all
	 * tokens of their protection list.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param token
	 *            the token to transform
	 */
	private static String uriProtectionAlgorithm(final ITMQLRuntime runtime,
			final String token) {
		String cleaned = new String(token);

		/*
		 * check if token is %prefix or %pragma
		 */
		if (token.equalsIgnoreCase("%prefix")
				|| token.equalsIgnoreCase("%pragma")) {
			return token;
		}
		/*
		 * check if token is a string
		 */
		else if (token.length() > 2 && token.startsWith("\"")
				&& token.endsWith("\"") && token.charAt(1) != '"'
				&& token.charAt(token.length() - 2) != '"') {
			return token;
		}

		/*
		 * check protected strings with data-type
		 */
		if (token.length() > 2 && token.startsWith("\"")
				&& token.contains("^^")) {
			return token;
		}

		/*
		 * save time and dateTime
		 */
		if (LiteralUtils.isDate(token) || LiteralUtils.isTime(token)
				|| LiteralUtils.isDateTime(token) || LiteralUtils.isInteger(token) || LiteralUtils.isDecimal(token)) {
			return token;
		}
		/*
		 * is known pattern
		 */
		if ( patterns.contains(token.trim())){
			return token;
		}

		/*
		 * current window size
		 */
		int windowSize = token.length();
		while (windowSize > 4) {
			/*
			 * changing windows size iterative
			 */
			for (int windowStart = 0; windowStart + windowSize <= token
					.length(); windowStart++) {
				/*
				 * extract IRI candidate
				 */
				String candidate = cleaned.substring(windowStart, windowSize
						+ windowStart);
				/*
				 * check if token is protected ( XML, IRI, string )
				 */
				if (isProtected(runtime, candidate)) {
					String tmp = "";
					/*
					 * method add cleaned token before detected candidate
					 */
					if (windowStart > 0) {
						tmp += uriProtectionAlgorithm(runtime, cleaned
								.substring(0, windowStart));
					}
					tmp += WHITESPACE;
					tmp += candidate;
					tmp += WHITESPACE;
					/*
					 * method add cleaned token after detected candidate
					 */
					if (windowStart + windowSize < cleaned.length()) {
						tmp += uriProtectionAlgorithm(runtime, cleaned
								.substring(windowStart + windowSize));
					}
					return tmp;

				}
			}
			windowSize--;
		}

		/*
		 * add white-spaces
		 */
		cleaned = transformSecure(runtime, cleaned);

		return cleaned;
	}

	/**
	 * Replace protected tokens by checking all tokens of their protection list.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param token
	 *            the token to transform
	 */
	private static String transformSecure(final ITMQLRuntime runtime,
			final String token) {
		String cleaned = new String(token);
		/*
		 * iterate over tokens
		 */
		for (Entry<String, String[]> entry : specials.entrySet()) {
			/*
			 * get the index of token in the string
			 */
			int index = cleaned.indexOf(entry.getKey());
			while (index != -1) {
				/*
				 * isolate a short element from given query
				 */
				String tmp = "";
				if (index > 0) {
					tmp = cleaned.substring(index - 1);
				} else {
					tmp = cleaned.substring(index);
				}
				if (tmp.length() > 4) {
					tmp = tmp.substring(0, 4);
				}
				/*
				 * check if isolated query part matches a protection of current
				 * token
				 */
				boolean matches = false;
				for (String pattern : entry.getValue()) {
					if (tmp.contains(pattern)) {
						matches = true;
						break;
					}
				}
				/*
				 * token at current position is not protected -> insert
				 * white-spaces
				 */
				if (!matches) {
					tmp = cleaned.substring(0, index);
					tmp += WHITESPACE;
					tmp += entry.getKey();
					tmp += WHITESPACE;
					tmp += cleaned.substring(index + entry.getKey().length());
					index += 2;
					cleaned = tmp;
				} else {
					index += 3;
				}
				index = cleaned.indexOf(entry.getKey(), index);
			}
		}

		return cleaned;
	}

	/**
	 * Method to transform all tokens without protection.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param token
	 *            the token to transform
	 */
	private static String transformPatterns(final ITMQLRuntime runtime,
			final String token) {
		String cleaned = new String(token);

		/*
		 * protect strings
		 */
		if (token.length() > 2 && token.startsWith("\"")
				&& token.endsWith("\"") && token.charAt(1) != '"'
				&& token.charAt(token.length() - 2) != '"') {
			return token;
		}

		/*
		 * check protected strings with data-type
		 */
		if (token.length() > 2 && token.startsWith("\"")
				&& token.contains("^^")) {
			return token;
		}

		/*
		 * save time and dateTime
		 */
		if (LiteralUtils.isDate(token) || LiteralUtils.isTime(token)
				|| LiteralUtils.isDateTime(token)) {
			return token;
		}

		/*
		 * isolate patterns using white-spaces
		 */
		for (String pattern : patterns) {
			String tmp = cleaned.replaceAll("\\" + pattern, WHITESPACE + "\\"
					+ pattern + WHITESPACE);
			cleaned = tmp;
		}

		return cleaned;
	}

	/**
	 * Method simply checks if given token representing an URI ,XML node or
	 * simply a string. It also checks if the given URI is relative.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param token
	 *            the token to check
	 * @return <code>true</code> if given token is an absolute or relative URI
	 *         or a XML node, <code>false</code> otherwise
	 */
	private static boolean isProtected(final ITMQLRuntime runtime,
			final String token) {

		String uri = token;
		/*
		 * If token starts with < and ends with > and do not contain any
		 * whitespace characters than it will be detect as URI or XML
		 */
		if (token.startsWith("<") && token.endsWith(">")) {
			return true;
		} else if (token.startsWith("\"") && token.endsWith("\"")) {
			return true;
		} else if ( patterns.contains(token.trim())){
			return true;
		}
		/*
		 * Check if given token is an absolute URI
		 */
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			// non absolute URI
		}

		PrefixHandler handler = runtime.getLanguageContext().getPrefixHandler();
		/*
		 * Check if given token is a known shortcut, for example tm:name,
		 * tm:subject or tm:occurrence
		 */
		if (handler.isKnownSystemIdentifier(token)) {
			return true;
		}

		/*
		 * try to replace all QNames
		 */
		StringTokenizer tokenizer = new StringTokenizer(token, ":");
		uri = new String();
		while (tokenizer.hasMoreTokens()) {
			String tok = tokenizer.nextToken();
			if (handler.isKnownPrefix(tok)) {
				uri += handler.getPrefix(tok);
			} else {
				uri += tok + (tokenizer.hasMoreTokens() ? ":" : "");
			}
		}

		/*
		 * Check if given token is an relative URI
		 */
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			// non relative URI
		}

		/*
		 * non absolute or relative URI
		 */
		return false;

	}

}
