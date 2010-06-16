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
package de.topicmapslab.tmql4j.interpreter.model.context;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.interpreter.core.predefinition.Environment;

/**
 * Interface definition of the initial context of each TMQL4J execution process.
 * <p>
 * The initial context provides information about the system-variables states at
 * the begin of querying process and about the predefined environment.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IInitialContext {

	/**
	 * Method deliver the instance of the {@link IVariableSet} of system
	 * variables of TMQL, like $# or %%.
	 * 
	 * @return the {@link IVariableSet} of system variables of TMQL
	 */
	public IVariableSet getPredefinedVariableSet();

	/**
	 * Method deliver the topic map defined in TMQL clause, which shall be
	 * queried within the parsing process.
	 * 
	 * @return the topic maps to queried
	 */
	public TopicMap getQueriedTopicMap();

	/**
	 * Method deliver the topic maps system defined in context of the instance
	 * of the TMQL engine.
	 * 
	 * @return the topic maps system
	 */
	public TopicMapSystem getQueriedTopicMapSystem();

	/**
	 * Method the deliver the environment map of the TMQL engine defined in
	 * current draft.
	 * 
	 * @return the environment map
	 */
	public Environment getEnvironment();
}
