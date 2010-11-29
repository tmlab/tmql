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
package de.topicmapslab.tmql4j.exception;

import de.topicmapslab.tmql4j.extensions.IExtensionPoint;

/***
 * Special exception class thrown if any extensions represented by the
 * {@link IExtensionPoint} interface can not be registered correctly.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLExtensionRegistryException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor
	 * 
	 * @param message
	 *            the message containing information about the cause of this
	 *            exception
	 */
	public TMQLExtensionRegistryException(String message) {
		super(message);
	}

	/**
	 * base constructor
	 * 
	 * @param message
	 *            the message containing information about the cause of this
	 *            exception
	 * @param cause
	 *            the cause of the exception
	 */
	public TMQLExtensionRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * base constructor
	 * 
	 * @param cause
	 *            the cause of the exception
	 */
	public TMQLExtensionRegistryException(Throwable cause) {
		super(cause);
	}

}
