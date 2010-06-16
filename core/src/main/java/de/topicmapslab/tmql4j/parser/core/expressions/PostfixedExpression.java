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
import de.topicmapslab.tmql4j.lexer.token.At;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleClose;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundClose;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareClose;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;
import de.topicmapslab.tmql4j.lexer.token.Combination;
import de.topicmapslab.tmql4j.lexer.token.Comma;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.Every;
import de.topicmapslab.tmql4j.lexer.token.Exists;
import de.topicmapslab.tmql4j.lexer.token.Function;
import de.topicmapslab.tmql4j.lexer.token.GreaterEquals;
import de.topicmapslab.tmql4j.lexer.token.GreaterThan;
import de.topicmapslab.tmql4j.lexer.token.If;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.Minus;
import de.topicmapslab.tmql4j.lexer.token.Modulo;
import de.topicmapslab.tmql4j.lexer.token.Null;
import de.topicmapslab.tmql4j.lexer.token.Percent;
import de.topicmapslab.tmql4j.lexer.token.Plus;
import de.topicmapslab.tmql4j.lexer.token.RegularExpression;
import de.topicmapslab.tmql4j.lexer.token.Scope;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.lexer.token.Some;
import de.topicmapslab.tmql4j.lexer.token.Star;
import de.topicmapslab.tmql4j.lexer.token.Substraction;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * postfixed-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  postfixed-expression ::= tuple-expression | ( simple-content { postfix } )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PostfixedExpression extends ExpressionImpl {

	/**
	 * postfixed expression containing a tuple-expression
	 */
	public static final int TYPE_TUPLE_EXPRESSSION = 0;
	/**
	 * postfixed expression containing a simple-content
	 */
	public static final int TYPE_SIMPLE_CONTENT = 1;

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
	@SuppressWarnings("unchecked")
	public PostfixedExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is a tuple-expression
		 */
		if (isTupleExpression()) {
			/*
			 * tuple-expression starts with round brackets
			 */
			if (tmqlTokens.get(0).equals(BracketRoundOpen.class)) {
				checkForExtensions(TupleExpression.class, tmqlTokens.subList(1,
						tmqlTokens.size() - 1), tokens.subList(1,
						tokens.size() - 1), runtime);
			}
			/*
			 * tuple-expression does not start with round brackets
			 */
			else {
				checkForExtensions(TupleExpression.class, tmqlTokens, tokens,
						runtime);
			}
			setGrammarType(TYPE_TUPLE_EXPRESSSION);
		}
		/*
		 * is simple-content
		 */
		else {

			/*
			 * define opening brackets as beginning of protected section
			 */
			Set<Class<? extends IToken>> protectionStarts = HashUtil
					.getHashSet();
			protectionStarts.add(BracketAngleOpen.class);
			protectionStarts.add(BracketSquareOpen.class);

			/*
			 * define closing brackets as end of protected section
			 */
			Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
			protectionEnds.add(BracketAngleClose.class);
			protectionEnds.add(BracketSquareClose.class);

			/*
			 * indicators for projection-postfix
			 */
			Set<Class<? extends IToken>> indicators = HashUtil.getHashSet();
			indicators.add(BracketRoundOpen.class);

			int index = ParserUtils.indexOfTokens(tmqlTokens, indicators,
					protectionStarts, protectionEnds);
			/*
			 * has projection-postifx
			 */
			if (index != -1) {
				checkForExtensions(SimpleContent.class, tmqlTokens.subList(0,
						index), tokens.subList(0, index), runtime);
				checkForExtensions(Postfix.class, tmqlTokens.subList(index,
						tmqlTokens.size()), tokens
						.subList(index, tokens.size()), runtime);
			} else {
				/*
				 * define opening brackets as beginning of protected section
				 */
				protectionStarts.clear();
				protectionStarts.add(BracketRoundOpen.class);
				protectionStarts.add(BracketAngleOpen.class);

				/*
				 * define closing brackets as end of protected section
				 */
				protectionEnds.clear();
				protectionEnds.add(BracketRoundClose.class);
				protectionEnds.add(BracketAngleClose.class);

				/*
				 * indicators for filter-postfix
				 */
				indicators.clear();
				indicators.add(BracketSquareOpen.class);
				indicators.add(Scope.class);

				index = ParserUtils.indexOfTokens(tmqlTokens, indicators,
						protectionStarts, protectionEnds);
				/*
				 * has filter-postifx
				 */
				if (index != -1) {
					checkForExtensions(SimpleContent.class, tmqlTokens.subList(
							0, index), tokens.subList(0, index), runtime);
					checkForExtensions(Postfix.class, tmqlTokens.subList(index,
							tmqlTokens.size()), tokens.subList(index, tokens
							.size()), runtime);
				} else {
					index = ParserUtils.lastIndexOfTokens(tmqlTokens,
							ShortcutAxisInstances.class);
					/*
					 * has NCL type-filter
					 */
					if (index > 0) {
						checkForExtensions(SimpleContent.class, tmqlTokens
								.subList(0, index), tokens.subList(0, index),
								runtime);
						checkForExtensions(Postfix.class, tmqlTokens.subList(
								index, tmqlTokens.size()), tokens.subList(
								index, tokens.size()), runtime);
					}
					/*
					 * no postfix
					 */
					else {
						checkForExtensions(SimpleContent.class, tmqlTokens,
								tokens, runtime);
					}
				}
			}
			setGrammarType(TYPE_SIMPLE_CONTENT);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

	/**
	 * Internal method try to detect if the current expression is a
	 * tuple-expression. Tuple-expression can be identify if the expression
	 * contains a comma, except commas of function-invocations or if it contains
	 * brackets except of function-brackets.
	 * 
	 * @return <code>true</code> if this expression was identified as
	 *         tuple-expression, <code>false</code> if it is simple-content.
	 */
	private final boolean isTupleExpression() {

		Set<Class<? extends IToken>> tupleExpressionIndicators = HashUtil
				.getHashSet();
		/*
		 * special tokens only allowed as tuple-expression parts
		 */
		tupleExpressionIndicators.add(Function.class);
		tupleExpressionIndicators.add(Comma.class);
		/*
		 * mathematical operators
		 */
		tupleExpressionIndicators.add(Plus.class);
		tupleExpressionIndicators.add(Minus.class);
		tupleExpressionIndicators.add(Star.class);
		tupleExpressionIndicators.add(Percent.class);
		tupleExpressionIndicators.add(Modulo.class);
		/*
		 * comparison operators
		 */
		tupleExpressionIndicators.add(GreaterThan.class);
		tupleExpressionIndicators.add(GreaterEquals.class);
		tupleExpressionIndicators.add(LowerThan.class);
		tupleExpressionIndicators.add(LowerEquals.class);
		tupleExpressionIndicators.add(RegularExpression.class);
		/*
		 * set operators
		 */

		tupleExpressionIndicators.add(Equality.class);
		tupleExpressionIndicators.add(Substraction.class);
		tupleExpressionIndicators.add(Combination.class);
		/*
		 * special boolean keywords
		 */
		tupleExpressionIndicators.add(Some.class);
		tupleExpressionIndicators.add(Every.class);
		tupleExpressionIndicators.add(Exists.class);
		tupleExpressionIndicators.add(If.class);
		tupleExpressionIndicators.add(At.class);
		/*
		 * the NULL keyword
		 */
		tupleExpressionIndicators.add(Null.class);

		/*
		 * starts with round bracket
		 */
		if (getTmqlTokens().get(0).equals(BracketRoundOpen.class)) {
			return true;
		}
		return ParserUtils.containsTokens(getTmqlTokens(),
				tupleExpressionIndicators);
	}

}
