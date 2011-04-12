/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Construct;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisId;

/**
 * Class definition representing the id axis.
 * <p>
 * If the value is a construct in forward direction this step returns the
 * internal id of the construct. The optional item has no relevance.
 * </p>
 * <p>
 * If the value is a string, in backward direction this step returns the
 * construct with the id.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IdNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public IdNavigationAxis() {
		super(AxisId.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Construct.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Object.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		Collection<Object> set = new LinkedList<Object>();
		Construct c = getTopicMap().getConstructById(construct.toString());
		if (c != null) {
			set.add(c);
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {

		/*
		 * check if anchor is a construct
		 */
		if (construct instanceof Construct) {
			Collection<Object> col = new LinkedList<Object>();
			col.add(((Construct) construct).getId());
			return col;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Object && optional instanceof TopicMap) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Construct) {
			return true;
		}
		return false;
	}

}
