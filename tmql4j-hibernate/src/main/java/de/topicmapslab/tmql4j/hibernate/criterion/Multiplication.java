/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Star;

/**
 * @author Sven
 * 
 */
public class Multiplication extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Multiplication() {
		super(Star.TOKEN);
	}

}
