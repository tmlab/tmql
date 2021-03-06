package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.And;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Not;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Or;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class representing the boolean-expression of the new TMQL draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpression extends ExpressionImpl {

	/**
	 * type conjunction
	 */
	public static final int TYPE_CONJUNCTION = 0;
	/**
	 * type disjunction
	 */
	public static final int TYPE_DISJUNCTION = 1;
	/**
	 * type negation
	 */
	public static final int TYPE_NOTEXPRESSION = 2;
	/**
	 * type content expression
	 */
	public static final int TYPE_EXPRESSION = 3;
	/**
	 * type clamped expression
	 */
	public static final int TYPE_CLAMPED = 4;
	/**
	 * type comparison expression
	 */
	public static final int TYPE_COMPARISIONEXPRESSION = 5;

	/**
	 * base constructor to create a new expression without sub-nodes
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
		 * is clamped
		 */
		if (isParenthetic(tmqlTokens)) {
			checkForExtensions(BooleanExpression.class, tmqlTokens.subList(1, tmqlTokens.size() - 1), tokens.subList(1, tokens.size() - 1), runtime);
			setGrammarType(TYPE_CLAMPED);
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
		 * no combination operator
		 */
		else {
			Class<? extends IToken> token = tmqlTokens.get(0);
			/*
			 * starts with NOT
			 */
			if (token.equals(Not.class)) {
				/*
				 * extract not-expression not (... )
				 */
				checkForExtensions(BooleanExpression.class, tmqlTokens.subList(1, tmqlTokens.size()), tokens.subList(1, tokens.size()), runtime);
				setGrammarType(TYPE_NOTEXPRESSION);
			} else if (isComparison(tmqlTokens)) {
				/*
				 * is a comparison expression
				 */
				checkForExtensions(ComparisonExpression.class, tmqlTokens, tokens, runtime);
				setGrammarType(TYPE_COMPARISIONEXPRESSION);
			} else {
				/*
				 * is expression
				 */
				checkForExtensions(Expression.class, tmqlTokens, tokens, runtime);
				setGrammarType(TYPE_EXPRESSION);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public boolean isValid() {
		return true;
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
	 * Method checks if the given tokens are representing a comparison
	 * expression.
	 * 
	 * @param tmqlTokens
	 *            the tokens to check
	 * @return <code>true</code> if the expression is a comparison expression,
	 *         <code>false</code> otherwise.
	 */
	private boolean isComparison(List<Class<? extends IToken>> tmqlTokens) {
		long bracketCount = 0;
		for (Class<? extends IToken> token : tmqlTokens) {
			if (token.equals(BracketSquareOpen.class)) {
				bracketCount++;
			} else if (token.equals(BracketSquareClose.class)) {
				bracketCount--;
			}
			/*
			 * is operator '=' | '==' | '!=' | '<' | '>' | '<=' | '>=' | '=~'
			 */
			else if (bracketCount == 0
					&& (token.equals(Equals.class) || token.equals(Equality.class) || token.equals(Unequals.class) || token.equals(LowerThan.class) || token.equals(LowerEquals.class)
							|| token.equals(GreaterThan.class) || token.equals(GreaterEquals.class) || token.equals(MatchesRegExp.class))) {
				return true;
			}
		}
		return false;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartBefore(StringBuilder builder) {
		if (getGrammarType() == TYPE_CLAMPED) {
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(WHITESPACE);
		} else if (getGrammarType() == TYPE_NOTEXPRESSION) {
			builder.append(Not.TOKEN);
			builder.append(WHITESPACE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartAfter(StringBuilder builder) {
		if (getGrammarType() == TYPE_CLAMPED) {
			builder.append(BracketRoundClose.TOKEN);
			builder.append(WHITESPACE);
		}
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
