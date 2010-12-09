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
package de.topicmapslab.tmql4j.draft2010.query.preprocessing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.components.lexer.TMQLTokenizer;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PrefixHandler;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.WhiteSpace;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Query transformer class to cleaned query by removing unnecessary white-spaces
 * and add necessary white-spaces.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TmqlQueryTransformer {

	/**
	 * the colon token 
	 */
	private static final String TOKEN_COLON = ":";
	/**
	 * the IRI start
	 */
	private static final String TOKEN_IRI_END = ">";
	/**
	 * the IRI end
	 */
	private static final String TOKEN_IRI_START = "<";
	/**
	 * the slash token
	 */
	private static final String SLASH = "\\";
	/**
	 * the datatyped token
	 */
	private static final String DATATYPE = "^^";
	/**
	 * the quote character
	 */
	private static final char CHAR_QUOTE = '"';
	/**
	 * the quote token
	 */
	private static final String QUOTE = "\"";
	/**
	 * the backslash token
	 */
	private static final String BACKSLASH = "//";
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
		patterns.add(SLASH);
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
		specials.put(TOKEN_COLON, new String[] {});
		specials.put(BACKSLASH, new String[] {});
		specials.put("/", new String[] { BACKSLASH, "/>", "</" });
		specials.put("..", new String[] { "..." });
		specials.put("*", new String[] {});
		specials.put("!", new String[] { "!=" });
	}

	/**
	 * Internal transformer method to remove unnecessary WhiteSpace.TOKEN or
	 * insert missing once. Method works iterative over all tokens by splitting
	 * them. <br />
	 * <br />
	 * First all unprotected tokens would be isolate by inserting
	 * WhiteSpace.TOKEN. <br />
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
	public static final String transform(final ITMQLRuntime runtime, final String query) {
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
			 * add transformed token to buffer and insert one WhiteSpace.TOKEN
			 */
			buffer.append(token.trim() + WhiteSpace.TOKEN);
		}

		return buffer.toString().trim();
	}

	/**
	 * Transformation of TMQL tokens <br />
	 * <br />
	 * First all unprotected tokens would be isolate by inserting
	 * WhiteSpace.TOKEN. <br />
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
		StringBuilder builder = new StringBuilder();
		/*
		 * iterate over all tokens
		 */
		while (tokenizer.hasMoreTokens()) {
			String t = tokenizer.nextToken();
			/*
			 * the pattern // will be detect as URI but at the beginning they
			 * represent the type-instance-association
			 */
			if (t.startsWith(BACKSLASH)) {
				builder.append(BACKSLASH);
				builder.append(WhiteSpace.TOKEN);
				t = t.substring(2);
			}
			builder.append(uriProtectionAlgorithm(runtime, t));
			builder.append(WhiteSpace.TOKEN);
		}
		return builder.toString();
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
	private static String uriProtectionAlgorithm(final ITMQLRuntime runtime, final String token) {
		String cleaned = new String(token);

		/*
		 * check if token is %prefix or %pragma
		 */
		if (token.equalsIgnoreCase(Prefix.TOKEN) || token.equalsIgnoreCase(Pragma.TOKEN)) {
			return token;
		}
		/*
		 * check if token is a string
		 */
		else if (token.length() > 2 && token.startsWith(QUOTE) && token.endsWith(QUOTE) && token.charAt(1) != CHAR_QUOTE && token.charAt(token.length() - 2) != CHAR_QUOTE) {
			return token;
		}

		/*
		 * check protected strings with data-type
		 */
		if (token.length() > 2 && token.startsWith(QUOTE) && token.contains(DATATYPE)) {
			return token;
		}

		/*
		 * save time and dateTime
		 */
		if (LiteralUtils.isDate(token) || LiteralUtils.isTime(token) || LiteralUtils.isDateTime(token) || LiteralUtils.isInteger(token) || LiteralUtils.isDecimal(token)) {
			return token;
		}
		/*
		 * is known pattern
		 */
		if (patterns.contains(token.trim())) {
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
			for (int windowStart = 0; windowStart + windowSize <= token.length(); windowStart++) {
				/*
				 * extract IRI candidate
				 */
				String candidate = cleaned.substring(windowStart, windowSize + windowStart);
				/*
				 * check if token is protected ( XML, IRI, string )
				 */
				if (isProtected(runtime, candidate)) {
					StringBuilder builder = new StringBuilder();
					/*
					 * method add cleaned token before detected candidate
					 */
					if (windowStart > 0) {
						builder.append(uriProtectionAlgorithm(runtime, cleaned.substring(0, windowStart)));
					}
					builder.append(WhiteSpace.TOKEN);
					builder.append(candidate);
					builder.append(WhiteSpace.TOKEN);
					/*
					 * method add cleaned token after detected candidate
					 */
					if (windowStart + windowSize < cleaned.length()) {
						builder.append(uriProtectionAlgorithm(runtime, cleaned.substring(windowStart + windowSize)));
					}
					return builder.toString();

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
	private static String transformSecure(final ITMQLRuntime runtime, final String token) {
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
				String temporaryString;
				if (index > 0) {
					temporaryString = cleaned.substring(index - 1);
				} else {
					temporaryString = cleaned.substring(index);
				}				
				if (temporaryString.length() > 4) {
					temporaryString = temporaryString.substring(0, 4);
				} 
				/*
				 * check if isolated query part matches a protection of current
				 * token
				 */
				boolean matches = false;
				for (String pattern : entry.getValue()) {
					if (temporaryString.contains(pattern)) {
						matches = true;
						break;
					}
				}
				/*
				 * token at current position is not protected -> insert
				 * white-spaces
				 */
				if (!matches) {
					StringBuilder builder = new StringBuilder();
					builder.append(cleaned.substring(0, index));
					builder.append(WhiteSpace.TOKEN);
					builder.append(entry.getKey());
					builder.append(WhiteSpace.TOKEN);
					builder.append(cleaned.substring(index + entry.getKey().length()));
					index += 2;
					cleaned = builder.toString();
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
	private static String transformPatterns(final ITMQLRuntime runtime, final String token) {
		String cleaned = new String(token);

		/*
		 * protect strings
		 */
		if (token.length() > 2 && token.startsWith(QUOTE) && token.endsWith(QUOTE)) {
			return token;
		}

		/*
		 * check protected strings with data-type
		 */
		if (token.length() > 2 && token.startsWith(QUOTE) && token.contains(DATATYPE)) {
			return token;
		}

		/*
		 * save time and dateTime
		 */
		if (LiteralUtils.isDate(token) || LiteralUtils.isTime(token) || LiteralUtils.isDateTime(token)) {
			return token;
		}

		/*
		 * isolate patterns using white-spaces
		 */
		for (String pattern : patterns) {
			String tmp = cleaned.replaceAll(SLASH + pattern, WhiteSpace.TOKEN + SLASH + pattern + WhiteSpace.TOKEN);
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
	private static boolean isProtected(final ITMQLRuntime runtime, final String token) {

		String uri = token;
		/*
		 * If token starts with < and ends with > and do not contain any
		 * WhiteSpace.TOKEN characters than it will be detect as URI or XML
		 */
		if (token.startsWith(TOKEN_IRI_START) && token.endsWith(TOKEN_IRI_END)) {
			return true;
		} else if (token.startsWith(QUOTE) && token.endsWith(QUOTE)) {
			return true;
		} else if (patterns.contains(token.trim())) {
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
		StringTokenizer tokenizer = new StringTokenizer(token, TOKEN_COLON);
		StringBuilder builder = new StringBuilder();
		while (tokenizer.hasMoreTokens()) {
			String tok = tokenizer.nextToken();
			if (handler.isKnownPrefix(tok)) {
				builder.append(handler.getPrefix(tok));
			} else {
				builder.append(tok);
				if (tokenizer.hasMoreTokens()) {
					builder.append(TOKEN_COLON);
				}
			}
		}

		/*
		 * Check if given token is an relative URI
		 */
		try {
			new URI(builder.toString());
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
