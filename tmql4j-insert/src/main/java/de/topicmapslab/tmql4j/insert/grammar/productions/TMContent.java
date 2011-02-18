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
package de.topicmapslab.tmql4j.insert.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * tm-content-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * tm-content ::= ctm-fragment
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMContent extends ExpressionImpl {

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
	public TMContent(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back handler for parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer)
					throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				/*
				 * is embed query content
				 */
				if (BracketAngleClose.class.equals(foundDelimer)) {
					checkForExtensions(QueryExpression.class, tmqlTokens,
							tokens, runtime);
				}
				/*
				 * is other content
				 */
				else if (foundDelimer == null
						|| BracketAngleOpen.class.equals(foundDelimer)) {
					checkForExtensions(NonInterpretedContent.class, tmqlTokens,
							tokens, runtime);
				}
			}
		};

		/*
		 * extract embed queries but remove first and last triple-quote token
		 */
		ParserUtils.getEmbedQueries(callback, tmqlTokens.subList(1, tmqlTokens
				.size() - 1), tokens.subList(1, tokens.size() - 1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

}
