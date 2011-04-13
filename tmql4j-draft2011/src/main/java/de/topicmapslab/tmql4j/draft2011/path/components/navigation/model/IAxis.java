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

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interface definition of a TMQL axis specified by the current TMQL draft.
 * <p>
 * Each navigation step is interpreted within the effective map . Navigational axes are derived from the structure of a
 * Topic Map instance [TMDM] and can either be followed in forward (>>) or in backward (<<) direction:
 * </p>
 * <p>
 * The optional anchor adds control information which is useful with some axes, but not others. If it is missing
 * tm:subject will be assumed.
 * <p>
 * </p>
 * When the anchor is evaluated, it must evaluate to a topic item and is interpreted as type. Then in all navigation
 * steps the current setting for type transitivity is honored. </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IAxis {

	/**
	 * Method called to execute navigation
	 * 
	 * @param context
	 *            the current context
	 * @param source
	 *            the source
	 * @param type
	 *            the optional type filter or <code>null</code>
	 * @return the results of navigation
	 * @throws TMQLRuntimeException
	 *             thrown if execution fails
	 */
	public Collection<?> navigate(final IContext context, final Object source, final Topic type) throws TMQLRuntimeException;

	/**
	 * Give the token represent this axis
	 * 
	 * @return the token
	 */
	public Class<? extends IToken> getIdentifier();
}
