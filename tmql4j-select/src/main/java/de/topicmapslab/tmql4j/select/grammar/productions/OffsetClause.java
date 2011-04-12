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

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Offset;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/*
 *
 */
/**
 * Special implementation of {@link ExpressionImpl} representing a
 * offset-clause.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * offset-clause ::=  OFFSET  value-expression 
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class OffsetClause extends ExpressionImpl {

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
	public OffsetClause(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		setGrammarType(0);
		if (tmqlTokens.contains(Wildcard.class)) {
			checkForExtensions(PreparedExpression.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		/*
		 * expects exactly two tokens beginning with the keyword OFFSET and an
		 * integer value
		 */
		return getTmqlTokens().size() == 2 && getTmqlTokens().get(0).equals(Offset.class) && (getTmqlTokens().get(1).equals(Wildcard.class) || LiteralUtils.isInteger(getTokens().get(1)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void asFlatString(StringBuilder builder) {
		for (String token : getTokens()) {
			builder.append(token);
			builder.append(WHITESPACE);
		}
	}
}
