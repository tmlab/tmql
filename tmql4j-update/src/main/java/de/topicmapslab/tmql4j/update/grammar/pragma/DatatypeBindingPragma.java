/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.grammar.pragma;

import de.topicmapslab.tmql4j.components.interpreter.IPragma;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * An extension for the pragma interpreter of the PATH style. The pragma
 * indicates the mode, that the update-clause of an occurrence and variant
 * should reuse the datatype.
 * 
 * @author Sven Krosse
 * 
 */
public class DatatypeBindingPragma implements IPragma {

	public static final String IDENTIFIER = "datatype-binding";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(ITMQLRuntime runtime, IContext context, String value) throws TMQLRuntimeException {
		context.setCustomFeature(IDENTIFIER, LiteralUtils.asBoolean(value));
	}

	/**
	 * Indicates the default value of this pragma
	 * 
	 * @return the default value?
	 */
	public static boolean getDefault() {
		return true;
	}

}
