/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module;

import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IPragma;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IPragmaRegistry;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.pragma.Taxonometry;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Implementation of {@link IPragma}
 * 
 * @author Sven Krosse
 *
 */
public class PragmaRegistry implements IPragmaRegistry {

	/**
	 * 
	 */
	public static final String THE_GIVEN_IDENTIFIER_IS_UNKNOWN_AS_PRAGMA = "The given identifier is unknown as pragma!";
	/**
	 * internal storage
	 */
	private Map<String, IPragma> pragmas;
	
	/**
	 * constructor
	 */
	public PragmaRegistry() {
		register(new Taxonometry());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void register(IPragma pragma) {
		if ( pragmas == null ){
			pragmas = HashUtil.getHashMap();
		}
		pragmas.put(pragma.getIdentifier().toLowerCase(), pragma);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isKnownPragma(String identifier) {
		return pragmas != null && pragmas.containsKey(identifier.toLowerCase());
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(ITMQLRuntime runtime, IContext context, String identifier, String value) throws TMQLRuntimeException {
		if ( !isKnownPragma(identifier)){
			throw new TMQLRuntimeException(THE_GIVEN_IDENTIFIER_IS_UNKNOWN_AS_PRAGMA);
		}
		pragmas.get(identifier.toLowerCase()).interpret(runtime, context, value);
	}

}
