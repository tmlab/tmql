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

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;

/**
 * Utility class providing some frequently used functions in context of insert
 * events.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertEventsUtility {

	/**
	 * private and hidden constructor
	 */
	private InsertEventsUtility() {
	}

	/**
	 * Extracts the new content which will be inserted into the topic map.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the topic map parsed from CTM
	 * @return a map containing the new items
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	public static Map<Class<? extends Construct>, Set<?>> extractChanges(
			TopicMap origin, TopicMap input) throws TMQLRuntimeException {

		Map<Class<? extends Construct>, Set<?>> map = HashUtil.getHashMap();

		/*
		 * extract all topics
		 */
		map.put(Topic.class, extractTopicChanges(origin, input));
		/*
		 * extract all associations
		 */
		map.put(Association.class, extractAssociationChanges(origin, input));
		/*
		 * extract all names
		 */
		map.put(Name.class, extractNameChanges(origin, input));
		/*
		 * extract all occurrences
		 */
		map.put(Occurrence.class, extractOccurrenceChanges(origin, input));

		return map;
	}

	/**
	 * Extracts the new topics which will be inserted into the topic map.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the topic map parsed from CTM
	 * @return a set containing the new topics
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	public static Set<Association> extractAssociationChanges(TopicMap origin,
			TopicMap input) throws TMQLRuntimeException {
		return input.getAssociations();
	}

	/**
	 * Extracts the new associations which will be inserted into the topic map.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the topic map parsed from CTM
	 * @return a set containing the new associations
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	public static Set<Topic> extractTopicChanges(TopicMap origin, TopicMap input)
			throws TMQLRuntimeException {
		return input.getTopics();
	}

	/**
	 * Extracts the new names which will be inserted into the topic map.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the topic map parsed from CTM
	 * @return a set containing the new names
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	public static Set<Name> extractNameChanges(TopicMap origin, TopicMap input)
			throws TMQLRuntimeException {
		Set<Name> names = HashUtil.getHashSet();

		for (Topic topic : input.getTopics()) {
			names.addAll(topic.getNames());
		}

		return names;
	}

	/**
	 * Extracts the new occurrences which will be inserted into the topic map.
	 * 
	 * @param origin
	 *            the unmodified topic map
	 * @param input
	 *            the topic map parsed from CTM
	 * @return a set containing the new occurrences
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	public static Set<Occurrence> extractOccurrenceChanges(TopicMap origin,
			TopicMap input) throws TMQLRuntimeException {
		Set<Occurrence> occurrences = HashUtil.getHashSet();

		for (Topic topic : input.getTopics()) {
			occurrences.addAll(topic.getOccurrences());
		}

		return occurrences;
	}
}
