package de.topicmapslab.tmql4j.hibernate.exception;

public class InvalidModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public InvalidModelException(final String msg) {
		super(msg);
	}

	/**
	 * constructor
	 */
	public InvalidModelException(final Throwable cause) {
		super(cause);
	}
}
