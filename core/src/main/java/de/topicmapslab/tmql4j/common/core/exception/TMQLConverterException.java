/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.exception;

import de.topicmapslab.tmql4j.common.model.query.IQuery;

/**
 * Exception class thrown by a {@link IQuery} implementation if the given query
 * cannot be convert to a TMQL query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLConverterException extends TMQLRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing information about the reason
	 */
	public TMQLConverterException(String message) {
		super(message);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message containing information about the reason
	 * @param cause
	 *            the cause of this exception
	 */
	public TMQLConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause of this exception
	 */
	public TMQLConverterException(Throwable cause) {
		super(cause);
	}

}
