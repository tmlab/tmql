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
package de.topicmapslab.tmql4j.extension.tmml.exception;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

/**
 * Special implementation of a runtime exception of the TMQL engine. This
 * exception will be thrown if the deletion of some content failed.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeletionException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;


	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public DeletionException(String message) {
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
	public DeletionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public DeletionException(Throwable cause) {
		super(cause);
	}

}
