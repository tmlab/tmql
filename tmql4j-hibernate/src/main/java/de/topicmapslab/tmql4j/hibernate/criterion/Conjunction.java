package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.And;

public class Conjunction extends CriteriaImpl {

	@Override
	protected String getOperatorToken() {
		return And.TOKEN;
	}

	@Override
	protected void validate() throws InvalidModelException {
		if (size() < 2) {
			throw new InvalidModelException("At least two criteria are expected, but '" + size() + "' was found.");
		}
	}

}
