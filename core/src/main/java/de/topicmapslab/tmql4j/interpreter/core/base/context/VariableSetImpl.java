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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Base implementation of {@link IVariableSet} to define basic functions of each
 * variable set of TMQL.
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
public class VariableSetImpl implements IVariableSet {

	/**
	 * internal map containing the variable bindings
	 */
	private Map<String, Object> map = HashUtil.getHashMap();

	/**
	 * base constructor to create an instance
	 */
	public VariableSetImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getVariables() {
		return map.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String variablename) throws TMQLRuntimeException {
		if (map.containsKey(variablename)) {
			return map.get(variablename);
		}
		throw new TMQLRuntimeException("Variable '" + variablename
				+ "' not contained by the current variable layer.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(String variable, Object value)
			throws TMQLRuntimeException {
		if (map.containsKey(variable)) {
			/*
			 * variable has currently no binding
			 */
			if (map.get(variable) == null) {
				map.put(variable, value);
			}
			/*
			 * variable has currently an existing binding, but is not defined as
			 * uniquely binding set
			 */
			else if (!isUniqueBindingSet()) {
				map.put(variable, value);
			}
		} else {
			map.put(variable, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public VariableSetImpl copy() throws TMQLRuntimeException {
		VariableSetImpl set = new VariableSetImpl();
		for (String variable : map.keySet()) {
			set.setValue(variable, getValue(variable));
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public IVariableSet copyWithValues() throws TMQLRuntimeException {
		IVariableSet set = copy();
		for (Entry<String, Object> entry : map.entrySet()) {
			set.setValue(entry.getKey(), entry.getValue());
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueBindingSet() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(String variable) {
		return map.containsKey(variable);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(String variable) throws TMQLRuntimeException {
		if (contains(variable)) {
			map.remove(variable);
		}
	}
}
