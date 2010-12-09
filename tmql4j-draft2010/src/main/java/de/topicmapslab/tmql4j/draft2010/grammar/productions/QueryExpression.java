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
package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.Iterator;
import java.util.List;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * query-expression.
 * <p>
 * query-expression ::= [ environment-clause ] path-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpression extends ExpressionImpl {

	/**
	 * base constructor to create a new instance used by {@link IParserTree}
	 * 
	 * <p>
	 * Constructor is calling
	 * {@link QueryExpression#QueryExpression(IExpression, List, List, ITMQLRuntime)}
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 * @param lexer
	 *            the lexical scanner containing all lexical tokens
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 * 
	 */
	public QueryExpression(ITMQLRuntime runtime, ILexer lexer) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		this(null, lexer.getTmqlTokens(), lexer.getTokens(), runtime);
	}

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
	@SuppressWarnings("unchecked")
	public QueryExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * initialize token iterator
		 */
		Iterator<Class<? extends IToken>> iteratorTmqlTokens = tmqlTokens.iterator();
		/*
		 * get key token
		 */
		Class<? extends IToken> token = iteratorTmqlTokens.next();

		/*
		 * index of first non-environmental content
		 */
		int index = 0;

		/*
		 * check if first token is %prefix or %pragma
		 */
		if (token.equals(de.topicmapslab.tmql4j.draft2010.grammar.lexical.Pragma.class) || token.equals(Prefix.class)) {
			/*
			 * lookup last index of keyword %pragma
			 */
			List<Integer> indizes = ParserUtils.indizes(tmqlTokens, de.topicmapslab.tmql4j.draft2010.grammar.lexical.Pragma.class);
			int pragmaIndex = indizes.isEmpty() ? -1 : indizes.get(indizes.size() - 1);
			/*
			 * lookup last index of keyword %prefix
			 */
			indizes = ParserUtils.indizes(tmqlTokens, Prefix.class);
			int directiveIndex = indizes.isEmpty() ? -1 : indizes.get(indizes.size() - 1);

			/*
			 * get last index of environment definition
			 */
			index = pragmaIndex > directiveIndex ? pragmaIndex + 3 : directiveIndex + 3;

			/*
			 * add environment-clause
			 */
			checkForExtensions(EnvironmentClause.class, tmqlTokens.subList(0, index), tokens.subList(0, index), runtime);
		}

		/*
		 * is there non-environmental content
		 */
		if (index < tmqlTokens.size()) {
			int size = tmqlTokens.size();
			checkForExtensions(Expression.class, tmqlTokens.subList(index, size), tokens.subList(index, size), runtime);
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
