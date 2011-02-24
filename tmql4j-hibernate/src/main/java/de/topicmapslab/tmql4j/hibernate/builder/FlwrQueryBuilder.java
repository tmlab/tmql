/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.builder;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.core.OrderBy;
import de.topicmapslab.tmql4j.hibernate.core.Where;
import de.topicmapslab.tmql4j.hibernate.criterion.Conjunction;
import de.topicmapslab.tmql4j.hibernate.criterion.Disjunction;
import de.topicmapslab.tmql4j.hibernate.criterion.ICriterion;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.flwr.Flwr;
import de.topicmapslab.tmql4j.hibernate.flwr.For;
import de.topicmapslab.tmql4j.hibernate.flwr.Return;
import de.topicmapslab.tmql4j.hibernate.path.Navigation;

/**
 * @author Sven Krosse
 * 
 */
public class FlwrQueryBuilder extends QueryBuilderImpl {

	private ICriterion criterion;
	private long offset = -1;
	private long limit = -1;
	private Flwr flwr = new Flwr();
	private Return return_;
	private OrderBy orderBy;

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
	 * Adds a new for clause
	 * 
	 * @param variable
	 *            the variable
	 * @param content
	 *            the content of variable binding
	 */
	public void for_(final String variable, final IQueryPart content) {
		flwr.add(new For(variable, content));
	}

	/**
	 * Adds the given query part to return clause
	 * 
	 * @param part
	 *            the query part
	 */
	public void return_(final IQueryPart part) {
		if (return_ == null) {
			return_ = new Return();
		}
		return_.add(part);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toQueryString() {
		/*
		 * check criterion
		 */
		if (criterion != null) {
			flwr.setWhere(new Where(criterion));
		}
		/*
		 * order by
		 */
		if (orderBy != null) {
			flwr.setOrderBy(orderBy);
		}
		/*
		 * check offset
		 */
		if (offset >= 0) {
			flwr.setOffset(offset);
		}
		/*
		 * check limit
		 */
		if (limit >= 0) {
			flwr.setLimit(limit);
		}
		flwr.setReturn(return_);
		return flwr.toTmql();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FlwrQueryBuilder clone() throws CloneNotSupportedException {
		FlwrQueryBuilder clone = new FlwrQueryBuilder();
		/*
		 * clone criterion
		 */
		if (criterion != null) {
			clone.criterion = criterion.clone();
		}
		/*
		 * clone FLWR part
		 */
		clone.flwr = flwr.clone();
		/*
		 * clone offset
		 */
		clone.offset(offset);
		/*
		 * clone limit
		 */
		clone.limit(limit);
		/*
		 * clone order by
		 */
		if (orderBy != null) {
			clone.orderBy = orderBy.clone();
		}
		/*
		 * clone return
		 */
		if (return_ != null) {
			clone.return_ = return_.clone();
		}
		return clone;
	}

	/**
	 * Adds a new entry to the order by clause
	 * 
	 * @param navigation
	 *            the navigation to specify the order by part
	 * @param ascending
	 *            flag indicates the ordering order
	 */
	public void orderBy(final Navigation navigation, boolean ascending) {
		if (orderBy == null) {
			orderBy = new OrderBy();
		}
		orderBy.add(navigation, ascending);
	}
}
