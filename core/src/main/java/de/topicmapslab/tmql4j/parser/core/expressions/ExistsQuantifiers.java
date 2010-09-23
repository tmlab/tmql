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

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.At;
import de.topicmapslab.tmql4j.lexer.token.Least;
import de.topicmapslab.tmql4j.lexer.token.Most;
import de.topicmapslab.tmql4j.lexer.token.Some;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * exists-quantifiers.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * exists-quantifier ::= some | at least integer | at most integer
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExistsQuantifiers extends ExpressionImpl {

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
	public ExistsQuantifiers(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		/*
		 * is some
		 */
		if (getTmqlTokens().size() == 1
				&& getTmqlTokens().get(0).equals(Some.class)) {
			return true;
		}
		/*
		 * is at least integer
		 */
		else if (getTmqlTokens().size() == 3
				&& getTmqlTokens().get(0).equals(At.class)
				&& getTmqlTokens().get(1).equals(Least.class)) {
			return true;
		}
		/*
		 * is at most integer
		 */
		else if (getTmqlTokens().size() == 3
				&& getTmqlTokens().get(0).equals(At.class)
				&& getTmqlTokens().get(1).equals(Most.class)) {
			return true;
		}
		return false;
	}

}
