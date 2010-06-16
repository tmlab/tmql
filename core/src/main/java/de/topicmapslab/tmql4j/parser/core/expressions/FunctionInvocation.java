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
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.Function;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * function-invocation.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * function-invocation ::= item-reference parameters
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionInvocation extends ExpressionImpl {

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
	public FunctionInvocation(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		int index = tmqlTokens.indexOf(BracketRoundOpen.class);

		/*
		 * has parameters
		 */
		if (index != -1) {
			checkForExtensions(Parameters.class, tmqlTokens.subList(index + 1,
					tmqlTokens.size() - 1), tokens.subList(index + 1, tokens
					.size() - 1), runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		/*
		 * expects at lest one token beginning with a function identifier
		 */
		return !getTmqlTokens().isEmpty()
				&& getTmqlTokens().get(0).equals(Function.class);
	}

}
