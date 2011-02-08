package de.topicmapslab.tmql4j.draft2010.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * test class of set operation
 * @author Sven Krosse
 *
 */
public class SetExpressionTest extends Tmql4JTestCase {

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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

		query = " / topic::myType INTERSECT / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other INTERSECT / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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

		query = " / topic::myType INTERSECT / topic::other INTERSECT / topic::another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::myType INTERSECT / topic::another INTERSECT / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other INTERSECT / topic::myType INTERSECT / topic::another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other INTERSECT / topic::another INTERSECT / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::another INTERSECT / topic::myType INTERSECT / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::another INTERSECT / topic::other INTERSECT / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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

		query = " / topic::myType UNION / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other UNION / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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

		query = " / topic::myType UNION / topic::other UNION / topic::another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::myType UNION / topic::another UNION / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other UNION / topic::myType UNION / topic::another";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::other UNION / topic::another UNION / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::another UNION / topic::myType UNION / topic::other";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::another UNION / topic::other UNION / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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

		query = " / topic::other MINUS / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}

		query = " / topic::myType MINUS / topic::other";
		set = execute(query);
		assertEquals(0, set.size());
	}

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
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
		 * second part will be interpret first ( / topic::other UNION /
		 * topic::myType )
		 */
		query = " / topic::another MINUS / topic::other MINUS / topic::myType";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

}
