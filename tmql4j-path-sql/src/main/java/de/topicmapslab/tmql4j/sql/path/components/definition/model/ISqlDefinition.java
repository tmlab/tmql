/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.model;

import java.util.List;

import de.topicmapslab.tmql4j.sql.path.components.definition.core.orderBy.OrderBy;

/**
 * @author Sven Krosse
 * 
 */
public interface ISqlDefinition extends Cloneable {

	/**
	 * Method calculates a new unique alias for from clause
	 * 
	 * @return the new alias and never <code>null</code>
	 */
	public String getAlias();

	/**
	 * Method adds a new part to the internal from clause with the given alias
	 * 
	 * @param fromPart
	 *            the from part
	 * @param alias
	 *            the alias to use for
	 * @param isTable
	 *            flag indicates if content of from part is a table
	 */
	public void addFromPart(final String fromPart, final String alias, final boolean isTable);

	/**
	 * Method adds a new part to the internal from clause and generates a new
	 * alias
	 * 
	 * @param fromPart
	 *            the from part to add
	 * @param isTable
	 *            flag indicates if content of from part is a table
	 * @return the generated alias
	 */
	public String addFromPart(final String fromPart, final boolean isTable);

	/**
	 * Method adds a new part to the internal from clause and generates a new
	 * alias
	 * 
	 * @param part
	 *            part
	 */
	public void addFromPart(IFromPart part);

	/**
	 * Returns the last entry of from clause
	 * 
	 * @return the last entry of from clause or <code>null</code>.
	 */
	public IFromPart getLastFromPart();

	/**
	 * removes all entries form from parts
	 */
	public void clearFromParts();

	/**
	 * Returns the last entry of selection clause
	 * 
	 * @return the last entry of selection clause or <code>null</code>.
	 */
	public ISelection getLastSelection();

	/**
	 * removes all entries form selection
	 */
	public void clearSelection();

	/**
	 * Adding a new entry to the selection part
	 * 
	 * @param selectionPart
	 *            the selection part
	 */
	public void addSelection(final String selectionPart);

	/**
	 * Adding a new entry to the selection part
	 * 
	 * @param selectionPart
	 *            the selection part
	 */
	public void addSelection(final ISelection selectionPart);

	/**
	 * Adding a new condition to the internal where clause.
	 * 
	 * @param criterion
	 *            the new criterion
	 */
	public void add(final ICriterion criterion);

	/**
	 * Adding a new condition to the internal where clause. The given string
	 * will be transformed to an {@link ICriterion} object.
	 * 
	 * @param criterion
	 *            the new criterion
	 */
	public void add(final String criterion);

	/**
	 * Overwrites the Object#clone() method
	 * 
	 * @return the cloned construct
	 */
	public ISqlDefinition clone();

	/**
	 * Returns all selections
	 * 
	 * @return the selections
	 */
	public List<ISelection> getSelectionParts();

	/**
	 * Updates the internal index to the given index
	 * 
	 * @param index
	 *            the new index
	 */
	public void setInternalAliasIndex(int index);

	/**
	 * Returns the internal index
	 * 
	 * @return the index
	 */
	public int getInternalAliasIndex();

	/**
	 * Adds a new order by part to the SQL selection
	 * 
	 * @param orderBy
	 *            the order by part
	 */
	public void addOrderByPart(OrderBy orderBy);

	/**
	 * Removes all order by parts
	 */
	public void clearOrderBy();

	/**
	 * Setting the limit
	 * 
	 * @param limit
	 *            the limit
	 */
	public void setLimit(Integer limit);

	/**
	 * Setting the offset
	 * 
	 * @param offset
	 *            the offset
	 */
	public void setOffset(Integer offset);

	/**
	 * Set the distinct flag to the SQL definition
	 * 
	 * @param distinct
	 *            <code>true</code> if DISTINCT selection is required,
	 *            <code>false</code> otherwise.
	 */
	public void setDistinct(boolean distinct);

}
