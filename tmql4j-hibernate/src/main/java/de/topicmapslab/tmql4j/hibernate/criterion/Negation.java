package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Not;

public class Negation extends CriteriaImpl {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		/*
		 * validate
		 */
		validate();

		StringBuilder builder = new StringBuilder();
		builder.append(getOperatorToken());
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(getCriteria().get(0).toTmql());
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(IHibernateConstants.WHITESPACE);
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validate() throws InvalidModelException {
		if (size() != 1) {
			throw new InvalidModelException("Only one criterion is expected, but '" + size() + "' was found.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getOperatorToken() {
		return Not.TOKEN;
	}

}
