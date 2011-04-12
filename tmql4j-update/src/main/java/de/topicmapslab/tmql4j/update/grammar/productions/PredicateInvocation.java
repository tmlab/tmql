/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;

/**
 * Predicate invocation extension especially for update-clauses
 * 
 * @author Sven Krosse
 * 
 */
public class PredicateInvocation extends de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation {

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
	public PredicateInvocation(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkForExtensions(Class<? extends IExpression> clazz, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		if (clazz.equals(de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocationRolePlayerExpression.class)) {
			super.checkForExtensions(PredicateInvocationRolePlayerExpression.class, tmqlTokens, tokens, runtime);
		} else {
			super.checkForExtensions(clazz, tmqlTokens, tokens, runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void asFlatString(StringBuilder builder) {
		builder.append(getTokens().get(0));
		builder.append(WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);
		boolean first = true;
		for (PredicateInvocationRolePlayerExpression expression : getExpressionFilteredByType(PredicateInvocationRolePlayerExpression.class)) {
			if (!first) {
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			expression.asFlatString(builder);
			first = false;
		}
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
	}

}
