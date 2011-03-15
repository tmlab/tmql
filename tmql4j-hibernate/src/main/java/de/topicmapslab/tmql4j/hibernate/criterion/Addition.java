/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Plus;

/**
 * @author Sven
 * 
 */
public class Addition extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Addition() {
		super(Plus.TOKEN);
	}

}
