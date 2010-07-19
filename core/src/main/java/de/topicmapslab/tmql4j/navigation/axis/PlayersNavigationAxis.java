/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.navigation.exception.InvalidValueException;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;

/**
 * Class definition representing the players axis.
 * <p>
 * If the value is an association item, in forward direction this step computes
 * all role-playing items of that item. The optional item specifies the type of
 * the roles to be considered. If a playing topic plays several roles in such an
 * association item, then it appears as many times in the result (multiset
 * interpretation).
 * </p>
 * <p>
 * If the value is a topic item, in backward direction this step computes all
 * association items in which that topic plays a role. If a role playing topic
 * plays several roles in one and the same association, this association will
 * appear as many times. The optional item specifies the type of the roles to be
 * considered.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PlayersNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public PlayersNavigationAxis() {
		super(NavigationAxis.players);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getBackwardNavigationResultClass(Object construct)
			throws NavigationException {
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
	public Collection<?> navigateBackward(Object construct, Construct optional)
			throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			TopicMap map = topic.getTopicMap();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * iterate over all associations
			 */
			Set<Association> associations = map.getAssociations();
			for (Association a : associations) {
				Set<Role> roles = null;
				/*
				 * extract roles by type if optional parameter is used
				 */
				if (optional != null && optional instanceof Topic) {
					roles = a.getRoles((Topic) optional);
				}
				/*
				 * extract all roles
				 */
				else {
					roles = a.getRoles();
				}
				/*
				 * iterate over roles
				 */
				for (Role role : roles) {
					/*
					 * check if player of role is equal to the given topic
					 */
					if (role.getPlayer().equals(topic)) {
						set.add(a);
					}
				}
			}
			return set;
		}
		throw new InvalidValueException("unsupported navigation value");
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Association) {
			Association association = (Association) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			Set<Role> roles = null;
			if (optional != null && optional instanceof Topic) {
				roles = association.getRoles((Topic) optional);
			} else {
				roles = association.getRoles();
			}
			for (Role role : roles) {
				set.add(role.getPlayer());
			}
			return set;
		} else if (construct instanceof Topic) {
			Topic type = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			for (Association association : type.getTopicMap().getAssociations()) {
				if (association.getType().equals(type)) {
					Set<Role> roles = null;
					if (optional != null && optional instanceof Topic) {
						roles = association.getRoles((Topic) optional);
					} else {
						roles = association.getRoles();
					}
					for (Role role : roles) {
						set.add(role.getPlayer());
					}
				}
			}
			return set;
		} else if (construct instanceof Role) {
			Topic type = ((Role) construct).getType();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			for (Association association : type.getTopicMap().getAssociations()) {
				if (association.getType().equals(type)) {
					Set<Role> roles = null;
					if (optional != null && optional instanceof Topic) {
						roles = association.getRoles((Topic) optional);
					} else {
						roles = association.getRoles();
					}
					for (Role role : roles) {
						set.add(role.getPlayer());
					}
				}
			}
			return set;
		}
		throw new InvalidValueException("unsupported navigation value");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		boolean result = false;
		if (construct instanceof Topic) {
			result = true;
		}
		if (optional != null && optional instanceof Topic) {
			result &= true;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional)
			throws NavigationException {
		boolean result = false;
		if (construct instanceof Association) {
			result = true;
		}
		if (optional != null && optional instanceof Topic) {
			result &= true;
		}
		return result;
	}

}
