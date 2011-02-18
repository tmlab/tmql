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

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationRegistry;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.exception.UnsupportedNavigationTypeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;

/**
 * Special implementation of {@link ExpressionImpl} representing a step.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  step ::= ( >> | << ) axis [ anchor ]
 * </p>
 * <p>
 * step ::= // anchor
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Step extends ExpressionImpl {

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
	public Step(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		setGrammarType(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (getTmqlTokens().size() > 3) {
			return false;
		} else if (!getTmqlTokens().get(0).equals(MoveBackward.class) && !getTmqlTokens().get(0).equals(MoveForward.class)) {
			return false;
		} 
		try{
			/*
			 * check if the axis is known
			 */
			NavigationRegistry.buildHandler().lookup(getTmqlTokens().get(1));
		}catch(UnsupportedNavigationTypeException ex){
			return false;
		}catch(NavigationException ex){
			return false;
		}
		return true;
	}

}
