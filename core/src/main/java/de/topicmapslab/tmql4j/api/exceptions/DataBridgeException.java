/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.exceptions;

import de.topicmapslab.tmql4j.api.model.IDataBridge;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

/**
 * Base exception of the {@link IDataBridge}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DataBridgeException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public DataBridgeException(String message) {
		super(message);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param cause
	 *            the cause
	 */
	public DataBridgeException(Throwable cause) {
		super(cause);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 * @param cause
	 *            the cause
	 */
	public DataBridgeException(String message, Throwable cause) {
		super(message, cause);
	}

}
