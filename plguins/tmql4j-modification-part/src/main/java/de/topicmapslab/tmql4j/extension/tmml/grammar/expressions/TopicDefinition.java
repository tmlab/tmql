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
package de.topicmapslab.tmql4j.extension.tmml.grammar.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.AxisIndicators;
import de.topicmapslab.tmql4j.lexer.token.AxisItem;
import de.topicmapslab.tmql4j.lexer.token.AxisLocators;
import de.topicmapslab.tmql4j.lexer.token.Literal;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisItem;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * topic-definition as a kind of update-clauses.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * topic-definition ::= string-literal ( ! | ~ | = )
 * topic-definition ::= string-literal << ( item | locators | indicators )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TopicDefinition extends ExpressionImpl {

	private final Class<? extends IToken> identifierType;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            the parent expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @param runtime
	 *            the runtime
	 * @throws TMQLInvalidSyntaxException
	 * @throws TMQLGeneratorException
	 */
	public TopicDefinition(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		if (getTokens().size() == 1) {
			identifierType = AxisIndicators.class;
		} else if (getTokens().size() == 2) {
			identifierType = getTmqlTokens().get(1);
		} else if (getTokens().size() == 3) {
			identifierType = getTmqlTokens().get(2);
		} else {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens,
					TopicDefinition.class);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		if (getTokens().size() == 1) {
			return getTmqlTokens().get(0).equals(Literal.class);
		} else if (getTokens().size() == 2) {
			Class<? extends IToken> token = getTmqlTokens().get(1);
			return getTmqlTokens().get(0).equals(Literal.class)
					&& (token.equals(ShortcutAxisItem.class)
							|| token.equals(ShortcutAxisLocators.class) || token
							.equals(ShortcutAxisIndicators.class)|| getTokens().get(1).equalsIgnoreCase("="));
		} else if (getTokens().size() == 3) {
			Class<? extends IToken> token = getTmqlTokens().get(2);
			return getTmqlTokens().get(0).equals(Literal.class)
					&& (token.equals(AxisItem.class)
							|| token.equals(AxisLocators.class) || token
							.equals(AxisIndicators.class));
		}
		return false;
	}

	/**
	 * Returns the token specifying the type of topic identifier
	 * 
	 * @return the token representing the identifier type
	 */
	public Class<? extends IToken> getIdentifierType() {
		return identifierType;
	}

}
