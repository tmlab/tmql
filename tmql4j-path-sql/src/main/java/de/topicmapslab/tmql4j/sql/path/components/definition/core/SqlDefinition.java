/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriteria;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class SqlDefinition implements ISqlDefinition {

	private static final String QUERY_WITHOUT_FROM = "SELECT {0}";
	private static final String QUERY_WITH_FROM = "SELECT {0} FROM {1} {2}";
	public static final String WS = " ";
	public static final String ALIAS = "alias";

	private List<ISelection> selectionParts;
	private List<IFromPart> fromParts;
	private int aliasIndex;
	private ICriteria criteria;
	private SqlTables sqlTable;

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
	SqlDefinition(SqlDefinition clone) {
		if (clone.selectionParts != null) {
			this.selectionParts = HashUtil.getList(clone.selectionParts);
		}
		if (clone.fromParts != null) {
			this.fromParts = HashUtil.getList(clone.fromParts);
		}
		this.aliasIndex = clone.aliasIndex;
		this.criteria = clone.criteria;
		this.sqlTable = clone.sqlTable;
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
	protected List<ISelection> getSelectionParts() {
		if (selectionParts == null) {
			return Collections.emptyList();
		}
		return selectionParts;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAlias() {
		return ALIAS + Integer.toString(aliasIndex++);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addFromPart(String fromPart, String alias, boolean isTable) {
		addFromPart(new FromPart(fromPart, alias, isTable));
	}

	/**
	 * {@inheritDoc}
	 */
	public void addFromPart(IFromPart part) {
		if (fromParts == null) {
			fromParts = HashUtil.getList();
		}
		fromParts.add(part);
	}

	/**
	 * {@inheritDoc}
	 */
	public String addFromPart(String fromPart, boolean isTable) {
		String alias = getAlias();
		addFromPart(fromPart, alias, isTable);
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	public IFromPart getLastFromPart() {
		if (fromParts == null) {
			return null;
		}
		return fromParts.get(fromParts.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public ISelection getLastSelection() {
		if (selectionParts == null) {
			return null;
		}
		return selectionParts.get(selectionParts.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearSelection() {
		if (selectionParts != null) {
			selectionParts.clear();
			selectionParts = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addSelection(String selectionPart) {
		addSelection(new Selection(selectionPart));
	}

	/**
	 * {@inheritDoc}
	 */
	public void addSelection(ISelection selectionPart) {
		if (selectionParts == null) {
			selectionParts = HashUtil.getList();
		}
		selectionParts.add(selectionPart);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(String criterion) {
		add(new Criterion(criterion));
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(ICriterion criterion) {
		if (criteria == null) {
			criteria = new Conjunction();
		}
		criteria.add(criterion);
	}

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition clone() {
		return new SqlDefinition(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public SqlTables getCurrentTable() {
		return sqlTable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCurrentTable(SqlTables table) {
		this.sqlTable = table;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		/*
		 * generate selection part
		 */
		StringBuilder selection = new StringBuilder();
		for (ISelection sel : this.selectionParts) {
			if (!selection.toString().isEmpty()) {
				selection.append(Comma.TOKEN);
				selection.append(WS);
			}
			selection.append(sel.toString());
		}
		/*
		 * ignore missing from is inner selection
		 */
		if ( fromParts == null ){
			return MessageFormat.format(QUERY_WITHOUT_FROM, selection.toString());
		}
		/*
		 * generate from part
		 */
		StringBuilder from = new StringBuilder();
		for (IFromPart fromPart : getFromParts()) {
			if (!from.toString().isEmpty()) {
				from.append(Comma.TOKEN);
				from.append(WS);
			}
			from.append(fromPart.toString());
		}
		/*
		 * generate where part
		 */
		StringBuilder where = new StringBuilder();
		if (criteria != null) {
			where.append(Where.TOKEN);
			where.append(WS);
			where.append(criteria.toString());
		}
		/*
		 * generate query
		 */
		return MessageFormat.format(QUERY_WITH_FROM, selection.toString(), from.toString(), where.toString());
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
		 * set state
		 */
		this.sqlTable = SqlTables.ANY;

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
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInternalAliasIndex(int index) {
		this.aliasIndex = index;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInternalAliasIndex() {
		return this.aliasIndex;
	}
}
