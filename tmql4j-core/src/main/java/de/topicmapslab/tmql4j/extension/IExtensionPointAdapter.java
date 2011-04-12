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
package de.topicmapslab.tmql4j.extension;

import java.util.Set;

import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Extension adapter importing the register extensions using Java service
 * providers.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IExtensionPointAdapter {

	/**
	 * Loading all included extensions contained in the class path. The method
	 * uses a service provider of the interface {@link IExtensionPoint} to get
	 * all implementing extensions.
	 * 
	 * @throws TMQLExtensionRegistryException
	 *             thrown if a duplicated id will be found
	 */
	public void loadExtensionPoints() throws TMQLExtensionRegistryException;

	/**
	 * Getter of a set of language extension applicable for the given expression
	 * type.
	 * 
	 * @param expressionType
	 *            the expression type
	 * @return a set of the language extensions or <code>null</code>
	 */
	public Set<ILanguageExtension> getLanguageExtensions(Class<? extends IExpression> expressionType);

	/**
	 * Getter of a set of language extension registered by the given extension
	 * point
	 * 
	 * @param point
	 *            the language extension
	 * @return a set of the language extensions or <code>null</code>
	 */
	public Set<ILanguageExtension> getLanguageExtensions(IExtensionPoint point);

	/**
	 * Enable the extension point with the given id if it exists
	 * 
	 * @param extensionPointId
	 *            the id
	 */
	public void enableExtensionPoint(final String extensionPointId);

	/**
	 * Disable the extension point with the given id if it exists
	 * 
	 * @param extensionPointId
	 *            the id
	 */
	public void disableExtensionPoint(final String extensionPointId);

}
