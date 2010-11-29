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
package de.topicmapslab.tmql4j.path.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * predicate-invocation.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * predicate-invocation ::= anchor ( < anchor : value-expression > [ , ... ] )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocation extends ExpressionImpl {

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
	public PredicateInvocation(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException,
					TMQLInvalidSyntaxException {
				checkForExtensions(
						PredicateInvocationRolePlayerExpression.class,
						tmqlTokens, tokens, runtime);
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
		ParserUtils.split(callback, tmqlTokens
				.subList(2, tmqlTokens.size() - 1), tokens.subList(2, tokens
				.size() - 1), delimers, true);

		setGrammarType(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
}
