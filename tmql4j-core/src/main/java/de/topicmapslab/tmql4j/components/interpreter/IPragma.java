/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.interpreter;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * 
 */
public interface IPragma {

	/**
	 * Returns the identifier of the pragma
	 * 
	 * @return the identifier of the pragma
	 */
	public String getIdentifier();

	/**
	 * Interpret the given pragma
	 * 
	 * @param runtiem
	 *            the runtime
	 * @param context
	 *            the context
	 * @param value
	 *            the value
	 * @throws TMQLRuntimeException
	 */
	public void interpret(final ITMQLRuntime runtime, final IContext context, final String value) throws TMQLRuntimeException;

}
