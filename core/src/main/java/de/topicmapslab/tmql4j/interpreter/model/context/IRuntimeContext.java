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
package de.topicmapslab.tmql4j.interpreter.model.context;

import java.util.Stack;


/**
 * Interface definition of the runtime context which will be used during the
 * querying process of each TMQL4J execution.
 * <p>
 * The runtime context represents the current state of the process chain. It
 * stores all information about the current variable states and handle the
 * access of the internal variable stack.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IRuntimeContext {

	/**
	 * Method removes the current variable set form the top of the stack and
	 * returned it. This operation is redirected to {@link Stack#pop()}.
	 * 
	 * @return the removed {@link IVariableSet}
	 */
	public IVariableSet pop();

	/**
	 * Method returns the variable set form the top of the stack. This operation
	 * is redirected to {@link Stack#peek()}.
	 * 
	 * @return the current {@link IVariableSet}
	 */
	public IVariableSet peek();

	/**
	 * Method puts the given variable set on the top of the stack. This
	 * operation is redirected to {@link Stack#push()}.
	 * 
	 * @param set
	 *            the new {@link IVariableSet}
	 */
	void push(IVariableSet set);

	/**
	 * Method creates a new instance of {@link IVariableSet} and puts it on the
	 * top of the stack. This operation is redirected to {@link Stack#push()}.
	 * 
	 * @return the new {@link IVariableSet}
	 */
	IVariableSet push();

	/**
	 * Method deliver the initial context of the instance of the TMQL engine
	 * 
	 * @return the initial context
	 */
	public IInitialContext getInitialContext();

	/**
	 * Method removes all variables sets from the stack and clean the state of
	 * the process-chain. After calling this method the process-chain will be
	 * returned to start-position.
	 */
	public void clearAll();

	/**
	 * Method checks if at least one variable set will be on the top of the
	 * stack.
	 * 
	 * @return <code>true</code> if the stack is empty, <code>false</code>
	 *         otherwise.
	 */
	public boolean isStackEmty();

}
