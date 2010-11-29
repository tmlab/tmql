/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation.model;

import java.util.Collection;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.exception.UnsupportedNavigationTypeException;

/**
 * Interface of the navigation handler within a topic map instance. Handler
 * supports methods to find supported navigation axis of different topic map
 * constructs.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface INavigationHandler {

	/**
	 * Retrieve all navigation axis, which supports the given topic map
	 * construct. The returned set can not be <code>null</code>, but can be
	 * empty.
	 * 
	 * @param construct
	 *            the given topic map construct
	 * @return a set of supported navigation axis
	 * @throws NavigationException
	 *             navigation failed
	 */
	Collection<INavigationAxis> getSupportedNavigationAxis(final Construct construct) throws NavigationException;

	/**
	 * Retrieve all navigation axis, which supports the given topic map
	 * construct and the optional parameter. The returned set can not be
	 * <code>null</code>, but can be empty.
	 * 
	 * @param construct
	 *            the given topic map construct
	 * @param optional
	 *            the optional parameter
	 * @return a set of supported navigation axis
	 * @throws NavigationException
	 *             navigation failed
	 */
	Collection<INavigationAxis> getSupportedNavigationAxis(final Construct construct, final Construct optional) throws NavigationException;

	/**
	 * Retrieve the navigation axis instance for the given type. Method can
	 * throws an exception if the type is unknown or unregistered. It can never
	 * return <code>null</code>.
	 * 
	 * @param type
	 *            the given type of navigation axis
	 * @return the instance of the navigation axis
	 * @throws UnsupportedNavigationTypeException
	 *             unknown or unregistered type.
	 */
	INavigationAxis lookup(final NavigationAxis type) throws UnsupportedNavigationTypeException;

}
