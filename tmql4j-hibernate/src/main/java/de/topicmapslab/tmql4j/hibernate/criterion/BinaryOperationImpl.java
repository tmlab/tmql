/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;

/**
 * @author Sven Krosse
 * 
 */
public abstract class BinaryOperationImpl extends CriteriaImpl {

	private String token;

	/**
	 * constructor
	 * 
	 * @param token
	 *            the token
	 */
	public BinaryOperationImpl(final String token) {
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getOperatorToken() {
		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validate() throws InvalidModelException {
		if (size() != 2) {
			throw new InvalidModelException("Exact two criteria are expected, but '" + size() + "' was found.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BinaryOperationImpl clone() throws CloneNotSupportedException {
		try {
			BinaryOperationImpl clone = getClass().getConstructor().newInstance();
			clone.token = token;
			clone(clone);
			return clone;
		} catch (Exception e) {
			throw new InvalidModelException(e);
		}
	}

}
