/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.function;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.path.Navigation;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetBestLabel;

/**
 * @author Sven Krosse
 * 
 */
public class BestlabelFunction extends FunctionImpl {

	/**
	 * constructor
	 * 
	 * @param topic
	 *            the topic
	 */
	public BestlabelFunction(final IQueryPart topic) {
		addArgument(topic);
	}

	/**
	 * constructor
	 * 
	 * @param topic
	 *            the topic
	 * @param strict
	 *            the strict part
	 */
	public BestlabelFunction(final IQueryPart topic, boolean strict) {
		addArgument(topic);
		addArgument(new Navigation(Boolean.toString(strict)));
	}

	/**
	 * constructor
	 * 
	 * @param topic
	 *            the topic
	 * @param theme
	 *            the theme
	 * @param strict
	 *            the strict part
	 */
	public BestlabelFunction(final IQueryPart topic, final IQueryPart theme, boolean strict) {
		addArgument(topic);
		addArgument(theme);
		addArgument(new Navigation(Boolean.toString(strict)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFunctionName() {
		return GetBestLabel.GETBESTLABEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BestlabelFunction clone() throws CloneNotSupportedException {
		IQueryPart argument = getArguments().get(0).clone();
		BestlabelFunction clone = new BestlabelFunction(argument);
		for (int i = 1; i < getArguments().size(); i++) {
			IQueryPart part = getArguments().get(i).clone();
			clone.addArgument(part);
		}
		return clone;
	}

}
