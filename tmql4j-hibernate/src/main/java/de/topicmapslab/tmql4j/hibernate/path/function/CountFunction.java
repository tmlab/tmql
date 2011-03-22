/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.function;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;

/**
 * @author Sven Krosse
 * 
 */
public class CountFunction extends FunctionImpl {

	/**
	 * constructor
	 * 
	 * @param topic
	 *            the topic
	 */
	public CountFunction(final IQueryPart topic) {
		addArgument(topic);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFunctionName() {
		return de.topicmapslab.tmql4j.path.grammar.functions.sequences.CountFunction.IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CountFunction clone() throws CloneNotSupportedException {
		IQueryPart argument = getArguments().get(0).clone();
		return new CountFunction(argument);
	}

}
