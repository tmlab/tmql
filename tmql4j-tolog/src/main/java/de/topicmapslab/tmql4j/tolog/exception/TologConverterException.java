package de.topicmapslab.tmql4j.tolog.exception;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;

/**
 * Exception definition of a special converter exception thrown during the
 * transformation from tolog to TMQL.
 * 
 * @author Sven Krosse
 * 
 */
public class TologConverterException extends TMQLConverterException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message
	 */
	public TologConverterException(String message) {
		super(message);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public TologConverterException(Throwable cause) {
		super(cause);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public TologConverterException(String message, Throwable cause) {
		super(message, cause);
	}

}
