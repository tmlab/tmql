package de.topicmapslab.tmql4j.flwr.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;

public class TestFilterPostfixWithVariables extends Tmql4JTestCase {

	@Test
	public void testNCLTypeFilterDoubleBackslash() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName(type, "Value", new Topic[0]);
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(type, "Value", new Topic[0]);
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = " FOR $type IN theType RETURN myTopic >> characteristics // $type";
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
	public void testNCLTypeFilterCirconflex() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName(type, "Value", new Topic[0]);
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(type, "Value", new Topic[0]);
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		SimpleResultSet set = null;


		query = " FOR $type IN theType RETURN myTopic >> characteristics [ ^ $type ]";		
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
	public void testNCLScopeFilterAt() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Scoped[] scopeds = new Scoped[100];
		for (int i = 0; i < scopeds.length; i++) {
			if (i % 2 == 0) {
				scopeds[i] = topic.createName("Value", new Topic[0]);
			} else if (i % 2 == 1) {
				scopeds[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
			scopeds[i].addTheme(theme);
		}
		String query = null;
		SimpleResultSet set = null;

		query = " FOR $theme IN theme RETURN myTopic >> characteristics @ $theme ";
		set = execute(query);
		assertEquals(scopeds.length, set.size());

		Set<Scoped> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Scoped);
			result.add((Scoped) r.first());
		}

		for (int i = 0; i < scopeds.length; i++) {
			assertTrue(result.contains(scopeds[i]));
		}
	}

	@Test
	public void testNCLScopeFilterCrampedAt() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Scoped[] scopeds = new Scoped[100];
		for (int i = 0; i < scopeds.length; i++) {
			if (i % 2 == 0) {
				scopeds[i] = topic.createName("Value", new Topic[0]);
			} else if (i % 2 == 1) {
				scopeds[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
			scopeds[i].addTheme(theme);
		}
		String query = null;
		SimpleResultSet set = null;

		query = " FOR $theme IN theme RETURN myTopic >> characteristics [ @ $theme ]";
		set = execute(query);
		assertEquals(scopeds.length, set.size());

		Set<Scoped> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Scoped);
			result.add((Scoped) r.first());
		}

		for (int i = 0; i < scopeds.length; i++) {
			assertTrue(result.contains(scopeds[i]));
		}
	}
	
}
