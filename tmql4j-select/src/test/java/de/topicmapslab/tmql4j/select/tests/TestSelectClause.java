/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestSelectClause extends Tmql4JTestCase {

	@Test
	public void testSingleSelectClause() throws Exception {

		Topic topic = createTopicBySI("myTopic");

		String query = "SELECT myTopic";
		SimpleResultSet set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testDoubleSelectClause() throws Exception {

		Topic topic = createTopicBySI("myTopic");

		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name" + Integer.toString(i)));
		}
		assertEquals(100, topic.getNames().size());

		String query = "SELECT myTopic , myTopic >> characteristics tm:name";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(names.contains(r.getResults().get(1)));
		}

		query = "SELECT ( myTopic , myTopic >> characteristics tm:name )";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(names.contains(r.getResults().get(1)));
		}
	}
}
