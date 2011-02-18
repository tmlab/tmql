/** 
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
import static junit.framework.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestTupleExpression extends Tmql4JTestCase {

	@Test
	public void testSingleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[10];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName(type, "Value", new Topic[0]);
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(type, "Value",
						new Topic[0]);
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "( myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(constructs.length, set.size());

		Set<Construct> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Construct);
			result.add((Construct) r.first());
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	@Test
	public void testDoubleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Set<Construct> constructs = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				constructs.add(topic.createName(type, "Value", new Topic[0]));
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs.add(topic.createOccurrence(type, "Value",
						new Topic[0]));
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = " ( myTopic  , myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(constructs.contains(r.getResults().get(1)));
		}
	}

	@Test
	public void testTrippleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Set<Construct> constructs = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				constructs.add(topic.createName(type, "Value", new Topic[0]));
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs.add(topic.createOccurrence(type, "Value",
						new Topic[0]));
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = " ( myTopic  , theType, myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(3, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertEquals(type, r.getResults().get(1));
			assertTrue(constructs.contains(r.getResults().get(2)));
		}
	}

	@Test
	public void testDoubleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Set<Construct> constructs = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				constructs.add(topic.createName(type, "Value", new Topic[0]));
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs.add(topic.createOccurrence(type, "Value",
						new Topic[0]));
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic ( .  , . >> characteristics theType)";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(constructs.contains(r.getResults().get(1)));
		}
	}

	@Test
	public void testTrippleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Set<Construct> constructs = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				constructs.add(topic.createName(type, "Value", new Topic[0]));
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs.add(topic.createOccurrence(type, "Value",
						new Topic[0]));
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;
		
		query = " myTopic ( .  , . / nonExists )";
		set = execute(query);

		query = " myTopic ( .  , theType , . >> characteristics theType , . / nonExists )";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(4, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertEquals(type, r.getResults().get(1));
			assertTrue(constructs.contains(r.getResults().get(2)));
			assertTrue(r.isNullValue(3));
		}
		
		query = " myTopic ( . / nonExists   , . / nonExists  , . / nonExists , . / nonExists )";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.isNullValue(0,0));
		assertTrue(set.isNullValue(0,1));
		assertTrue(set.isNullValue(0,2));
		assertTrue(set.isNullValue(0,3));
	}

	@Test
	public void testSingleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Set<Construct> constructs = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				constructs.add(topic.createName(type, "Value", new Topic[0]));
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs.add(topic.createOccurrence(type, "Value",
						new Topic[0]));
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic ( . >> characteristics theType)";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(constructs.contains(r.getResults().get(0)));
		}
	}

	@Test
	public void testSingleTupleExpressionWithOrder() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		List<String> constructs = new LinkedList<String>();
		for (int i = 0; i < 10; i++) {
			topic.createName(type, "Value " + Integer.toString(i), new Topic[0]);
			constructs.add("Value " + Integer.toString(i));
		}
		String query = null;
		IResultSet<?> set = null;

		query = "( myTopic  / theType ASC )";
		set = execute(query);
		assertEquals(10, set.size());
		int i = 0;
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(constructs.get(i++), r.getResults().get(0));
		}

		query = "( myTopic  / theType DESC )";
		set = execute(query);
		assertEquals(10, set.size());
		i = constructs.size() - 1;
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(constructs.get(i--), r.getResults().get(0));
		}
	}

	@Test
	public void testEmptyTupleExpression() throws Exception {
		String query = null;
		IResultSet<?> set = null;

		query = "(  )";
		try {
			set = execute(query);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			fail("Interpretation of empty tuple fails!");
		}
		assertEquals(0, set.size(),1.0);

		query = " NULL ";
		try {
			set = execute(new TMQLQuery(topicMap,query));
		} catch (TMQLRuntimeException e) {
			fail("Interpretation of empty tuple fails!");
		}
		/*
		 * in combination with the new draft the result contains the string NULL if a topic was not found with the given ID
		 */
		assertEquals(0, set.size(),1.0);
	}
}
