/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.builder;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.core.Where;
import de.topicmapslab.tmql4j.hibernate.criterion.Conjunction;
import de.topicmapslab.tmql4j.hibernate.criterion.Disjunction;
import de.topicmapslab.tmql4j.hibernate.criterion.ICriterion;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.select.From;
import de.topicmapslab.tmql4j.hibernate.select.Select;

/**
 * @author Sven Krosse
 * 
 */
public class SelectQueryBuilder extends QueryBuilderImpl {

	private ICriterion criterion;
	private long offset = -1;
	private long limit = -1;
	private Select select = new Select();
	private From from;

	/**
	 * Adding the given criterion to internal condition of where clause as conjunction. If no criterion was set before,
	 * the given one will be set as initial one, otherwise it will be add in a conjunction with the internal one.
	 * 
	 * @param criterion
	 *            the conjunction part
	 * @throws InvalidModelException
	 */
	public void conjunction(ICriterion criterion) throws InvalidModelException {
		/*
		 * no criterion set before
		 */
		if (this.criterion == null) {
			this.criterion = criterion;
		}
		/*
		 * internal criterion is also a conjunction
		 */
		else if (this.criterion instanceof Conjunction) {
			((Conjunction) this.criterion).add(criterion);
		}
		/*
		 * internal criterion is anything else
		 */
		else {
			Conjunction conjunction = new Conjunction();
			conjunction.add(this.criterion);
			conjunction.add(criterion);
			this.criterion = conjunction;
		}
	}

	/**
	 * Adding the given criterion to internal condition of where clause as disjunction. If no criterion was set before,
	 * the given one will be set as initial one, otherwise it will be add in a disjunction with the internal one.
	 * 
	 * @param criterion
	 *            the disjunction part
	 * @throws InvalidModelException
	 */
	public void disjunction(ICriterion criterion) throws InvalidModelException {
		/*
		 * no criterion set before
		 */
		if (this.criterion == null) {
			this.criterion = criterion;
		}
		/*
		 * internal criterion is also a disjunction
		 */
		else if (this.criterion instanceof Disjunction) {
			((Disjunction) this.criterion).add(criterion);
		}
		/*
		 * internal criterion is anything else
		 */
		else {
			Disjunction disjunction = new Disjunction();
			disjunction.add(this.criterion);
			disjunction.add(criterion);
			this.criterion = disjunction;
		}
	}

	/**
	 * Set the given criterion as condition of where clause. If any other criterion was set before, a conjunction will
	 * be created automatically.
	 * 
	 * @param criterion
	 *            the criterion
	 * @throws InvalidModelException
	 */
	public void criterion(ICriterion criterion) throws InvalidModelException {
		conjunction(criterion);
	}

	/**
	 * Set the offset value of offset clause. If the value is <code>-1</code>, the offset clause will not created.
	 * 
	 * @param offset
	 *            the value
	 */
	public void offset(long offset) {
		this.offset = offset;
	}

	/**
	 * Set the limit value of limit clause. If the value is <code>-1</code>, the limit clause will not created.
	 * 
	 * @param limit
	 *            the value
	 */
	public void limit(long limit) {
		this.limit = limit;
	}

	/**
	 * Set the from clause
	 * 
	 * @param content
	 *            the content
	 */
	public void from(final IQueryPart content) {
		from = new From(content);
	}

	/**
	 * Adds the given query part to select clause
	 * 
	 * @param part
	 *            the query part
	 */
	public void select(final IQueryPart part) {
		select.add(part);
	}

	/**
	 * Change the unique mode of select clause
	 * 
	 * @param unique
	 *            the flag indicates if the unique class should be added
	 */
	public void unique(boolean unique) {
		this.select.setUnique(unique);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toQueryString() {
		/*
		 * check from
		 */
		if (from != null) {
			select.setFrom(from);
		}
		/*
		 * check criterion
		 */
		if (criterion != null) {
			select.setWhere(new Where(criterion));
		}
		/*
		 * check offset
		 */
		if (offset >= 0) {
			select.setOffset(offset);
		}
		/*
		 * check limit
		 */
		if (limit >= 0) {
			select.setLimit(limit);
		}
		return select.toTmql();
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public SelectQueryBuilder clone() throws CloneNotSupportedException {
		SelectQueryBuilder clone = new SelectQueryBuilder();
		/*
		 * clone criterion
		 */
		if ( criterion != null ){
			clone.criterion = criterion.clone();
		}
		/*
		 * clone from
		 */
		if ( from != null ){
			clone.from = from.clone();
		}
		/*
		 * clone offset
		 */
		clone.offset(offset);
		/*
		 * clone limit
		 */
		clone.limit(limit);
		/*
		 * clone select
		 */
		clone.select = clone.select;
		return clone;
	}

}
