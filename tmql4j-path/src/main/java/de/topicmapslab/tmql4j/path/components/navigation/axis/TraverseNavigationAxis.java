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

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Class definition representing the traverse axis.
 * <p>
 * If the value is a topic item, in forward direction this step computes first
 * all associations where the topic plays a role. The type of the associations
 * is constrained by the optional item. The overall result of this navigation is
 * then a sequence of all players of these associations, whereby the incoming
 * topic is deducted once from that sequence. In backward direction the result
 * sequence will always be empty.
 * </p>
 * <p>
 * If the value is a topic item, in forward direction this step computes first
 * all associations where the topic plays a role. The type of the associations
 * is constrained by the optional item. The overall result of this navigation is
 * then a sequence of all players of these associations, whereby the incoming
 * topic is deducted once from that sequence. In backward direction the result
 * sequence will always be empty.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TraverseNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public TraverseNavigationAxis() {
		super(NavigationAxis.traverse);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(
			Object construct) throws NavigationException {
		return Association.class;
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
	public Collection<?> navigateBackward(Object construct, Object optional)
			throws NavigationException {

		Set<Association> associations = new HashSet<Association>();
		/*
		 * check if construct is an association
		 */
		if (construct instanceof Association) {
			associations.add((Association) construct);
		}
		/*
		 * check if construct is a topic type of associations
		 */
		else if (construct instanceof Topic) {
			try {
				TypeInstanceIndex index = getTopicMap().getIndex(
						TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				associations.addAll(index.getAssociations((Topic) construct));
			} catch (UnsupportedOperationException e) {
				/*
				 * index is not supported
				 */
				for (Association association : getTopicMap().getAssociations()) {
					if (association.getType().equals((Topic) construct)) {
						associations.add(association);
					}
				}
			}
		} else {
			throw new InvalidValueException();
		}

		/*
		 * check optional parameter
		 */
		Topic type = null;
		if (optional != null && optional instanceof Topic) {
			type = (Topic) optional;
		}

		/*
		 * extract all co-players of a specific type
		 */
		Set<Topic> players = new HashSet<Topic>();
		for (Association association : associations) {
			for (Role role : association.getRoles()) {
				if (type != null) {
					if (role.getPlayer().getTypes().contains(type)) {
						players.add(role.getPlayer());
					}
				} else {
					players.add(role.getPlayer());
				}

			}
		}

		Set<Association> temp_ = new HashSet<Association>();
		for (Association association : ((Construct) construct).getTopicMap()
				.getAssociations()) {
			if (!associations.contains(association)) {
				temp_.add(association);
			}
		}

		associations = temp_;
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();

		/*
		 * iterate over associations
		 */
		for (Association association : associations) {
			/*
			 * iterate over all roles
			 */
			for (Role role : association.getRoles()) {
				/*
				 * check if players contains role-player
				 */
				for (Topic player : players) {
					if (player.equals(role.getPlayer())) {
						set.add(association);
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
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			TopicMap map = topic.getTopicMap();
			Set<Association> associations = new HashSet<Association>();
			Set<Association> associations_ = new HashSet<Association>();
			/*
			 * check optional type
			 */
			if (optional != null) {
				/*
				 * optional type is an association
				 */
				if (optional instanceof Association) {
					associations_.add((Association) optional);
				}
				/*
				 * optional type is a topic type
				 */
				else if (optional instanceof Topic) {
					Topic type = (Topic) optional;
					TypeInstanceIndex index = type.getTopicMap().getIndex(
							TypeInstanceIndex.class);
					if (!index.isOpen()) {
						index.open();
					}
					associations_.addAll(index.getAssociations(type));
				}
			} else {
				associations_.addAll(map.getAssociations());
			}

			/*
			 * iterate over all associations and extract associations played by
			 * given topic
			 */
			for (Association a : associations_) {
				for (Role role : a.getRoles()) {
					if (role.getPlayer().equals(topic)) {
						associations.add(a);
						break;
					}
				}
			}
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			/*
			 * iterate over all associations and extract players
			 */
			for (Association association : associations) {
				for (Role role : association.getRoles()) {
					if (!role.getPlayer().equals(topic)) {
						set.add(role.getPlayer());
					}
				}
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
		if (construct instanceof Association && optional != null
				&& optional instanceof Topic) {
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
