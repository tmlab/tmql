/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module.model;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.exception.TMQLException;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Internal registry for interpreter classes. Provides access to interpreter
 * instances for all TMQL-expression types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IInterpreterRegistry {

	/**
	 * Method try to create a instance of the interpreter, which is used to
	 * interpret the TMQL-expression of the given type
	 * 
	 * @param ex
	 *            the expression which should be interpret by the new
	 *            interpreter
	 * @return a new interpreter instance and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if no interpreter class is registered for the given
	 *             expression type
	 */
	public IExpressionInterpreter<?> interpreterInstance(IExpression ex) throws TMQLRuntimeException;

	/**
	 * Method is used to register a new interpreter module for a TMQL-expression
	 * type.
	 * 
	 * @param expressionClass
	 *            the TMQL-expression type
	 * @param interpreterClass
	 *            the new interpreter module class
	 * @throws TMQLException
	 *             thrown if at least one parameter is <code>null</code>
	 */
	public <T extends IExpression> void registerInterpreterClass(Class<T> expressionClass, Class<? extends IExpressionInterpreter<T>> interpreterClass) throws TMQLInitializationException;
}
