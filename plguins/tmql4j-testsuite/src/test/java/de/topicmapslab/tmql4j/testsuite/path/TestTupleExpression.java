/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.path;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestTupleExpression extends Tmql4JTestCase {

	private boolean initialized = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_leipzig.topicmapslab.tmql.testsuite.base.BaseTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		if (!initialized) {
			super.setUp();
			initialized = true;
		}
	}

	public void testSingleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

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

	public void testDoubleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

		query = " ( myTopic  , myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(1, set.size());
		IResult r = set.first();
		assertTrue(r.getResults().get(0).equals(topic));
		Set<Construct> result = HashUtil.getHashSet();
		Object obj = r.getResults().get(1);
		assertTrue(obj instanceof Collection<?>);
		assertEquals(constructs.length, ((Collection<?>) obj).size());
		for (Object o : ((Collection<?>) obj)) {
			assertTrue(o instanceof Construct);
			result.add((Construct) o);
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	public void testTrippleTupleExpression() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

		query = " ( myTopic  , theType, myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(1, set.size());
		IResult r = set.first();
		assertTrue(r.getResults().get(0).equals(topic));
		assertTrue(r.getResults().get(1).equals(type));
		Set<Construct> result = HashUtil.getHashSet();
		Object obj = r.getResults().get(2);
		assertTrue(obj instanceof Collection<?>);
		assertEquals(constructs.length, ((Collection<?>) obj).size());
		for (Object o : ((Collection<?>) obj)) {
			assertTrue(o instanceof Construct);
			result.add((Construct) o);
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	public void testDoubleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

		query = "myTopic ( .  , . >> characteristics theType)";
		set = execute(query);
		assertEquals(1, set.size());
		IResult r = set.first();
		assertTrue(r.getResults().get(0).equals(topic));
		Set<Construct> result = HashUtil.getHashSet();
		Object obj = r.getResults().get(1);
		assertTrue(obj instanceof Collection<?>);
		assertEquals(constructs.length, ((Collection<?>) obj).size());
		for (Object o : ((Collection<?>) obj)) {
			assertTrue(o instanceof Construct);
			result.add((Construct) o);
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	public void testTrippleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

		query = " myTopic ( .  , theType , myTopic >> characteristics theType)";
		set = execute(query);
		assertEquals(1, set.size());
		IResult r = set.first();
		assertTrue(r.getResults().get(0).equals(topic));
		assertTrue(r.getResults().get(1).equals(type));
		Set<Construct> result = HashUtil.getHashSet();
		Object obj = r.getResults().get(2);
		assertTrue(obj instanceof Collection<?>);
		assertEquals(constructs.length, ((Collection<?>) obj).size());
		for (Object o : ((Collection<?>) obj)) {
			assertTrue(o instanceof Construct);
			result.add((Construct) o);
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	public void testSingleTupleExpressionAsProjection() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
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
		SimpleResultSet set = null;

		query = "myTopic ( . >> characteristics theType)";
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
}
