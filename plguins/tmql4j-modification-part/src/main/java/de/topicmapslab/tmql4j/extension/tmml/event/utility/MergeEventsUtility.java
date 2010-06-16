/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.event.utility;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;

/**
 * Utility class providing some frequently used functions in context of merge
 * events.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MergeEventsUtility {

	/**
	 * private and hidden constructor
	 */
	private MergeEventsUtility() {
	}

	/**
	 * Method tries to detect all merges which will be happened after inserting
	 * new content.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the new topic map parsed from CTM
	 * @return a map containing all potential merges
	 * @throws TMQLRuntimeException
	 *             thrown if identification fails
	 */
	public static Map<? extends Construct, Set<? extends Construct>> identifyMerges(
			TopicMap origin, TopicMap input) throws TMQLRuntimeException {
		Map<Construct, Set<? extends Construct>> merges = HashUtil.getHashMap();

		/*
		 * try to detect topic merges
		 */
		for (Entry<Topic, Set<Topic>> entry : identifyTopicMerges(origin, input)
				.entrySet()) {
			merges.put(entry.getKey(), entry.getValue());
		}

		/*
		 * try to detetc association merges
		 */
		for (Entry<Association, Set<Association>> entry : identifyAssociationMerges(
				origin, input).entrySet()) {
			merges.put(entry.getKey(), entry.getValue());
		}

		return merges;
	}

	/**
	 * Method tries to detect all merging topic which will be merged after
	 * inserting new content.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the new topic map parsed from CTM
	 * @return a map containing all potential merging topics
	 * @throws TMQLRuntimeException
	 *             thrown if identification fails
	 */
	public static Map<Topic, Set<Topic>> identifyTopicMerges(TopicMap origin,
			TopicMap input) throws TMQLRuntimeException {
		Map<Topic, Set<Topic>> merges = HashUtil.getHashMap();

		for (Topic topic : input.getTopics()) {
			Set<Topic> set = HashUtil.getHashSet();
			/*
			 * Merge by item-identifier
			 */
			for (Locator locator : topic.getItemIdentifiers()) {
				Construct construct = origin
						.getConstructByItemIdentifier(locator);
				if (construct instanceof Topic) {
					set.add((Topic) construct);
				}
			}
			/*
			 * Merge by subject-identifier
			 */
			for (Locator locator : topic.getSubjectIdentifiers()) {
				Topic t = origin.getTopicBySubjectIdentifier(locator);
				if (t != null) {
					set.add(t);
				}
			}
			/*
			 * Merge by subject-locators
			 */
			for (Locator locator : topic.getSubjectLocators()) {
				Topic t = origin.getTopicBySubjectLocator(locator);
				if (t != null) {
					set.add(t);
				}
			}

			// TODO include all merging rules

			merges.put(topic, set);
		}

		return merges;
	}

	/**
	 * Method tries to detect all merging association which will be merged after
	 * inserting new content.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the new topic map parsed from CTM
	 * @return a map containing all potential merging association
	 * @throws TMQLRuntimeException
	 *             thrown if identification fails
	 */
	public static Map<Association, Set<Association>> identifyAssociationMerges(
			TopicMap origin, TopicMap input) throws TMQLRuntimeException {
		Map<Association, Set<Association>> merges = HashUtil.getHashMap();

		for (Association association : input.getAssociations()) {
			Set<Association> set = HashUtil.getHashSet();
			/*
			 * Merge by item-identifier
			 */
			for (Locator locator : association.getItemIdentifiers()) {
				Construct construct = origin
						.getConstructByItemIdentifier(locator);
				if (construct instanceof Association) {
					set.add((Association) construct);
				}
			}

			// TODO include all merging rules

			merges.put(association, set);
		}

		return merges;
	}
}
