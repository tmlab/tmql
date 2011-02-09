/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests;

import org.junit.After;
import org.junit.Before;
import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Abstract base test class for TMQL4J test suite
 * 
 * @author Sven Krosse
 * 
 */
public abstract class Tmql4JTestCase {
	protected TopicMap topicMap;
	protected TopicMapSystemFactory factory;
	protected ITMQLRuntime runtime;
	protected final String base = "http://psi.example.org/test/";
	protected TopicMapSystem topicMapSystem;

	@Before
	public void setUp() throws Exception {
		factory = TopicMapSystemFactory.newInstance();
		factory.setFeature("http://tmapi.org/features/type-instance-associations", true);
		factory.setFeature("http://tmapi.org/features/supertype-subtype-associations", true);
		topicMapSystem = factory.newTopicMapSystem();
		topicMap = topicMapSystem.createTopicMap(base);
		runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem);
		runtime.getLanguageContext().getPrefixHandler().setDefaultPrefix(base);
	}

	@After
	public void tearDown() throws Exception {
		topicMap.remove();
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
		return topicMap.createTopicBySubjectIdentifier(createLocator(reference));
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
		return topicMap.createAssociation(type);
	}

	/**
	 * create an association with a generated type
	 * 
	 * @return the created association
	 */
	protected Association createAssociation() {
		return topicMap.createAssociation(createTopic());
	}

	/**
	 * create supertype-subtype association
	 * 
	 * @param type
	 *            the subtype
	 * @param supertype
	 *            the supertype
	 */
	protected void addSupertype(Topic type, Topic supertype) {
		Association association = createAssociation(topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)));
		association.createRole(topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE)), supertype);
		association.createRole(topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE)), type);
	}

	@SuppressWarnings("unchecked")
	protected <T extends IResultSet<?>> T execute(String query) throws TMQLRuntimeException {
		IQuery q = runtime.run(topicMap, query);
		return (T) q.getResults();
	}

	@SuppressWarnings("unchecked")
	protected <T extends IResultSet<?>> T execute(IQuery query) throws TMQLRuntimeException {
		query.setTopicMap(topicMap);
		runtime.run(query);
		return (T) query.getResults();
	}
}
