/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.grammar.pragma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.IPragma;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * 
 */
public class Taxonometry implements IPragma {

	/**
	 * 
	 */
	private static final String TM_TRANSITIVE = "tm:transitive";
	/**
	 * 
	 */
	private static final String TM_INTRANSITIVE = "tm:intransitive";
	/**
	 * 
	 */
	private static final String TAXONOMETRY = "taxonometry";
	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * {@inheritDoc}
	 */
	public String getIdentifier() {
		return TAXONOMETRY;
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(ITMQLRuntime runtime, IContext context, String value) throws TMQLRuntimeException {
		/*
		 * check if value is tm:intransitive
		 */
		if (value.equalsIgnoreCase(TM_INTRANSITIVE)) {
			context.setTransitive(false);
		}
		/*
		 * check if value is tm:transitive
		 */
		else if (value.equalsIgnoreCase(TM_TRANSITIVE)) {
			context.setTransitive(true);
		} else {
			logger.warn("Value '" + value + "' is unknown for pragma '" + getIdentifier() + "'");
		}
	}

}
