/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.path.grammar.lexical.Percent;

/**
 * @author Sven
 * 
 */
public class Division extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Division() {
		super(Percent.TOKEN);
	}

}
