/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.model;

import java.util.Collection;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.exception.UnsupportedNavigationTypeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

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
	 * @param token
	 *            the token
	 * @return the instance of the navigation axis
	 * @throws UnsupportedNavigationTypeException
	 *             unknown or unregistered type.
	 */
	INavigationAxis lookup(final Class<? extends IToken> token) throws UnsupportedNavigationTypeException;

	/**
	 * Retrieve the navigation axis instance for the given type. Method can
	 * throws an exception if the type is unknown or unregistered. It can never
	 * return <code>null</code>.
	 * 
	 * @param token
	 *            the token
	 * @return the instance of the navigation axis
	 * @throws UnsupportedNavigationTypeException
	 *             unknown or unregistered type.
	 */
	INavigationAxis lookup(IToken token) throws UnsupportedNavigationTypeException;

}
