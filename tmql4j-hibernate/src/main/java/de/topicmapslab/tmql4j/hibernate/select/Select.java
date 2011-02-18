/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.core.Limit;
import de.topicmapslab.tmql4j.hibernate.core.Offset;
import de.topicmapslab.tmql4j.hibernate.core.OrderBy;
import de.topicmapslab.tmql4j.hibernate.core.Where;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;
import de.topicmapslab.tmql4j.select.grammar.lexical.Unique;

/**
 * @author Sven Krosse
 * 
 */
public class Select implements IQueryPart {

	private From from;
	private List<IQueryPart> selects;
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	private Where where;
	private boolean unique = false;

	/**
	 * Set unique flag
	 * 
	 * @param unique
	 *            <code>true</code> if is unique, <code>false</code> if not
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	/**
	 * Adding a new for clause
	 * 
	 * @param select
	 *            the select clause
	 */
	public void add(IQueryPart select) {
		if (selects == null) {
			selects = new ArrayList<IQueryPart>();
		}
		selects.add(select);
	}

	/**
	 * Remove a for clause
	 * 
	 * @param select
	 *            the select clause
	 */
	public void remove(IQueryPart select) {
		if (selects != null) {
			selects.remove(select);
		}
	}

	/**
	 * Return all select clauses
	 * 
	 * @return the select clause
	 */
	List<IQueryPart> getSelects() {
		if (selects == null) {
			return Collections.emptyList();
		}
		return selects;
	}

	/**
	 * Set the limit clause
	 * 
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(long limit) {
		this.limit = new Limit(limit);
	}

	/**
	 * Remove the limit clause form internal model
	 */
	public void removeLimit() {
		this.limit = null;
	}

	/**
	 * Set the offset clause
	 * 
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = new Offset(offset);
	}

	/**
	 * Remove the offset clause form internal model
	 */
	public void removeOffset() {
		this.offset = null;
	}

	/**
	 * Set the from clause
	 * 
	 * @param from
	 *            the from to set
	 */
	public void setFrom(From from) {
		this.from = from;
	}

	/**
	 * Remove the from clause form internal model
	 */
	public void removeFrom() {
		this.from = null;
	}

	/**
	 * Set the order by clause
	 * 
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Remove the order by clause form internal model
	 */
	public void removeOrderBy() {
		this.orderBy = null;
	}

	/**
	 * Set the where clause
	 * 
	 * @param where
	 *            the where to set
	 */
	public void setWhere(Where where) {
		this.where = where;
	}

	/**
	 * Remove the where clause form internal model
	 */
	public void removeWhere() {
		this.where = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		/*
		 * adding the select clauses
		 */
		boolean first = true;
		for (IQueryPart select : getSelects()) {
			if (first) {
				builder.append(de.topicmapslab.tmql4j.select.grammar.lexical.Select.TOKEN);
			} else {
				builder.append(Comma.TOKEN);
			}
			builder.append(WhiteSpace.TOKEN);
			builder.append(select.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * set the from clause
		 */
		if (from != null) {
			builder.append(from.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * set the where clause
		 */
		if (where != null) {
			builder.append(where.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * set order by clause
		 */
		if (orderBy != null) {
			builder.append(orderBy.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * set offset clause
		 */
		if (offset != null) {
			builder.append(offset.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * set limit clause
		 */
		if (limit != null) {
			builder.append(limit.toTmql());
			builder.append(WhiteSpace.TOKEN);
		}
		/*
		 * add unique
		 */
		if ( unique){
			builder.append(Unique.TOKEN);
			builder.append(WhiteSpace.TOKEN);
		}
		return builder.toString();
	}

}
