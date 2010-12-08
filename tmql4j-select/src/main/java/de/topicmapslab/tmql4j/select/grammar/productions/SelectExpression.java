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
package de.topicmapslab.tmql4j.select.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Group;
import de.topicmapslab.tmql4j.path.grammar.lexical.Order;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.select.grammar.lexical.From;
import de.topicmapslab.tmql4j.select.grammar.lexical.Limit;
import de.topicmapslab.tmql4j.select.grammar.lexical.Offset;
import de.topicmapslab.tmql4j.select.grammar.lexical.Select;
import de.topicmapslab.tmql4j.select.grammar.lexical.Unique;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * select-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * select-expression	::=		 SELECT    < value-expression > [ FROM   value-expression ] [ WHERE   boolean-expression ] [ GROUP BY <variable> ] [  ORDER BY  < value-expression > ] [  UNIQUE  ] [  OFFSET  value-expression ][  LIMIT  value-expression ]
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
			final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
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
				 * is keyword GROUP
				 */
				else if (token.equals(Group.class)) {
					checkForExtensions(GroupByClause.class, tmqlTokens, tokens,
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
		delimers.add(Group.class);
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
