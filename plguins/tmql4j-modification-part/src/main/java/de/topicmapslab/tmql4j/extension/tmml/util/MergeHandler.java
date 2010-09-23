/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;

public class MergeHandler {

	public static final long doMerge(Collection<Construct> candidates,
			Collection<Construct> alreadyMerged) throws TMQLRuntimeException {
		Set<Topic> topics = HashUtil.getHashSet();
		Set<Association> associations = HashUtil.getHashSet();
		for (Construct c : candidates) {
			if (c instanceof Topic) {
				topics.add((Topic) c);
			} else if (c instanceof Association) {
				associations.add((Association) c);
			} else {
				throw new TMQLRuntimeException("Unsupported construct type '"
						+ c.getClass().getName() + "'.");
			}
		}

		if (!topics.isEmpty() && !associations.isEmpty()) {
			throw new TMQLRuntimeException(
					"Cannot merge topics and associations at the same time.");
		} else if (!associations.isEmpty()) {
			return doMergeAssociations(associations, alreadyMerged);
		} else {
			return doMergeTopics(topics, alreadyMerged);
		}
	}

	public static final long doMergeTopics(Collection<Topic> candidates,
			Collection<Construct> alreadyMerged) throws TMQLRuntimeException {
		long count = 0;
		Iterator<Topic> iterator = candidates.iterator();
		if (!iterator.hasNext()) {
			return 0;
		}
		Topic topic = iterator.next();
		while (alreadyMerged.contains(topic) && iterator.hasNext()) {			
			topic = iterator.next();
		}
		while (iterator.hasNext()) {
			Topic other = iterator.next();
			while (iterator.hasNext() && ( alreadyMerged.contains(other)
					|| topic.equals(other) )) {
				other = iterator.next();
			}
			if (!alreadyMerged.contains(other) && !topic.equals(other)) {
				topic.mergeIn(other);
				alreadyMerged.add(other);
				count++;
			}
		}
		return count;
	}

	public static final long doMergeAssociations(
			Collection<Association> candidates,
			Collection<Construct> alreadyMerged) throws TMQLRuntimeException {
		long count = 0;
		Iterator<Association> iterator = candidates.iterator();
		if (!iterator.hasNext()) {
			return 0;
		}
		Association association = iterator.next();
		while (iterator.hasNext() && alreadyMerged.contains(association)) {
			association = iterator.next();
		}

		while (iterator.hasNext()) {
			Association other = iterator.next();
			while (iterator.hasNext() && alreadyMerged.contains(other)) {
				other = iterator.next();
			}
			count += doMerge(association, other);
			alreadyMerged.add(other);
			other.remove();
		}
		return count;
	}

	public static final long doMerge(Association association, Association other)
			throws TMQLRuntimeException {
		long count = 0;
		if (!association.getType().equals(other.getType())) {
			throw new TMQLRuntimeException(
					"Associations have to be of the same type!");
		} else if (association.getReifier() != null
				&& other.getReifier() != null) {
			throw new TMQLRuntimeException("Both associations are reified!");
		} else if (!association.getScope().equals(other.getScope())) {
			throw new TMQLRuntimeException(
					"Associations have to have the same scope!");
		}

		roles: for (Role role : other.getRoles()) {
			for (Role r : association.getRoles(role.getType())) {
				if (role.getPlayer().equals(r.getPlayer())) {
					continue roles;
				}
			}
			/*
			 * copy role
			 */
			Role r = association.createRole(role.getType(), role.getPlayer());
			Topic reifier = role.getReifier();
			role.setReifier(null);
			r.setReifier(reifier);
			count++;
		}

		/*
		 * move reifier
		 */
		if (association.getReifier() == null && other.getReifier() != null) {
			Topic reifier = other.getReifier();
			other.setReifier(null);
			association.setReifier(reifier);
		}

		return count;
	}

}
