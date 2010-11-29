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

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.model.ITypeHierarchyNavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Class definition representing the supertypes axis.
 * <p>
 * In forward direction this step computes all supertypes of the value.
 * </p>
 * <p>
 * In backward direction this step produces all subtypes of the value. The
 * optional item has no relevance.
 * </p>
 * <p>
 * <b>Additional Notation:</b> Asking for all subtypes of an item is the inverse
 * of asking for all supertypes
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SupertypesNavigationAxis extends BaseNavigationAxisImpl implements
		ITypeHierarchyNavigationAxis {

	private boolean transitivity;

	/**
	 * base constructor to create an new instance
	 */
	public SupertypesNavigationAxis() {
		super(NavigationAxis.supertypes);
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
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Construct optional)
			throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			/*
			 * extract all sub-types
			 */
			return lookup(((Topic) construct).getTopicMap(), (Topic) construct,
					false);
		}
		/*
		 * check if topic is a role
		 */
		else if (construct instanceof Role) {
			/*
			 * extract all sub-types of the role-type
			 */
			return lookup(((Role) construct).getTopicMap(), ((Role) construct)
					.getType(), false);
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			/*
			 * extract all super-types
			 */
			return lookup(((Topic) construct).getTopicMap(), (Topic) construct,
					true);
		}
		/*
		 * check if topic is a role
		 */
		else if (construct instanceof Role) {
			/*
			 * extract all super-types of the role-type
			 */
			return lookup(((Role) construct).getTopicMap(), ((Role) construct)
					.getType(), true);
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof Topic) {
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

	/**
	 * Internal method to extract all super- or sub-types of the given topic
	 * type by using the {@link SupertypeSubtypeIndex} of the TMAPI-extension.
	 * 
	 * @param map
	 *            the topic map
	 * @param topic
	 *            the topic type
	 * @param extractSupertypes
	 *            flag which has to be <code>true</code> if super-types shall be
	 *            extracted, <code>false</code> if sub-types are expected.
	 * @return a tuple sequence containing the topic types
	 */
	private Collection<?> lookup(TopicMap map, Topic topic,
			boolean extractSupertypes) {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();

		/**
		 * look at TMDM association items for super-type-sub-type
		 */
		/*
		 * get association type
		 */
		Topic type = map
				.getTopicBySubjectIdentifier(map
						.createLocator("http://psi.topicmaps.org/iso13250/model/supertype-subtype"));
		/*
		 * get super-type role-type
		 */
		Topic supertype = map
				.getTopicBySubjectIdentifier(map
						.createLocator("http://psi.topicmaps.org/iso13250/model/supertype"));

		/*
		 * get sub-type role-type
		 */
		Topic subtype = map
				.getTopicBySubjectIdentifier(map
						.createLocator("http://psi.topicmaps.org/iso13250/model/subtype"));
		/*
		 * check if topics defined
		 */
		if (type != null && supertype != null && subtype != null) {

			/*
			 * get association items
			 */
			TypeInstanceIndex index = (TypeInstanceIndex) map
					.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			Collection<Association> associations = index.getAssociations(type);

			Collection<Topic> subset = new HashSet<Topic>();
			subset.add(topic);
			boolean newAdded = true;
			while (newAdded) {
				newAdded = false;
				for (Topic t : subset) {
					Collection<Topic> cache = new HashSet<Topic>();
					for (Association association : associations) {
						Collection<Role> roles = null;
						if (extractSupertypes) {
							roles = association.getRoles(subtype);
						} else {
							roles = association.getRoles(supertype);
						}
						for (Role r : roles) {
							if (r.getPlayer().equals(t)) {
								if (extractSupertypes) {
									for (Role _r : association
											.getRoles(supertype)) {
										Topic t_ = _r.getPlayer();
										if (!set.contains(t_)) {
											cache.add(_r.getPlayer());
											newAdded = true;
										}
									}
								} else {
									for (Role _r : association
											.getRoles(subtype)) {
										Topic t_ = _r.getPlayer();
										if (!set.contains(t_)) {
											cache.add(_r.getPlayer());
											newAdded = true;
										}
									}
								}
							}
						}
					}
					set.addAll(cache);
					subset.clear();
					subset.addAll(cache);
					cache.clear();

					if (!transitivity) {
						newAdded = false;
						break;
					}
				}
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTransitivity(boolean transitivity) {
		this.transitivity = transitivity;
	}
}
