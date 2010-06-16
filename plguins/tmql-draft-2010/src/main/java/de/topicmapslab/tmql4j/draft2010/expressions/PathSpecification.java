package de.topicmapslab.tmql4j.draft2010.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Class representing the production 'path-specification' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathSpecification extends ExpressionImpl {

	public static final int TYPE_AXIS_SPEC = 0;
	public static final int TYPE_ASSOCIATION_PATTERN = 1;

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
	public PathSpecification(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * path specification is an association pattern
		 */
		if (tmqlTokens.size() > 1 && tmqlTokens.get(1).equals(BracketRoundOpen.class)) {
			checkForExtensions(AssociationPattern.class, tmqlTokens, tokens,
					runtime);
			setGrammarType(TYPE_ASSOCIATION_PATTERN);
		}
		/*
		 * path specification is an axis specification
		 */
		else {
			setGrammarType(TYPE_AXIS_SPEC);
		}
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
