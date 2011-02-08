/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module;

import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IFunctionRegistry;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Internal registry for function classes. Provides access to registered
 * functions and enables the registration of new functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class FunctionRegistryImpl implements IFunctionRegistry {

	/**
	 * internal store of all known function interpreter
	 */
	private final Map<String, Class<? extends IFunction>> functions = HashUtil.getHashMap();

	/**
	 * the TMQL runtime instance
	 */
	private final ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 */
	public FunctionRegistryImpl(ITMQLRuntime runtime) {
		this.runtime = runtime;
		initialize();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void registerFunction(String itemIdentifier, Class<? extends IFunction> interpreter) {
		functions.put(itemIdentifier, interpreter);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Class<? extends IFunction> getFunction(final String itemIdentifier) {
		/*
		 * get stored interpreter class
		 */
		Class<? extends IFunction> interpreter = functions.get(itemIdentifier);
		return interpreter;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isKnownFunction(final String identifier) {
		return functions.containsKey(identifier);
	}

	/**
	 * Initialization method
	 */
	protected abstract void initialize();

	/**
	 * Returns the stored reference of the runtime
	 * 
	 * @return the runtime
	 */
	protected ITMQLRuntime getRuntime() {
		return runtime;
	}
}
