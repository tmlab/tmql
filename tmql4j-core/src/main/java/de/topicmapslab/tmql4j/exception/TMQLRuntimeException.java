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
 * Base runtime exception thrown if any process task or any operation fails
 * during the execution of the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLRuntimeException extends TMQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public TMQLRuntimeException(String message) {
		super(message);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 * @param cause
	 *            the cause
	 */
	public TMQLRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public TMQLRuntimeException(Throwable cause) {
		super(cause);
	}

}
