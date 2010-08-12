/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.impl.tmapi;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.api.exceptions.DataBridgeRuntimeException;
import de.topicmapslab.tmql4j.api.exceptions.UnsupportedModuleException;
import de.topicmapslab.tmql4j.api.model.IConstructResolver;
import de.topicmapslab.tmql4j.api.model.IDataBridge;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.navigation.NavigationHandler;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;
import de.topicmapslab.tmql4j.navigation.model.INavigationAxis;

/**
 * Implementation of {@link IDataBridge} to bind the TMQL4J engine to the TMAPI
 * functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMAPIDataBridge implements IDataBridge {

	private final ConstructResolver constructResolver;

	public TMAPIDataBridge() {
		this.constructResolver = new ConstructResolver();
	}

	/**
	 * {@inheritDoc}
	 */
	public Construct getConstructByIdentifier(final TMQLRuntime runtime,
			final String identifier) throws UnsupportedModuleException,
			DataBridgeRuntimeException {
		try {
			return constructResolver
					.getElementByIdentifier(runtime, identifier);
		} catch (TMQLRuntimeException e) {
			throw new DataBridgeRuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationAxis getImplementationOfTMQLAxis(TMQLRuntime runtime,
			String identifier) throws UnsupportedModuleException,
			DataBridgeRuntimeException {
		try {
			NavigationHandler handler = NavigationHandler.buildHandler();
			NavigationAxis axis = NavigationAxis.valueOf(identifier);
			INavigationAxis axisHandler = handler.lookup(axis);
			axisHandler.setTopicMap(runtime.getTopicMap());
			axisHandler.setEnvironment(runtime.getEnvironmentMap());
			return axisHandler;
		} catch (NavigationException e) {
			throw new DataBridgeRuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IConstructResolver getConstructResolver()
			throws UnsupportedModuleException, DataBridgeRuntimeException {
		return constructResolver;
	}

}
