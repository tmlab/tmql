/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Limit;
import de.topicmapslab.tmql4j.path.grammar.lexical.Offset;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.orderBy.OrderBy;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.Conjunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.Criterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriteria;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class SqlDefinition implements ISqlDefinition {

	private List<ISelection> selectionParts;
	private List<IFromPart> fromParts;
	private List<OrderBy> orderByParts;
	private int aliasIndex;
	private ICriteria criteria;
	private Integer offset;
	private Integer limit;
	private boolean distinct = false;

	/**
	 * constructor
	 */
	public SqlDefinition() {
	}

	/**
	 * constructor
	 * 
	 * @param clone
	 *            the clone
	 */
	public SqlDefinition(SqlDefinition clone) {
		if (clone.selectionParts != null) {
			this.selectionParts = HashUtil.getList(clone.selectionParts);
		}
		if (clone.fromParts != null) {
			this.fromParts = HashUtil.getList(clone.fromParts);
		}
		if (clone.orderByParts != null) {
			this.orderByParts = HashUtil.getList(clone.orderByParts);
		}
		this.aliasIndex = clone.aliasIndex;
		this.criteria = clone.criteria;
		this.offset = clone.offset;
		this.limit = clone.limit;
	}

	/**
	 * Returns all from parts
	 * 
	 * @return the from parts
	 */
	protected List<IFromPart> getFromParts() {
		if (fromParts == null) {
			return Collections.emptyList();
		}
		return fromParts;
	}

	/**
	 * Returns all selections
	 * 
	 * @return the selections
	 */
	@Override
	public List<ISelection> getSelectionParts() {
		if (selectionParts == null) {
			return Collections.emptyList();
		}
		return selectionParts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAlias() {
		return ISqlConstants.ALIAS_PREFIX + Integer.toString(aliasIndex++);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFromPart(String fromPart, String alias, boolean isTable) {
		addFromPart(new FromPart(fromPart, alias, isTable));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFromPart(IFromPart part) {
		if (fromParts == null) {
			fromParts = HashUtil.getList();
		}
		fromParts.add(part);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String addFromPart(String fromPart, boolean isTable) {
		String alias = getAlias();
		addFromPart(fromPart, alias, isTable);
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFromPart getLastFromPart() {
		if (fromParts == null) {
			return null;
		}
		return fromParts.get(fromParts.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearFromParts() {
		if (fromParts != null) {
			fromParts.clear();
			fromParts = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISelection getLastSelection() {
		if (selectionParts == null) {
			return null;
		}
		return selectionParts.get(selectionParts.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearSelection() {
		if (selectionParts != null) {
			selectionParts.clear();
			selectionParts = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSelection(String selectionPart) {
		addSelection(new Selection(selectionPart));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSelection(ISelection selectionPart) {
		if (selectionParts == null) {
			selectionParts = HashUtil.getList();
		}
		selectionParts.add(selectionPart);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String criterion) {
		add(new Criterion(criterion));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(ICriterion criterion) {
		if (criteria == null) {
			criteria = new Conjunction();
		}
		criteria.add(criterion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition clone() {
		return new SqlDefinition(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		/*
		 * generate selection part
		 */
		StringBuilder selection = new StringBuilder();
		for (ISelection sel : this.selectionParts) {
			if (!selection.toString().isEmpty()) {
				selection.append(Comma.TOKEN);
				selection.append(ISqlConstants.WHITESPACE);
			}
			selection.append(sel.toString());
		}
		builder.append(ISqlConstants.ISqlKeywords.SELECT);
		builder.append(ISqlConstants.WHITESPACE);
		if (distinct) {
			builder.append(ISqlConstants.ISqlKeywords.DISTINCT);
			builder.append(ISqlConstants.WHITESPACE);
		}
		builder.append(selection.toString());
		/*
		 * ignore missing from is inner selection
		 */
		if (fromParts == null) {
			return builder.toString();
		}
		/*
		 * generate from part
		 */
		StringBuilder from = new StringBuilder();
		for (IFromPart fromPart : getFromParts()) {
			if (!from.toString().isEmpty()) {
				from.append(Comma.TOKEN);
				from.append(ISqlConstants.WHITESPACE);
			}
			from.append(fromPart.toString());
		}
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.FROM);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(from.toString());
		/*
		 * generate where part
		 */
		StringBuilder where = new StringBuilder();
		if (criteria != null) {
			where.append(Where.TOKEN);
			where.append(ISqlConstants.WHITESPACE);
			where.append(criteria.toString());
		}
		builder.append(where.toString());
		if (!getOrderByParts().isEmpty()) {
			StringBuilder orderBys = new StringBuilder();
			for (OrderBy orderBy : getOrderByParts()) {
				if (!orderBys.toString().isEmpty()) {
					orderBys.append(Comma.TOKEN);
					orderBys.append(ISqlConstants.WHITESPACE);
				}
				orderBys.append(orderBy.toString());
			}
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(ISqlConstants.ISqlKeywords.ORDER_BY);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(orderBys.toString());
		}
		/*
		 * offset and limit
		 */
		if (offset != null) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(Offset.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(offset);
		}
		if (limit != null) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(Limit.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(limit);
		}
		/*
		 * generate query
		 */
		return builder.toString();
	}

	/**
	 * Merge the given definition in this definition
	 * 
	 * @param definition
	 *            the definition
	 */
	public void mergeIn(SqlDefinition definition) {
		/*
		 * add all selections
		 */
		if (this.selectionParts == null) {
			this.selectionParts = HashUtil.getList();
		}
		this.selectionParts.addAll(definition.getSelectionParts());
		if (this.fromParts == null) {
			this.fromParts = HashUtil.getList();
		}
		this.fromParts.addAll(definition.getFromParts());
		/*
		 * set alias index
		 */
		this.aliasIndex = definition.aliasIndex;

		/*
		 * add criteria
		 */
		if (definition.criteria != null) {
			if (this.criteria == null) {
				this.criteria = definition.criteria;
			} else {
				this.criteria.add(definition.criteria);
			}
		}
		/*
		 * order by parts
		 */
		if (!definition.getOrderByParts().isEmpty()) {
			if (this.orderByParts == null) {
				this.orderByParts = HashUtil.getList();
			}
			this.orderByParts.addAll(definition.getOrderByParts());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInternalAliasIndex(int index) {
		this.aliasIndex = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInternalAliasIndex() {
		return this.aliasIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearOrderBy() {
		if (orderByParts != null) {
			orderByParts.clear();
			orderByParts = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addOrderByPart(OrderBy orderBy) {
		if (orderByParts == null) {
			orderByParts = HashUtil.getList();
		}
		orderByParts.add(orderBy);
	}

	/**
	 * Internal method to retrieve all order by parts or an empty list but never
	 * <code>null</code>
	 * 
	 * @return the orderByParts
	 */
	protected List<OrderBy> getOrderByParts() {
		if (orderByParts == null) {
			return Collections.emptyList();
		}
		return orderByParts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
}
