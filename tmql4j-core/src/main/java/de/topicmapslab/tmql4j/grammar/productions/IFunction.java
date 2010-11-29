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
package de.topicmapslab.tmql4j.grammar.productions;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;

/**
 * Interface definition of a {@link IExpressionInterpreter} representing an
 * interpreter of a specific TMQL-function which will be defined by the current
 * TMQL draft
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IFunction<T extends IExpression> {

	/**
	 * Checks if the number of parameters is valid for the current function
	 * implementation.
	 * 
	 * @param numberOfParameters
	 *            the current number of parameters
	 * @return <code>true</code> if the number of parameters is supported,
	 *         <code>false</code> otherwise.
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters);

	/**
	 * Method return the internal subject-identifier of the TMQL-function which
	 * can be interpreted by this instance. The identifier are in the name-space
	 * fn and will be specified by the current draft.
	 * 
	 * @return the subject-identifier of the corresponding topic of this
	 *         function
	 */
	public String getItemIdentifier();

	/**
	 * Method called to interpret the function.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @return the result of querying
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context);

}
