/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.model.runtime;

import java.util.Set;

import de.topicmapslab.tmql4j.common.context.FunctionRegistry;
import de.topicmapslab.tmql4j.common.context.InterpreterRegistry;
import de.topicmapslab.tmql4j.common.context.PrefixHandler;
import de.topicmapslab.tmql4j.lexer.core.TokenRegistry;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Interface definition of the context store all import informations used for
 * the language interpretation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILanguageContext {

	/**
	 * Method get access to the internal instance of the token registry
	 * 
	 * @return the reference of the token registry
	 */
	public TokenRegistry getTokenRegistry();

	/**
	 * Method get access to the internal instance of the interpreter registry
	 * 
	 * @return the reference of the interpreter registry
	 */
	public InterpreterRegistry getInterpreterRegistry();

	/**
	 * Method get access to the internal instance of the function registry
	 * 
	 * @return the reference of the function registry
	 */
	public FunctionRegistry getFunctionRegistry();

	/**
	 * Method get access to the internal instance of the prefix handler
	 * 
	 * @return the reference of the prefix handler
	 */
	public PrefixHandler getPrefixHandler();
	
	/**
	 * Method returns the internal defined list of allowed expression types
	 * 
	 * @return a set containing the expression classes representing the allowed
	 *         expression
	 */
	public Set<Class<? extends IExpression>> getAllowedExpressionTypes();
}
