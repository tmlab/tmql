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
package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Function;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

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
	public FunctionInvocation(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * check if has more than 3 tokens -> has parameters
		 */
		if (tokens.size() > 3) {
			int index = tmqlTokens.indexOf(BracketRoundOpen.class);

			/*
			 * has parameters
			 */
			if (index != -1) {
				checkForExtensions(Parameters.class, tmqlTokens.subList(index + 1, tmqlTokens.size() - 1), tokens.subList(index + 1, tokens.size() - 1), runtime);
			}
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
		return getTmqlTokens().size() >= 3 && getTmqlTokens().get(1).equals(BracketRoundOpen.class) && getTmqlTokens().get(getTmqlTokens().size() - 1).equals(BracketRoundClose.class)
				&& getTmqlTokens().get(0).equals(Function.class) && getRuntime().getLanguageContext().getFunctionRegistry().isKnownFunction(getTokens().get(0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartBefore(StringBuilder builder) {
		builder.append(getTokens().get(0));
		builder.append(WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getJoinToken() {
		return Comma.TOKEN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartAfter(StringBuilder builder) {
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
	}

}
