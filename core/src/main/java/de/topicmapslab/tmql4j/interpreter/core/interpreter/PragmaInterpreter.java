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

import de.topicmapslab.java.tmapi.extension.model.index.SupertypeSubtypeIndex;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.Pragma;

/**
 * 
 * Special interpreter class to interpret pragmas.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * pragma ::= %pragma identifier QIRI
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PragmaInterpreter extends ExpressionInterpreterImpl<Pragma> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PragmaInterpreter(Pragma ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * extract identifier
		 */
		String identifier = getTokens().get(1);
		/*
		 * extract QIRI
		 */
		String qiri = getTokens().get(2);

		/*
		 * check if topic is taxonometry topic
		 */
		if (identifier.equalsIgnoreCase("taxonometry")) {
			/*
			 * check if value is tm:intransitive
			 */
			if (qiri.equalsIgnoreCase("tm:intransitive")) {
				/*
				 * switch type-transitivity off
				 */
				runtime.getProperties().setProperty(
						TMQLRuntimeProperties.TMDM_TYPE_TRANSITIVITY, "false");
				try {
					SupertypeSubtypeIndex index = runtime.getTopicMap().getIndex(SupertypeSubtypeIndex.class);
					if ( !index.isOpen()){
						index.open();
					}
					index.reindex();
				} catch (UnsupportedOperationException e) {
					// NOTHING TO DO
				}
				runtime.setActsTransitive(false);
			}
			/*
			 * check if value is tm:transitive
			 */
			else if (qiri.equalsIgnoreCase("tm:transitive")) {
				/*
				 * switch type-transitivity on
				 */
				runtime.getProperties().setProperty(
						TMQLRuntimeProperties.TMDM_TYPE_TRANSITIVITY, "true");
				try {
					runtime.getTopicMap().getIndex(SupertypeSubtypeIndex.class)
							.reindex();
				} catch (UnsupportedOperationException e) {
					// NOTHING TO DO
				}
				runtime.setActsTransitive(true);
			} else {
				throw new TMQLRuntimeException("Unknown QIRI for taxonometry.");
			}
		} else {
			throw new TMQLRuntimeException("Unknown identifier " + identifier);
		}

	}

}
