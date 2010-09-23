/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.navigation.exception;

/**
 * Special navigation exception representing the case of invalid value given as
 * anchor for navigation process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InvalidValueException extends NavigationException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor to create a new instance
	 */
	public InvalidValueException() {
		super("unsupported navigation value");
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param message
	 *            the message containing information about the cause
	 */
	public InvalidValueException(String message) {
		super(message);
	}

	/**
	 * base constructor to create a new instance
	 * 
	 * @param cause
	 *            the cause
	 */
	public InvalidValueException(Throwable cause) {
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
	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}

}
