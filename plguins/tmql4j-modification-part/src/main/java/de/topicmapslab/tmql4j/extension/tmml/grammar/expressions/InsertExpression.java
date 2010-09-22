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
package de.topicmapslab.tmql4j.extension.tmml.grammar.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Where;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;
import de.topicmapslab.tmql4j.parser.model.IExpression;

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
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * check if extension is supported
		 */
		if (!runtime.getProperties().isLanguageExtensionTmqlUlEnabled()) {
			throw new TMQLGeneratorException(
					"Keyword 'INSERT' not allowed at current TMQL session, because the TMQL-UL extension is disabled.");
		}

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
