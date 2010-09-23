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
import de.topicmapslab.tmql4j.lexer.token.Asc;
import de.topicmapslab.tmql4j.lexer.token.At;
import de.topicmapslab.tmql4j.lexer.token.Desc;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.Every;
import de.topicmapslab.tmql4j.lexer.token.Exists;
import de.topicmapslab.tmql4j.lexer.token.Function;
import de.topicmapslab.tmql4j.lexer.token.GreaterEquals;
import de.topicmapslab.tmql4j.lexer.token.GreaterThan;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.Minus;
import de.topicmapslab.tmql4j.lexer.token.Modulo;
import de.topicmapslab.tmql4j.lexer.token.Percent;
import de.topicmapslab.tmql4j.lexer.token.Plus;
import de.topicmapslab.tmql4j.lexer.token.RegularExpression;
import de.topicmapslab.tmql4j.lexer.token.Some;
import de.topicmapslab.tmql4j.lexer.token.Star;
import de.topicmapslab.tmql4j.lexer.token.Unequals;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;


/**
 * Special implementation of {@link ExpressionImpl} representing a
 * value-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * value-expression ::= value-expression infix-operator value-expression (0)
 * </p>
 * <p>
 * value-expression ::= prefix-operator value-expression
 * </p>
 * <p>
 * value-expression ::= function-invocation
 * </p>
 * <p>
 * value-expression ::= content
 * </p>
 * <p>
 * infix-operator ::= + | - | * | % | mod | < | <= | > | >= | =~ | == | !=
 * </p>
 * <p>
 * prefix-operator ::= - |
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueExpression extends ExpressionImpl {

	/**
	 * grammar type of value-expression containing an infix-operator
	 */
	public static final int TYPE_INFIX_OPERATOR = 0;
	/**
	 * grammar type of value-expression containing an prefix-operator
	 */
	public static final int TYPE_PREFIX_OPERATOR = 1;

	/**
	 * grammar type of value-expression containing a function-invocation
	 */
	public static final int TYPE_FUNCTION_INVOCATION = 2;

	/**
	 * grammar type of value-expression containing a content-expression
	 */
	public static final int TYPE_CONTENT = 3;
	/**
	 * index of the detected operator if grammar type is
	 * {@link ValueExpression#TYPE_INFIX_OPERATOR} or
	 * {@link ValueExpression#TYPE_PREFIX_OPERATOR}
	 */
	private int indexOfOperator = -1;

	/**
	 * internal flag if value expression ends with ASC or DESC
	 */
	private boolean ascOrDescOrdering = false;

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
	public ValueExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is an atom
		 */
		if (parent instanceof LimitClause || parent instanceof OffsetClause) {
			setGrammarType(TYPE_CONTENT);
			checkForExtensions(Content.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is prefix-operator value-expression
		 */
		else if (tmqlTokens.get(0).equals(Minus.class)) {
			setGrammarType(TYPE_PREFIX_OPERATOR);
			checkForExtensions(ValueExpression.class, tmqlTokens.subList(1,
					tmqlTokens.size()), tokens.subList(1, tokens.size()),
					runtime);
			indexOfOperator = 0;
		} else {
			int index = indexOfMathematicalOperators(tmqlTokens);
			/*
			 * is mathematical operation
			 */
			if (index != -1) {
				setGrammarType(TYPE_INFIX_OPERATOR);
				checkForExtensions(ValueExpression.class, tmqlTokens.subList(0,
						index), tokens.subList(0, index), runtime);
				checkForExtensions(ValueExpression.class, tmqlTokens.subList(
						index + 1, tmqlTokens.size()), tokens.subList(
						index + 1, tokens.size()), runtime);
				indexOfOperator = index;
			} else {
				index = indexOfComparisonOperators(tmqlTokens);
				/*
				 * is comparison operation
				 */
				if (index != -1) {
					setGrammarType(TYPE_INFIX_OPERATOR);
					checkForExtensions(ValueExpression.class, tmqlTokens
							.subList(0, index), tokens.subList(0, index),
							runtime);
					checkForExtensions(ValueExpression.class, tmqlTokens
							.subList(index + 1, tmqlTokens.size()), tokens
							.subList(index + 1, tokens.size()), runtime);
					indexOfOperator = index;
				}
				/*
				 * is function invocation
				 */
				else if (tmqlTokens.get(0).equals(Function.class)) {
					setGrammarType(TYPE_FUNCTION_INVOCATION);
					checkForExtensions(FunctionInvocation.class, tmqlTokens,
							tokens, runtime);
				}
				/*
				 * is exists-clause
				 */
				else if (tmqlTokens.get(0).equals(At.class)
						|| tmqlTokens.get(0).equals(Exists.class)
						|| tmqlTokens.get(0).equals(Some.class)) {
					setGrammarType(TYPE_CONTENT);
					checkForExtensions(ExistsClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is for-all-clause
				 */
				else if (tmqlTokens.get(0).equals(Every.class)) {
					setGrammarType(TYPE_CONTENT);
					checkForExtensions(ForAllClause.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is content
				 */
				else {
					setGrammarType(TYPE_CONTENT);
					/*
					 * set internal flag of ASC or DESC
					 */
					if (tmqlTokens.get(tmqlTokens.size() - 1).equals(Asc.class)
							|| tmqlTokens.get(tmqlTokens.size() - 1).equals(
									Desc.class)) {
						ascOrDescOrdering = true;
					}
					/*
					 * add content
					 */
					if (ascOrDescOrdering) {
						checkForExtensions(Content.class, tmqlTokens.subList(0,
								tmqlTokens.size() - 1), tokens.subList(0,
								tokens.size() - 1), runtime);
					} else {
						checkForExtensions(Content.class, tmqlTokens, tokens,
								runtime);
					}
				}
			}
		}
	}

	/**
	 * Method checks if there is a mathematical operator token. Only operators
	 * which are part of the expression and not cramped.
	 * 
	 * @param tokens
	 *            the token list
	 * @return the index of found operator or -1 if not operator was found
	 */
	private int indexOfMathematicalOperators(
			List<Class<? extends IToken>> tokens) {
		Set<Class<? extends IToken>> operators = HashUtil.getHashSet();
		operators.add(Plus.class);
		operators.add(Minus.class);
		operators.add(Star.class);
		operators.add(Modulo.class);
		operators.add(Percent.class);
		return ParserUtils.indexOfTokens(tokens, operators);
	}

	/**
	 * Method checks if there is a comparison operator token. Only operators
	 * which are part of the expression and not cramped.
	 * 
	 * @param tokens
	 *            the token list
	 * @return the index of found operator or -1 if not operator was found
	 */
	private int indexOfComparisonOperators(List<Class<? extends IToken>> tokens) {
		Set<Class<? extends IToken>> operators = HashUtil.getHashSet();
		operators.add(LowerThan.class);
		operators.add(LowerEquals.class);
		operators.add(GreaterThan.class);
		operators.add(GreaterEquals.class);
		operators.add(Equality.class);
		operators.add(Unequals.class);
		operators.add(RegularExpression.class);
		return ParserUtils.indexOfTokens(tokens, operators);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

	/**
	 * Method return the index of the detected operator contained by this
	 * value-expression.
	 * 
	 * @return the index of detected operator if grammar type is
	 *         {@link ValueExpression#TYPE_INFIX_OPERATOR} or
	 *         {@link ValueExpression#TYPE_PREFIX_OPERATOR}, otherwise
	 *         <code>-1</code> will be returned.
	 */
	public int getIndexOfOperator() {
		return indexOfOperator;
	}

	/**
	 * Getter of the internal flag if the keyword ASC or DESC is contained.
	 * 
	 * @return <code>true</code> if the keyword is contained.
	 */
	public boolean isAscOrDescOrdering() {
		return ascOrDescOrdering;
	}
}
