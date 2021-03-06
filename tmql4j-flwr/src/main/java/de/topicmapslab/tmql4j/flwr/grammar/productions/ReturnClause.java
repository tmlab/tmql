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
package de.topicmapslab.tmql4j.flwr.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.Return;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlStartTag;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * return-clause.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * return-clause ::= RETURN   content;
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReturnClause extends ExpressionImpl {

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
	public ReturnClause(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		setGrammarType(0);
		/*
		 * is canonical return-clause
		 */
		if (getTmqlTokens().get(0).equals(Return.class)) {
			/*
			 * add content without keyword RETURN
			 */
			checkForExtensions(Content.class, tmqlTokens.subList(1, tmqlTokens.size()), tokens.subList(1, tokens.size()), runtime);
		}
		/*
		 * is NCL return-clause containing XML content
		 */
		else {
			checkForExtensions(Content.class, tmqlTokens, tokens, runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		/*
		 * expects at least two tokens starting with the keyword RETURN or XML
		 * start-tag
		 */
		return getTmqlTokens().size() > 1 && (getTmqlTokens().get(0).equals(Return.class) || getTmqlTokens().get(0).equals(XmlStartTag.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartBefore(StringBuilder builder) {
		builder.append(Return.TOKEN);
		builder.append(WHITESPACE);
	}
}
