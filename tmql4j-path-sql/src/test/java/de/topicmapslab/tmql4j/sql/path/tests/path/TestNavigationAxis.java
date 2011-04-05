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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestNavigationAxis extends Tmql4JTestCase {

	@Test
	public void testInstancesAxis() throws Exception {
		Topic[] topics = new Topic[10];
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

		for (Topic topic : topics) {
			assertTrue(result.contains(topic));
		}
	}

	@Test
	public void testTypesAxisForTopic() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTypesAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTypesAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTypesAxisForTopicsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTypedAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Typed> typed = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) {
				typed.add(createAssociation(topic));
			} else if (i % 4 == 1) {
				typed.add(createTopic().createName(topic, "Name"));
			} else if (i % 4 == 2) {
				typed.add(createTopic().createOccurrence(topic, "Value"));
			} else {
				typed.add(createAssociation().createRole(topic, createTopic()));
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> typed";
		set = execute(query);
		assertEquals(typed.size(), set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(typed.contains(r.first()));
		}
	}

	@Test
	public void testTypedAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Typed> typed = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) {
				typed.add(createAssociation(topic));
			} else if (i % 4 == 1) {
				typed.add(createTopic().createName(topic, "Name"));
			} else if (i % 4 == 2) {
				typed.add(createTopic().createOccurrence(topic, "Value"));
			} else {
				typed.add(createAssociation().createRole(topic, createTopic()));
			}
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> typed << typed";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testInstancesAxisForTopics() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testSupertypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testSupertypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testSubtypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testSubtypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testPlayersAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testPlayersAxisForRole() throws Exception {
		Topic topic = createTopicBySI("myTopic");

		Association a = createAssociation(topic);
		Topic player = createTopic();
		Role r = a.createRole(createTopic(), player);
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + r.getId() + "\" << id >> players";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(player, set.first().first());
	}

	@Test
	public void testPlayersAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(type);
			createAssociation(topic).createRole(createTopic(), topics[i]);
			createAssociation(topic).createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> players myType";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testPlayersAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Role[] roles = new Role[10];
		for (int i = 0; i < roles.length; i++) {
			Association a = createAssociation();
			roles[i] = a.createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << players";
		set = execute(query);
		assertEquals(roles.length, set.size());

		Set<Role> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}
	}

	@Test
	public void testPlayersAxisWithParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Role[] roles = new Role[10];
		for (int i = 0; i < roles.length; i++) {
			Association a = createAssociation();
			roles[i] = a.createRole(roleType, topic);
			a.createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic << players roleType";
		set = execute(query);
		assertEquals(roles.length, set.size());

		Set<Role> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}
	}

	@Test
	public void testRoleTypesAxisForAssociation() throws Exception {
		Association a = createAssociation();
		Topic t = createTopic();
		a.createRole(t, createTopic());

		String query = null;
		SimpleResultSet set = null;

		query = "\"" + a.getId() + "\" << id >> roletypes";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.get(0, 0));

		t.addType(createTopicBySI("myType"));

		query = "\"" + a.getId() + "\" << id >> roletypes myType";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.get(0, 0));

		createTopicBySI("otherType");
		query = "\"" + a.getId() + "\" << id >> roletypes otherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testRoleTypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] types = new Topic[10];
		for (int i = 0; i < types.length; i++) {
			Topic t = createTopic();
			Association association = createAssociation(topic);
			association.createRole(t, createTopic());
			types[i] = t;
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> roletypes";
		set = execute(query);
		assertEquals(types.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (Topic type : types) {
			assertTrue(result.contains(type));
		}
	}

	@Test
	public void testRoleTypesAxisBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Topic type = createTopicBySI("myType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation(type);
			associations[i].createRole(roleType, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "roleType << roletypes";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (Association association : associations) {
			assertTrue(result.contains(association));
		}
		query = "roleType << roletypes myType";
		set = execute(query);
		assertEquals(associations.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (Association association : associations) {
			assertTrue(result.contains(association));
		}

		createTopicBySI("otherType");
		query = "roleType << roletypes otherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testRolesAxisForAssociation() throws Exception {
		Topic type = createTopicBySI("myType");
		createTopicBySI("otherType");
		Association a = createAssociation();
		Role[] roles = new Role[10];
		for (int i = 0; i < roles.length; i++) {
			Topic t = createTopic();
			Role r = a.createRole(type, t);
			roles[i] = r;
		}

		String query = null;
		SimpleResultSet set = null;

		query = "\"" + a.getId() + "\" << id >> roles";
		set = execute(query);
		assertEquals(roles.length, set.size());

		Set<Role> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}

		query = "\"" + a.getId() + "\" << id >> roles otherType";
		set = execute(query);
		assertEquals(0, set.size());

		query = "\"" + a.getId() + "\" << id >> roles myType";
		set = execute(query);
		assertEquals(roles.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}
	}

	@Test
	public void testRolesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		createTopicBySI("otherType");
		Role[] roles = new Role[10];
		for (int i = 0; i < roles.length; i++) {
			Topic t = createTopic();
			Association association = createAssociation(topic);
			Role r = association.createRole(type, t);
			roles[i] = r;
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> roles";
		set = execute(query);
		assertEquals(roles.length, set.size());

		Set<Role> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}

		query = "myTopic >> roles otherType";
		set = execute(query);
		assertEquals(0, set.size());

		query = "myTopic >> roles myType";
		set = execute(query);
		assertEquals(roles.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Role);
			result.add((Role) r.first());
		}

		for (Role role : roles) {
			assertTrue(result.contains(role));
		}
	}

	@Test
	public void testRolesAxisWithoutParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Association[] associations = new Association[10];
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

		for (Association association : associations) {
			assertTrue(result.contains(association));
		}
	}

	@Test
	public void testRolesAxisBWForRoles() throws Exception {
		Topic type = createTopicBySI("myType");
		createTopicBySI("otherType");
		Association association = createAssociation(type);
		Role r = association.createRole(createTopic(), createTopic());
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + r.getId() + "\" << id << roles";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(association, set.get(0, 0));

		query = "\"" + r.getId() + "\" << id << roles myType";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(association, set.get(0, 0));

		query = "\"" + r.getId() + "\" << id << roles otherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testRolesAxisWithParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Topic topic = createTopicBySI("myTopic");
		Association[] associations = new Association[10];
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

		for (Association association : associations) {
			assertTrue(result.contains(association));
		}
	}

	@Test
	public void testTraverseAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTraverseAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testTraverseAxisWithoutParameterBW() throws Exception {
		Topic type = createTopicBySI("assoType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			Topic t = createTopic();
			Association a = createAssociation(type);
			a.createRole(createTopic(), t);

			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), t);
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

		for (Association association : associations) {
			assertTrue(result.contains(association));
		}
	}

	@Test
	public void testTraverseAxisWithParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Set<Association> associations = new HashSet<Association>();
		for (int i = 0; i < 10; i++) {
			Topic player = createTopic();
			player.addType(topic);
			Association a = createAssociation(type);
			a.createRole(createTopic(), player);
			Association association = createAssociation();
			association.createRole(createTopic(), player);
			associations.add(association);
		}

		/*
		 * add type-instance-associations
		 */
		if (topicMapSystem.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			Topic t = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(Namespaces.TMDM.TYPE_INSTANCE));
			associations.addAll(((ITopicMap) topicMap).getAssociations(t));
		}

		String query = null;
		SimpleResultSet set = null;

		query = "assoType << traverse myTopic";
		set = execute(query);
		/*
		 * delta added to ignore the type-instance association if exists
		 */
		assertEquals(associations.size(), set.size());

		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			assertTrue(associations.contains(r.first()));
		}
	}

	@Test
	public void testCharacteristicsAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Construct[] constructs = new Construct[10];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName("Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
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

		for (Construct construct : constructs) {
			assertTrue(result.contains(construct));
		}
	}

	@Test
	public void testCharacteristicsAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[10];
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

		query = "myTopic >> characteristics theType";
		set = execute(query);
		assertEquals(constructs.length, set.size());

		Set<Construct> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Construct);
			result.add((Construct) r.first());
		}

		for (Construct construct : constructs) {
			assertTrue(result.contains(construct));
		}

		query = "myTopic >> characteristics theOtherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testCharacteristicsAxisWithTmName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
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

		for (Name name : names) {
			assertTrue(result.contains(name));
		}
	}

	@Test
	public void testCharacteristicsAxisWithTmOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value", new Topic[0]);
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

		for (Occurrence occurrence : occurrences) {
			assertTrue(result.contains(occurrence));
		}
	}

	@Test
	public void testCharacteristicsAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value", new Topic[0]);
			topic.createName(type, "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics << characteristics";
		set = execute(query);
		assertEquals(20, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testCharacteristicsAxisWithParameterBW() throws Exception {
		Topic type = createTopicBySI("theType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic >> characteristics << characteristics theType";
		set = execute(query);
		assertEquals(20, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testScopeAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name n = topic.createName("Name", new Topic[0]);
		Topic[] topics = new Topic[10];
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
		}
	}

	@Test
	public void testScopeAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Scoped[] scopeds = new Scoped[10];
		for (int i = 0; i < scopeds.length; i++) {
			if (i % 4 == 0) {
				scopeds[i] = createTopic().createName("Value", new Topic[0]);
			} else if (i % 4 == 1) {
				scopeds[i] = createTopic().createOccurrence(createTopic(), "Value", new Topic[0]);
			} else if (i % 4 == 2) {
				scopeds[i] = createTopic().createName("Value", new Topic[0]).createVariant("Value", createTopic());
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

		for (Scoped scoped : scopeds) {
			assertTrue(result.contains(scoped));
		}
	}

	@Test
	public void testLocatorsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Locator[] locators = new Locator[10];
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

		for (Locator locator : locators) {
			assertTrue(result.contains(locator));
		}
	}

	@Test
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

	@Test
	public void testIndicatorsAxis() throws Exception {
		Locator[] locators = new Locator[10];
		locators[0] = createLocator(Integer.toString(0));
		Topic topic = topicMap.createTopicBySubjectIdentifier(locators[0]);
		for (int i = 1; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = base + "0 >> indicators";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (Locator locator : locators) {
			assertTrue(result.contains(locator));
		}
	}

	@Test
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

	@Test
	public void testItemAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Locator[] locators = new Locator[10];
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

		for (Locator locator : locators) {
			assertTrue(result.contains(locator));
		}
	}

	@Test
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

	@Test
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
		Occurrence o = createTopic().createOccurrence(createTopic(), "Value", new Topic[0]);
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
		Variant v = createTopic().createName("Value", new Topic[0]).createVariant("Value", createTopic());
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

	@Test
	public void testReifierAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName("Value" + i, new Topic[0]).setReifier(topics[i]);
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

		for (Topic topic2 : topics) {
			assertTrue(result.contains(topic2));
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

	@Test
	public void testAtomifyAxisForLocators() throws Exception {
		Locator[] locators = new Locator[10];
		locators[0] = createLocator(Integer.toString(0));
		Topic topic = topicMap.createTopicBySubjectIdentifier(locators[0]);
		for (int i = 1; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = base + "0 >> indicators >> atomify";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (Locator locator : locators) {
			assertTrue(result.contains(locator.getReference()));
		}
	}

	@Test
	public void testAtomifyAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
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

		for (Name name : names) {
			assertTrue(result.contains(name.getValue()));
		}
	}

	@Test
	public void testAtomifyAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
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

		for (Occurrence occurrence : occurrences) {
			assertTrue(result.contains(occurrence.getValue()));
		}
	}

	@Test
	public void testAtomifyAxisLocatorsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" << atomify";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Locator);
		assertTrue(set.getResults().get(0).first().equals(topic.getSubjectIdentifiers().iterator().next()));
	}

	@Test
	public void testAtomifyAxisNamesBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
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

	@Test
	public void testAtomifyAxisOccurrenceBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
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

	@Test
	public void testIdAxis() throws Exception {
		Topic t = createTopic();
		Name n = t.createName("Name");
		Variant v = n.createVariant("Value", createTopic());
		Occurrence o = t.createOccurrence(createTopic(), "Val");
		Association a = createAssociation();
		Role r = a.createRole(createTopic(), createTopic());

		String query = null;
		SimpleResultSet set = null;

		// for topic
		String id = t.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for name
		id = n.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for variant
		id = v.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for occurrence
		id = o.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for role
		id = r.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(r, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for association
		id = a.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());
	}

	@Test
	public void testVariantsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name name = topic.createName("name");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + name.getId() + "\" << id >> variants";
		set = execute(query);
		assertEquals(0, set.size());

		Set<Variant> variants = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			variants.add(name.createVariant("Var" + i, createTopic()));
		}
		query = "\"" + name.getId() + "\" << id >> variants";
		set = execute(query);
		assertEquals(variants.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.get(0) instanceof IVariant);
			assertTrue(variants.contains(r.first()));
		}

		for (Variant v : variants) {
			query = "\"" + v.getId() + "\" << id << variants";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(name, set.first().first());
		}

		createTopicBySI("otherNameType");
		Topic nt = createTopicBySI("nameType");
		name.setType(nt);
		for (Variant v : variants) {
			query = "\"" + v.getId() + "\" << id << variants otherNameType";
			set = execute(query);
			assertEquals(0, set.size());

			query = "\"" + v.getId() + "\" << id << variants nameType";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(name, set.first().first());
		}
	}

	@Test
	public void testDatatypeAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Name n = topic.createName("Name");
		Locator xsdString = topicMap.createLocator(Namespaces.XSD.STRING);
		Locator xsdInt = topicMap.createLocator(Namespaces.XSD.INT);
		Set<DatatypeAware> strings = new HashSet<DatatypeAware>();
		Set<DatatypeAware> ints = new HashSet<DatatypeAware>();

		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) {
				strings.add(topic.createOccurrence(type, "Value" + i, xsdString));
			} else if (i % 4 == 1) {
				ints.add(topic.createOccurrence(type, Integer.toString(i), xsdInt));
			} else if (i % 4 == 2) {
				strings.add(n.createVariant("Variant" + i, xsdString, createTopic()));
			} else if (i % 4 == 3) {
				ints.add(n.createVariant(Integer.toString(i), xsdInt, createTopic()));
			}
		}

		assertEquals(6, topic.getOccurrences().size());
		assertEquals(4, n.getVariants().size());

		ILiteralIndex index = topicMap.getIndex(ILiteralIndex.class);
		index.open();

		assertEquals(5, index.getDatatypeAwares(xsdInt).size());
		assertEquals(5, index.getDatatypeAwares(xsdString).size());

		String query = null;
		SimpleResultSet set = null;
		/*
		 * test forward
		 */
		for (DatatypeAware d : strings) {
			query = "\"" + d.getId() + "\" << id >> datatype >> atomify ";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(Namespaces.XSD.STRING, set.get(0, 0));
		}
		for (DatatypeAware d : ints) {
			query = "\"" + d.getId() + "\" << id >> datatype >> atomify ";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(Namespaces.XSD.INT, set.get(0, 0));
		}
		/*
		 * test backward
		 */
		query = "\"" + Namespaces.XSD.STRING + "\" << datatype";
		set = execute(query);
		assertEquals(5, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(strings.contains(r.first()));

		}

		query = "\"" + Namespaces.XSD.INT + "\" << datatype";
		set = execute(query);
		assertEquals(5, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(ints.contains(r.first()));
		}

		/*
		 * test backward with type
		 */
		query = "\"" + Namespaces.XSD.STRING + "\" << datatype myType";
		set = execute(query);
		assertEquals(3, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(strings.contains(r.first()));

		}

		query = "\"" + Namespaces.XSD.INT + "\" << datatype myType";
		set = execute(query);
		assertEquals(3, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(ints.contains(r.first()));
		}
	}

}
