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
package de.topicmapslab.tmql4j.parser.core.expressions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.From;
import de.topicmapslab.tmql4j.lexer.token.Limit;
import de.topicmapslab.tmql4j.lexer.token.Offset;
import de.topicmapslab.tmql4j.lexer.token.Order;
import de.topicmapslab.tmql4j.lexer.token.Select;
import de.topicmapslab.tmql4j.lexer.token.Unique;
import de.topicmapslab.tmql4j.lexer.token.Where;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.IParserUtilsCallback;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * select-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * select-expression	::=		 SELECT    < value-expression > [ FROM   value-expression ] [ WHERE   boolean-expression ] [  ORDER BY  < value-expression > ] [  UNIQUE  ] [  OFFSET  value-expression ][  LIMIT  value-expression ]
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SelectExpression extends ExpressionImpl {

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
	public SelectExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException,
					TMQLInvalidSyntaxException {
				Class<? extends IToken> token = tmqlTokens.get(0);
				/*
				 * is keyword SELECT
				 */
				if (token.equals(Select.class)) {
					checkForExtensions(SelectClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword FROM
				 */
				else if (token.equals(From.class)) {
					checkForExtensions(FromClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword WHERE
				 */
				else if (token.equals(Where.class)) {
					checkForExtensions(WhereClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword ORDER
				 */
				else if (token.equals(Order.class)) {
					checkForExtensions(OrderByClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword OFFSET
				 */
				else if (token.equals(Offset.class)) {
					checkForExtensions(OffsetClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword LIMIT
				 */
				else if (token.equals(Limit.class)) {
					checkForExtensions(LimitClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is keyword UNIQUE
				 */
				else if (token.equals(Unique.class)) {
					// NOTHING TO DO HERE
				}
			}
		};

		/*
		 * create set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Select.class);
		delimers.add(From.class);
		delimers.add(Where.class);
		delimers.add(Order.class);
		delimers.add(Unique.class);
		delimers.add(Limit.class);
		delimers.add(Offset.class);

		/*
		 * split expression
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		/*
		 * expects at least two tokens starting with the keyword SELECT
		 */
		return getTmqlTokens().size() > 1 && getTmqlTokens().get(0).equals(Select.class);
	}

}
