/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.navigation.exception;

import org.tmapi.core.TMAPIException;

/**
 * Base exception of navigation interpretation of all TMQL axis. This exception
 * is a special type of {@link TMAPIException} defined by the library to handle
 * all exception during the navigation process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NavigationException extends TMAPIException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing information about the cause
	 */
	public NavigationException(String message) {
		super(message);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param cause
	 *            the cause of the exception
	 */
	public NavigationException(Throwable cause) {
		super(cause);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing information about the cause
	 * @param cause
	 *            the cause
	 */
	public NavigationException(String message, Throwable cause) {
		super(message, cause);
	}

}
