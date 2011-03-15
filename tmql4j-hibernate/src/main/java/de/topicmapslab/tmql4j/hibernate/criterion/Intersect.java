/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

/**
 * @author Sven Krosse
 * 
 */
public class Intersect extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Intersect() {
		super(de.topicmapslab.tmql4j.path.grammar.lexical.Intersect.TOKEN);
	}

}
