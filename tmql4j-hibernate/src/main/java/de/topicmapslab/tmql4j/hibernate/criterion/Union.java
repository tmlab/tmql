/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;


/**
 * @author Sven Krosse
 * 
 */
public class Union extends BinaryOperationImpl {

	/**
	 * constructor
	 */
	public Union() {
		super(de.topicmapslab.tmql4j.path.grammar.lexical.Union.TOKEN);
	}

}
