/**
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

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.tmapi.core.Construct;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.event.model.EventManager;
import de.topicmapslab.tmql4j.extension.tmml.event.InsertEvent;
import de.topicmapslab.tmql4j.extension.tmml.event.MergeEvent;
import de.topicmapslab.tmql4j.extension.tmml.event.utility.InsertEventsUtility;
import de.topicmapslab.tmql4j.extension.tmml.event.utility.MergeEventsUtility;
import de.topicmapslab.tmql4j.extension.tmml.exception.InsertException;
import de.topicmapslab.tmql4j.interpreter.utility.ctm.CTMConverter;

/**
 * Utility class to handle the inserts of topic map items. Class handles all
 * operation which will be proceeded during the interpretation of a
 * insert-expression.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertHandler {

	/**
	 * the topic map containing the items to delete
	 */
	private final TopicMap topicMap;
	/**
	 * a reference of the event manager of the TMQL4J event model
	 */
	private final EventManager eventManager;
	/**
	 * a reference of the topic maps system to create temporary instances
	 */
	private final TopicMapSystem topicMapSystem;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @throws InsertException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public InsertHandler(TMQLRuntime runtime) throws InsertException {
		try {
			topicMap = (TopicMap) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.CURRENT_MAP);
			eventManager = runtime.getEventManager();
			topicMapSystem = runtime.getTopicMapSystem();
		} catch (TMQLRuntimeException e) {
			throw new InsertException(e);
		}
	}

	/**
	 * Method insert new content in the internal topic map instance. The new
	 * content is specified as CTM fragments which containing topic definitions
	 * or association definitions.
	 * 
	 * @param ctmFragments
	 *            a set of CTM fragments
	 * @return a number of new content which was inserted.
	 * @throws InsertException
	 *             thrown if CTM parsing or the merging process of the topic map
	 *             fragments fails.
	 */
	public long insert(ITupleSequence<Object> ctmFragments)
			throws InsertException {
		long count = 0;

		for (Object obj : ctmFragments) {
			if (obj instanceof String) {
				count += insert((String) obj);
			}
		}

		return count;
	}

	/**
	 * Method insert new content in the internal topic map instance. The new
	 * content is specified as a CTM fragment which containing topic definitions
	 * or association definitions.
	 * 
	 * @param ctmFragment
	 *            a CTM fragments
	 * @return a number of new content which was inserted.
	 * @throws InsertException
	 *             thrown if CTM parsing or the merging process of the topic map
	 *             fragments fails.
	 */
	public long insert(String ctmFragment) throws InsertException {
		try {
			/*
			 * parse the CTM to a topic map fragment
			 */
			TopicMap inserts = CTMConverter.toTopicMap(ctmFragment,
					topicMapSystem);
			/*
			 * utility is used to extract the content which will be added
			 */
			Map<Class<? extends Construct>, Set<?>> insertChanges = InsertEventsUtility
					.extractChanges(topicMap, inserts);
			/*
			 * utility is used to extract the potential merges after insert
			 */
			Map<? extends Construct, Set<? extends Construct>> mergeChanges = MergeEventsUtility
					.identifyMerges(topicMap, inserts);
			/*
			 * insert the new topic map
			 */
			insert(inserts);
			/*
			 * fire events
			 */
			return events(insertChanges, mergeChanges);
		} catch (TMQLRuntimeException e) {
			throw new InsertException("Cannot insert new values", e);
		}
	}

	/**
	 * Method insert new content in the internal topic map instance. The new
	 * content is specified as a topic map fragment which containing topic
	 * definitions or association definitions.
	 * 
	 * @param inserts
	 *            a topic map fragment containing the new content
	 * @return a number of new content which was inserted.
	 * @throws InsertException
	 *             thrown if the merging process of the topic map fragments
	 *             fails.
	 */
	public void insert(TopicMap inserts) throws InsertException {
		try {
			this.topicMap.mergeIn(inserts);
		} catch (ModelConstraintException e) {
			throw new InsertException("Cannot insert new values", e);
		}
	}

	/**
	 * Internal method to handle event processing of each insert operation.
	 * <p>
	 * Please note that each insert operation can cause insert and merge events
	 * because there are topics with the same identifier in the base topic map
	 * which shall be merged according the TMDM.
	 * </p>
	 * 
	 * @param insertChanges
	 *            a set of new content which was inserted. For each item an
	 *            insert event will be fired.
	 * @param mergeChanges
	 *            a set of potential merges. For each item a merge event will be
	 *            fired.
	 * @return a number of items which where effected by the event manager
	 */
	private final long events(
			Map<Class<? extends Construct>, Set<?>> insertChanges,
			Map<? extends Construct, Set<? extends Construct>> mergeChanges) {

		long count = 0;

		/*
		 * iterate over inserted items
		 */
		for (Set<?> set : insertChanges.values()) {
			for (Object obj : set) {
				/*
				 * fire an insert event
				 */
				eventManager.event(new InsertEvent(obj, this));
				count++;
			}
		}

		/*
		 * iterate over all potential merging items
		 */
		for (Entry<? extends Construct, Set<? extends Construct>> entry : mergeChanges
				.entrySet()) {
			/*
			 * fire a merge event
			 */
			eventManager.event(new MergeEvent(entry.getKey(), entry.getValue(),
					this));
			count++;
		}
		return count;

	}
}
