package de.topicmapslab.tmql4j.hibernate.criterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;

public abstract class CriteriaImpl implements ICriteria {

	private List<ICriterion> criteria;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(ICriterion criterion) {
		if (criteria == null) {
			criteria = new ArrayList<ICriterion>();
		}
		criteria.add(criterion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(ICriterion criterion) {
		if (criteria != null) {
			criteria.remove(criterion);
		}
	}

	/**
	 * Returns the size of a criterion container
	 * 
	 * @return the size
	 */
	protected int size() {
		return getCriteria().size();
	}

	/**
	 * Internal method to get all criteria
	 * 
	 * @return the criteria
	 */
	protected List<ICriterion> getCriteria() {
		if (criteria == null) {
			return Collections.emptyList();
		}
		return criteria;
	}

	@Override
	public String toTmql() throws InvalidModelException {
		/*
		 * check validation
		 */
		validate();
		StringBuilder builder = new StringBuilder();
		for (ICriterion criterion : getCriteria()) {
			if (!builder.toString().isEmpty()) {
				builder.append(getOperatorToken());
			}
			builder.append(IHibernateConstants.WHITESPACE);
			/*
			 * add brackets if inner criterion is a criteria
			 */
			if (criterion instanceof ICriteria) {
				builder.append(BracketRoundOpen.TOKEN);
				builder.append(IHibernateConstants.WHITESPACE);
			}
			builder.append(criterion.toTmql());
			/*
			 * add brackets if inner criterion is a criteria
			 */
			if (criterion instanceof ICriteria) {
				builder.append(IHibernateConstants.WHITESPACE);
				builder.append(BracketRoundClose.TOKEN);
			}
			builder.append(IHibernateConstants.WHITESPACE);
		}
		return builder.toString();
	}

	/**
	 * Internal method to get the operator token for this criteria
	 * 
	 * @return the operator token
	 */
	protected abstract String getOperatorToken();

	/**
	 * Method called before transforming the query structure to a TMQL string. The implementation should throw an
	 * exception if the state is invalid.
	 * 
	 * @throws InvalidModelException
	 *             thrown if the state is invalid
	 */
	protected abstract void validate() throws InvalidModelException;
}
