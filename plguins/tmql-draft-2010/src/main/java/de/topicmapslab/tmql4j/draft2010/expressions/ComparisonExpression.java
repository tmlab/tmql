package de.topicmapslab.tmql4j.draft2010.expressions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.tokens.Equals;
import de.topicmapslab.tmql4j.draft2010.tokens.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.tokens.Unequals;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.GreaterEquals;
import de.topicmapslab.tmql4j.lexer.token.GreaterThan;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.Or;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.IParserUtilsCallback;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Class representing the production 'comparison-expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ComparisonExpression extends ExpressionImpl {

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
			final TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException,
					TMQLInvalidSyntaxException {
				checkForExtensions(Expression.class, tmqlTokens, tokens,
						runtime);
				if ( foundDelimer != null ){
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
		delimers.add(ShortcutAxisLocators.class);
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
