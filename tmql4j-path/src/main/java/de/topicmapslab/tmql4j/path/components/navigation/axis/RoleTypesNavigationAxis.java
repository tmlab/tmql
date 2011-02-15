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
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisRoleTypes;

/**
 * Class definition representing the roles axis.
 * <p>
 * If the value is an association item, in forward direction this step computes
 * all role-typing topics. Multiple uses of the same role type in one
 * association causes multiple results. The optional item has no relevance.
 * </p>
 * <p>
 * If the value is a topic item, in backward direction this step computes all
 * association items where that topic is the role type. Multiple uses of one
 * topic as role in one association causes multiple results. The optional item
 * identifier has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class RoleTypesNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public RoleTypesNavigationAxis() {
		super(AxisRoleTypes.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Association.class;
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
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			TopicMap map = topic.getTopicMap();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			Set<Association> associations = new HashSet<Association>();
			/*
			 * check optional type
			 */
			if (optional != null && optional instanceof Topic) {
				TypeInstanceIndex index = map.getIndex(TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				associations.addAll(index.getAssociations((Topic) optional));
			} else {
				associations.addAll(map.getAssociations());
			}

			/*
			 * iterate over all associations
			 */
			for (Association association : associations) {
				Set<Topic> types = association.getRoleTypes();
				if (types.contains(topic)) {
					set.add(association);
				}
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
			TopicMap map = topic.getTopicMap();
			Set<Association> associations =  new HashSet<Association>();
			/*
			 * check optional type
			 */
			if (optional != null && optional instanceof Topic) {
				TypeInstanceIndex index = map.getIndex(TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				associations.addAll(index.getAssociations((Topic) optional));
			} else {
				associations.addAll(map.getAssociations());
			}
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * iterate over all associations
			 */
			for (Association association : associations) {
				set.addAll(association.getRoleTypes());				
			}
			return set;
		}
		/*
		 * check if construct is an association
		 */
		else if (construct instanceof Association) {
			Association association = (Association) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			set.addAll(association.getRoleTypes());
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Association) {
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
