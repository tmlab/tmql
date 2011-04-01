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
 * Exception thrown by the parser because of invalid syntax or because of some
 * problems during the generation of the parser tree.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLParserException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public TMQLParserException(String message) {
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
	public TMQLParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public TMQLParserException(Throwable cause) {
		super(cause);
	}

}
