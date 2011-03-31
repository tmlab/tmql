/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Equality;

/**
 * @author Sven Krosse
 * 
 */
public class Equals extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Equals() {
		super(Equality.TOKEN);
	}

}
