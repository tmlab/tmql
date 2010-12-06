/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.util;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Utility class to store a role-player-restriction
 */
public class Restriction {
	/**
	 * the player
	 */
	protected Object player;
	/**
	 * the role type
	 */
	protected Object roleType;
	/**
	 * the parent expression
	 */
	protected IExpression ex;

	/**
	 * Checks if the given association satisfies the association
	 * 
	 * @param association
	 *            the association
	 * @return <code>true</code> if the restriction satisfies the given
	 *         {@link Association}, <code>false</code> otherwise
	 */
	public boolean satisfy(Association association) {
		if (roleType instanceof Topic && !TmdmSubjectIdentifier.isTmdmSubject(roleType)) {

			Set<Role> roles = HashUtil.getHashSet();
			roles = association.getRoles((Topic) roleType);

			if (roles.isEmpty()) {
				return false;
			}
			if (player instanceof Topic) {
				if (!TmdmSubjectIdentifier.isTmdmSubject(player)) {
					boolean satisfy = false;
					for (Role r : roles) {
						if (r.getPlayer().equals(player)) {
							satisfy = true;
							break;
						}
					}
					if (!satisfy) {
						return false;
					}
				}
			}

		} else {
			Set<Role> roles = association.getRoles();
			if (roles.isEmpty()) {
				return false;
			}
			if (player instanceof Topic) {
				if (!TmdmSubjectIdentifier.isTmdmSubject(player)) {
					boolean satisfy = false;
					for (Role r : roles) {
						if (r.getPlayer().equals(player)) {
							satisfy = true;
							break;
						}
					}
					if (!satisfy) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns the player restriction
	 * 
	 * @return the player restriction
	 */
	public Object getPlayer() {
		return player;
	}

	/**
	 * Setting the player restriction
	 * 
	 * @param player
	 *            the player restriction
	 */
	public void setPlayer(Object player) {
		this.player = player;
	}

	/**
	 * Returns the role type restriction
	 * 
	 * @return the role type restriction
	 */
	public Object getRoleType() {
		return roleType;
	}

	/**
	 * Setting the role type restriction
	 * 
	 * @return the role type restriction
	 */
	public void setRoleType(Object roleType) {
		this.roleType = roleType;
	}

	/**
	 * Returns the parent expression
	 * 
	 * @return the expression
	 */
	public IExpression getExpression() {
		return ex;
	}

	/**
	 * Setting the parent expression
	 * 
	 * @param expression
	 *            the expression
	 */
	public void setExpression(IExpression expression) {
		this.ex = expression;
		;
	}

}