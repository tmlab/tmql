/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module.model;

import de.topicmapslab.tmql4j.components.interpreter.IPragma;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * 
 */
public interface IPragmaRegistry {

	/**
	 * Method register a new pragma for the runtime.
	 * 
	 * @param pragma
	 *            the pragma
	 */
	public void register(final IPragma pragma);

	/**
	 * Method checks if a program is known for the identifier
	 * 
	 * @param identifier
	 *            the identifier
	 * @return <code>true</code> if there is a pragma for this identifier,
	 *         <code>false</code> otherwise
	 */
	public boolean isKnownPragma(final String identifier);

	/**
	 * Call the pragma with the given arguments.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param identifier
	 *            the identifier
	 * @param the
	 *            value for the identifier
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	public void interpret(final ITMQLRuntime runtime, final IContext context, final String identifier, final String value) throws TMQLRuntimeException;

}
