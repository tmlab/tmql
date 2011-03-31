/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.flwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.core.Limit;
import de.topicmapslab.tmql4j.hibernate.core.Offset;
import de.topicmapslab.tmql4j.hibernate.core.OrderBy;
import de.topicmapslab.tmql4j.hibernate.core.Where;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 * 
 */
public class Flwr implements IQueryPart {

	private Return return_;
	private List<For> fors;
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	private Where where;

	/**
	 * constructor
	 * 
	 * @param return_
	 *            the return clause
	 */
	public Flwr(Return return_) {
		this.return_ = return_;
	}

	/**
	 * empty constructor
	 */
	public Flwr() {

	}

	/**
	 * Adding a new for clause
	 * 
	 * @param for_
	 *            the for clause
	 */
	public void add(For for_) {
		if (fors == null) {
			fors = new ArrayList<For>();
		}
		fors.add(for_);
	}

	/**
	 * Remove a for clause
	 * 
	 * @param for_
	 *            the for clause
	 */
	public void remove(For for_) {
		if (fors != null) {
			fors.remove(for_);
		}
	}

	/**
	 * Return all for clauses
	 * 
	 * @return the for clause
	 */
	List<For> getFors() {
		if (fors == null) {
			return Collections.emptyList();
		}
		return fors;
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
	 * Set the return clause
	 * 
	 * @param return_
	 *            the return_ to set
	 */
	public void setReturn(Return return_) {
		this.return_ = return_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		if (return_ == null) {
			throw new InvalidModelException("The return clause is mandatory but not set!");
		}
		StringBuilder builder = new StringBuilder();
		/*
		 * adding the for clauses
		 */
		for (For for_ : getFors()) {
			builder.append(for_.toTmql());
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
		builder.append(return_.toTmql());
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flwr clone() throws CloneNotSupportedException {
		Flwr clone = new Flwr();
		if (return_ != null) {
			clone.setReturn(return_.clone());
		}
		for (For for_ : getFors()) {
			clone.add(for_.clone());
		}
		if (where != null) {
			clone.setWhere(where.clone());
		}
		if (orderBy != null) {
			clone.setOrderBy(orderBy.clone());
		}
		if (offset != null) {
			clone.offset = offset.clone();
		}
		if (limit != null) {
			clone.limit = limit.clone();
		}
		return clone;
	}

}
