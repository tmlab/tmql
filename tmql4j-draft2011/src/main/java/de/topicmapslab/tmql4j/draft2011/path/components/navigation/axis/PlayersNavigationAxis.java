/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition representing the players axis.
 * <p>
 * If the value is an association item, in forward direction this step computes all topic players. If the value is a
 * role, the player will be returned. The optional item specifies the type of the player to be considered.
 * </p>
 * <p>
 * If the current item is a role this step returns the player of this role. If the item is an association all players of
 * all roles are returned. The optional item specifies the type of the roles to be considered.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PlayersNavigationAxis extends Axis {

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
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		Set<Role> roles = null;
		if (source instanceof Association) {
			Association association = (Association) source;
			roles = HashUtil.getHashSet(association.getRoles());
		} else if (source instanceof Role) {
			roles = HashUtil.getHashSet();
			roles.add((Role) source);
		}

		if (roles == null) {
			throw new InvalidValueException();
		}
		Collection<Object> set = new LinkedList<Object>();
		for (Role r : roles) {
			Topic player = r.getPlayer();
			if (type == null) {
				set.add(player);
			} else {
				if (matches(context, player, type)) {
					set.add(player);
				}
			}
		}
		return set;
	}

}
