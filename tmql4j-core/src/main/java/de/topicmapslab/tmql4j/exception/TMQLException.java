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
 * Base exception of the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class TMQLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public TMQLException(String message) {
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
	public TMQLException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public TMQLException(Throwable cause) {
		super(cause);
	}

}
