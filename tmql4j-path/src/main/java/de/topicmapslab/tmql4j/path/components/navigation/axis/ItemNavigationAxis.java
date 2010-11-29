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
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Class definition representing the items axis.
 * <p>
 * If the value is a topic item, then in forward direction this steps finds one
 * item identifier and produces a string. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ItemNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public ItemNavigationAxis() {
		super(NavigationAxis.item);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(
			Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct)
			throws NavigationException {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Construct optional)
			throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		/*
		 * check if construct is a string
		 */
		if (construct instanceof String) {
			TopicMap map = (TopicMap) getTopicMap();
			Locator locator = map.createLocator((String) construct);
			Construct c = map.getConstructByItemIdentifier(locator);
			if (c != null) {
				set.add(c);
			}
			return set;
		}
		/*
		 * check if construct is a topic
		 */
		else if (construct instanceof Topic) {
			set.add((Topic) construct);
			return set;
		}
		/*
		 * check if construct is a locator
		 */
		else if (construct instanceof Locator) {
			TopicMap map = (TopicMap) getTopicMap();
			Locator locator = (Locator) construct;
			Construct c = map.getConstructByItemIdentifier(locator);
			if (c != null) {
				set.add(c);
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Construct) {
			Construct topic = (Construct) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * add all item-identifiers
			 */
			set.addAll(topic.getItemIdentifiers());
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof String && optional instanceof TopicMap) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Topic) {
			return true;
		}
		return false;
	}

}
