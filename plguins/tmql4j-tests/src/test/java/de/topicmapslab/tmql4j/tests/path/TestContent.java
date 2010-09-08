/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestContent extends Tmql4JTestCase {

	@Test
	public void testIntersection() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				topics.add(t);
			}
			t.addType(other);
		}

		query = " // myType == // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other == // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testIntersectionChain() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 3 == 0) {
				t.addType(topic);
				topics.add(t);
				t.addType(other);
				t.addType(another);
			} else if (i % 3 == 1) {
				t.addType(other);
			} else {
				t.addType(another);
			}
		}

		query = " // myType == // other == // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType == // another == // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other == // myType == // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other == // another == // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another == // myType == // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another == // other == // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testUnion() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
			} else {
				t.addType(other);
			}
			topics.add(t);
		}

		query = " // myType ++ // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other ++ // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testUnionChain() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 3 == 0) {
				t.addType(topic);
			} else if (i % 3 == 1) {
				t.addType(other);
			} else {
				t.addType(another);
			}
			topics.add(t);
		}

		query = " // myType ++ // other ++ // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType ++ // another ++ // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other ++ // myType ++ // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other ++ // another ++ // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another ++ // myType ++ // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another ++ // other ++ // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testMinus() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.addType(other);
			} else {
				t.addType(other);
				topics.add(t);
			}
		}

		query = " // other -- // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType -- // other";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testMinusChain() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 3 == 0) {
				t.addType(topic);
				t.addType(another);
			} else if (i % 3 == 1) {
				t.addType(other);
				t.addType(another);
			} else {
				t.addType(another);
				topics.add(t);
			}
		}

		/*
		 * second part will be interpret first 
		 * ( // other ++ // myType )
		 */
		query = " // another -- // other ++ // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}		
	}
	
	@Test
	public void testIfThenElseClause() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");		
		Set<String> values = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			if (i % 2 == 0) {
				t.createName("Name" + Integer.toString(i));
				values.add("Name" + Integer.toString(i));
			}
		}

		query = " // myType ( IF ( fn:count( . / tm:name ) > 0 ) THEN . / tm:name ) ";
		set = execute(query);
		assertEquals(values.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}	

		values.add("Missing");
		query = " // myType ( IF ( fn:count( . / tm:name ) > 0 ) THEN . / tm:name ELSE \"Missing\" ) ";
		set = execute(query);
		assertEquals(values.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}	
		
		values.add("Missing");
		query = " // myType ( . / tm:name || \"Missing\" ) ";
		set = execute(query);
		assertEquals(values.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}	
	}

}
