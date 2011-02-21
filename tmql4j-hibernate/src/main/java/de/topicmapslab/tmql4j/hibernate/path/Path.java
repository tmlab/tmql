/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.path.function.FunctionImpl;

/**
 * @author Sven Krosse
 * 
 */
public class Path implements IQueryPart {

	/**
	 * the content
	 */
	private IQueryPart content;

	/**
	 * constructor
	 */
	public Path() {
		// VOID
	}

	/**
	 * constructor
	 * 
	 * @param navigation
	 *            the navigation
	 */
	public Path(Navigation navigation) {
		this.content = navigation;
	}

	/**
	 * Constructor
	 * 
	 * @param projection
	 *            the tuple expression
	 */
	public Path(Projection projection) {
		this.content = projection;
	}

	/**
	 * Constructor
	 * 
	 * @param function
	 *            the function
	 */
	public Path(FunctionImpl function) {
		this.content = function;
	}

	/**
	 * Set the tuple expression as internal content
	 * 
	 * @param projection
	 *            the projection
	 */
	public void setContent(Projection projection) {
		this.content = projection;
	}

	/**
	 * Set the navigation as internal content
	 * 
	 * @param navigation
	 *            the navigation
	 */
	public void setContent(Navigation navigation) {
		this.content = navigation;
	}

	/**
	 * Set the function as internal content
	 * 
	 * @param function
	 *            the function
	 */
	public void setContent(FunctionImpl function) {
		this.content = function;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		if (content == null) {
			throw new InvalidModelException("Missing content of path expression!");
		}
		return content.toTmql();
	}

}
