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
package de.topicmapslab.tmql4j.path.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.DatatypedElement;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.Literal;

/**
 * Special implementation of {@link ExpressionImpl} representing a anchor.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * anchor ::= '.' | variable | topic-ref | literal
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Anchor extends ExpressionImpl {

	/**
	 * representing the anchor type for current node '.'
	 */
	public static final int TYPE_DOT = 0;
	/**
	 * representing the anchor type for variables
	 */
	public static final int TYPE_VARIABLE = 1;
	/**
	 * representing the anchor type for literals
	 */
	public static final int TYPE_LITERAL = 2;
	/**
	 * representing the anchor type for topic-references
	 */
	public static final int TYPE_TOPICREF = 3;
	/**
	 * representing the anchor type for data-typed-literals
	 */
	public static final int TYPE_DATATYPED = 4;

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
	public Anchor(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		Class<? extends IToken> token = getTmqlTokens().get(0);
		if (token.equals(Dot.class)) {
			setGrammarType(TYPE_DOT);
		} else if (token.equals(Variable.class)) {
			setGrammarType(TYPE_VARIABLE);
		} else if (token.equals(Literal.class)) {
			setGrammarType(TYPE_LITERAL);
		} else if (token.equals(Element.class)) {
			setGrammarType(TYPE_TOPICREF);
		} else if (token.equals(DatatypedElement.class)) {
			setGrammarType(TYPE_DATATYPED);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (getTmqlTokens().size() != 1) {
			return false;
		}
		Class<? extends IToken> token = getTmqlTokens().get(0);
		return token.equals(Dot.class) || token.equals(Variable.class) || token.equals(Element.class) || token.equals(Literal.class) || token.equals(DatatypedElement.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
}
