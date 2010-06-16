/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.exceptions;

/**
 * Special {@link DataBridgeException} to handle exception during the execution
 * of the tmql4j data-bridge function.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DataBridgeRuntimeException extends DataBridgeException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public DataBridgeRuntimeException(String message) {
		super(message);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param cause
	 *            the cause
	 */
	public DataBridgeRuntimeException(Throwable cause) {
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
	public DataBridgeRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
