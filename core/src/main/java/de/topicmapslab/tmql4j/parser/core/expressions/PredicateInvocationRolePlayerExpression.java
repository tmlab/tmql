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
package de.topicmapslab.tmql4j.parser.core.expressions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Colon;
import de.topicmapslab.tmql4j.lexer.token.Ellipsis;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.IParserUtilsCallback;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * role-player-constraint of predicate-invocation.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * predicate-invocation-role-player ::= anchor : value-expression | ...
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationRolePlayerExpression extends ExpressionImpl {

	/**
	 * grammar type of a role-player-constraint containing a
	 * role-player-definition
	 */
	public static final int TYPE_ROLE_PLAYER_COMBINATION = 0;
	/**
	 * grammar type of an ellipsis
	 */
	public static final int TYPE_ELLIPSIS = 1;

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
	public PredicateInvocationRolePlayerExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is ellipsis '...'
		 */
		if (tmqlTokens.get(0).equals(Ellipsis.class)) {
			setGrammarType(TYPE_ELLIPSIS);
		}
		/*
		 * is anchor : value-expression
		 */
		else {
			/*
			 * call-back instance of parser utility
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				@Override
				public void newToken(List<Class<? extends IToken>> tmqlTokens,
						List<String> tokens,
						Class<? extends IToken> foundDelimer)
						throws TMQLGeneratorException,
						TMQLInvalidSyntaxException {
					if (tmqlTokens.size() == 1
							&& tmqlTokens
									.get(0)
									.equals(de.topicmapslab.tmql4j.lexer.token.Variable.class)) {
						checkForExtensions(Variable.class, tmqlTokens, tokens,
								runtime);
					} else {
						checkForExtensions(ValueExpression.class, tmqlTokens,
								tokens, runtime);
					}
				}
			};

			/*
			 * create set containing all delimers
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(Colon.class);

			/*
			 * split expression
			 */
			ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);

			setGrammarType(0);
			setGrammarType(TYPE_ROLE_PLAYER_COMBINATION);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

}
