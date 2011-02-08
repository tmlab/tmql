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

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;

/**
 * Base interface definition of an extension point of the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IExtensionPoint {

	/**
	 * Method called by the runtime to register the extension endpoint before
	 * running the query process.
	 * 
	 * @param runtime
	 *            the calling runtime
	 * @throws TMQLExtensionRegistryException
	 *             thrown if an exception caused by the internal runtime
	 */
	public void registerExtension(ITMQLRuntime runtime)
			throws TMQLExtensionRegistryException;

	/**
	 * Each extension point has to define an unique extension point id used to
	 * represent the extension point in context of the current TMQL runtime. If
	 * two extension points has the same identifier the extension adpater will
	 * throw an exception during the initialization time of extension points on
	 * startup.
	 * 
	 * @return the unique id
	 */
	public String getExtensionPointId();
	
	/**
	 * Returns all language extensions of this extension point
	 * @return the language extensions
	 */
	public Set<ILanguageExtension> getLanguageExtensions();

}
