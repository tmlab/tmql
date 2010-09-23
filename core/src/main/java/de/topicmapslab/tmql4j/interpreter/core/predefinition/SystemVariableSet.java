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
package de.topicmapslab.tmql4j.interpreter.core.predefinition;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.context.VariableSetImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;

/**
 * Base implementation of {@link ISystemVariableSet}. This set only contains the
 * system variables specified by the current TMQL draft.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SystemVariableSet extends VariableSetImpl {

	/**
	 * variable definition representing the variable %%
	 */
	public final String currentEnvironmentMap = VariableNames.ENVIRONMENT_MAP;
	/**
	 * variable definition representing the variable %_
	 */
	public final String currentContextMap = VariableNames.CURRENT_MAP;

	/**
	 * Constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the initial context
	 * @throws TMQLRuntimeException
	 *             thrown if initialization failed
	 */
	public SystemVariableSet(ITMQLRuntime runtime, IInitialContext context)
			throws TMQLRuntimeException {
		setValue(currentEnvironmentMap, runtime.getEnvironment().getTopicMap());
		setValue(currentContextMap, context.getQueriedTopicMap());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueBindingSet() {
		return false;
	}

}
