/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.QueryFactory;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestForClause extends Tmql4JTestCase {

	@Test
	public void testForClause() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			topics.add(t);
		}

		query = "FOR $t IN // myType RETURN $t";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testDoubleForClause() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		Set<Topic> others = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				topics.add(t);
			} else {
				t.addType(other);
				others.add(t);
			}
		}

		query = "FOR $t IN // myType FOR $p IN // other RETURN $t , $p";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertTrue(topics.contains(r.first()));
			assertTrue(others.contains(r.getResults().get(1)));
		}
	}

	@Test
	public void doSo() {
		Topic topic = createTopicBySI("myType");
		Set<Topic> instances = HashUtil.getHashSet();
		Set<Topic> otherInstances = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
				t.createName("Name");
				instances.add(t);
			} else {
				otherInstances.add(t);
				t.createName("Name");
				t.createName("Name");
			}
		}
		IQuery query = QueryFactory.getFactory().getQuery(topicMap, "FOR $t IN // tm:subject [ fn:count ( . >> types ) == 0 ] RETURN $t,  $t >> characteristics >> types");

		SimpleResultSet set = execute(query);
		System.out.println(set);
		System.out.println(set.size());
	}

}
