package de.topicmapslab.tmql4j.extension.tmml.util;

import java.util.Collection;
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

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.event.model.EventManager;
import de.topicmapslab.tmql4j.extension.tmml.event.DeletionEvent;
import de.topicmapslab.tmql4j.extension.tmml.exception.DeletionException;
import de.topicmapslab.tmql4j.navigation.model.INavigationAxis;

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
	 * a reference of the event manager of the TMQL4J event model
	 */
	private final EventManager eventManager;

	/**
	 * The TMQL4J runtime
	 */
	private final TMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @throws DeletionException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public DeletionHandler(TMQLRuntime runtime) throws DeletionException {
		try {
			topicMap = (TopicMap) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.CURRENT_MAP);
			eventManager = runtime.getEventManager();
			this.runtime = runtime;
		} catch (TMQLRuntimeException e) {
			throw new DeletionException(e);
		}
	}

	/**
	 * Method removes all constructs for the topic map.
	 * 
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	public long deleteAll() throws DeletionException {
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
		long amount = 0;

		/*
		 * delete all topic types
		 */
		types.addAll(index.getTopicTypes());
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();

		/*
		 * delete all name-types
		 */
		types.addAll(index.getNameTypes());
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();

		/*
		 * delete all occurrence-types
		 */
		types.addAll(index.getOccurrenceTypes());
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();

		/*
		 * delete all association-types
		 */
		types.addAll(index.getAssociationTypes());
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();

		/*
		 * delete all role-types
		 */
		types.addAll(index.getRoleTypes());
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();
		
		/*
		 * delete all type-less topics
		 */
		types.addAll(index.getTopics(null));
		for (Topic topic : types) {
			amount += deleteTopic(topicMap, topic, true);
		}
		types.clear();

		return amount;
	}

	/**
	 * Method removes the given content from the internal topic map.
	 * 
	 * @param matches
	 *            a set of items which shall be removed
	 * @param cascade
	 *            flag if dependent content shall also removed
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	public long delete(ITupleSequence<Object> matches, boolean cascade)
			throws DeletionException {
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long delete(TopicMap topicMap, Object match, boolean cascade)
			throws DeletionException {
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
			return deleteLocator(topicMap, (Locator) match, cascade);
		} else if (match instanceof Collection<?>) {
			long count = 0;
			for (Object obj : (Collection<?>) match) {
				count += delete(topicMap, obj, cascade);
			}
			return count;
		}
		return 0;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteTopic(final TopicMap topicMap, final Topic topic,
			final boolean cascade) throws DeletionException {
		try {
			long count = 0;
			if (cascade) {
				/*
				 * delete all names of the topic
				 */
				Set<Name> names = HashUtil.getHashSet();
				names.addAll(topic.getNames());
				for (Name name : names) {
					count += deleteName(topicMap, name, cascade);
				}
				/*
				 * delete all occurrences of the topic
				 */
				Set<Occurrence> occurrences = HashUtil.getHashSet();
				occurrences.addAll(topic.getOccurrences());
				for (Occurrence occurrence : occurrences) {
					count += deleteOccurrence(topicMap, occurrence, cascade);
				}

				INavigationAxis axis = runtime.getDataBridge()
						.getImplementationOfTMQLAxis(runtime, "players");
				/*
				 * delete all associations played by the topic
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					count += deleteAssociation(topicMap, (Association) obj,
							cascade);
				}

				// axis = handler.lookup(NavigationAxis.types);
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, "types");
				/*
				 * delete all instances of the topic as type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					count += deleteTopic(topicMap, (Topic) obj, cascade);
				}

				// axis = handler.lookup(NavigationAxis.supertypes);
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, "supertypes");
				/*
				 * delete all sub-types of the topic as type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					count += deleteTopic(topicMap, (Topic) obj, cascade);
				}

				// axis = handler.lookup(NavigationAxis.roles);
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, "roles");
				axis.setTopicMap(topicMap);
				/*
				 * delete all associations which used the topic as role-type
				 */
				for (Object obj : axis.navigateBackward(topic)) {
					count += deleteAssociation(topicMap, (Association) obj,
							cascade);
				}

				/*
				 * delete reification
				 */
				Reifiable reifiable = topic.getReified();
				if (reifiable != null) {
					count += delete(topicMap, reifiable, cascade);
				}

				/*
				 * remove from scope where topic is used as theme
				 */
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, "scope");//
				axis.setTopicMap(topicMap);
				for (Object obj : axis.navigateBackward(topic)) {
					count += deleteScoped(topicMap, (Scoped) obj, cascade);
				}

				/*
				 * check typed of the given topic type
				 */
				try {
					TypeInstanceIndex index = topicMap
							.getIndex(TypeInstanceIndex.class);
					if (!index.isOpen()) {
						index.open();
					}
					for (Name n : index.getNames(topic)) {
						count += deleteName(topicMap, n, cascade);
					}
					for (Occurrence o : index.getOccurrences(topic)) {
						count += deleteOccurrence(topicMap, o, cascade);
					}
					Set<Association> set = HashUtil.getHashSet();
					for (Role r : index.getRoles(topic)) {
						if (set.contains(r.getParent())) {
							continue;
						}
						count += deleteAssociation(topicMap, r.getParent(),
								cascade);
						set.add(r.getParent());
					}
					for (Association a : index.getAssociations(topic)) {
						if (set.contains(a)) {
							continue;
						}
						count += deleteAssociation(topicMap, a, cascade);
					}
				} catch (UnsupportedOperationException e) {
					// NOTHING TO DO
					Logger.getLogger(getClass().getName())
							.warning(
									"TypeInstanceIndex not supported by the current topic map system. Deletion of cascaded typed objects not possible!");
				}

			}

			/*
			 * delete the topic itself
			 */
			eventManager.event(new DeletionEvent(topic, this));

			topic.remove();
			count++;
			return count;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteAssociation(final TopicMap topicMap,
			final Association association, final boolean cascade)
			throws DeletionException {
		/*
		 * delete the association itself
		 */
		long count = association.getRoles().size();
		association.remove();
		eventManager.event(new DeletionEvent(association, this));
		return count+1;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteName(final TopicMap topicMap, final Name name,
			final boolean cascade) throws DeletionException {
		long count = 0;
		if (cascade) {
			/*
			 * delete all variants of the name
			 */
			Set<Variant> variants = HashUtil.getHashSet();
			variants.addAll(name.getVariants());
			for (Variant variant : variants) {
				count += deleteVariant(topicMap, variant, cascade);
			}
		}
		/*
		 * delete the name itself
		 */
		name.remove();
		eventManager.event(new DeletionEvent(name, this));
		count++;
		return count;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteOccurrence(final TopicMap topicMap,
			final Occurrence occurrence, final boolean cascade)
			throws DeletionException {
		/*
		 * delete the occurrence itself
		 */
		occurrence.remove();
		eventManager.event(new DeletionEvent(occurrence, this));
		return 1;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteVariant(final TopicMap topicMap, final Variant variant,
			final boolean cascade) throws DeletionException {
		/*
		 * delete the variant itself
		 */
		variant.remove();
		eventManager.event(new DeletionEvent(variant, this));
		return 1;
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteLocator(final TopicMap topicMap, final Locator locator,
			final boolean cascade) throws DeletionException {

		long count = 0;
		/*
		 * look up the construct using the locator as item-identifier
		 */
		Construct construct = topicMap.getConstructByItemIdentifier(locator);
		if (construct != null) {
			construct.removeItemIdentifier(locator);
			eventManager.event(new DeletionEvent(locator, this));
			count++;
		}
		/*
		 * look up the construct using the locator as subject-identifier
		 */
		Topic topic = topicMap.getTopicBySubjectIdentifier(locator);
		if (topic != null) {
			topic.removeSubjectIdentifier(locator);
			eventManager.event(new DeletionEvent(locator, this));
			count++;
		}

		/*
		 * look up the construct using the locator as subject-locator
		 */
		topic = topicMap.getTopicBySubjectLocator(locator);
		if (topic != null) {
			topic.removeSubjectLocator(locator);
			eventManager.event(new DeletionEvent(locator, this));
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
	 * @return a number of elements which was removed
	 * @throws DeletionException
	 *             thrown if deletion fails
	 */
	private long deleteScoped(final TopicMap topicMap, final Scoped scoped,
			final boolean cascade) throws DeletionException {
		return delete(topicMap, scoped, cascade);
	}

}
