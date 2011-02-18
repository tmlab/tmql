package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Or;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class representing the production 'comparison-expression' of the new draft
 * 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ComparisonExpression extends ExpressionImpl {

	/**
	 * the operator
	 */
	private Class<? extends IToken> operator = null;

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
	public ComparisonExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {

			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer)
					throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				checkForExtensions(Expression.class, tmqlTokens, tokens,
						runtime);
				if (foundDelimer != null) {
					operator = foundDelimer;
				}
			}
		};

		/*
		 * create set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Or.class);
		delimers.add(Equals.class);
		delimers.add(Equality.class);
		delimers.add(Unequals.class);
		delimers.add(LowerThan.class);
		delimers.add(LowerEquals.class);
		delimers.add(GreaterThan.class);
		delimers.add(GreaterEquals.class);
		delimers.add(MatchesRegExp.class);

		/*
		 * split expression
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);

		/*
		 * should not be empty
		 */
		if (getExpressions().isEmpty()) {
			throw new TMQLInvalidSyntaxException(
					tmqlTokens,
					tokens,
					ComparisonExpression.class,
					"Operator ( '=' | '!=' | '<' | '>' | '<=' | '>=' ) was expected , but nothing was found.");
		}
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * Returns the token representing the current operator
	 * 
	 * @return the operator token
	 */
	public Class<? extends IToken> getOperator() {
		return operator;
	}
}
