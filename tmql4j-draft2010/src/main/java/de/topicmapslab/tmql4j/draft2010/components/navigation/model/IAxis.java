package de.topicmapslab.tmql4j.draft2010.components.navigation.model;

import java.util.Collection;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interface definition of a TMQL axis ( Draft 2010 )
 * 
 * @author Sven Krosse
 * 
 */
public interface IAxis {

	/**
	 * Method called to execute navigation
	 * 
	 * @param source
	 *            the source
	 * @param type
	 *            the optional type filter or <code>null</code>
	 * @return the results of navigation
	 * @throws TMQLRuntimeException
	 *             thrown if execution fails
	 */
	public Collection<?> navigate(final Construct source, final Topic type) throws TMQLRuntimeException;

	/**
	 * Give the token represent this axis
	 * 
	 * @return the token
	 */
	public IToken getIdentifier();

}
