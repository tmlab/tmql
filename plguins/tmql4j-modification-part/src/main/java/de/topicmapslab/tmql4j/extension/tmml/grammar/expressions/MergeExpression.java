/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.grammar.expressions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.All;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Merge;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Comma;
import de.topicmapslab.tmql4j.lexer.token.Where;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.utility.IParserUtilsCallback;
import de.topicmapslab.tmql4j.parser.utility.ParserUtils;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * merge-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * merge-expression ::= MERGE path-expression
 * </p>
 * <p>
 * merge-expression ::= MERGE &lt;value-expression&gt; WHERE boolean-expression
 * </p>
 * <p>
 * merge-expression ::= MERGE ALL WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MergeExpression extends ExpressionImpl {
	/**
	 * grammar type of merge-expression using value-expressions
	 */
	public static final int TYPE_VALUEEXPRESSION = 0;
	/**
	 * grammar type of merge-expression using the keyword ALL
	 */
	public static final int TYPE_ALL = 1;

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
	public MergeExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			final TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * check if extension is supported
		 */
		if (!runtime.getProperties().isLanguageExtensionTmqlUlEnabled()) {
			throw new TMQLGeneratorException(
					"Keyword 'MERGE' not allowed at current TMQL session, because the TMQL-UL extension is disabled.");
		}

		final boolean constainsWhere = getTmqlTokens().contains(Where.class);

		IParserUtilsCallback callback = new IParserUtilsCallback() {

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens,
					List<String> tokens, Class<? extends IToken> foundDelimer)
					throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				/*
				 * is WHERE-clause
				 */
				if (foundDelimer == null && constainsWhere) {
					/*
					 * add remove where token
					 */
					List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tmqlTokens_.add(Where.class);
					tmqlTokens_.addAll(tmqlTokens);
					List<String> tokens_ = new LinkedList<String>();
					tokens_.add(new Where().getLiteral());
					tokens_.addAll(tokens);
					checkForExtensions(WhereClause.class, tmqlTokens_, tokens_,
							runtime);
				} else if (foundDelimer == null
						|| foundDelimer.equals(Where.class)
						|| foundDelimer.equals(Comma.class)) {
					checkForExtensions(ValueExpression.class, tmqlTokens,
							tokens, runtime);
					setGrammarType(TYPE_VALUEEXPRESSION);
				}
			}
		};

		Collection<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Comma.class);
		delimers.add(Where.class);
		delimers.add(Merge.class);

		ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);

		if (tmqlTokens.contains(All.class)) {
			/*
			 * set grammar type
			 */
			setGrammarType(TYPE_ALL);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (getTmqlTokens().isEmpty()) {
			return false;
		} else if (!getTmqlTokens().get(0).equals(Merge.class)) {
			return false;
		}
		return true;
	}

}
