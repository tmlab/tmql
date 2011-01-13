/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * @author Sven Krosse
 * 
 */
public interface ITmqlSqlTranslator<T extends IExpression> {

	/**
	 * Returns the type of expression which can be handled by this SQL
	 * transformer
	 * 
	 * @return the expression type
	 */
	public Class<T> getType();

	/**
	 * Checks if the given expression is applicable for this translator
	 * 
	 * @param expression
	 *            the expression
	 * @return <code>true</code> if the expression can be handled by this
	 *         translator, <code>false</code> otherwise.
	 */
	public boolean isApplicable(IExpression expression);

	/**
	 * Transform the given expression by using the given state
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param state
	 *            the current state of state machine
	 * @return the new state of state machine
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public IState transform(ITMQLRuntime runtime, IContext context, IExpression expression, IState state) throws TMQLRuntimeException;

}
