/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.exception;

/**
 * Special definition of a {@link DataBridgeException} representing the case
 * that a method is not supported by the real implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UnsupportedModuleException extends DataBridgeException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public UnsupportedModuleException(String message) {
		super(message);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param cause
	 *            the cause
	 */
	public UnsupportedModuleException(Throwable cause) {
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
	public UnsupportedModuleException(String message, Throwable cause) {
		super(message, cause);
	}

}
