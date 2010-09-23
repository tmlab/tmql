/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.path;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestNavigationAxis extends Tmql4JTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_leipzig.topicmapslab.tmql.testsuite.base.BaseTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testInstancesAxis() throws Exception {
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
		}
		String query = null;
		SimpleResultSet set = null;

		query = "tm:subject >> instances";
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

	public void testTypesAxisForTopic() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.addType(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> types";
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

	public void testTypesAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createOccurrence(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics >> types";
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

	public void testTypesAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics >> types";
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

	public void testTypesAxisForTopicsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << types";
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

	public void testInstancesAxisForTopics() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> instances";
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

	public void testSupertypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topic, topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> supertypes";
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

	public void testSupertypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topics[i], topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << supertypes";
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

	public void testSubtypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topics[i], topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> subtypes";
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

	public void testSubtypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topic, topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << subtypes";
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

	public void testPlayersAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			createAssociation(topic).createRole(createTopic(), topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> players";
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

	public void testPlayersAxisWithParameter() throws Exception {
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

		query = "myTopic >> players roleType";
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

	public void testPlayersAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << players";
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

	public void testPlayersAxisWithParameterBW() throws Exception {
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

		query = "myTopic << players roleType";
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

	public void testRolesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association association = createAssociation(topic);
			association.createRole(topics[i], createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> roles";
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

	public void testRolesAxisWithoutParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation(createTopic());
			associations[i].createRole(roleType, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "roleType << roles";
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

	public void testRolesAxisWithParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Topic topic = createTopicBySI("myTopic");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation(topic);
			associations[i].createRole(roleType, createTopic());
			createAssociation().createRole(roleType, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "roleType << roles myTopic";
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

	public void testTraverseAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association a = createAssociation();
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> traverse";
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

	public void testTraverseAxisWithParameter() throws Exception {
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

		query = "myTopic >> traverse assoType";
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

	public void testTraverseAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			Association a = createAssociation(type);
			a.createRole(createTopic(), topic);

			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "assoType << traverse";
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

	public void testTraverseAxisWithParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic player = createTopic();
		player.addType(topic);
		Topic type = createTopicBySI("assoType");
		Association[] associations = new Association[100];
		for (int i = 0; i < associations.length; i++) {
			Association a = createAssociation(type);
			a.createRole(createTopic(), player);

			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), player);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "assoType << traverse myTopic";
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

	public void testCharacteristicsAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName("Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(createTopic(), "Value",
						new Topic[0]);
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics";
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

	public void testCharacteristicsAxisWithParameter() throws Exception {
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

		query = "myTopic >> characteristics theType";
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

	public void testCharacteristicsAxisWithTmName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createOccurrence(type, "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics tm:name";
		set = execute(query);
		assertEquals(names.length, set.size());

		Set<Name> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Name);
			result.add((Name) r.first());
		}

		for (int i = 0; i < names.length; i++) {
			assertTrue(result.contains(names[i]));
		}
	}

	public void testCharacteristicsAxisWithTmOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic
					.createOccurrence(type, "Value", new Topic[0]);
			topic.createName(type, "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics tm:occurrence";
		set = execute(query);
		assertEquals(occurrences.length, set.size());

		Set<Occurrence> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Occurrence);
			result.add((Occurrence) r.first());
		}

		for (int i = 0; i < occurrences.length; i++) {
			assertTrue(result.contains(occurrences[i]));
		}
	}

	public void testCharacteristicsAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic
					.createOccurrence(type, "Value", new Topic[0]);
			topic.createName(type, "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics << characteristics";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	public void testCharacteristicsAxisWithParameterBW() throws Exception {
		Topic type = createTopicBySI("theType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value",
					new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics << characteristics theType";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	public void testScopeAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name n = topic.createName("Name", new Topic[0]);
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			n.addTheme(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics >> scope";
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

	public void testScopeAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Scoped[] scopeds = new Scoped[100];
		for (int i = 0; i < scopeds.length; i++) {
			if (i % 4 == 0) {
				scopeds[i] = createTopic().createName("Value", new Topic[0]);
			} else if (i % 4 == 1) {
				scopeds[i] = createTopic().createOccurrence(createTopic(),
						"Value", new Topic[0]);
			} else if (i % 4 == 2) {
				scopeds[i] = createTopic().createName("Value", new Topic[0])
						.createVariant("Value", createTopic());
			} else {
				scopeds[i] = createAssociation();
			}
			scopeds[i].addTheme(topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << scope";
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

	public void testLocatorsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectLocator(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> locators";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	public void testLocatorsAxisBW() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" << locators";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	public void testIndicatorsAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> indicators";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	public void testIndicatorsAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" << indicators";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	public void testItemAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addItemIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> item";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	public void testItemAxisBW() throws Exception {
		Topic topic = createTopicByII("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" << item";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	public void testReifierAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		/*
		 * name
		 */
		Name n = createTopic().createName("Value", new Topic[0]);
		n.setReifier(topic);
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> reifier";
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

		query = "myTopic >> reifier";
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

		query = "myTopic >> reifier";
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

		query = "myTopic >> reifier";
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

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Role);
		assertTrue(set.getResults().get(0).first().equals(r));
		r.setReifier(null);
		/*
		 * topic map
		 */
		topicMap.setReifier(topic);
		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof TopicMap);
		assertTrue(set.getResults().get(0).first().equals(topicMap));
	}

	public void testReifierAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName("Value", new Topic[0]).setReifier(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics << reifier";
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
		
		/*
		 * the topic map reifier
		 */
		Topic t = createTopic();
		topicMap.setReifier(t);
		query = "%_ << reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(t, set.first().first());
	}

	public void testAtomifyAxisForLocators() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> indicators >> atomify";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i].getReference()));
		}
	}

	public void testAtomifyAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics tm:name >> atomify";
		set = execute(query);
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

	public void testAtomifyAxisForOccurrence() throws Exception {
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

		query = "myTopic >> characteristics tm:occurrence >> atomify";
		set = execute(query);
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

	public void testAtomifyAxisLocatorsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" << atomify";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Locator);
		assertTrue(set.getResults().get(0).first().equals(
				topic.getSubjectIdentifiers().iterator().next()));
	}

	public void testAtomifyAxisNamesBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		for (int i = 0; i < names.length; i++) {
			query = "\"Value" + i + "\" << atomify";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Name);
			assertTrue(set.getResults().get(0).first().equals(names[i]));
		}
	}

	public void testAtomifyAxisOccurrenceBW() throws Exception {
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

		for (int i = 0; i < occurrences.length; i++) {
			query = "\"Value" + i + "\" << atomify";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
			assertTrue(set.getResults().get(0).first().equals(occurrences[i]));
		}
	}

}
