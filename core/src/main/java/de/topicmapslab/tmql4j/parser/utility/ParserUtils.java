package de.topicmapslab.tmql4j.parser.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleClose;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundClose;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareClose;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;

/**
 * Utility class defining some helpful methods for parser.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ParserUtils {

	/**
	 * define opening brackets as beginning of protected section
	 */
	private final static Set<Class<? extends IToken>> protectionStarts = HashUtil
			.getHashSet();
	static {
		protectionStarts.add(BracketRoundOpen.class);
		protectionStarts.add(BracketAngleOpen.class);
		protectionStarts.add(BracketSquareOpen.class);
	}

	/**
	 * define closing brackets as end of protected section
	 */
	private final static Set<Class<? extends IToken>> protectionEnds = HashUtil
			.getHashSet();
	static {
		protectionEnds.add(BracketRoundClose.class);
		protectionEnds.add(BracketAngleClose.class);
		protectionEnds.add(BracketSquareClose.class);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return <code>true</code> if the token is contained in a non-protected
	 *         section, <code>false</code> otherwise.
	 */
	public static boolean containsTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Class<? extends IToken>... tokensToFound) {
		return containsTokens(tmqlTokens, Arrays.asList(tokensToFound),
				protectionStarts, protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return <code>true</code> if the token is contained in a non-protected
	 *         section, <code>false</code> otherwise.
	 */
	public static boolean containsTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound) {
		return containsTokens(tmqlTokens, tokensToFound, protectionStarts,
				protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a tokens which are part of the expression and
	 * not contained by a protected section
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @return <code>true</code> if the token is contained in a non-protected
	 *         section, <code>false</code> otherwise.
	 */
	public static boolean containsTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds) {
		return -1 != indexOfTokens(tmqlTokens, tokensToFound, protectionStarts,
				protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return the index of found token or -1 if no token was found
	 */
	public static int indexOfTokens(List<Class<? extends IToken>> tmqlTokens,
			Class<? extends IToken>... tokensToFound) {
		return indexOfTokens(tmqlTokens, Arrays.asList(tokensToFound),
				protectionStarts, protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return the index of found token or -1 if no token was found
	 */
	public static int indexOfTokens(List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound) {
		return indexOfTokens(tmqlTokens, tokensToFound, protectionStarts,
				protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a tokens which are part of the expression and
	 * not contained by a protected section
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @return the index of found token or -1 if no token was found
	 */
	public static int indexOfTokens(List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds) {
		long protectionCount = 0;
		Class<? extends IToken> last = null;
		for (int index = 0; index < tmqlTokens.size(); index++) {
			Class<? extends IToken> token = tmqlTokens.get(index);
			if (protectionStarts.contains(token)) {
				protectionCount++;
			} else if (protectionEnds.contains(token)) {
				protectionCount--;
			} else if (protectionCount == 0 && tokensToFound.contains(token)
					&& (last == null || !tokensToFound.contains(last))) {
				return index;
			}
			last = token;
		}
		return -1;
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return the last index of found token or -1 if no token was found
	 */
	public static int lastIndexOfTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Class<? extends IToken>... tokensToFound) {
		return lastIndexOfTokens(tmqlTokens, Arrays.asList(tokensToFound),
				protectionStarts, protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a token which are part of the expression and
	 * not cramped.
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @return the last index of found token or -1 if no token was found
	 */
	public static int lastIndexOfTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound) {
		return lastIndexOfTokens(tmqlTokens, tokensToFound, protectionStarts,
				protectionEnds);
	}

	/**
	 * Method checks if there is a token of the given collection contained in
	 * the other token list. Only a tokens which are part of the expression and
	 * not contained by a protected section
	 * 
	 * @param tmqlTokens
	 *            the token list
	 * @param tokensToFound
	 *            the tokens to look for
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @return the last index of found token or -1 if no token was found
	 */
	public static int lastIndexOfTokens(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> tokensToFound,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds) {
		long protectionCount = 0;
		Class<? extends IToken> last = null;
		for (int index = tmqlTokens.size() - 1; index >= 0; index--) {
			Class<? extends IToken> token = tmqlTokens.get(index);
			if (protectionStarts.contains(token)) {
				protectionCount++;
			} else if (protectionEnds.contains(token)) {
				protectionCount--;
			} else if (protectionCount == 0 && tokensToFound.contains(token)
					&& (last == null || !tokensToFound.contains(last))) {
				return index;
			}
			last = token;
		}
		return -1;
	}

	/**
	 * Method splits the given token chain at each position of the given
	 * delimers. If a delimer is found the callback will be informed.
	 * 
	 * @param callback
	 *            the handler of a found delimer
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param tokens
	 *            the string-represented tokens to split
	 * @param delimers
	 *            the set of delimer tokens
	 * @param ignoreDelimer
	 *            indicates if the delimer should be a part add to sub lists
	 * 
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException redirect from the
	 *             {@link IParserUtilsCallback}
	 */
	public static void split(final IParserUtilsCallback callback,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens,
			final Collection<Class<? extends IToken>> delimers,
			final boolean ignoreDelimer) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * split tokens
		 */
		split(callback, tmqlTokens, tokens, delimers, ignoreDelimer,
				protectionStarts, protectionEnds);
	}

	/**
	 * Method splits the given token chain at each position of the given
	 * delimers. If a delimer is found the callback will be informed.
	 * 
	 * @param callback
	 *            the handler of a found delimer
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param tokens
	 *            the string-represented tokens to split
	 * @param delimers
	 *            the delimer tokens
	 * @param ignoreDelimer
	 *            indicates if the delimer should be a part add to sub lists
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException redirect from the
	 *             {@link IParserUtilsCallback}
	 */
	public static void split(final IParserUtilsCallback callback,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens,
			final Collection<Class<? extends IToken>> delimers,
			final boolean ignoreDelimer,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds)
			throws TMQLGeneratorException, TMQLInvalidSyntaxException {

		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();

		Iterator<Class<? extends IToken>> iteratorTmqlTokens = tmqlTokens
				.iterator();
		Iterator<String> iteratorTokens = tokens.iterator();

		long protectionCount = 0;
		while (iteratorTmqlTokens.hasNext() && iteratorTokens.hasNext()) {
			Class<? extends IToken> tmqlToken = iteratorTmqlTokens.next();
			String token = iteratorTokens.next();
			if (protectionStarts.contains(tmqlToken)) {
				protectionCount++;
			} else if (protectionEnds.contains(tmqlToken)) {
				protectionCount--;
			} else if (protectionCount == 0 && delimers.contains(tmqlToken)) {
				if (!tmqlTokens_.isEmpty()) {
					callback.newToken(tmqlTokens_, tokens_, tmqlToken);
					tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tokens_ = new LinkedList<String>();
				}
				if (ignoreDelimer) {
					continue;
				}
			}
			tmqlTokens_.add(tmqlToken);
			tokens_.add(token);
		}

		if (!tmqlTokens_.isEmpty()) {
			callback.newToken(tmqlTokens_, tokens_, null);
		}
	}

	/**
	 * Method returns a list containing each index of the given token. Tokens as
	 * part of the protected section will be ignored.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param keys
	 *            as key tokens which should be looked for
	 * @return a list of all indexes
	 */
	public static List<Integer> indizes(
			List<Class<? extends IToken>> tmqlTokens,
			Class<? extends IToken>... keys) {
		return indizes(tmqlTokens, Arrays.asList(keys), protectionStarts,
				protectionEnds);
	}

	/**
	 * Method returns a list containing each index of the given token. Tokens as
	 * part of the protected section will be ignored.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param keys
	 *            as key tokens which should be looked for
	 * @return a list of all indexes
	 */
	public static List<Integer> indizes(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> keys) {
		return indizes(tmqlTokens, keys, protectionStarts, protectionEnds);
	}

	/**
	 * Method returns a list containing each index of the given token. Tokens as
	 * part of the protected section will be ignored.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param keys
	 *            as key tokens which should be looked for
	 * 
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @return a list of all indexes
	 */
	public static List<Integer> indizes(
			List<Class<? extends IToken>> tmqlTokens,
			Collection<Class<? extends IToken>> keys,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds) {
		List<Integer> indizes = new LinkedList<Integer>();
		long protectionCount = 0;
		for (int index = 0; index < tmqlTokens.size(); index++) {
			Class<? extends IToken> token = tmqlTokens.get(index);
			if (protectionStarts.contains(token)) {
				protectionCount++;
			} else if (protectionEnds.contains(token)) {
				protectionCount--;
			} else if (protectionCount == 0 && keys.contains(token)) {
				indizes.add(index);
			}
		}
		return indizes;
	}

	/**
	 * Method extracts the embed queries of the given expression. The embed
	 * queries will be detected by the angle brackets '{' and '}' but only if
	 * they are not contained by a embed-query too.
	 * 
	 * @param callback
	 *            the handler of a found embed query
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException redirect from the
	 *             {@link IParserUtilsCallback}
	 */
	public static void getEmbedQueries(final IParserUtilsCallback callback,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create list representing tokens indicating the beginning of a
		 * protected section
		 */
		Set<Class<? extends IToken>> protectionStarts = HashUtil.getHashSet();
		protectionStarts.addAll(ParserUtils.protectionStarts);
		protectionStarts.remove(BracketAngleOpen.class);
		/*
		 * create list representing tokens indicating the end of a protected
		 * section
		 */
		Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
		protectionEnds.addAll(ParserUtils.protectionEnds);
		protectionEnds.remove(BracketAngleClose.class);

		getEmbedQueries(callback, tmqlTokens, tokens, protectionStarts,
				protectionEnds);
	}

	/**
	 * Method extracts the embed queries of the given expression. The embed
	 * queries will be detected by the angle brackets '{' and '}' but only if
	 * they are not contained by a embed-query too.
	 * 
	 * @param callback
	 *            the handler of a found embed query
	 * @param tmqlTokens
	 *            the language-specific tokens to split
	 * @param protectionStarts
	 *            a set of tokens which indicates the beginning of a protected
	 *            section
	 * @param protectionEnds
	 *            a set of tokens which indicates the end of a protected section
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException redirect from the
	 *             {@link IParserUtilsCallback}
	 */
	public static void getEmbedQueries(final IParserUtilsCallback callback,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens,
			Collection<Class<? extends IToken>> protectionStarts,
			Collection<Class<? extends IToken>> protectionEnds)
			throws TMQLGeneratorException, TMQLInvalidSyntaxException {

		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();

		Iterator<Class<? extends IToken>> iteratorTmqlTokens = tmqlTokens
				.iterator();
		Iterator<String> iteratorTokens = tokens.iterator();

		long protectionCount = 0;
		long embedLevel = 0;
		while (iteratorTmqlTokens.hasNext() && iteratorTokens.hasNext()) {
			Class<? extends IToken> tmqlToken = iteratorTmqlTokens.next();
			String token = iteratorTokens.next();
			if (embedLevel > 0 && protectionStarts.contains(tmqlToken)) {
				protectionCount++;
			} else if (embedLevel > 0 && protectionEnds.contains(tmqlToken)) {
				protectionCount--;
			} else if (protectionCount == 0
					&& tmqlToken.equals(BracketAngleOpen.class)) {
				embedLevel++;
				/*
				 * reset token lists
				 */
				if (embedLevel == 1) {
					callback.newToken(tmqlTokens_, tokens_,
							BracketAngleOpen.class);
					tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tokens_ = new LinkedList<String>();
					continue;
				}
			} else if (protectionCount == 0
					&& tmqlToken.equals(BracketAngleClose.class)) {
				embedLevel--;
				/*
				 * reset token lists
				 */
				if (embedLevel == 0) {
					callback.newToken(tmqlTokens_, tokens_,
							BracketAngleClose.class);
					tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tokens_ = new LinkedList<String>();
					continue;
				}
			}
			tmqlTokens_.add(tmqlToken);
			tokens_.add(token);
		}

		if (!tmqlTokens_.isEmpty()) {
			callback.newToken(tmqlTokens_, tokens_, null);
		}
	}
}
