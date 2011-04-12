/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.exceptions;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Special implementation of a runtime exception. This exception will be thrown
 * if some new content can not be insert.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;


	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containg some information about the cause
	 */
	public InsertException(String message) {
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
	public InsertException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public InsertException(Throwable cause) {
		super(cause);

	}

}
