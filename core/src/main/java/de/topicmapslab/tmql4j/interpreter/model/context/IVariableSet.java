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

import java.util.Collection;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

/**
 * Interface definition of a variable binding in the context of the querying
 * model of the current TMQL draft.
 * <p>
 * A variable binding connects one particular variable with a value. A binding
 * set is a set of such bindings, with the constraint that one particular
 * variable may only appear once.
 * </p>
 * <p>
 * 
 * Once a variable is bound to a particular value, this binding cannot be
 * changed. The same variable can get a different value in another binding,
 * though, hiding the former binding (immutability of variables).
 * 
 * During the course of the nested evaluation of a query expression a processor
 * will maintain stack of binding sets, the variable context (short: context).
 * Whenever variables are introduced, a corresponding set of bindings will be
 * generated. These binding sets will be pushed onto the context for the
 * duration of the evaluation of the inner expressions. At the end of that
 * evaluation the last binding set is popped from the context.
 * </p>
 * <p>
 * A processor will always maintain the following constraints on contexts:
 * <p>
 * 1. The value of a particular variable in the context is determined by a
 * binding for that variable in that binding set which has been pushed last onto
 * the context.
 * </p>
 * <p>
 * 2. If the variable names differ only in the number of primes, then their
 * values MUST differ.
 * </p>
 * <p>
 * 3. Any two different variables may be bound to different or the same values.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IVariableSet {

	/**
	 * Method deliver all stored {@link IVariableDefinition}s of the set.
	 * 
	 * @return all {@link IVariableDefinition}s
	 */
	Collection<String> getVariables();

	/**
	 * Method deliver the value of the variable defined by the given name
	 * 
	 * @param variable
	 *            the name of the variable
	 * @return the value of the variable
	 * @throws TMQLRuntimeException
	 *             thrown if the variable is unknown
	 */
	Object getValue(final String variable) throws TMQLRuntimeException;

	/**
	 * Method set the value of the variable defined by the given name
	 * 
	 * @param variable
	 *            the name of the variable
	 * @param value
	 *            the new value of the variable
	 * @throws TMQLRuntimeException
	 *             thrown if the value can not be
	 *             set
	 */
	void setValue(final String variable, Object value)
			throws TMQLRuntimeException;

	/**
	 * Method creates a copy of the {@link IVariableSet} by copying all
	 * {@link IVariableDefinition}s without adding their current values.
	 * 
	 * @return the copy
	 * @throws TMQLRuntimeException
	 *             thrown if the copying process fails
	 */
	public IVariableSet copy() throws TMQLRuntimeException;

	/**
	 * Method creates a copy of the {@link IVariableSet} by copying all
	 * {@link IVariableDefinition}s with adding their current values.
	 * 
	 * @return the copy
	 * @throws TMQLRuntimeException
	 *             thrown if the copying process fails
	 */
	public IVariableSet copyWithValues() throws TMQLRuntimeException;

	/**
	 * Method checks if the given definition is part of the internal set
	 * 
	 * @param variable
	 *            the name of the variable
	 * @return <code>true</code> if variable definition is part of the internal
	 *         set, <code>false</code> otherwise.
	 */
	boolean contains(String variable);

	/**
	 * Deliver internal flag, specified if the the set is an unique binding set.
	 * 
	 * @return If the value is <code>true</code>, each variable definition can
	 *         only be bind to a value one-time. If it returns
	 *         <code>false</code>, there are no restriction of binding count.
	 */
	boolean isUniqueBindingSet();

	/**
	 * Removes the internal variable definition if it exists
	 * 
	 * @param variable
	 *            the name of the variable definition to remove
	 *@throws TMQLRuntimeException
	 *             if the operation fails
	 */
	public void remove(final String variable)
			throws TMQLRuntimeException;

}
