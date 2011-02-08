/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module.model;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;

/**
 * Internal registry for function classes. Provides access to registered
 * functions and enables the registration of new functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IFunctionRegistry {

	/**
	 * Register a new function in the TMQL engine. Please note that only
	 * registered functions can be used in context of a TMQL query.
	 * 
	 * @param itemIdentifier
	 *            the identifier of the function
	 * @param interpreter
	 *            the interpreter class of the new function
	 */
	public void registerFunction(String itemIdentifier, Class<? extends IFunction> interpreter);

	/**
	 * Method returns the stored interpreter class for a TMQL function
	 * identifies by the given identifier. If the identifier is relative the
	 * runtime tries to resolve the absolute IRI.
	 * 
	 * @param itemIdentifier
	 *            the function identifier
	 * @return the interpreter class if the identifier is known,
	 *         <code>null</code> otherwise
	 */
	public Class<? extends IFunction> getFunction(final String itemIdentifier);

	/**
	 * Method checks if the given identifier is used as identification of a
	 * registered function.
	 * 
	 * @param identifier
	 *            the identifier
	 * @return <code>true</code> if the identifier is known as function,
	 *         <code>false</code> otherwise
	 */
	public boolean isKnownFunction(final String identifier);
}
