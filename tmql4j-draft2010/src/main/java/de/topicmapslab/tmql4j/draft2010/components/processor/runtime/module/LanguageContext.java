/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor.runtime.module;

import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PrefixHandler;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IFunctionRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

public class LanguageContext implements ILanguageContext {

	/**
	 * a set containing all allowed expression types
	 */
	private final Set<Class<? extends IExpression>> allowedExpressionTypes;

	/**
	 * the function register
	 */
	private final FunctionRegistryImpl functionRegistry;
	/**
	 * the prefix handler
	 */
	private final PrefixHandler prefixHandler;
	/**
	 * the token register
	 */
	private final TokenRegistryImpl tokenRegistry;
	/**
	 * the interpreter register
	 */
	private final InterpreterRegistryImpl interpreterRegistry;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the parent runtime
	 * @throws TMQLInitializationException
	 *             thrown if initialization fails
	 */
	public LanguageContext(final ITMQLRuntime runtime) throws TMQLInitializationException {
		this.functionRegistry = new FunctionRegistry(runtime);
		this.prefixHandler = new PrefixHandler();
		this.tokenRegistry = new TokenRegistry(runtime);
		this.interpreterRegistry = new InterpreterRegistry();
		this.allowedExpressionTypes = HashUtil.getHashSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFunctionRegistry getFunctionRegistry() {
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
	public ITokenRegistry getTokenRegistry() {
		return tokenRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public IInterpreterRegistry getInterpreterRegistry() {
		return interpreterRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Class<? extends IExpression>> getAllowedExpressionTypes() {
		return allowedExpressionTypes;
	}
}
