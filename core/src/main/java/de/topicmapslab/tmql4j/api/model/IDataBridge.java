/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.model;

import org.tmapi.core.Construct;

import de.topicmapslab.java.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.api.exceptions.DataBridgeRuntimeException;
import de.topicmapslab.tmql4j.api.exceptions.UnsupportedModuleException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;

/**
 * Interface definition of the data-bridge as abstraction from the underlying
 * back-end. The bridge is used to get core information, for example items, or
 * the axis handler of the underlying implementation module.
 * 
 * <p>
 * The interface defines the mapping between the real implemented back-end and
 * the TMQL4J engine. The real back-end provides the core functionality and wrap
 * the internal objects to TMAPI objects.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IDataBridge {

	/**
	 * Method returns the corresponding TMAPI object for the given identifier.
	 * The identifier is a string representation used in the context of a TMQL
	 * query to reference a topic map item instance.
	 * <p>
	 * If the item reference is an identifier then this identifier is
	 * interpreted as an topic item identifier ([TMDM], Clause 5.1) for a topic
	 * in the effective map. The result is then this topic item; if no such
	 * topic exists, an error will be flagged.
	 * </p>
	 * <p>
	 * If the item reference is an identifier then this identifier is
	 * interpreted as an topic item identifier ([TMDM], Clause 5.1) for a topic
	 * in the effective map. The result is then this topic item; if no such
	 * topic exists, an error will be flagged.
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param identifier
	 *            the identifier
	 * @return the found element but never <code>null</code>
	 * @throws UnsupportedModuleException
	 *             thrown if operation is unsupported
	 * @throws DataBridgeRuntimeException
	 *             thrown if execution of external method fails
	 */
	Construct getConstructByIdentifier(final TMQLRuntime runtime,
			final String identifier) throws UnsupportedModuleException,
			DataBridgeRuntimeException;

	/**
	 * Method tries to instantiate the navigation axis by identifier. The method
	 * returns the specific axis for the given identifier if it exists.
	 * <p>
	 * Each navigation step is interpreted within the effective map .
	 * Navigational axes are derived from the structure of a Topic Map instance
	 * [TMDM] and can either be followed in forward (>>) or in backward (<<)
	 * direction:
	 * </p>
	 * <p>
	 * The optional anchor adds control information which is useful with some
	 * axes, but not others. If it is missing tm:subject will be assumed.
	 * <p>
	 * </p>
	 * When the anchor is evaluated, it must evaluate to a topic item and is
	 * interpreted as type. Then in all navigation steps the current setting for
	 * type transitivity is honored. </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param identifier
	 *            the axis identifier
	 * @return the {@link INavigationAxis} instance and never <code>null</code>
	 * @throws UnsupportedModuleException
	 *             thrown if operation is unsupported
	 * @throws DataBridgeRuntimeException
	 *             thrown if execution of external method fails
	 */
	INavigationAxis getImplementationOfTMQLAxis(final TMQLRuntime runtime,
			final String identifier) throws UnsupportedModuleException,
			DataBridgeRuntimeException;

	/**
	 * Method tries to instantiate an {@link IConstructResolver} implementation
	 * specialized to found results by given identifiers for specific back-end.
	 * <p>
	 * The internal construct resolver instance is often used to realize the
	 * method getConstructbyIdentifier() of a data-bridge implementation.
	 * </p>
	 * 
	 * @see IDataBridge#getConstructByIdentifier(TMQLRuntime, String)
	 * 
	 * @return the internal construct resolver instance and never
	 *         <code>null</code>.
	 * @throws UnsupportedModuleException
	 *             thrown if operation is unsupported
	 * @throws DataBridgeRuntimeException
	 *             thrown if execution of external method fails
	 */
	IConstructResolver getConstructResolver()
			throws UnsupportedModuleException, DataBridgeRuntimeException;

}
