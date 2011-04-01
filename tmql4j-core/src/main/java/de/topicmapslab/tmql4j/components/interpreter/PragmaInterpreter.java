/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.interpreter;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PragmaRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IPragmaRegistry;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.Pragma;

/**
 * @author Sven Krosse
 * 
 */
public class PragmaInterpreter extends ExpressionInterpreterImpl<Pragma> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public PragmaInterpreter(Pragma ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		final String identifier = getTokens().get(1);
		final String value = getTokens().get(2);
		IPragmaRegistry registry = runtime.getLanguageContext().getPragmaRegistry();
		if (registry.isKnownPragma(identifier)) {
			registry.interpret(runtime, context, identifier, value);
			return QueryMatches.emptyMatches();
		}
		
		throw new TMQLRuntimeException(PragmaRegistry.THE_GIVEN_IDENTIFIER_IS_UNKNOWN_AS_PRAGMA);
	}

}
