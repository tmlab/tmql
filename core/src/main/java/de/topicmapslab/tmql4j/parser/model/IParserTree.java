/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.parser.model;

import de.topicmapslab.tmql4j.common.model.query.IQuery;

/**
 * Interface definition of a parser tree. The parser tree is a tree-structured
 * representation of the origin query. Each node of the tree represent exactly
 * one production rule of the current TMQL draft. A node can have a
 * non-quantified number of children representing the current node in the
 * aggregate. Nodes of the same level never overlapping and cannot contain the
 * same tokens at the same time.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IParserTree {

	/**
	 * Method returns the {@link IExpression} representing the root node of the
	 * generated parsing tree. The expression has to be of type query-expression
	 * and the result of calling the method {@link IExpression#getParent()} has
	 * to be <code>null</code>.
	 * 
	 * @return the root node
	 */
	public IExpression root();

	/**
	 * Method returns the internal copy of the origin query represented by this
	 * tree-structure.
	 * 
	 * @return the query instance
	 */
	public IQuery getQuery();

	/**
	 * Method transform the tree-structure to a string and write them into the
	 * given string builder.
	 * 
	 * @param builder
	 *            the string builder where the generated string will be written
	 *            in
	 */
	public void toStringTree(final StringBuilder builder);
}
