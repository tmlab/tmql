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
package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.At;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Desc;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Every;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Exists;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Function;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Not;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Or;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Percent;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

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
	 * grammar type of value-expression containing a boolean-expression
	 */
	public static final int TYPE_BOOLEAN = 4;
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
	@SuppressWarnings("unchecked")
	public ValueExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is prefix-operator value-expression
		 */
		if (tmqlTokens.get(0).equals(Minus.class)) {
			setGrammarType(TYPE_PREFIX_OPERATOR);
			checkForExtensions(ValueExpression.class, tmqlTokens.subList(1, tmqlTokens.size()), tokens.subList(1, tokens.size()), runtime);
			indexOfOperator = 0;
		} else {
			int index = indexOfMathematicalOperators(tmqlTokens);
			/*
			 * is mathematical operation
			 */
			if (index != -1) {
				setGrammarType(TYPE_INFIX_OPERATOR);
				checkForExtensions(ValueExpression.class, tmqlTokens.subList(0, index), tokens.subList(0, index), runtime);
				checkForExtensions(ValueExpression.class, tmqlTokens.subList(index + 1, tmqlTokens.size()), tokens.subList(index + 1, tokens.size()), runtime);
				indexOfOperator = index;
			} else {
				index = indexOfComparisonOperators(tmqlTokens);
				/*
				 * is comparison operation
				 */
				if (index != -1) {
					setGrammarType(TYPE_INFIX_OPERATOR);
					checkForExtensions(ValueExpression.class, tmqlTokens.subList(0, index), tokens.subList(0, index), runtime);
					checkForExtensions(ValueExpression.class, tmqlTokens.subList(index + 1, tmqlTokens.size()), tokens.subList(index + 1, tokens.size()), runtime);
					indexOfOperator = index;
				}
				/*
				 * has boolean operator
				 */
				else if (ParserUtils.containsTokens(tmqlTokens, Or.class, And.class, Not.class)) {
					checkForExtensions(BooleanExpression.class, tmqlTokens, tokens, runtime);
					setGrammarType(TYPE_BOOLEAN);
				}
				/*
				 * is function invocation
				 */
				else if (tmqlTokens.get(0).equals(Function.class)) {
					setGrammarType(TYPE_FUNCTION_INVOCATION);
					/*
					 * set internal flag of ASC or DESC
					 */
					if (tmqlTokens.get(tmqlTokens.size() - 1).equals(Asc.class) || tmqlTokens.get(tmqlTokens.size() - 1).equals(Desc.class)) {
						ascOrDescOrdering = true;
					}
					/*
					 * add function
					 */
					if (ascOrDescOrdering) {
						checkForExtensions(FunctionInvocation.class, tmqlTokens.subList(0, tmqlTokens.size() - 1), tokens.subList(0, tokens.size() - 1), runtime);
					} else {
						checkForExtensions(FunctionInvocation.class, tmqlTokens, tokens, runtime);
					}
				}
				/*
				 * is exists-clause
				 */
				else if (tmqlTokens.get(0).equals(At.class) || tmqlTokens.get(0).equals(Exists.class) || tmqlTokens.get(0).equals(Some.class)) {
					setGrammarType(TYPE_CONTENT);
					checkForExtensions(ExistsClause.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is for-all-clause
				 */
				else if (tmqlTokens.get(0).equals(Every.class)) {
					setGrammarType(TYPE_CONTENT);
					checkForExtensions(ForAllClause.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is content
				 */
				else {
					setGrammarType(TYPE_CONTENT);
					/*
					 * set internal flag of ASC or DESC
					 */
					if (tmqlTokens.get(tmqlTokens.size() - 1).equals(Asc.class) || tmqlTokens.get(tmqlTokens.size() - 1).equals(Desc.class)) {
						ascOrDescOrdering = true;
					}
					/*
					 * add content
					 */
					if (ascOrDescOrdering) {
						checkForExtensions(Content.class, tmqlTokens.subList(0, tmqlTokens.size() - 1), tokens.subList(0, tokens.size() - 1), runtime);
					} else {
						checkForExtensions(Content.class, tmqlTokens, tokens, runtime);
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
	private int indexOfMathematicalOperators(List<Class<? extends IToken>> tokens) {
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
