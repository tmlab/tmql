/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.where;

import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriteria;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriterion;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public abstract class Criteria implements ICriteria {
	private List<ICriterion> criterions = null;

	/**
	 * {@inheritDoc}
	 */
	public void add(ICriterion criterion) {
		if (criterions == null) {
			criterions = HashUtil.getList();
		}
		criterions.add(criterion);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(String criterion) {
		add(new Criterion(criterion));
	}

	/**
	 * Returns the list of all criteria
	 * 
	 * @return the criteria
	 */
	protected List<ICriterion> getCriterions() {
		if (criterions == null) {
			return Collections.emptyList();
		}
		return criterions;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (ICriterion criterion : getCriterions()) {
			/*
			 * is boolean operator necessary
			 */
			if (!first) {
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(getBooleanOperator());
				builder.append(ISqlConstants.WHITESPACE);
			}
			first = false;
			/*
			 * is conjunction or disjunction
			 */
			if (criterion instanceof ICriteria) {
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(BracketRoundOpen.TOKEN);
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(criterion.toString());
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(BracketRoundClose.TOKEN);
				builder.append(ISqlConstants.WHITESPACE);
			}
			/*
			 * is criterion
			 */
			else {
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(criterion.toString());
				builder.append(ISqlConstants.WHITESPACE);
			}

		}
		return builder.toString();
	}

	/**
	 * Returns the boolean operator used to combine the criterion
	 * 
	 * @return the boolean operator
	 */
	protected abstract String getBooleanOperator();
}
