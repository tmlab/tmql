package de.topicmapslab.tmql4j.delete.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.delete.exception.DeletionException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationRegistry;
import de.topicmapslab.tmql4j.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Utility class to handle the deletion of topic map items. Class handles all
 * operation which will be proceeded during the interpretation of a
 * delete-expression.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeletionHandler {

	/**
	 * the topic map containing the items to delete
	 */
	private final TopicMap topicMap;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the context
	 * @throws DeletionException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public DeletionHandler(ITMQLRuntime runtime, IContext context) throws DeletionException {
		try {
			this.topicMap = context.getQuery().getTopicMap();
		} catch (TMQLRuntimeException e) {
			throw new DeletionException(e);
		}
	}

	/**
	 * Method removes all constructs for the topic map.
	 * 
	 * @return a set of all removed IDs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	public Set<String> deleteAll() throws DeletionException {
		TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		/*
		 * internal cache
		 */
		Set<Topic> types = HashUtil.getHashSet();
		/*
		 * store over all amount
		 */
		Set<String> ids = HashUtil.getHashSet();
		/*
		 * delete all topic types
		 */
		types.addAll(index.getTopicTypes());
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		/*
		 * delete all name-types
		 */
		types.addAll(index.getNameTypes());
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		/*
		 * delete all occurrence-types
		 */
		types.addAll(index.getOccurrenceTypes());
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		/*
		 * delete all association-types
		 */
		types.addAll(index.getAssociationTypes());
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		/*
		 * delete all role-types
		 */
		types.addAll(index.getRoleTypes());
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		/*
		 * delete all type-less topics
		 */
		types.addAll(index.getTopics(null));
		for (Topic topic : types) {
			ids.addAll(deleteTopic(topicMap, topic, true));
		}
		types.clear();

		return ids;
	}

	/**
	 * Method removes the given content from the internal topic map.
	 * 
	 * @param matches
	 *            a set of items which shall be removed
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	public Set<String> delete(List<Object> matches, boolean cascade) throws DeletionException {
		return delete(topicMap, matches, cascade);
	}

	/**
	 * Method remove the given item from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param match
	 *            the item to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> delete(TopicMap topicMap, Object match, boolean cascade) throws DeletionException {
		if (match instanceof Topic) {
			return deleteTopic(topicMap, (Topic) match, cascade);
		} else if (match instanceof Association) {
			return deleteAssociation(topicMap, (Association) match, cascade);
		} else if (match instanceof Occurrence) {
			return deleteOccurrence(topicMap, (Occurrence) match, cascade);
		} else if (match instanceof Name) {
			return deleteName(topicMap, (Name) match, cascade);
		} else if (match instanceof Variant) {
			return deleteVariant(topicMap, (Variant) match, cascade);
		} else if (match instanceof Locator) {
			deleteLocator(topicMap, (Locator) match, cascade);
		} else if (match instanceof Collection<?>) {
			Set<String> ids = HashUtil.getHashSet();
			for (Object obj : (Collection<?>) match) {
				ids.addAll(delete(topicMap, obj, cascade));
			}
			return ids;
		}
		return Collections.emptySet();
	}

	/**
	 * Internal method to remove the given topic from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param topic
	 *            the topic to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteTopic(final TopicMap topicMap, final Topic topic, final boolean cascade) throws DeletionException {
		try {
			Set<String> ids = HashUtil.getHashSet();
			if (cascade) {
				/*
				 * delete all names of the topic
				 */
				Set<Name> names = HashUtil.getHashSet();
				names.addAll(topic.getNames());
				for (Name name : names) {
					ids.addAll(deleteName(topicMap, name, cascade));
				}
				/*
				 * delete all occurrences of the topic
				 */
				Set<Occurrence> occurrences = HashUtil.getHashSet();
				occurrences.addAll(topic.getOccurrences());
				for (Occurrence occurrence : occurrences) {
					ids.addAll(deleteOccurrence(topicMap, occurrence, cascade));
				}

				NavigationRegistry handler = NavigationRegistry.buildHandler();

				INavigationAxis axis = handler.lookup(AxisPlayers.class);
				axis.setTopicMap(topicMap);
				/*
				 * delete all associations played by the topic
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					ids.addAll(deleteAssociation(topicMap, (Association) obj, cascade));
				}
				axis = handler.lookup(AxisTypes.class);
				axis.setTopicMap(topicMap);
				/*
				 * delete all instances of the topic as type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					ids.addAll(deleteTopic(topicMap, (Topic) obj, cascade));
				}
				axis = handler.lookup(AxisSupertypes.class);
				axis.setTopicMap(topicMap);
				/*
				 * delete all sub-types of the topic as type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					ids.addAll(deleteTopic(topicMap, (Topic) obj, cascade));
				}

				axis = handler.lookup(AxisRoles.class);
				axis.setTopicMap(topicMap);
				/*
				 * delete all associations which used the topic as role-type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					ids.addAll(deleteAssociation(topicMap, (Association) obj, cascade));
				}

				/*
				 * delete reification
				 */
				Reifiable reifiable = topic.getReified();
				if (reifiable != null) {
					ids.addAll(delete(topicMap, reifiable, cascade));
				}

				/*
				 * remove from scope where topic is used as theme
				 */
				axis = handler.lookup(AxisScope.class);
				axis.setTopicMap(topicMap);
				for (Object obj : axis.navigateBackward(topic)) {
					ids.addAll(deleteScoped(topicMap, (Scoped) obj, cascade));
				}

				/*
				 * check typed of the given topic type
				 */
				try {
					TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
					if (!index.isOpen()) {
						index.open();
					}
					for (Name n : index.getNames(topic)) {
						ids.addAll(deleteName(topicMap, n, cascade));
					}
					for (Occurrence o : index.getOccurrences(topic)) {
						ids.addAll(deleteOccurrence(topicMap, o, cascade));
					}
					Set<Association> set = HashUtil.getHashSet();
					for (Role r : index.getRoles(topic)) {
						if (set.contains(r.getParent())) {
							continue;
						}
						ids.addAll(deleteAssociation(topicMap, r.getParent(), cascade));
						set.add(r.getParent());
					}
					for (Association a : index.getAssociations(topic)) {
						if (set.contains(a)) {
							continue;
						}
						ids.addAll(deleteAssociation(topicMap, a, cascade));
					}
				} catch (UnsupportedOperationException e) {
					// NOTHING TO DO
					Logger.getLogger(getClass().getName()).warning("TypeInstanceIndex not supported by the current topic map system. Deletion of cascaded typed objects not possible!");
				}

			}
			ids.add(topic.getId());
			topic.remove();
			return ids;
		} catch (Exception e) {
			throw new DeletionException(e);
		}
	}

	/**
	 * Internal method to remove the given association from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param association
	 *            the association to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteAssociation(final TopicMap topicMap, final Association association, final boolean cascade) throws DeletionException {
		/*
		 * delete the association itself
		 */
		Set<String> ids = HashUtil.getHashSet();
		for (Role role : association.getRoles()) {
			/*
			 * check reification
			 */
			Topic reifier = role.getReifier();
			if (reifier != null) {
				ids.add(reifier.getId());
			}
			ids.add(role.getId());
		}
		ids.add(association.getId());
		/*
		 * check reification
		 */
		Topic reifier = association.getReifier();
		if (reifier != null) {
			ids.add(reifier.getId());
		}
		association.remove();
		return ids;
	}

	/**
	 * Internal method to remove the given name from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param name
	 *            the name to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteName(final TopicMap topicMap, final Name name, final boolean cascade) throws DeletionException {
		Set<String> ids = HashUtil.getHashSet();
		if (cascade) {
			/*
			 * delete all variants of the name
			 */
			Set<Variant> variants = HashUtil.getHashSet();
			variants.addAll(name.getVariants());
			for (Variant variant : variants) {
				ids.addAll(deleteVariant(topicMap, variant, cascade));
			}
		}
		/*
		 * delete the name itself
		 */
		ids.add(name.getId());
		/*
		 * check reification
		 */
		Topic reifier = name.getReifier();
		if (reifier != null) {
			ids.add(reifier.getId());
		}
		name.remove();
		return ids;
	}

	/**
	 * Internal method to remove the given occurrence from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param occurrence
	 *            the occurrence to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteOccurrence(final TopicMap topicMap, final Occurrence occurrence, final boolean cascade) throws DeletionException {
		Set<String> ids = HashUtil.getHashSet();
		/*
		 * check reification
		 */
		Topic reifier = occurrence.getReifier();
		if (reifier != null) {
			ids.add(reifier.getId());
		}
		/*
		 * delete the occurrence itself
		 */
		ids.add(occurrence.getId());
		occurrence.remove();
		return ids;
	}

	/**
	 * Internal method to remove the given variant from the given topic map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param variant
	 *            the variant to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteVariant(final TopicMap topicMap, final Variant variant, final boolean cascade) throws DeletionException {
		Set<String> ids = HashUtil.getHashSet();
		/*
		 * check reification
		 */
		Topic reifier = variant.getReifier();
		if (reifier != null) {
			ids.add(reifier.getId());
		}
		/*
		 * delete the variant itself
		 */
		ids.add(variant.getId());
		variant.remove();
		return ids;
	}

	/**
	 * Internal method to remove the the item identified by the given locator.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param locator
	 *            the locator of the item to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteLocator(final TopicMap topicMap, final Locator locator, final boolean cascade) throws DeletionException {

		long count = 0;
		/*
		 * look up the construct using the locator as item-identifier
		 */
		Construct construct = topicMap.getConstructByItemIdentifier(locator);
		if (construct != null) {
			construct.removeItemIdentifier(locator);
			count++;
		}
		/*
		 * look up the construct using the locator as subject-identifier
		 */
		Topic topic = topicMap.getTopicBySubjectIdentifier(locator);
		if (topic != null) {
			topic.removeSubjectIdentifier(locator);
			count++;
		}

		/*
		 * look up the construct using the locator as subject-locator
		 */
		topic = topicMap.getTopicBySubjectLocator(locator);
		if (topic != null) {
			topic.removeSubjectLocator(locator);
			count++;
		}

		return count;
	}

	/**
	 * Internal method to remove the given scoped-element from the given topic
	 * map.
	 * 
	 * @param topicMap
	 *            the topic map which contains the item to delete
	 * @param scoped
	 *            the scoped-element to remove
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a set containing all IDs of removed constructs
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private Set<String> deleteScoped(final TopicMap topicMap, final Scoped scoped, final boolean cascade) throws DeletionException {
		return delete(topicMap, scoped, cascade);
	}

}
