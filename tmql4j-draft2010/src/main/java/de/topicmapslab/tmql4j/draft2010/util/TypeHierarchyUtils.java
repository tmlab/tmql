/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.util;

import java.util.List;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Utility class to handle type hierarchy of topic maps
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TypeHierarchyUtils {

	/**
	 * hidden constructor
	 */
	private TypeHierarchyUtils() {

	}

	/**
	 * Static method to extract all instances of the given type by looking for
	 * all instances and the instances of all subtypes
	 * 
	 * @param type
	 *            the type
	 * @param transitive
	 *            handle type hierarchy as transitive
	 * @return a {@link List} of all instances
	 * @throws ModelConstraintException
	 *             thrown if TMDM default association type or role types not
	 *             found
	 */
	public static List<Topic> getInstances(final Topic type, final boolean transitive) throws ModelConstraintException {
		List<Topic> topics = HashUtil.getList();

		TypeInstanceIndex index = type.getTopicMap().getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}

		topics.addAll(index.getTopics(type));
		if (transitive) {
			for (Topic subtype : getSubtypes(type, transitive)) {
				topics.addAll(index.getTopics(subtype));
			}
		}

		return topics;
	}

	/**
	 * Static method to extract all types of the given typed construct by looking for
	 * all types and their supertypes
	 * 
	 * @param typed
	 *            the typed
	 * @param transitive
	 *            handle type hierarchy as transitive
	 * @return a {@link List} of all instances
	 * @throws ModelConstraintException
	 *             thrown if TMDM default association type or role types not
	 *             found
	 */
	public static List<Topic> getTypes(final Typed typed, final boolean transitive) throws ModelConstraintException {
		List<Topic> topics =  HashUtil.getList();

		topics.add(typed.getType());
		if (transitive) {
			topics.addAll(getSupertypes(typed.getType(), transitive));
		}

		return topics;
	}

	/**
	 * Static method to extract all types of the given instance by looking for
	 * all types and their supertypes
	 * 
	 * @param instance
	 *            the instance
	 * @param transitive
	 *            handle type hierarchy as transitive
	 * @return a {@link List} of all instances
	 * @throws ModelConstraintException
	 *             thrown if TMDM default association type or role types not
	 *             found
	 */
	public static List<Topic> getTypes(final Topic instance, final boolean transitive) throws ModelConstraintException {
		List<Topic> topics =  HashUtil.getList();

		for (Topic type : instance.getTypes()) {
			topics.add(type);
			if (transitive) {
				topics.addAll(getSupertypes(type, transitive));
			}
		}

		return topics;
	}

	/**
	 * Static method to extract all sub-types of the given type by looking for
	 * special association items.
	 * 
	 * @param supertype
	 *            the type
	 * @param transitive
	 *            handle type hierarchy as transitive
	 * @return a {@link List} of all sub-types
	 * 
	 * @throws ModelConstraintException
	 *             thrown if TMDM default association type or role types not
	 *             found
	 */
	public static List<Topic> getSubtypes(final Topic supertype, boolean transitive) throws ModelConstraintException {
		List<Topic> subtypes = HashUtil.getList();
		TopicMap topicMap = supertype.getTopicMap();

		/*
		 * get supertype-subtype association type
		 */
		Topic kindOf = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));

		if (kindOf != null) {
			/*
			 * get subtype-role type
			 */
			Topic subtypeRole = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
			/*
			 * get supertype-role type
			 */
			Topic supertypeRole = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));

			/*
			 * check if exists
			 */
			if (subtypeRole == null || supertypeRole == null) {
				new ModelConstraintException(kindOf, "Invalid association item of type 'supertype-subtype' - unexprected role types.");
			}

			/*
			 * get type-instance-index
			 */
			TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * iterate over all association items
			 */
			for (Association association : index.getAssociations(kindOf)) {
				Set<Role> superTypePlayers = association.getRoles(supertypeRole);
				if (superTypePlayers.size() != 1) {
					throw new ModelConstraintException(association, "Invalid association item of type 'supertype-subtype' - expected number of players of role-type 'supertype' is 1, but was"
							+ superTypePlayers.size());
				}
				/*
				 * extract player of supertype-role
				 */
				Topic superTypePlayer = superTypePlayers.iterator().next().getPlayer();
				/*
				 * check if player equals given type
				 */
				if (superTypePlayer.equals(supertype)) {
					Set<Role> subTypePlayers = association.getRoles(subtypeRole);
					if (subTypePlayers.size() != 1) {
						throw new ModelConstraintException(association, "Invalid association item of type 'supertype-subtype' - expected number of players of role-type 'subtype' is 1, but was"
								+ subTypePlayers.size());
					}
					/*
					 * add subtype-role player
					 */
					subtypes.add(subTypePlayers.iterator().next().getPlayer());
				}
			}
		}

		if (transitive) {
			for (Topic subtype : subtypes) {
				List<Topic> iteration = getSubtypes(subtype, transitive);
				for (Topic t : iteration) {
					if (!subtypes.contains(t)) {
						subtypes.add(t);
					}
				}
			}
		}
		return subtypes;
	}

	/**
	 * Static method to extract all super-types of the given type by looking for
	 * special association items.
	 * 
	 * @param subtype
	 *            the type
	 * @param transitive
	 *            handle type hierarchy as transitive
	 * @return a {@link List} of all super-types
	 * @throws ModelConstraintException
	 *             thrown if TMDM default association type or role types not
	 *             found
	 */
	public static List<Topic> getSupertypes(final Topic subtype, final boolean transitive) throws ModelConstraintException {
		List<Topic> supertypes = HashUtil.getList();
		TopicMap topicMap = subtype.getTopicMap();

		/*
		 * get supertype-subtype association type
		 */
		Topic kindOf = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));

		if (kindOf != null) {
			/*
			 * get subtype-role type
			 */
			Topic subtypeRole = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
			/*
			 * get supertype-role type
			 */
			Topic supertypeRole = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));

			/*
			 * check if exists
			 */
			if (subtypeRole == null || supertypeRole == null) {
				new ModelConstraintException(kindOf, "Invalid association item of type 'supertype-subtype' - unexprected role types.");
			}

			/*
			 * get type-instance-index
			 */
			TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * iterate over all association items
			 */
			for (Association association : index.getAssociations(kindOf)) {
				Set<Role> subtypePlayers = association.getRoles(subtypeRole);
				if (subtypePlayers.size() != 1) {
					throw new ModelConstraintException(association, "Invalid association item of type 'supertype-subtype' - expected number of players of role-type 'subtype' is 1, but was"
							+ subtypePlayers.size());
				}
				/*
				 * extract player of subtype-role
				 */
				Topic subtypePlayer = subtypePlayers.iterator().next().getPlayer();
				/*
				 * check if player equals given type
				 */
				if (subtypePlayer.equals(subtype)) {
					Set<Role> supertypePlayers = association.getRoles(supertypeRole);
					if (supertypePlayers.size() != 1) {
						throw new ModelConstraintException(association, "Invalid association item of type 'supertype-subtype' - expected number of players of role-type 'supertype' is 1, but was"
								+ supertypePlayers.size());
					}
					/*
					 * add supertype-role player
					 */
					supertypes.add(supertypePlayers.iterator().next().getPlayer());
				}
			}
		}

		if (transitive) {
			for (Topic supertype : supertypes) {
				List<Topic> iteration = getSupertypes(supertype, transitive);
				for (Topic t : iteration) {
					if (!supertypes.contains(t)) {
						supertypes.add(t);
					}
				}
			}
		}
		return supertypes;
	}

}
