package de.topicmapslab.tmql4j.draft2010.internal;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.tokens.DoubleColon;
import de.topicmapslab.tmql4j.lexer.token.WhiteSpace;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLWhiteSpacer;

public class PathWhitespacer extends TMQLWhiteSpacer {

	private IQuery query;

	public PathWhitespacer(ITMQLRuntime runtime, IQuery query) {
		super(runtime, query);
	}

	@Override
	public void execute() throws TMQLRuntimeException {
		super.execute();

		String query = super.getTransformedQuery().toString();

		StringBuilder builder = new StringBuilder();

		final String doubleColon = new DoubleColon().getLiteral();
		int index = query.indexOf(doubleColon);
		int last = 0;
		while (index != -1) {
			String leftHand = query.substring(last, index);
			builder.append(leftHand + new WhiteSpace().getLiteral()
					+ doubleColon + new WhiteSpace().getLiteral());
			last = index + doubleColon.length();
			index = query.indexOf(new DoubleColon().getLiteral(), last);
		}

		String leftHand = query.substring(last);
		builder.append(leftHand);

		this.query = new TMQLQuery(builder.toString());

	}

	@Override
	public IQuery getTransformedQuery() {
		return query;
	}
}
