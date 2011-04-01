/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Unequals;

/**
 * @author Sven Krosse
 * 
 */
public class UnEquals extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public UnEquals() {
		super(Unequals.TOKEN);
	}

}
