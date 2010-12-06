package de.topicmapslab.tmql4j.draft2010.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

public class FilterTest extends Tmql4JTestCase {

	@Test
	public void testScopeFilter() throws Exception {
		Topic otherTheme = createTopicBySI("theme");
		Topic topic = createTopicBySI("myTopic");
		Name n = topic.createName("Name");
		Set<Scoped> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			if (i % 4 == 0) {
				results.add(topicMap.createAssociation(createTopic(),
						otherTheme));
			} else if (i % 4 == 1) {
				results.add(topic.createName("Name", otherTheme));
			} else if (i % 4 == 2) {
				results.add(topic.createOccurrence(createTopic(), "Occ",
						otherTheme));
			} else {
				results.add(n.createVariant("Variant", otherTheme));
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / name:: @theme ";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / occurrence:: @theme ";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / name:: / variant:: @theme ";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / association:: @theme ";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	@Test
	public void testBooleanFilterWithExpression() throws TMQLRuntimeException {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			if (i % 2 == 0) {
				t.createName("Name");
				results.add(t);
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / instance:: [ . / name:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	@Test
	public void testBooleanFilterWithConjunction() throws TMQLRuntimeException {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			if (i % 2 == 0) {
				t.createName("Name");
				t.addSubjectIdentifier(createLocator("si-"
						+ Integer.toString(i)));
				results.add(t);
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / instance:: [ . / name:: AND . / subject-identifier:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / instance:: [ . / subject-identifier:: AND . / name:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / instance:: [ . / subject-identifier:: ][ . / name:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / instance:: [ . / name:: ][ . / subject-identifier:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	@Test
	public void testBooleanFilterWithDisjunction() throws TMQLRuntimeException {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			if (i % 2 == 0) {
				t.createName("Name");
			} else {
				t.addSubjectIdentifier(createLocator("si-"
						+ Integer.toString(i)));
			}
			results.add(t);
		}

		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / instance:: [ . / name:: OR . / subject-identifier:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / instance:: [ . / subject-identifier:: OR . / name:: ] ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	@Test
	public void testBooleanFilterWithNegation() throws TMQLRuntimeException {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			if (i % 2 == 0) {
				t.createName("Name");
			} else {
				results.add(t);
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / instance:: [ NOT . / name:: ] ";
		set = execute(query);
		assertEquals(set.size(), results.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	@Test
	public void testBooleanFilterComparison() throws TMQLRuntimeException {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopicBySI("si-" + Integer.toString(i));
			t.addType(topic);
			results.add(t);
		}
		String query = null;
		SimpleResultSet set = null;

		query = " / topic::myTopic [ 1 = 1 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ 1 != 0 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ 1 > 0 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ 1 >= 0 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ 1 < 2 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ 1 <= 2 ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::myTopic [ . / subject-identifier:: =~ \"" + base
				+ ".+\" ]";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}
}
