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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.PrefixDirective;

/**
 * 
 * Special interpreter class to interpret prefix-directives.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * prefix-directive ::= %prefix identifier QIRI
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PrefixDirectiveInterpreter extends
		ExpressionInterpreterImpl<PrefixDirective> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PrefixDirectiveInterpreter(PrefixDirective ex) {
		super(ex);
	}

	/*
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * get QName
		 */
		String identifier = getTokens().get(1);
		/*
		 * get IRI
		 */
		String qiri = getTokens().get(2);

		/*
		 * store prefix
		 */
		runtime.getLanguageContext().getPrefixHandler().registerPrefix(identifier, qiri);
	}

}
