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
package de.topicmapslab.tmql4j.select.grammar.productions;

import java.util.Iterator;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.path.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.select.grammar.lexical.Select;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * query-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * query-expression ::= [ environment-clause ] select-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpression extends ExpressionImpl {

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
		if (token.equals(de.topicmapslab.tmql4j.path.grammar.lexical.Pragma.class) || token.equals(Prefix.class)) {
			/*
			 * lookup last index of keyword %pragma
			 */
			List<Integer> indizes = ParserUtils.indizes(tmqlTokens, de.topicmapslab.tmql4j.path.grammar.lexical.Pragma.class);
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
			/*
			 * check if token is keyword SELECT
			 */
			if (tmqlTokens.get(index).equals(Select.class)) {
				checkForExtensions(SelectExpression.class, tmqlTokens.subList(index, size), tokens.subList(index, size), runtime);
			} else {
				throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, getClass());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return isValid(getTmqlTokens());
	}
	
	/**
	 * Utility method checks if the tokens are valid content for this expression type. 
	 * @param tokens the tokens
	 * @return <code>true</code> if the tokens represent a valid select-style-query-expression, <code>false</code> otherwise
	 */
	public static boolean isValid(List<Class<? extends IToken>> tokens){
		if ( tokens.isEmpty()){
			return false;
		}
		int index = 0;
		/*
		 * look for first token which is not part of an environment clause
		 */
		while ( tokens.size() > index && ( tokens.get(index).equals(Pragma.class) || tokens.get(index).equals(Prefix.class))){
			index += 3;
		}
		return tokens.size() > index && tokens.get(index).equals(Select.class);
	}

}
