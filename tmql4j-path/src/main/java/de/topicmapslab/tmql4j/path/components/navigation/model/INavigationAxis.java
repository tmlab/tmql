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
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Interface definition of a TMQL axis specified by the current TMQL draft.
 * <p>
 * Each navigation step is interpreted within the effective map . Navigational
 * axes are derived from the structure of a Topic Map instance [TMDM] and can
 * either be followed in forward (>>) or in backward (<<) direction:
 * </p>
 * <p>
 * The optional anchor adds control information which is useful with some axes,
 * but not others. If it is missing tm:subject will be assumed.
 * <p>
 * </p>
 * When the anchor is evaluated, it must evaluate to a topic item and is
 * interpreted as type. Then in all navigation steps the current setting for
 * type transitivity is honored. </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface INavigationAxis {

	/**
	 * Method returns the internal stored instance of the topic map. The topic
	 * map instance will be used during the navigation process.
	 * 
	 * @return the internal topic map instance and never <code>null</code>
	 * @throws NavigationException
	 *             thrown if topic map reference is <code>null</code>
	 */
	TopicMap getTopicMap() throws NavigationException;

	/**
	 * Method changes the internal instance of the topic map and set it to the
	 * given value. The topic map instance will be used during the navigation
	 * process.
	 * 
	 * @param topicMap
	 *            the new topic map reference
	 */
	void setTopicMap(TopicMap topicMap);

	/**
	 * Method returns a enumeration value representing the type of the TMQL
	 * axis. The enumeration values are defined by the current TMQL draft.
	 * 
	 * @return a value of the enumeration {@link NavigationAxis}
	 */
	NavigationAxis getNavigationType();

	/**
	 * Method checks if this instance of a TMQL navigation axis supports the
	 * navigation in backward direction for the given construct.
	 * 
	 * @param construct
	 *            the construct to check
	 * @return <code>true</code> if the axis supports the navigation in backward
	 *         direction for the given construct, false otherwise.
	 * @throws NavigationException
	 *             thrown if support cannot be checked
	 */
	boolean supportsBackwardNavigation(final Object construct) throws NavigationException;

	/**
	 * Method checks if this instance of a TMQL navigation axis supports the
	 * navigation in backward direction for the given construct and optional
	 * type.
	 * 
	 * <p>
	 * Please note that some axis don't support the optional type, it will be
	 * ignored.
	 * </p>
	 * 
	 * @param construct
	 *            the construct to check
	 * @param optional
	 *            the optional parameter for the axis
	 * @return <code>true</code> if the axis supports the navigation in backward
	 *         direction for the given construct, false otherwise.
	 * @throws NavigationException
	 *             thrown if support cannot be checked
	 */
	boolean supportsBackwardNavigation(final Object construct, final Construct optional) throws NavigationException;

	/**
	 * Method checks if this instance of a TMQL navigation axis supports the
	 * navigation in forward direction for the given construct and optional
	 * type.
	 * 
	 * @param construct
	 *            the construct to check
	 * @return <code>true</code> if the axis supports the navigation in forward
	 *         direction for the given construct, false otherwise.
	 * @throws NavigationException
	 *             thrown if support cannot be checked
	 */
	boolean supportsForwardNavigation(final Object construct) throws NavigationException;

	/**
	 * Method checks if this instance of a TMQL navigation axis supports the
	 * navigation in forward direction for the given construct and optional
	 * type.
	 * 
	 * <p>
	 * Please note that some axis don't support the optional type, it will be
	 * ignored.
	 * </p>
	 * 
	 * @param construct
	 *            the construct to check
	 * @param optional
	 *            the optional parameter for the axis
	 * @return <code>true</code> if the axis supports the navigation in forward
	 *         direction for the given construct, false otherwise.
	 * @throws NavigationException
	 *             thrown if support cannot be checked
	 */
	boolean supportsForwardNavigation(final Object construct, final Object optional) throws NavigationException;

	/**
	 * Method navigate through the abstract topic map graph over the axis in
	 * backward direction defined by the current instance.
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @return the result of navigation
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Collection<?> navigateBackward(final Object construct) throws NavigationException;

	/**
	 * Method navigate through the abstract topic map graph over the axis in
	 * backward direction defined by the current instance.
	 * 
	 * <p>
	 * Please note that some axis don't support the optional type, it will be
	 * ignored.
	 * </p>
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @param optional
	 *            the optional type parameter of the axis
	 * @return the result of navigation
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Collection<?> navigateBackward(final Object construct, final Object optional) throws NavigationException;

	/**
	 * Method navigate through the abstract topic map graph over the axis in
	 * forward direction defined by the current instance.
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @return the result of navigation
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Collection<?> navigateForward(final Object construct) throws NavigationException;

	/**
	 * Method navigate through the abstract topic map graph over the axis in
	 * forward direction defined by the current instance.
	 * 
	 * <p>
	 * Please note that some axis don't support the optional type, it will be
	 * ignored.
	 * </p>
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @param optional
	 *            the optional type parameter of the axis
	 * @return the result of navigation
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Collection<?> navigateForward(final Object construct, final Object optional) throws NavigationException;

	/**
	 * Method returns a class object representing the type of the results which
	 * will be expected by the navigation process in backward direction. The
	 * class object can represent a topic map construct, a locator of an object.
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @return the type of expected results
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Class<?> getBackwardNavigationResultClass(final Object construct) throws NavigationException;

	/**
	 * Method returns a class object representing the type of the results which
	 * will be expected by the navigation process in forward direction. The
	 * class object can represent a topic map construct, a locator of an object.
	 * 
	 * @param construct
	 *            the construct used as anchor or start node for navigation
	 * @return the type of expected results
	 * @throws NavigationException
	 *             thrown if navigation fails, because of unsupported types
	 */
	Class<?> getForwardNavigationResultClass(final Object construct) throws NavigationException;
}
