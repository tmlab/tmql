package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Or;

public class Disjunction extends CriteriaImpl {

	@Override
	protected String getOperatorToken() {
		return Or.TOKEN;
	}

	@Override
	protected void validate() throws InvalidModelException {
		if (size() < 2) {
			throw new InvalidModelException("At least two criteria are expected, but '" + size() + "' was found.");
		}
	}

}
