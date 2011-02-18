/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path;

import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.path.filter.Filter;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 *
 */
public class Step implements IQueryPart {

	private final Class<? extends IToken> axis;
	private final boolean forward;
	private final String optionalType;
	private Filter filter;
	
	/**
	 * constructor
	 * @param axis the axis
	 * @param forward boolean flag if navigation should be forward, otherwise it is backward.
	 */
	public Step(Class<? extends IToken> axis, final boolean forward ) {
		this(axis, forward, null);
	}
	/**
	 * constructor	 
	 * @param axis the axis
	 * @param forward boolean flag if navigation should be forward, otherwise it is backward.
	 * @param optionalType the subject-identifier of the optional type or <code>null</code>
	 */
	public Step(Class<? extends IToken> axis, final boolean forward, final String optionalType) {
		this.axis = axis;
		this.forward = forward;
		this.optionalType = optionalType;
	}

	/**
	 * Set a filter
	 * @param filter the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	/**
	 * Removing the filter
	 */
	public void removeFilter() {
		this.filter = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(forward? MoveForward.TOKEN : MoveBackward.TOKEN);		
		builder.append(WhiteSpace.TOKEN);
		try {
			builder.append(axis.newInstance().getLiteral());
		} catch (Exception e) {
			throw new InvalidModelException("Cannot fetch token of axis");
		}
		builder.append(WhiteSpace.TOKEN);
		/*
		 * add the optional type
		 */
		if ( optionalType != null ){
			builder.append(optionalType);
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * add the filter
		 */
		if ( filter != null ){
			builder.append(filter.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		return builder.toString();
	}

}
