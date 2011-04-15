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
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.At;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Every;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Exists;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Function;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.If;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Null;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Percent;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Except;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Union;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a postfixed-expression.
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
	 *            the list of language-specific tokens contained by this expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	public PostfixedExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is a tuple-expression
		 */
		if (isTupleExpression()) {
			/*
			 * tuple-expression starts with round brackets
			 */
			if (tmqlTokens.get(0).equals(BracketRoundOpen.class)) {

				checkForExtensions(TupleExpression.class, tmqlTokens.subList(1, tmqlTokens.size() - 1), tokens.subList(1, tokens.size() - 1), runtime);
			}
			/*
			 * tuple-expression does not start with round brackets
			 */
			else {
				checkForExtensions(TupleExpression.class, tmqlTokens, tokens, runtime);
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
			Set<Class<? extends IToken>> protectionStarts = HashUtil.getHashSet();
			protectionStarts.add(BracketRoundOpen.class);
			protectionStarts.add(BracketSquareOpen.class);

			/*
			 * define closing brackets as end of protected section
			 */
			Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
			protectionEnds.add(BracketRoundClose.class);
			protectionEnds.add(BracketSquareClose.class);

			/*
			 * indicators for projection-postfix
			 */
			Set<Class<? extends IToken>> indicators = HashUtil.getHashSet();
			indicators.add(BracketAngleOpen.class);

			int index = ParserUtils.indexOfTokens(tmqlTokens, indicators, protectionStarts, protectionEnds);
			/*
			 * has projection-postifx
			 */
			if (index != -1) {
				checkForExtensions(SimpleContent.class, tmqlTokens.subList(0, index), tokens.subList(0, index), runtime);
				checkForExtensions(Postfix.class, tmqlTokens.subList(index, tmqlTokens.size()), tokens.subList(index, tokens.size()), runtime);
			}
			/* no postfix */
			else {
				checkForExtensions(SimpleContent.class, tmqlTokens, tokens, runtime);
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
	 * Internal method try to detect if the current expression is a tuple-expression. Tuple-expression can be identify
	 * if the expression contains a comma, except commas of function-invocations or if it contains brackets except of
	 * function-brackets.
	 * 
	 * @return <code>true</code> if this expression was identified as tuple-expression, <code>false</code> if it is
	 *         simple-content.
	 */
	private final boolean isTupleExpression() {

		Set<Class<? extends IToken>> tupleExpressionIndicators = HashUtil.getHashSet();
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
		tupleExpressionIndicators.add(Equality.class);
		tupleExpressionIndicators.add(Unequals.class);
		/*
		 * set operators
		 */
		tupleExpressionIndicators.add(Intersect.class);
		tupleExpressionIndicators.add(Except.class);
		tupleExpressionIndicators.add(Union.class);
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
		 * the alias keyword AS
		 */
		tupleExpressionIndicators.add(As.class);

		/*
		 * starts with angle bracket
		 */
		if (getTmqlTokens().get(0).equals(BracketAngleOpen.class)) {
			return true;
		}
		return ParserUtils.containsTokens(getTmqlTokens(), tupleExpressionIndicators);
	}

}
