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
package de.topicmapslab.tmql4j.path.components.parser;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.parser.ParserTreeImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Base implementation of a {@link IParserTree}. The parser tree is a
 * tree-structured representation of the origin query. Each node of the tree
 * represent exactly one production rule of the current TMQL draft. A node can
 * have a non-quantified number of children representing the current node in the
 * aggregate. Nodes of the same level never overlapping and cannot contain the
 * same tokens at the same time.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TmqlParserTree extends ParserTreeImpl {

	/**
	 * private and hidden constructor to create a new instance
	 * 
	 * @param runtime
	 *            the runtime
	 * @param query
	 *            the query to transform
	 * @param lexer
	 *            the lexical scanner
	 */
	public TmqlParserTree(ITMQLRuntime runtime, IQuery query, ILexer lexer) {
		super(runtime, query, lexer);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class<? extends IExpression> getRootExpressionClass() {
		return QueryExpression.class;
	}

}
