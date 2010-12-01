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
 * Class definition representing the indicators axis.
 * <p>
 * If the value is a topic item, in forward direction this step retrieves all
 * subject indicators of this item. If the value is an IRI, in backward
 * direction this step produces the topic which has this IRI as subject
 * indicator. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IndicatorsNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public IndicatorsNavigationAxis() {
		super(NavigationAxis.indicators);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getBackwardNavigationResultClass(Object construct)
			throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct)
			throws NavigationException {
		return Locator.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct,
			Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		/*
		 * check if construct is a locator
		 */
		if (construct instanceof Locator) {
			TopicMap map = getTopicMap();
			Locator locator = (Locator) construct;
			/*
			 * get topic by subject-identifier
			 */
			Construct c = map.getTopicBySubjectIdentifier(locator);
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
		 * check if construct is a string
		 */
		else if (construct instanceof String) {
			TopicMap map = getTopicMap();
			Locator locator = map.createLocator((String) construct);
			Construct c = map.getTopicBySubjectIdentifier(locator);
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
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * add all subject-identifiers
			 */
			set.addAll(topic.getSubjectIdentifiers());
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof Locator && optional instanceof TopicMap) {
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
