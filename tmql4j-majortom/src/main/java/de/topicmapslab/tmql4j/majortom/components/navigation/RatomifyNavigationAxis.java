/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.components.navigation;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Construct;
import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Name;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.tmql4j.majortom.grammar.literals.AxisRatomify;
import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.axis.AtomifyNavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Class definition representing the atomify axis.
 * <p>
 * If the value is a name or occurrence item, in forward direction this step
 * schedules the item for atomification, i.e. marks the item to be converted to
 * the atomic value (integer, string, etc.) within the item. The item is
 * eventually converted into an atom according to the atomification rules. The
 * optional item has no relevance.
 * </p>
 * <p>
 * If the value is an atom, in backward direction this step de-atomifies
 * immediately the atom and returns all names and occurrences where this atom is
 * used as data value. Also here the optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class RatomifyNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public RatomifyNavigationAxis() {
		super(AxisRatomify.class);
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
		if (construct instanceof Object) {
			TopicMap map = getTopicMap();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * get literal index
			 */
			ILiteralIndex index = map.getIndex(ILiteralIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * check if occurrence with literal exists
			 */
			set.addAll(index.getCharacteristicsMatches(construct.toString()));
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		return new AtomifyNavigationAxis().navigateForward(construct, optional);
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
		if (construct instanceof Name || construct instanceof DatatypeAware) {
			return true;
		}
		return false;
	}

}
