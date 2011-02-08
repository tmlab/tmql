/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestContent extends Tmql4JTestCase {

	@Test
	public void testIntersection() throws Exception {
		String query;
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				topics.add(t);
			}
			t.addType(other);
		}

		query = " // myType INTERSECT // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other INTERSECT // myType";
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
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
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

		query = " // myType INTERSECT // other INTERSECT // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType INTERSECT // another INTERSECT // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other INTERSECT // myType INTERSECT // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other INTERSECT // another INTERSECT // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another INTERSECT // myType INTERSECT // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another INTERSECT // other INTERSECT // myType";
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
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
			} else {
				t.addType(other);
			}
			topics.add(t);
		}

		query = " // myType UNION // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other UNION // myType";
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
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
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

		query = " // myType UNION // other UNION // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType UNION // another UNION // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other UNION // myType UNION // another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // other UNION // another UNION // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another UNION // myType UNION // other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // another UNION // other UNION // myType";
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
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.addType(other);
			} else {
				t.addType(other);
				topics.add(t);
			}
		}

		query = " // other MINUS // myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " // myType MINUS // other";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testMinusChain() throws Exception {
		String query;
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic another = createTopicBySI("another");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
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
		 * ( // other UNION // myType )
		 */
		query = " // another MINUS // other UNION // myType";
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
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");		
		Set<String> values = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
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
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}	
		
		values.add("Missing");
		query = " // myType ( . / tm:name || \"Missing\" ) ";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}	
	}

}
