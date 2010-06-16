package de.topicmapslab.tmql4j.draft2010.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareClose;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Class representing the production 'boolean-filter' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanFilter extends ExpressionImpl {

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
	public BooleanFilter(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * add boolean expression without brackets
		 */
		checkForExtensions(BooleanExpression.class, tmqlTokens.subList(1,
				tmqlTokens.size() - 1), tokens.subList(1, tokens.size() - 1),
				runtime);
	}

	@Override
	public boolean isValid() {
		/*
		 * the expression has to contain at least 3 tokens and has to be
		 * encapsulated by square brackets
		 */
		return getTmqlTokens().size() > 2
				&& getTmqlTokens().get(0).equals(BracketSquareOpen.class)
				&& getTmqlTokens().get(getTmqlTokens().size() - 1).equals(
						BracketSquareClose.class);
	}

}
