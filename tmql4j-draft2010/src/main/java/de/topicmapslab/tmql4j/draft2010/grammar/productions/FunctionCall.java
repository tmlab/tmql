package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Function;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class representing the production 'function-call' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionCall extends ExpressionImpl {

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
	public FunctionCall(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				checkForExtensions(Expression.class, tmqlTokens, tokens, runtime);
			}
		};

		/*
		 * create set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Comma.class);

		/*
		 * split expression
		 */
		ParserUtils.split(callback, tmqlTokens.subList(2, tmqlTokens.size() - 1), tokens.subList(2, tokens.size() - 1), delimers, true);
	}

	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty() && getTmqlTokens().get(0).equals(Function.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartBefore(StringBuilder builder) {
		builder.append(getTokens().get(0));
		builder.append(WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartAfter(StringBuilder builder) {
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getJoinToken() {
		return Comma.TOKEN;
	}

}
