/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * insert-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 *  insert-expression ::= INSERT tm-content [ WHERE boolean-expression ]
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertExpression extends ExpressionImpl {

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param parent
	 *            the known parent node
	 * @param tmqlTokens
	 *            the list of language-specific tokens contained by this
	 *            expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this
	 *            expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	public InsertExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		/*
		 * check if the keyword WHERE is contained
		 */
		if (tmqlTokens.contains(Where.class)) {
			/*
			 * get index of the keyword WHERE
			 */
			int index = tmqlTokens.indexOf(Where.class);
			/*
			 * add insert-clause
			 */
			checkForExtensions(InsertClause.class, tmqlTokens.subList(0, index),
					tokens.subList(0, index), runtime);
			/*
			 * add where-clause
			 */
			checkForExtensions(WhereClause.class, tmqlTokens.subList(index,
					tmqlTokens.size()), tokens.subList(index, tokens.size()),
					runtime);
		} else {
			/*
			 * add insert-clause
			 */
			checkForExtensions(InsertClause.class, tmqlTokens, tokens, runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (getTmqlTokens().isEmpty()) {
			return false;
		} else if (!getTmqlTokens().get(0).equals(Insert.class)) {
			return false;
		}
		return true;
	}

}
