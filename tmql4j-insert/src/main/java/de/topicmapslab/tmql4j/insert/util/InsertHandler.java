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
package de.topicmapslab.tmql4j.insert.util;

import java.util.List;

import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.insert.exceptions.InsertException;

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
	 * a reference of the topic maps system to create temporary instances
	 */
	private final TopicMapSystem topicMapSystem;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the context
	 * @throws InsertException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public InsertHandler(ITMQLRuntime runtime, IContext context) throws InsertException {
		try {
			this.topicMap = context.getQuery().getTopicMap();
			this.topicMapSystem = runtime.getTopicMapSystem();
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
	public long insert(List<Object> ctmFragments) throws InsertException {
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
			TopicMap inserts = CTMConverter.toTopicMap(ctmFragment, topicMapSystem);
			/*
			 * insert the new topic map
			 */
			insert(inserts);
			/*
			 * fire events
			 */
			// TODO handle partial merge
			return inserts.getTopics().size() + inserts.getAssociations().size();
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
}
