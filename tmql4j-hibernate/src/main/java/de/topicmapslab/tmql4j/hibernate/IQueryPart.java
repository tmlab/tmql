/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate;

import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;

/**
 * @author Sven Krosse
 * 
 */
public interface IQueryPart {

	/**
	 * Method called to translate the object tree to a TMQL query
	 * 
	 * @return the TMQL query
	 * @throws InvalidModelException thrown if internal structure is invalid
	 */
	String toTmql() throws InvalidModelException;

}
