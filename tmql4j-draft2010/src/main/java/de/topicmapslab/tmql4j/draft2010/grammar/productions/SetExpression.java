package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Subtraction;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Union;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class representing the production 'set-expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SetExpression extends ExpressionImpl {

	/**
	 * the operators
	 */
	private final List<Class<? extends IToken>> operators = new LinkedList<Class<? extends IToken>>();

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
	public SetExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * callback of found delimers
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				checkForExtensions(Expression.class, tmqlTokens, tokens, runtime);
				operators.add(foundDelimer);
			}
		};

		/*
		 * creating set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Union.class);
		delimers.add(Intersect.class);
		delimers.add(Subtraction.class);

		/*
		 * split language-specific tokens by using delimers
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/**
	 * Returns the internal list of set operators.
	 * 
	 * @return a list of set operators
	 */
	public List<Class<? extends IToken>> getOperators() {
		return operators;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void asFlatString(StringBuilder builder) {
		int index = 0;
		boolean first = true;
		for (Expression ex : getExpressionFilteredByType(Expression.class)) {
			try {
				if (!first) {
					builder.append(operators.get(index++).newInstance().getLiteral());
					builder.append(WHITESPACE);
				}
				ex.asFlatString(builder);
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}
	}
}
