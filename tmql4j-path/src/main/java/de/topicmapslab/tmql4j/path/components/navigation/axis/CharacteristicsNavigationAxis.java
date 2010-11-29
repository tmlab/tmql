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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
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
	public Class<? extends Construct> getBackwardNavigationResultClass(
			Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getForwardNavigationResultClass(
			Object construct) throws NavigationException {
		return Construct.class;
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
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * get a set of all occurrences
			 */
			Set<Occurrence> occurences = topic.getOccurrences();
			/*
			 * get a set of all names
			 */
			Set<Name> names = topic.getNames();
			/*
			 * check if optional type is defined
			 */
			if (optional != null && optional instanceof Topic) {
				Topic type = ((Topic) optional);
				/*
				 * extract all super-types of the types
				 */
				Collection<Topic> types = new HashSet<Topic>();
				types.add(type);
				
				boolean isTmOccurrence = false;
				for ( Locator locator : type.getSubjectIdentifiers()){
					if ( locator.getReference().equalsIgnoreCase(TmdmSubjectIdentifier.TMDM_OCCURRENCE_TYPE)){
						isTmOccurrence = true;
						break;
					}
				}
				
				boolean isTmName = false;
				for ( Locator locator : type.getSubjectIdentifiers()){
					if ( locator.getReference().equalsIgnoreCase(TmdmSubjectIdentifier.TMDM_NAME_TYPE)){
						isTmName = true;
						break;
					}
				}
				
				/*
				 * iterate over all occurrences
				 */
				for (Occurrence occurrence : occurences) {
					if ( isTmOccurrence  || types.contains(occurrence.getType())) {
						set.add(occurrence);
					}
				}
				/*
				 * iterate over all names
				 */
				for (Name name : names) {
					if ( isTmName || types.contains(name.getType())) {
						set.add(name);
					}
				}
			} else {
				set.addAll(occurences);
				set.addAll(names);
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof Occurrence || construct instanceof Name) {
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
