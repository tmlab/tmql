/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

/**
 * @author Sven Krosse
 * 
 */
public class GreaterThan extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public GreaterThan() {
		super(de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan.TOKEN);
	}

}
