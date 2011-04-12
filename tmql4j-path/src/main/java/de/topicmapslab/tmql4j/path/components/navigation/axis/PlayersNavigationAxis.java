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
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition representing the players axis.
 * <p>
 * If the value is an association item, in forward direction this step computes
 * all topic players. If the value is a role, the player will be returned. The
 * optional item specifies the type of the player to be considered.
 * </p>
 * <p>
 * If the value is a topic item, in backward direction this step computes all
 * role items in which that topic plays. If a role playing topic plays several
 * roles in one and the same association, this association will appear as many
 * times. The optional item specifies the type of the roles to be considered.
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
		super(AxisPlayers.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Role.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;

			Collection<Object> set = new LinkedList<Object>();
			if (optional != null && optional instanceof Topic) {
				set.addAll(topic.getRolesPlayed((Topic) optional));
			} else {
				set.addAll(topic.getRolesPlayed());
			}
			return set;
		}
		throw new InvalidValueException("unsupported navigation value");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		Set<Role> roles = null;
		if (construct instanceof Association) {
			Association association = (Association) construct;
			roles = HashUtil.getHashSet(association.getRoles());
		} else if (construct instanceof Topic) {
			Topic type = (Topic) construct;
			roles = HashUtil.getHashSet();
			for (Association association : type.getTopicMap().getAssociations()) {
				roles.addAll(association.getRoles(type));
			}
		} else if (construct instanceof Role) {
			roles = HashUtil.getHashSet();
			roles.add((Role) construct);
		}

		if (roles == null) {
			throw new InvalidValueException("unsupported navigation value");
		}
		Collection<Object> set = new LinkedList<Object>();
		for (Role r : roles) {
			if (optional == null || !(optional instanceof Topic)) {
				set.add(r.getPlayer());
			} else if (r.getPlayer().getTypes().contains(optional)) {
				set.add(r.getPlayer());
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
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
	@Override
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
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
