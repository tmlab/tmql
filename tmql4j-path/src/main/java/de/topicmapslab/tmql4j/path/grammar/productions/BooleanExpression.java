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
package de.topicmapslab.tmql4j.path.grammar.productions;

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
import de.topicmapslab.tmql4j.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Every;
import de.topicmapslab.tmql4j.path.grammar.lexical.Not;
import de.topicmapslab.tmql4j.path.grammar.lexical.Or;
import de.topicmapslab.tmql4j.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * boolean-expression.
 * <p>
 * The grammar production rule of the expression is: <code> * 
 * <p>
 *   boolean-expression ::= boolean-expression | boolean-expression |
 * </p>
 * <p>
 * boolean-expression & boolean-expression | boolean-primitive
 * </p>
 * <p>
 * boolean-expression ::= forall-clause
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpression extends ExpressionImpl {

	/**
	 * grammar type of boolean-expressions represents a disjunction
	 */
	public static final int TYPE_DISJUNCTION = 0;
	/**
	 * grammar type of boolean-expressions represents a conjunction
	 */
	public static final int TYPE_CONJUNCTION = 1;
	/**
	 * grammar type of boolean-expressions containing a boolean-primitive
	 */
	public static final int TYPE_BOOLEAN_PRIMITIVE = 2;
	/**
	 * grammar type of boolean-expressions containing a forall-clause
	 */
	public static final int TYPE_FORALL_CLAUSE = 3;

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
	public BooleanExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is boolean-expression ::= every binding-set satisfies
		 * boolean-expression
		 */
		if (tmqlTokens.get(0).equals(Every.class)) {
			checkForExtensions(ForAllClause.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_FORALL_CLAUSE);
		}
		/*
		 * not some binding-set satisfies not ( boolean-expression )
		 */
		else if (tmqlTokens.get(0).equals(Not.class) && tmqlTokens.get(1).equals(Some.class)) {
			checkForExtensions(ForAllClause.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_FORALL_CLAUSE);
		}
		/*
		 * is cramped boolean primitive
		 */
		else if (isParenthetic(tmqlTokens)) {
			checkForExtensions(BooleanPrimitive.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_BOOLEAN_PRIMITIVE);
		}
		/*
		 * is disjunction
		 */
		else if (ParserUtils.containsTokens(tmqlTokens, Or.class)) {
			setGrammarType(TYPE_DISJUNCTION);
			/*
			 * call-back instance of parser utility
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				@Override
				public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
					checkForExtensions(BooleanExpression.class, tmqlTokens, tokens, runtime);
				}
			};

			/*
			 * create set containing all delimers
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(Or.class);

			/*
			 * split expression
			 */
			ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);
		}
		/*
		 * is conjunction
		 */
		else if (ParserUtils.containsTokens(tmqlTokens, And.class)) {
			setGrammarType(TYPE_CONJUNCTION);
			/*
			 * call-back instance of parser utility
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				@Override
				public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
					checkForExtensions(BooleanExpression.class, tmqlTokens, tokens, runtime);
				}
			};

			/*
			 * create set containing all delimers
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(And.class);

			/*
			 * split expression
			 */
			ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);
		}
		/*
		 * is other boolean primitive like negation or exists-clause
		 */
		else {
			checkForExtensions(BooleanPrimitive.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_BOOLEAN_PRIMITIVE);
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
	 * Method checks if the given tokens are representing a cramped expression.
	 * 
	 * @param tmqlTokens
	 *            the tokens to check
	 * @return <code>true</code> if the expression is a cramped expression,
	 *         <code>false</code> otherwise.
	 */
	private boolean isParenthetic(List<Class<? extends IToken>> tmqlTokens) {
		/*
		 * first token has to be a round opening bracket
		 */
		if (!tmqlTokens.get(0).equals(BracketRoundOpen.class)) {
			return false;
		}

		long bracketCount = 1;
		for (Class<? extends IToken> token : tmqlTokens.subList(1, tmqlTokens.size())) {
			if (token.equals(BracketRoundOpen.class)) {
				/*
				 * new first round opening bracket --> no parenthetic
				 */
				if (bracketCount == 0) {
					return false;
				}
				bracketCount++;
			} else if (token.equals(BracketRoundClose.class)) {
				bracketCount--;
			}
			/*
			 * element after last bracket
			 */
			else if (bracketCount == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getJoinToken() {
		if (getGrammarType() == TYPE_CONJUNCTION) {
			return And.TOKEN;
		} else if (getGrammarType() == TYPE_DISJUNCTION) {
			return Or.TOKEN;
		}
		return super.getJoinToken();
	}
}
