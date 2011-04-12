package de.topicmapslab.tmql4j.tolog.exception;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;

/**
 * Exception thrown during the transformation, if a tolog rule cannot transform
 * to TMQL.
 * 
 * @author Sven Krosse
 * 
 */
public class IncompatibleTologRuleException extends TMQLConverterException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param message
	 *            the message
	 */
	public IncompatibleTologRuleException(String message) {
		super(message);
	}

	/**
	 * constructor
	 * 
	 * @param cause
	 *            the cause
	 */
	public IncompatibleTologRuleException(Throwable cause) {
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
	public IncompatibleTologRuleException(String message, Throwable cause) {
		super(message, cause);
	}
}
