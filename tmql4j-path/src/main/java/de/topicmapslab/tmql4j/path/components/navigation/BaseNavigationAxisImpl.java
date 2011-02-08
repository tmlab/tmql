/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation;

import java.util.Collection;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Base implementation of a navigation axis to implement some core functionality
 * of each TMQL axis.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BaseNavigationAxisImpl implements INavigationAxis {

	/**
	 * the internal enumeration value representing the axis type
	 */
	private final Class<? extends IToken> axisToken;
	/**
	 * the internal topic map instance
	 */
	private TopicMap topicMap;

	/**
	 * constructor
	 * 
	 * @param axisToken
	 *            the axis token
	 */
	protected BaseNavigationAxisImpl(final Class<? extends IToken> axisToken) {
		this.axisToken = axisToken;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IToken> getNavigationType() {
		return axisToken;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct) throws NavigationException {
		return navigateBackward(construct, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct) throws NavigationException {
		return navigateForward(construct, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct) throws NavigationException {
		return supportsBackwardNavigation(construct, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct) throws NavigationException {
		return supportsForwardNavigation(construct, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMap getTopicMap() throws NavigationException {
		if (topicMap == null) {
			throw new NavigationException("topic map not set!");
		}
		return topicMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopicMap(TopicMap topicMap) {
		this.topicMap = topicMap;
	}

}
