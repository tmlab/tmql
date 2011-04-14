/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestFilterPostfix extends Tmql4JTestCase {

	@Test
	public void testNCLTypeFilterDoubleBackslash() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names // theType";
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
			constructs[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names [ ^ theType ]";
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
	public void testTypeFilter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names [ . / types == theType ]";
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
			scopeds[i] = topic.createName("Value", new Topic[0]);
			scopeds[i].addTheme(theme);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names  @ theme";
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
			scopeds[i] = topic.createName("Value", new Topic[0]);
			scopeds[i].addTheme(theme);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names [ @ theme ]";
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
	public void testScopeFilter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Scoped[] scopeds = new Scoped[100];
		for (int i = 0; i < scopeds.length; i++) {
			scopeds[i] = topic.createName("Value", new Topic[0]);
			scopeds[i].addTheme(theme);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names [ . / scope == theme ]";
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
	public void testIndexFilter() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic / occurrences [ $# == " + i + " ]";
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		}
	}

	@Test
	public void testNCLIndexFilter() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic / occurrences [" + i + "]";
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		}
	}

	@Test
	public void testNCLIndexFilterOutOfBounds() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic  / occurrences[ -1 ]";
			set = execute(query);
			assertEquals(0, set.size());

			query = "myTopic / occurrences [  999 ]";
			set = execute(query);
			assertEquals(0, set.size());
		}
	}

	@Test
	public void testNCLIndexRangeFilter() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic  / occurrences [ " + i + " .. " + (i + 1) + " ]";
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		}
	}

	@Test
	public void testNCLIndexRangeFilterOutOfBounds() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic / occurrences [ -1 .. " + i + " ]";
			set = execute(query);
			assertEquals(i, set.size());

			query = "myTopic  / occurrences [ -1 .. 999 ]";
			set = execute(query);
			assertEquals(i + 1, set.size());

			query = "myTopic  / occurrences [ " + i + " .. 999 ]";
			set = execute(query);
			assertEquals(1, set.size());
		}
	}

	@Test
	public void testIndexRangeFilter() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myTopic");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			query = "myTopic  / occurrences [ " + i + " <= $# AND $# < " + (i + 1) + " ]";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		}
	}

	@Test
	public void testDatatypeFilter() throws Exception {
		Topic type = createTopicBySI("type");
		String value = "Wed Jun 23 19:11:23 +0200 2010";
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = createTopic().createOccurrence(type, value, topicMap.createLocator(XmlSchemeDatatypes.XSD_DATETIME));
		}

		String query = null;
		IResultSet<?> set = null;

		query = "// tm:subject  / occurrences type [ . / value == \"" + value + "\" ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject  / occurrences type [  . / value =~ \"Wed Jun 23 19:11:23 \\\\+0200 2010\" ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject  / occurrences type [  fn:count ( . / scope ) == 0 ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject  / occurrences type [ fn:count ( . / scope ) == 0 AND . / value == \"" + value + "\" ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject  / occurrences type [ fn:count ( . / scope ) == 0 AND . / value =~ \"Wed Jun 23 19:11:23 \\\\+0200 2010\" ]";
		set = execute(query);
		assertEquals(100, set.size());

	}

	@Test
	public void testIntegerFilter() throws Exception {
		Topic type = createTopicBySI("type");
		String value = "10";
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = createTopic().createOccurrence(type, value, topicMap.createLocator(XmlSchemeDatatypes.XSD_INT));
		}

		String query = null;
		IResultSet<?> set = null;

		query = "// tm:subject / occurrences type [ . / value == \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [  . / value =~ \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [  fn:count ( . / scope ) == 0 ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences type [ fn:count ( . / scope ) == 0 AND . / value == \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [ fn:count ( . / scope ) == 0 AND . / value =~ \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

	}

	@Test
	public void testDecimalFilter() throws Exception {
		Topic type = createTopicBySI("type");
		String value = "10.5";
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = createTopic().createOccurrence(type, value, topicMap.createLocator(XmlSchemeDatatypes.XSD_DECIMAL));
		}

		String query = null;
		IResultSet<?> set = null;

		query = "// tm:subject / occurrences  type [ . / value == \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [  . / value =~ \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [  fn:count ( . / scope ) == 0 ]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [ fn:count ( . / scope ) == 0 AND . / value == \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

		query = "// tm:subject / occurrences  type [ fn:count ( . / scope ) == 0 AND . / value =~ \"" + value + "\"]";
		set = execute(query);
		assertEquals(100, set.size());

	}

	@Test
	public void testConjunctionFilter() throws Exception {

		Topic topic = createTopicBySI("myTopic");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			if (i % 4 == 0) {
				createAssociation(topic);
			} else if (i % 4 == 1) {
				Topic t = createTopic();
				t.createName(topic, "Name");
				topics.add(t);
			} else if (i % 4 == 2) {
				Topic t = createTopic();
				t.createOccurrence(topic, "Value");
				topics.add(t);
			} else {
				Topic t = createTopic();
				createAssociation().createRole(topic, t);
				topics.add(t);
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject [ fn:count ( . / instances ) == 0 AND fn:count ( . / typed ) == 0 ]";
		set = execute(query);
		assertEquals(75, set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testFilterAfterStep() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("type");
		Topic otherType = createTopicBySI("otherType");
		Set<String> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			if (i % 2 == 0) {
				topic.createOccurrence(type, "Name" + i);
				results.add("Name" + i);
			} else {
				topic.createOccurrence(otherType, "Value " + i);
			}
		}

		final String query = " myTopic / occurrences [ . / types ==  \"" + type.getId() + "\" / by-id ] / value ";
		IResultSet<?> rs = execute(query);
		assertEquals(results.size(), rs.size());
		for (IResult r : rs) {
			assertTrue(results.contains(r.get(0)));
		}
	}

}
