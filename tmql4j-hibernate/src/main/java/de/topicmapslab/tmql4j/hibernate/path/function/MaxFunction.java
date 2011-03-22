/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.function;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;

/**
 * @author Sven Krosse
 * 
 */
public class MaxFunction extends FunctionImpl {

	/**
	 * constructor
	 * 
	 * @param context
	 *            the context of counting
	 * @param condition
	 *            the condition
	 */
	public MaxFunction(final IQueryPart context, final IQueryPart condition) {
		addArgument(context);
		addArgument(condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFunctionName() {
		return de.topicmapslab.tmql4j.path.grammar.functions.aggregate.MaxFunction.IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MaxFunction clone() throws CloneNotSupportedException {
		IQueryPart context = getArguments().get(0).clone();
		IQueryPart condition = getArguments().get(1).clone();
		return new MaxFunction(context, condition);
	}

}
