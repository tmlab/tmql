/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.tests.prepared;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestPreparedStatement extends Tmql4JTestCase {

	@Test
	public void testPreparedStatement() {
		IPreparedStatement statement = runtime.preparedStatement(" ? / names ");
		statement.setTopicMap(topicMap);

		Map<Topic, Set<Construct>> values = HashUtil.getHashMap();
		for (int i = 0; i < 10000; i++) {
			Topic topic = createTopic();
			Set<Construct> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		/*
		 * test set before querying
		 */
		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.setTopic(0, entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(1, result.size());
				assertTrue(entry.getValue().contains(result.first()));
			}
			assertEquals("\"" + entry.getKey().getItemIdentifiers().iterator().next().getReference() + "\" / by-item-identifier / names", statement.getNonParameterizedQueryString());
		}

		/*
		 * test set with run method
		 */
		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.run(entry.getKey());
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(1, result.size());
				assertTrue(entry.getValue().contains(result.first()));
			}
			assertEquals("\"" + entry.getKey().getItemIdentifiers().iterator().next().getReference() + "\" / by-item-identifier / names", statement.getNonParameterizedQueryString());
		}
	}

	@Test
	public void testAnonymousNamedWildcards() {
		IPreparedStatement statement = runtime.preparedStatement(" ? { ? , . / names }");
		statement.setTopicMap(topicMap);

		Map<Topic, Set<Construct>> values = HashUtil.getHashMap();
		for (int i = 0; i < 10000; i++) {
			Topic topic = createTopic();
			Set<Construct> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set(entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set(IPreparedStatement.ANONYMOUS, entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set("?", entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set("", entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}
	}

	@Test
	public void testNamedWildcards() {
		IPreparedStatement statement = runtime.preparedStatement(" ?topic { ?topic , . / names }");
		statement.setTopicMap(topicMap);

		Map<Topic, Set<Construct>> values = HashUtil.getHashMap();
		for (int i = 0; i < 10000; i++) {
			Topic topic = createTopic();
			Set<Construct> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set(0, entry.getKey());
			statement.set(1, entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set("?topic", entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			statement.set("topic", entry.getKey());
			statement.run();
			IResultSet<?> set = statement.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(2, result.size());
				assertEquals(entry.getKey(), result.first());
				assertTrue(entry.getValue().contains(result.get(1)));
			}
		}
	}

	@Test
	public void testProtectedStatement() {
		Map<Topic, Set<Construct>> values = HashUtil.getHashMap();
		for (int i = 0; i < 10000; i++) {
			Topic topic = createTopic();
			Set<Construct> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			IResultSet<?> set = runtime.run(topicMap, " ? / names", entry.getKey()).getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(1, result.size());
				assertTrue(entry.getValue().contains(result.first()));
			}
		}
	}

	@Test
	public void testIndexPrepared() {
		Map<Topic, Set<Name>> values = HashUtil.getHashMap();
		for (int i = 0; i < 100; i++) {
			Topic topic = createTopic();
			Set<Name> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		for (Entry<Topic, Set<Name>> entry : values.entrySet()) {
			IResultSet<?> set = runtime.run(topicMap, " ? / names", entry.getKey()).getResults();
			assertEquals(entry.getValue().size(), set.size());
			set = runtime.run(topicMap, " ? / names [ ? ]", entry.getKey(), 0).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? ]", entry.getKey(), 0L).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? ]", entry.getKey(), BigInteger.valueOf(0L)).getResults();
			assertEquals(1, set.size());

			set = runtime.run(topicMap, " ? / names [ $# == ? ]", entry.getKey(), 0).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ $# == ? ]", entry.getKey(), 0L).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ $# == ? ]", entry.getKey(), BigInteger.valueOf(0L)).getResults();
			assertEquals(1, set.size());
		}

		for (Entry<Topic, Set<Name>> entry : values.entrySet()) {
			IResultSet<?> set = runtime.run(topicMap, " ? / names", entry.getKey()).getResults();
			assertEquals(entry.getValue().size(), set.size());
			set = runtime.run(topicMap, " ? / names [ ? .. ? ]", entry.getKey(), 0, 1).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? .. ? ]", entry.getKey(), 0L, 1L).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? .. ? ]", entry.getKey(), BigInteger.valueOf(0L), BigInteger.valueOf(1L)).getResults();
			assertEquals(1, set.size());

			set = runtime.run(topicMap, " ? / names [ ? <= $# AND $# < ? ]", entry.getKey(), 0, 1).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? <= $# AND $# < ? ]", entry.getKey(), 0L, 1L).getResults();
			assertEquals(1, set.size());
			set = runtime.run(topicMap, " ? / names [ ? <= $# AND $# < ? ]", entry.getKey(), BigInteger.valueOf(0L), BigInteger.valueOf(1L)).getResults();
			assertEquals(1, set.size());
		}
	}

	@Test
	public void testAnchorPrepared() {
		Topic t = createTopic();
		Topic type = createTopic();
		Set<Name> names = new HashSet<Name>();
		for (int i = 0; i < 100; i++) {
			if (i % 2 == 0) {
				names.add(t.createName(type, "Name" + i));
			} else {
				t.createName("Name" + i);
			}
		}

		IResultSet<?> set = runtime.run(topicMap, " ? / names", t).getResults();
		assertEquals(100, set.size());
		set = runtime.run(topicMap, " ? / names ?", t, type).getResults();
		assertEquals(50, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.get(0)));
		}
	}

	@Test
	@Ignore
	public void testWithoutPreparedStatement() {
		Map<Topic, Set<Construct>> values = HashUtil.getHashMap();
		for (int i = 0; i < 10000; i++) {
			Topic topic = createTopicBySI("base" + i);
			Set<Construct> set = HashUtil.getHashSet();
			for (int j = 0; j < 10; j++) {
				set.add(topic.createName("Name"));
			}
			values.put(topic, set);
		}

		for (Entry<Topic, Set<Construct>> entry : values.entrySet()) {
			String query = entry.getKey().getSubjectIdentifiers().iterator().next().getReference() + " / names ";
			IQuery q = runtime.run(topicMap, query);
			IResultSet<?> set = q.getResults();
			assertEquals(10, set.size());
			for (IResult result : set) {
				assertEquals(1, result.size());
				assertTrue(entry.getValue().contains(result.first()));
			}
		}
	}

}
