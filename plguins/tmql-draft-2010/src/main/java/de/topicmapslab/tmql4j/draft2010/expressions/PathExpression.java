package de.topicmapslab.tmql4j.draft2010.expressions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.IParserUtilsCallback;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Class representing the production 'path-expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathExpression extends ExpressionImpl {

	public static final int TYPE_SIMPLEEXPRESSION = 0;
	public static final int TYPE_PATHSTEPS = 1;

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
	public PathExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * callback of found delimers
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer)
					throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				Class<? extends IToken> token = tmqlTokens.get(0);
				/*
				 * is path step
				 */
				if (token.equals(ShortcutAxisAtomifyMoveForward.class)) {
					if ( getExpressions().isEmpty()){
						setGrammarType(TYPE_PATHSTEPS);
					}
					checkForExtensions(PathStep.class, tmqlTokens, tokens,
							runtime);					
				} else {
					/*
					 * add simple expression
					 */
					checkForExtensions(SimpleExpression.class, tmqlTokens,
							tokens, runtime);
					setGrammarType(TYPE_SIMPLEEXPRESSION);
				}
			}
		};

		/*
		 * creating set containing all splitting tokens
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(ShortcutAxisAtomifyMoveForward.class);

		/*
		 * split language-specific tokens
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, false);
		
		
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
