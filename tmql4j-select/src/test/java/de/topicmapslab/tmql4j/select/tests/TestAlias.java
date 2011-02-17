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

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestAlias extends Tmql4JTestCase {

	@Test(expected = IllegalArgumentException.class)
	public void testError() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		Topic other = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(other);
			topics.add(t);
		}
		IQuery q = runtime.run(topicMap, "SELECT $t  WHERE $t ISA myType");
		q.getResults().get(0, "topic");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testError2() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		Topic other = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(other);
			topics.add(t);
		}
		IQuery q = runtime.run(topicMap, "SELECT $t  WHERE $t ISA myType");
		q.getResults().get(0).get("topic");
	}

	@Test
	public void testProjectionWithAlias() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		Topic other = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(other);
			topics.add(t);
		}
		IQuery q = runtime.run(topicMap, "SELECT $t AS \"topic\" WHERE $t ISA myType");
		assertTrue(topics.contains(q.getResults().get(0, "topic")));
		assertTrue(topics.contains(q.getResults().get(0).get("topic")));
	}

	@Test
	public void testProjectionWithAlias2() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		Map<Topic, Set<Name>> names = HashUtil.getHashMap();
		Topic other = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(other);
			topics.add(t);
			Set<Name> n = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				n.add(t.createName(t, "Name" + j));				
			}
			names.put(t,n);
		}

		IQuery q = runtime.run(topicMap, "SELECT $t AS \"topic\" , $t >> characteristics tm:name AS \"name\" WHERE $t ISA myType");
		IResultSet<?> set = q.getResults();
		assertEquals(1000, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			Topic t = r.get("topic");
			assertTrue(topics.contains(t));
			assertTrue(names.get(t).contains(r.get("name")));
		}
	}

	@Test
	public void testProjectionWithAlias3() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		Topic other = createTopicBySI("myType");
		Map<Topic, Set<Name>> names = HashUtil.getHashMap();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(other);
			topics.add(t);
			Set<Name> n = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				n.add(t.createName(t, "Name" + j));				
			}
			names.put(t,n);
		}
		IQuery q = runtime.run(topicMap, "SELECT $t AS \"topic\" , $t >> characteristics tm:name AS \"name\" , $t >> types AS \"type\" WHERE $t ISA myType");
		IResultSet<?> set = q.getResults();
		assertEquals(1000, set.size());
		for (IResult r : set) {
			assertEquals(3, r.size());
			Topic t = r.get("topic");
			assertTrue(topics.contains(t));
			assertTrue(names.get(t).contains(r.get("name")));
			assertEquals(other, r.get("type"));
		}
	}

}
