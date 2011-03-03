/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

/**
 * @author Sven Krosse
 * 
 */
public class RegularExpression extends ComparisonImpl {

	/**
	 * constructor
	 */
	public RegularExpression() {
		super(de.topicmapslab.tmql4j.path.grammar.lexical.RegularExpression.TOKEN);
	}

}
