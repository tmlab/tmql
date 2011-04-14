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
import java.util.HashSet;
import java.util.LinkedList;

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition representing the subtypes axis.
 * <p>
 * In forward direction this step computes all supertypes of the value.
 * </p>
 * <p>
 * This step produces all subtypes of the value. The optional item has no relevance.
 * </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubtypesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public SubtypesAxis() {
		super(AxisSubtypes.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * check if construct is a topic
		 */
		if (source instanceof Topic) {
			/*
			 * extract all sub-types
			 */
			return lookup(context, (Topic) source, false);
		}
		throw new InvalidValueException();
	}

	/**
	 * Internal method to extract all super- or sub-types of the given topic type by using the
	 * {@link SupertypeSubtypeIndex} of the TMAPI-extension.
	 * 
	 * @param context
	 *            the context
	 * @param topic
	 *            the topic type
	 * @param extractSupertypes
	 *            flag which has to be <code>true</code> if super-types shall be extracted, <code>false</code> if
	 *            sub-types are expected.
	 * @return a tuple sequence containing the topic types
	 */
	public static Collection<?> lookup(IContext context, Topic topic, boolean extractSupertypes) {
		TopicMap map = context.getQuery().getTopicMap();
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
		Topic type = map.getTopicBySubjectIdentifier(map.createLocator("http://psi.topicmaps.org/iso13250/model/supertype-subtype"));
		/*
		 * get super-type role-type
		 */
		Topic supertype = map.getTopicBySubjectIdentifier(map.createLocator("http://psi.topicmaps.org/iso13250/model/supertype"));

		/*
		 * get sub-type role-type
		 */
		Topic subtype = map.getTopicBySubjectIdentifier(map.createLocator("http://psi.topicmaps.org/iso13250/model/subtype"));
		/*
		 * check if topics defined
		 */
		if (type != null && supertype != null && subtype != null) {

			/*
			 * get association items
			 */
			TypeInstanceIndex index = map.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			Collection<Association> associations = index.getAssociations(type);

			Collection<Topic> subset = new HashSet<Topic>();
			subset.add(topic);
			boolean newAdded = true;
			while (newAdded) {
				newAdded = false;
				for (Topic t : HashUtil.getHashSet(subset)) {
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
									for (Role _r : association.getRoles(supertype)) {
										Topic t_ = _r.getPlayer();
										if (!set.contains(t_)) {
											cache.add(_r.getPlayer());
											newAdded = true;
										}
									}
								} else {
									for (Role _r : association.getRoles(subtype)) {
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

					if (!context.isTransitive()) {
						newAdded = false;
						break;
					}
				}
			}
		}
		return set;
	}

}
