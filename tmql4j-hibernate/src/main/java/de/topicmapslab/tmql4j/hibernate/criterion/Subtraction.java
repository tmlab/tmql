/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Minus;

/**
 * @author Sven
 * 
 */
public class Subtraction extends NumericalImpl {

	/**
	 * constructor
	 */
	public Subtraction() {
		super(Minus.TOKEN);
	}

}
