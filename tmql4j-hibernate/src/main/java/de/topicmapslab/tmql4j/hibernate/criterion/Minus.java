/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

/**
 * @author Sven Krosse
 * 
 */
public class Minus extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Minus() {
		super(de.topicmapslab.tmql4j.path.grammar.lexical.Substraction.TOKEN);
	}

}
