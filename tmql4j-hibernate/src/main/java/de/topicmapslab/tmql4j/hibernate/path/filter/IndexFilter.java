/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

/**
 * @author Sven Krosse
 * 
 */
public class IndexFilter extends Filter {

	private final long number;

	/**
	 * constructor
	 * 
	 * @param number
	 *            the index
	 */
	public IndexFilter(long number) {
		this.number = number;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String getFilterPart() {
		return Long.toString(number);
	}

}
