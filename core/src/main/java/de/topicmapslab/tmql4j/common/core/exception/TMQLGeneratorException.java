/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.exception;

/**
 * Exception is thrown during the parsing process if the generation of the
 * parser tree fails.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLGeneratorException extends TMQLParserException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing some information about the cause
	 */
	public TMQLGeneratorException(String message) {
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
	public TMQLGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public TMQLGeneratorException(Throwable cause) {
		super(cause);
	}

}
