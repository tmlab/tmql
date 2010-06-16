/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.exception;

import java.util.List;

import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * The exception is thrown if the given query is invalid. Exception caused by
 * internal parser.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLInvalidSyntaxException extends TMQLParserException {

	private static final long serialVersionUID = 1L;
	private final List<Class<? extends IToken>> tmqlTokens;
	private final List<String> tokens;
	private final Class<? extends IExpression> clazz;

	/**
	 * constructor
	 * 
	 * @param tmqlTokens
	 *            the set of TMQL tokens which affected this exception
	 * @param tokens
	 *            the set of tokens which affected this exception
	 * @param clazz
	 *            the expression thrown this exception
	 */
	public TMQLInvalidSyntaxException(
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, Class<? extends IExpression> clazz) {
		super("Invalid syntax detected in production " + clazz.getSimpleName());
		this.tokens = tokens;
		this.tmqlTokens = tmqlTokens;
		this.clazz = clazz;
	}

	/**
	 * constructor
	 * 
	 * @param tmqlTokens
	 *            the set of TMQL tokens which affected this exception
	 * @param tokens
	 *            the set of tokens which affected this exception
	 * @param clazz
	 *            the expression thrown this exception
	 * @param cause
	 *            the cause
	 */
	public TMQLInvalidSyntaxException(
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, Class<? extends IExpression> clazz,
			Throwable cause) {
		super(cause);
		this.tokens = tokens;
		this.tmqlTokens = tmqlTokens;
		this.clazz = clazz;
	}

	/**
	 * constructor
	 * 
	 * @param tmqlTokens
	 *            the set of TMQL tokens which affected this exception
	 * @param tokens
	 *            the set of tokens which affected this exception
	 * @param clazz
	 *            the expression thrown this exception
	 * @param message
	 *            a message containing information about the reason of this
	 *            exception
	 * @param cause
	 *            the cause
	 */
	public TMQLInvalidSyntaxException(
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, Class<? extends IExpression> clazz,
			String message) {
		super(message);
		this.tokens = tokens;
		this.tmqlTokens = tmqlTokens;
		this.clazz = clazz;
	}

	/**
	 * constructor
	 * 
	 * @param tmqlTokens
	 *            the set of TMQL tokens which affected this exception
	 * @param tokens
	 *            the set of tokens which affected this exception
	 * @param clazz
	 *            the expression thrown this exception
	 * @param message
	 *            a message containing information about the reason of this
	 *            exception
	 * @param cause
	 *            the cause
	 */
	public TMQLInvalidSyntaxException(
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, Class<? extends IExpression> clazz,
			String message, Throwable cause) {
		super(message, cause);
		this.tokens = tokens;
		this.tmqlTokens = tmqlTokens;
		this.clazz = clazz;
	}

	/**
	 * Getter of the language-specific-representation of the tokens affected
	 * this exception
	 * 
	 * @return a list of language-specific-represented tokens
	 */
	public List<Class<? extends IToken>> getTmqlTokens() {
		return tmqlTokens;
	}

	/**
	 * Getter of the string-representation of the tokens affected this exception
	 * 
	 * @return a list of string-represented tokens
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * Getter of the expression type thrown this exception
	 * 
	 * @return the class object
	 */
	public Class<? extends IExpression> getClazz() {
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();

		builder.append("Message: " + super.getMessage() + "\r\n");
		builder.append("Expression: " + clazz.getSimpleName() + "\r\n");
		builder.append("language-specific Tokens: ");
		boolean first = true;
		for (Class<? extends IToken> clazz : tmqlTokens) {
			builder.append((first ? " " : ", ") + clazz.getSimpleName());
		}
		builder.append("\r\n");
		builder.append("string-represented Tokens: " + tokens.toString()
				+ "\r\n");

		return builder.toString();
	}
}
