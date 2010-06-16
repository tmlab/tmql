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
package de.topicmapslab.tmql4j.interpreter.core.base.context;

import java.util.Stack;

import de.topicmapslab.tmql4j.common.core.exception.TMQLException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;
import de.topicmapslab.tmql4j.interpreter.model.context.IRuntimeContext;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Base implementation of {@link IRuntimeContext}.
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
public class RuntimeContextImpl implements IRuntimeContext {

	/**
	 * reference of the {@link IInitialContext}
	 */
	private final IInitialContext initialContext;

	private final Stack<IVariableSet> stack;

	/**
	 * Constructor
	 * 
	 * @param initialContext
	 *            the predefined initial context
	 * @throws TMQLException
	 *             thrown if initialization failed
	 */
	public RuntimeContextImpl(final IInitialContext initialContext)
			throws TMQLRuntimeException {
		this.initialContext = initialContext;
		this.stack = new Stack<IVariableSet>();
	}

	/**
	 * {@inheritDoc}
	 */
	public IInitialContext getInitialContext() {
		return initialContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized IVariableSet peek() {
		return stack.peek();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized IVariableSet pop() {
		return stack.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void push(IVariableSet set) {
		stack.push(set);
	}

	/**
	 * {@inheritDoc}
	 */
	public IVariableSet push() {
		try {
			IVariableSet set = peek().copyWithValues();
			set.remove(VariableNames.QUERYMATCHES);
			push(set);
			return set;
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearAll() {
		stack.clear();
	}

	public boolean isStackEmty() {
		return stack.isEmpty();
	}

}
