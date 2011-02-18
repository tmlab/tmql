/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestResultSet {

	/**
	 * Test method for
	 * {@link de.topicmapslab.tmql4j.components.processor.results.model.ResultSet#unify()}
	 * .
	 */
	@Test
	public void testUnify() {
		SimpleResultSet resultSet = new SimpleResultSet(null, null);

		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 5; j++) {
				SimpleResult result = new SimpleResult(resultSet);
				result.add(i);
				result.add(i);
				result.add(i);
				resultSet.addResult(result);
			}
		}

		assertEquals(5000, resultSet.size());
		resultSet.unify();
		assertEquals(1000, resultSet.size());
	}

	@Test
	public void testToTopicMap() throws Exception {
		TopicMapSystem tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		TopicMap tm = tms.createTopicMap("http://test.tm");
		SimpleResultSet resultSet = new SimpleResultSet(tms, tm);
		for (int i = 0; i < 100; i++) {
			SimpleResult result = new SimpleResult(resultSet);
			result.add(tm.createTopic());
			resultSet.addResult(result);
		}
		tm.createTopic();

		TopicMap copy = resultSet.toTopicMap();
		assertEquals(100, copy.getTopics().size());
		
		resultSet.toCTM(System.out);
		resultSet.toXTM(System.out);
		resultSet.toJTMQR(System.out);
	}

}
