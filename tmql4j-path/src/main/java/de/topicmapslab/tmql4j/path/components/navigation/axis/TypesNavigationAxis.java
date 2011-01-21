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
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.model.ITypeHierarchyNavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTypes;

/**
 * Class definition representing the types axis.
 * <p>
 * In forward direction this step computes all types of the value.
 * </p>
 * <p>
 * In backward direction this step produces all instances of the value. The
 * optional item has no relevance.
 * </p>
 * <p>
 * <b>Additional Notation:</b> Asking for all instances of an item is the
 * inverse of asking for all types.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TypesNavigationAxis extends BaseNavigationAxisImpl implements ITypeHierarchyNavigationAxis {

	private boolean transitivity;

	/**
	 * base constructor to create an new instance
	 */
	public TypesNavigationAxis() {
		super(AxisTypes.class);
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
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		Topic topic = null;
		/*
		 * check if construct is a topic
		 */

		if (construct instanceof Topic) {
			topic = (Topic) construct;
		}
		/*
		 * check if construct is a role
		 */
		else if (construct instanceof Role) {
			topic = ((Role) construct).getType();
		}
		/*
		 * check if it is special string tm:subject
		 */
		else if ("tmdm:subject".equalsIgnoreCase(construct.toString()) || "tm:subject".equalsIgnoreCase(construct.toString())) {
			set.addAll(getTopicMap().getTopics());
			return set;
		} else {
			throw new InvalidValueException();
		}

		boolean tmdmSubject = false;

		/*
		 * check if topic is tmdm:subject
		 */
		for (Locator locator : topic.getSubjectIdentifiers()) {
			if (locator.getReference().equalsIgnoreCase("http://psi.topicmaps.org/iso13250/glossary/topic-type")) {
				tmdmSubject = true;
				break;
			}
		}

		/*
		 * get all topics
		 */
		if (tmdmSubject) {
			set.addAll(getTopicMap().getTopics());
		}
		/*
		 * get instances by index
		 */
		else {

			TypeInstanceIndex index = getTopicMap().getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			set.addAll(index.getTopics(topic));

			if (transitivity) {
				SupertypesNavigationAxis axis = new SupertypesNavigationAxis();
				axis.setTopicMap(getTopicMap());
				axis.setTransitivity(transitivity);
				Collection<Topic> subtypes = (Collection<Topic>) axis.navigateBackward(topic);
				for (Topic subtype : subtypes) {
					set.addAll(index.getTopics(subtype));
				}
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		Topic topic = null;
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			topic = (Topic) construct;
		}
		/*
		 * check if construct is a role
		 */
		else if (construct instanceof Role) {
			set.add(((Role) construct).getType());
			return set;
		}
		/*
		 * check if construct is a name, occurrence or association
		 */
		else if (construct instanceof Typed) {
			set.add(((Typed) construct).getType());
			return set;
		} else {
			throw new InvalidValueException();
		}

		set.addAll(topic.getTypes());

		if (transitivity) {
			SupertypesNavigationAxis axis = new SupertypesNavigationAxis();
			axis.setTopicMap(getTopicMap());
			axis.setTransitivity(transitivity);
			for (Topic type : topic.getTypes()) {
				set.addAll((Collection<Topic>) axis.navigateForward(type));
			}
		}

		return set;

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Topic) {
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

	/**
	 * {@inheritDoc}
	 */
	public void setTransitivity(boolean transitivity) {
		this.transitivity = transitivity;
	}

}
