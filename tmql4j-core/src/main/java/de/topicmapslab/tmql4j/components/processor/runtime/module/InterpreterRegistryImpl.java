/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.interpreter.PragmaInterpreter;
import de.topicmapslab.tmql4j.components.interpreter.PreparedExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.Pragma;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Internal registry for interpreter classes. Provides access to interpreter
 * instances for all TMQL-expression types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class InterpreterRegistryImpl implements IInterpreterRegistry {

	/**
	 * internal registry cache containing all mappings
	 */
	private final Map<Class<? extends IExpression>, Class<? extends IExpressionInterpreter<?>>> registry = HashUtil.getHashMap();

	/**
	 * constructor
	 */
	public InterpreterRegistryImpl() {
		registerInterpreterClass(PreparedExpression.class, PreparedExpressionInterpreter.class);
		registerInterpreterClass(Pragma.class, PragmaInterpreter.class);
		initialize();
	}

	/**
	 * Intialization method
	 */
	protected abstract void initialize();

	/**
	 * 
	 * {@inheritDoc}
	 */
	public IExpressionInterpreter<?> interpreterInstance(IExpression ex) throws TMQLRuntimeException {
		if (registry.containsKey(ex.getClass())) {
			try {
				return registry.get(ex.getClass()).getConstructor(ex.getClass()).newInstance(ex);
			} catch (IllegalArgumentException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			} catch (SecurityException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			} catch (InstantiationException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			} catch (IllegalAccessException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			} catch (InvocationTargetException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			} catch (NoSuchMethodException e) {
				throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName(), e);
			}
		}
		throw new TMQLRuntimeException("Cannot create interpreter instance for expression-type " + ex.getClass().getSimpleName());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public <T extends IExpression> void registerInterpreterClass(Class<T> expressionClass, Class<? extends IExpressionInterpreter<T>> interpreterClass) throws TMQLInitializationException {
		if (expressionClass == null || interpreterClass == null) {
			throw new TMQLInitializationException("parameters may not be null.");
		}
		registry.put(expressionClass, interpreterClass);
	}
}
