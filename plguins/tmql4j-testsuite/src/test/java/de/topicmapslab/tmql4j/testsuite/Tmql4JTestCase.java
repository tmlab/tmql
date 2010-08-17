/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite;

import junit.framework.TestCase;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.identifier.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Abstract base test class for TMQL4J test suite
 * 
 * @author Sven Krosse
 * 
 */
public abstract class Tmql4JTestCase extends TestCase {
	protected TopicMap topicMap;
	protected TopicMapSystemFactory factory;
	protected ITMQLRuntime runtime;
	protected final String base = "http://psi.example.org/test/";

	/**
	 * {@inheritDoc}
	 */
	protected void setUp() throws Exception {
		factory = TopicMapSystemFactory.newInstance();
		topicMap = factory.newTopicMapSystem().createTopicMap(base);
		runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMap);
		runtime.getProperties().enableLanguageExtensionTmqlUl(true);
		runtime.getLanguageContext().getPrefixHandler().setDefaultPrefix(base);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void tearDown() throws Exception {
		topicMap.close();
	}

	/**
	 * Create a locator
	 * 
	 * @param reference
	 *            the locator reference
	 * @return the locator
	 */
	protected Locator createLocator(final String reference) {
		return topicMap.createLocator(base + reference);
	}

	/**
	 * create a topic without any identity
	 * 
	 * @return the topic
	 */
	protected Topic createTopic() {
		return topicMap.createTopic();
	}

	/**
	 * create a topic with an item-identifier
	 * 
	 * @param reference
	 *            the reference of the item-identifier
	 * @return the created topic
	 */
	protected Topic createTopicByII(String reference) {
		return topicMap.createTopicByItemIdentifier(createLocator(reference));
	}

	/**
	 * create a topic with an subject-identifier
	 * 
	 * @param reference
	 *            the reference of the subject-identifier
	 * @return the created topic
	 */
	protected Topic createTopicBySI(String reference) {
		return topicMap
				.createTopicBySubjectIdentifier(createLocator(reference));
	}

	/**
	 * create a topic with an subject-locator
	 * 
	 * @param reference
	 *            the reference of the subject-locator
	 * @return the created topic
	 */
	protected Topic createTopicBySL(String reference) {
		return topicMap.createTopicBySubjectLocator(createLocator(reference));
	}

	/**
	 * create an association
	 * 
	 * @param type
	 *            the association type
	 * @return the created association
	 */
	protected Association createAssociation(final Topic type) {
		return topicMap.createAssociation(type, new Topic[0]);
	}

	/**
	 * create an association with a generated type
	 * 
	 * @return the created association
	 */
	protected Association createAssociation() {
		return topicMap.createAssociation(createTopic(), new Topic[0]);
	}

	protected void addSupertype(Topic type, Topic supertype) {
		Association association = createAssociation(topicMap
				.createTopicBySubjectIdentifier(topicMap
						.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)));
		association
				.createRole(
						topicMap
								.createTopicBySubjectIdentifier(topicMap
										.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE)),
						supertype);
		association.createRole(topicMap.createTopicBySubjectIdentifier(topicMap
				.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE)),
				type);
	}

	@SuppressWarnings("unchecked")
	protected <T extends IResultSet<?>> T execute(String query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		IQuery q = runtime.run(query);
		return (T) q.getResults();
	}

	@SuppressWarnings("unchecked")
	protected <T extends IResultSet<?>> T execute(IQuery query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		runtime.run(query);
		return (T) query.getResults();
	}
}
