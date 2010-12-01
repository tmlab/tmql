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
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Class definition representing the characteristics axis.
 * <p>
 * If the value is a topic item, in forward direction this step computes all
 * names and occurrences of that topic which are subtypes of the optionally
 * specified item. The result is a sequence of name and occurrence items.
 * </p>
 * <p>
 * If the value is a name or an occurrence item, in backward direction this step
 * computes the topic to which the name or the occurrence is attached. The
 * optional item can be used to constrain the type of the items one is
 * interested in.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CharacteristicsNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public CharacteristicsNavigationAxis() {
		super(NavigationAxis.characteristics);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Construct.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();

		/*
		 * check if construct is an occurrence
		 */
		if (construct instanceof Occurrence) {
			Occurrence occurrence = (Occurrence) construct;
			if (optional != null && optional instanceof Topic) {
				if (occurrence.getParent().getTypes().contains(((Topic) optional))) {
					set.add(occurrence.getParent());
				}
			} else {
				set.add(occurrence.getParent());
			}
			return set;
		}
		/*
		 * check if construct is a name
		 */
		else if (construct instanceof Name) {
			Name name = (Name) construct;
			if (optional != null && optional instanceof Topic) {
				if (name.getParent().getTypes().contains(((Topic) optional))) {
					set.add(name.getParent());
				}
			} else {
				set.add(name.getParent());
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * check if optional type is defined
			 */
			if (optional != null) {
				/*
				 * optional item is a topic
				 */
				if (optional instanceof Topic) {
					Topic type = ((Topic) optional);
					set.addAll(topic.getNames(type));
					set.addAll(topic.getOccurrences(type));
				}
				/*
				 * optional item is a TMDM string
				 */
				else if (optional instanceof String) {
					/*
					 * optional is tm:occurrence for all occurrences
					 */
					if (TmdmSubjectIdentifier.isTmdmOccurrence(optional)) {
						set.addAll(topic.getOccurrences());
					}
					/*
					 * optional is tm:name for all names
					 */
					if (TmdmSubjectIdentifier.isTmdmName(optional)) {
						set.addAll(topic.getNames());
					}
				}
			}
			/*
			 * non optional item
			 */
			else {
				set.addAll(topic.getOccurrences());
				set.addAll(topic.getNames());
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Occurrence || construct instanceof Name) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Topic) {
			return true;
		}
		return false;
	}

}
