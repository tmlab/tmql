/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime.module;

import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PrefixHandler;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistry;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

public class LanguageContext implements ILanguageContext {

	/**
	 * a set containing all allowed expression types
	 */
	private final Set<Class<? extends IExpression>> allowedExpressionTypes;

	/**
	 * the function register
	 */
	private final FunctionRegistry functionRegistry;
	/**
	 * the prefix handler
	 */
	private final PrefixHandler prefixHandler;
	/**
	 * the token register
	 */
	private final TokenRegistry tokenRegistry;
	/**
	 * the interpreter register
	 */
	private final InterpreterRegistry interpreterRegistry;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the parent runtime
	 * @throws TMQLInitializationException
	 *             thrown if initialization fails
	 */
	public LanguageContext(final ITMQLRuntime runtime) throws TMQLInitializationException {
		this.functionRegistry = new FunctionRegistry2007(runtime);
		this.prefixHandler = new PrefixHandler();
		this.tokenRegistry = new TokenRegistry2007(runtime);
		this.interpreterRegistry = new InterpreterRegistry2007();
		this.allowedExpressionTypes = HashUtil.getHashSet();
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
	public InterpreterRegistry getInterpreterRegistry() {
		return interpreterRegistry;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Class<? extends IExpression>> getAllowedExpressionTypes() {
		return allowedExpressionTypes;
	}
}
