/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;

/**
 * @author Sven Krosse
 * 
 */
public abstract class NumericalImpl extends CriteriaImpl {

	private final String token;

	/**
	 * constructor
	 * 
	 * @param token
	 *            the token
	 */
	public NumericalImpl(final String token) {
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
		if (size() < 2) {
			throw new InvalidModelException("At least two criteria are expected, but '" + size() + "' was found.");
		}
	}

}
