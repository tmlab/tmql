package de.topicmapslab.tmql4j.hibernate.criterion;

public interface ICriteria extends ICriterion {

	/**
	 * Adding a criterion to the criteria
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public void add(ICriterion criterion);

	/**
	 * Removing a criterion to the criteria
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public void remove(ICriterion criterion);

}
