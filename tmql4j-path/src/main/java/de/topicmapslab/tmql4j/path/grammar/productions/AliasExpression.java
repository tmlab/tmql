/*
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
import de.topicmapslab.tmql4j.path.grammar.lexical.As;

/**
 * The alias expression used to set column names for the specific
 * value-expression.
 * 
 * @since 3.0.0
 * 
 * @author Sven Krosse
 * 
 */
public class AliasExpression extends ExpressionImpl {

	/**
	 * constructor
	 * 
	 * @param parent
	 *            the parent
	 * @param tmqlTokens
	 *            the language specific tokens
	 * @param tokens
	 *            the literal tokens
	 * @param runtime
	 *            the runtime
	 * @throws TMQLInvalidSyntaxException
	 * @throws TMQLGeneratorException
	 */
	public AliasExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return getTmqlTokens().size() == 2 && getTmqlTokens().get(0).equals(As.class);
	}

}
