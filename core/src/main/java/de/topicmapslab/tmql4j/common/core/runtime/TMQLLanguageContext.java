/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.runtime;

import java.util.Set;

import de.topicmapslab.tmql4j.common.context.FunctionRegistry;
import de.topicmapslab.tmql4j.common.context.InterpreterRegistry;
import de.topicmapslab.tmql4j.common.context.PrefixHandler;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.model.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.core.TokenRegistry;
import de.topicmapslab.tmql4j.parser.model.IExpression;

public class TMQLLanguageContext implements ILanguageContext {

	/**
	 * a set containing all allowed expression types
	 */
	private final Set<Class<? extends IExpression>> allowedExpressionTypes;

	/**
	 * the function registry
	 */
	private final FunctionRegistry functionRegistry;
	/**
	 * the interpreter registry
	 */
	private final InterpreterRegistry interpreterRegistry;
	/**
	 * the prefix handler
	 */
	private final PrefixHandler prefixHandler;
	/**
	 * the token registry
	 */
	private final TokenRegistry tokenRegistry;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the parent runtime
	 * @param allowedExpressionTypes
	 *            a list of all allowed expression types
	 * @throws TMQLInitializationException
	 *             thrown if initialization fails
	 */
	public TMQLLanguageContext(final ITMQLRuntime runtime,
			Class<? extends IExpression>... allowedExpressionTypes)
			throws TMQLInitializationException {
		this.functionRegistry = new FunctionRegistry(runtime);
		this.interpreterRegistry = new InterpreterRegistry();
		this.prefixHandler = new PrefixHandler();
		this.tokenRegistry = new TokenRegistry(runtime);
		this.allowedExpressionTypes = HashUtil.getHashSet();
		for (Class<? extends IExpression> type : allowedExpressionTypes) {
			this.allowedExpressionTypes.add(type);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public FunctionRegistry getFunctionRegistry() {
		return functionRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public InterpreterRegistry getInterpreterRegistry() {
		return interpreterRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public PrefixHandler getPrefixHandler() {
		return prefixHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public TokenRegistry getTokenRegistry() {
		return tokenRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Class<? extends IExpression>> getAllowedExpressionTypes() {
		return allowedExpressionTypes;
	}
}
