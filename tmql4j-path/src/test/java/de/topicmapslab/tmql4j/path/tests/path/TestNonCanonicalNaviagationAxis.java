/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;
/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestNonCanonicalNaviagationAxis extends Tmql4JTestCase {

	@Test
	public void testNCLInstancesAxisTmSubject() throws Exception {
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLInstancesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// myTopic";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLAtomifyAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		String[] values = new String[100];
		for (int i = 0; i < values.length; i++) {
			values[i] = "Value" + i;

			if (i % 2 == 0) {
				topic.createOccurrence(type, values[i], new Topic[0]);
			} else {
				topic.createName(type, values[i], new Topic[0]);
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / theType";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(values.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < values.length; i++) {
			assertTrue(result.contains(values[i]));
		}
	}

	@Test
	public void testNCLAtomifyAxisTmName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / tm:name";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(names.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < names.length; i++) {
			assertTrue(result.contains(names[i].getValue()));
		}
	}

	@Test
	public void testNCLAtomifyAxisTmOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i,
					new Topic[0]);
			topic.createName(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / tm:occurrence";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(occurrences.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < occurrences.length; i++) {
			assertTrue(result.contains(occurrences[i].getValue()));
		}
	}

	@Test
	public void testNCLDeAotmifyAxisName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		topic.addType(type);
		String query = null;
		SimpleResultSet set = null;
		for (int i = 0; i < 100; i++) {
			topic.createName(type, "Value" + i, new Topic[0]);
			query = "\"Value" + i + "\" \\ theType";
			set = execute(new TMQLQuery(topicMap,query));
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Topic);
			assertTrue(set.getResults().get(0).first().equals(topic));
		}
	}

	@Test
	public void testNCLDeAotmifyAxisOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		topic.addType(type);
		String query = null;
		SimpleResultSet set = null;
		for (int i = 0; i < 100; i++) {
			topic.createOccurrence(type, "Value" + i, new Topic[0]);
			query = "\"Value" + i + "\" \\ theType";
			set = execute(new TMQLQuery(topicMap,query));
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Topic);
			assertTrue(set.getResults().get(0).first().equals(topic));
		}
	}

	@Test
	public void testNCLPlayersAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			createAssociation(topic).createRole(roleType, topics[i]);
			createAssociation(topic).createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic -> roleType";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLPlayersBWAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation();
			associations[i].createRole(roleType, topic);
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic <- roleType";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testNCLReifierAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		/*
		 * name
		 */
		Name n = createTopic().createName("Value", new Topic[0]);
		n.setReifier(topic);
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Name);
		assertTrue(set.getResults().get(0).first().equals(n));
		n.setReifier(null);

		/*
		 * occurrence
		 */
		Occurrence o = createTopic().createOccurrence(createTopic(), "Value",
				new Topic[0]);
		o.setReifier(topic);

		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		assertTrue(set.getResults().get(0).first().equals(o));
		o.setReifier(null);
		/*
		 * variant
		 */
		Variant v = createTopic().createName("Value", new Topic[0])
				.createVariant("Value", createTopic());
		v.setReifier(topic);

		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Variant);
		assertTrue(set.getResults().get(0).first().equals(v));
		v.setReifier(null);
		/*
		 * association
		 */
		Association a = createAssociation();
		a.setReifier(topic);

		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Association);
		assertTrue(set.getResults().get(0).first().equals(a));
		a.setReifier(null);
		/*
		 * association role
		 */
		Role r = createAssociation().createRole(createTopic(), createTopic());
		r.setReifier(topic);

		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Role);
		assertTrue(set.getResults().get(0).first().equals(r));
		r.setReifier(null);
		/*
		 * topic map
		 */
		topicMap.setReifier(topic);
		query = "myTopic ~~>";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof TopicMap);
		assertTrue(set.getResults().get(0).first().equals(topicMap));
	}

	@Test
	public void testNCLReifierBWAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName("Value", new Topic[0]).setReifier(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics <~~";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLTraverseAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type =  createTopicBySI("theType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association a = createAssociation(type);
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic <-> theType";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLTraverseAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association a = createAssociation(type);
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), topics[i]);
			a = createAssociation();
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic <-> assoType";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testNCLItemAxis() throws Exception {
		Topic topic = createTopicByII("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" !";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testNCLIndicatorsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" ~";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testNCLLocatorsAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" =";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

}
