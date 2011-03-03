package de.topicmapslab.tmql4j.hibernate.exception;

public class InvalidModelException extends RuntimeException {

	/**
	 * constructor
	 */
	private static final long serialVersionUID = 1L;

	public InvalidModelException(final String msg) {
		super(msg);
	}

}
