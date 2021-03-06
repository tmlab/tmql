/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;

/**
 * A special expression symbolize a place-holder of a prepared statement
 * 
 * @author Sven Krosse
 * 
 */
public class PreparedExpression extends ExpressionImpl {

	/**
	 * constructor
	 * 
	 * @param parent
	 *            parent
	 * @param tmqlTokens
	 *            tmqlTokens
	 * @param tokens
	 *            tokens
	 * @param runtime
	 *            the runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if expression is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if sub-expression cannot be instantiate
	 */
	public PreparedExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		/*
		 * only one token is expected
		 */
		return getTmqlTokens().size() == 1 && getTmqlTokens().get(0).equals(Wildcard.class);
	}

}
