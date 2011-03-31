/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 * 
 */
public class Navigation implements IQueryPart {

	private List<Step> steps;
	private final String anchor;
	private Projection projection;

	/**
	 * constructor
	 * 
	 * @param anchor
	 *            the navigation anchor
	 */
	public Navigation(final String anchor) {
		this.anchor = anchor;
	}

	/**
	 * Adding a step to internal navigation
	 * 
	 * @param axis
	 *            the axis
	 * @param forward
	 *            boolean flag if navigation should be forward, otherwise it is backward.
	 */
	public void addStep(Class<? extends IToken> axis, final boolean forward) {
		addStep(new Step(axis, forward));
	}

	/**
	 * Adding a step to internal navigation
	 * 
	 * @param axis
	 *            the axis
	 * @param forward
	 *            boolean flag if navigation should be forward, otherwise it is backward.
	 * @param optionalType
	 *            the subject-identifier of the optional type or <code>null</code>
	 */
	public void addStep(Class<? extends IToken> axis, final boolean forward, final String optionalType) {
		addStep(new Step(axis, forward, optionalType));
	}

	/**
	 * Adding a step to internal navigation
	 * 
	 * @param step
	 *            the step
	 */
	public void addStep(Step step) {
		if (steps == null) {
			steps = new ArrayList<Step>();
		}
		steps.add(step);
	}

	/**
	 * Removes a step from internal navigation
	 * 
	 * @param step
	 *            the step
	 */
	public void removeStep(Step step) {
		if (steps != null) {
			steps.remove(step);
		}
	}

	/**
	 * Setting a projection to the end of this navigation
	 * 
	 * @param projection
	 *            the projection to set
	 */
	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	/**
	 * @param projection
	 *            the projection to set
	 */
	public void removeProjection() {
		this.projection = null;
	}

	/**
	 * Returns all steps
	 * 
	 * @return the steps
	 */
	public List<Step> getSteps() {
		if (steps == null) {
			return Collections.emptyList();
		}
		return steps;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(anchor);
		builder.append(WhiteSpace.TOKEN);
		for (Step step : getSteps()) {
			builder.append(step.toTmql());
		}
		builder.append(WhiteSpace.TOKEN);
		/*
		 * set projection
		 */
		if (projection != null) {
			builder.append(projection.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigation clone() throws CloneNotSupportedException {
		Navigation clone = new Navigation(anchor);
		for (Step step : getSteps()) {
			clone.addStep(step.clone());
		}
		if (projection != null) {
			clone.setProjection(projection.clone());
		}
		return clone;
	}
}
