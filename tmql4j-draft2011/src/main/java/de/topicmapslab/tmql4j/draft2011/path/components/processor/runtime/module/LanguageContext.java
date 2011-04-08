/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PragmaRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PrefixHandler;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistryImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IFunctionRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IPragmaRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;

public class LanguageContext implements ILanguageContext {
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
	 * the pragma register
	 */
	private final PragmaRegistry pragmaRegistry;

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
		this.pragmaRegistry = new PragmaRegistry();
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
	public IPragmaRegistry getPragmaRegistry() {
		return pragmaRegistry;
	}
}
