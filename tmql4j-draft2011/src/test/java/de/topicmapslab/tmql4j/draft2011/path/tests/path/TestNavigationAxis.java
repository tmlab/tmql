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

import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestNavigationAxis extends Tmql4JTestCase {

	@Test
	public void testInstancesAxis() throws Exception {
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
		}
		String query = null;
		SimpleResultSet set = null;

		query = "tm:subject / instances";
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
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.addType(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / types";
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
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createOccurrence(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / occurrences / types";
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
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names / types";
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
	public void testAxisInstances() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / instances";
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
		for (int i = 0; i < 100; i++) {
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

		query = "myTopic / typed";
		set = execute(query);
		assertEquals(typed.size(), set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(typed.contains(r.first()));
		}

		// query = "myTopic / typed tm:name ";
		// set = execute(query);
		// assertEquals(25, set.size());
		// for (IResult r : set.getResults()) {
		// assertEquals(1, r.size());
		// assertTrue(r.first() instanceof Name);
		// assertTrue(typed.contains(r.first()));
		// }
		//
		// query = "myTopic / typed tm:occurrence ";
		// set = execute(query);
		// assertEquals(25, set.size());
		// for (IResult r : set.getResults()) {
		// assertEquals(1, r.size());
		// assertTrue(r.first() instanceof Occurrence);
		// assertTrue(typed.contains(r.first()));
		// }
		//
		// query = "myTopic >> typed tm:association ";
		// set = execute(query);
		// assertEquals(25, set.size());
		// for (IResult r : set.getResults()) {
		// assertEquals(1, r.size());
		// assertTrue(r.first() instanceof Association);
		// assertTrue(typed.contains(r.first()));
		// }
		//
		// query = "myTopic >> typed tm:role ";
		// set = execute(query);
		// assertEquals(25, set.size());
		// for (IResult r : set.getResults()) {
		// assertEquals(1, r.size());
		// assertTrue(r.first() instanceof Role);
		// assertTrue(typed.contains(r.first()));
		// }
	}

	@Test
	public void testSupertypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topic, topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / supertypes";
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
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topics[i], topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / subtypes";
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
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			createAssociation(createTopic()).createRole(topic, topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / typed / players";
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

		query = "\"" + r.getId() + "\" / by-id / players";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(player, set.first().first());
	}

	@Test
	public void testPlayersAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(type);
			createAssociation(createTopic()).createRole(topic, topics[i]);
			createAssociation(createTopic()).createRole(topic, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / typed  / players myType";
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
	public void testAxisPlayedRoles() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Role[] roles = new Role[100];
		for (int i = 0; i < roles.length; i++) {
			Association a = createAssociation();
			roles[i] = a.createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / played-roles";
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
	public void testAxisPlayedRolesParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Role[] roles = new Role[100];
		for (int i = 0; i < roles.length; i++) {
			Association a = createAssociation();
			roles[i] = a.createRole(roleType, topic);
			a.createRole(createTopic(), topic);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / played-roles roleType";
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

		query = "\"" + a.getId() + "\" / by-id / role-types";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.get(0, 0));

		t.addType(createTopicBySI("myType"));

		query = "\"" + a.getId() + "\"  / by-id / role-types myType";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.get(0, 0));

		createTopicBySI("otherType");
		query = "\"" + a.getId() + "\" / by-id / role-types otherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testRoleTypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] types = new Topic[100];
		for (int i = 0; i < types.length; i++) {
			Topic t = createTopic();
			Association association = createAssociation(topic);
			association.createRole(t, createTopic());
			types[i] = t;
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / typed / role-types";
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
	public void testRolesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Role[] roles = new Role[100];
		for (int i = 0; i < roles.length; i++) {
			Topic t = createTopic();
			Association association = createAssociation(topic);
			Role r = association.createRole(createTopic(), t);
			roles[i] = r;
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / typed / roles";
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

		query = "\"" + a.getId() + "\" /  by-id / roles";
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

		query = "\"" + a.getId() + "\" / by-id / roles otherType";
		set = execute(query);
		assertEquals(0, set.size());

		query = "\"" + a.getId() + "\" / by-id / roles myType";
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
	public void testParentAxisForRoles() throws Exception {
		Topic type = createTopicBySI("myType");
		createTopicBySI("otherType");
		Association association = createAssociation(type);
		Role r = association.createRole(createTopic(), createTopic());
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + r.getId() + "\" / by-id / parent";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(association, set.get(0, 0));

		query = "\"" + r.getId() + "\" / by-id / parent myType";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(association, set.get(0, 0));

		query = "\"" + r.getId() + "\" / by-id / parent otherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
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

		query = "myTopic / traverse";
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

		query = "myTopic / traverse assoType";
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
	public void testNamesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createName("Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names";
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
	public void testNamesAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names theType";
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

		query = "myTopic / names theOtherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testOccurrencesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / occurrences";
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
	public void testOccurrencesAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[100];
		for (int i = 0; i < constructs.length; i++) {
			constructs[i] = topic.createOccurrence(type, "Value", new Topic[0]);
			topic.createOccurrence(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / occurrences theType";
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

		query = "myTopic / occurrences theOtherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testParentAxisForName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		for (int i = 0; i < 100; i++) {
			topic.createName(type, "Value");
			createTopic().createName("Value");
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names / parent";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testParentAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		for (int i = 0; i < 100; i++) {
			topic.createOccurrence(type, "Value");
			createTopic().createOccurrence(createTopic(), "Value");
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / occurrences / parent";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
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

		query = "\"" + n.getId() + "\" / by-id / scope";
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
	public void testScopedAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Scoped[] scopeds = new Scoped[100];
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

		query = "myTopic / scoped";
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
	public void testSubjectLocatorsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectLocator(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / subject-locators";
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
	public void testBySubjectLocator() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" / by-subject-locator";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testSubjectIdentifiersAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / subject-identifiers";
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
	public void testBySubjectIdentifier() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" / by-subject-identifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testItemIdentifiersAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addItemIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / item-identifiers";
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
	public void testByItemIdentifierAxis() throws Exception {
		Topic topic = createTopicByII("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = "\"" + base + "myTopic\" / by-item-identifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testReifiedAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		/*
		 * name
		 */
		Name n = createTopic().createName("Value", new Topic[0]);
		n.setReifier(topic);
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / reified";
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

		query = "myTopic / reified";
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

		query = "myTopic / reified";
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

		query = "myTopic / reified";
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

		query = "myTopic / reified";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Role);
		assertTrue(set.getResults().get(0).first().equals(r));
		r.setReifier(null);
		/*
		 * topic map
		 */
		topicMap.setReifier(topic);
		query = "myTopic / reified";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof TopicMap);
		assertTrue(set.getResults().get(0).first().equals(topicMap));
	}

	@Test
	public void testReifierAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName("Value", new Topic[0]).setReifier(topics[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names / reifier";
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
		query = "%_ / reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(t, set.first().first());
	}

	@Test
	public void testValueAxisForLocator() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[100];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / subject-identifiers / value";
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
	public void testValueAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / names / value";
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
	public void testValueAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
			topic.createName(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "myTopic / occurrences / value";
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
	public void testByValueAxisForNames() throws Exception {
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
			query = "\"Value" + i + "\" / by-value";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Name);
			assertTrue(set.getResults().get(0).first().equals(names[i]));
		}
	}

	@Test
	public void testByValueAxisOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
			topic.createName(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		for (int i = 0; i < occurrences.length; i++) {
			query = "\"Value" + i + "\" / by-value";
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
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.first().first());

		query = "\"" + id + "\" / by-id / id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for name
		id = n.getId();
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n, set.first().first());

		query = "\"" + id + "\" / by-id / id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for variant
		id = v.getId();
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v, set.first().first());

		query = "\"" + id + "\" / by-id / id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for occurrence
		id = o.getId();
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o, set.first().first());

		query = "\"" + id + "\" / by-id / id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for role
		id = r.getId();
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(r, set.first().first());

		query = "\"" + id + "\" / by-id / id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for association
		id = a.getId();
		query = "\"" + id + "\" / by-id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a, set.first().first());

		query = "\"" + id + "\" / by-id / id";
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

		query = "\"" + name.getId() + "\" / by-id / variants";
		set = execute(query);
		assertEquals(0, set.size());

		Set<Variant> variants = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			variants.add(name.createVariant("Var" + i, createTopic()));
		}
		query = "\"" + name.getId() + "\" / by-id / variants";
		set = execute(query);
		assertEquals(variants.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.get(0) instanceof IVariant);
			assertTrue(variants.contains(r.first()));
		}

		for (Variant v : variants) {
			query = "\"" + v.getId() + "\" / by-id / parent";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(name, set.first().first());
		}

		createTopicBySI("otherNameType");
		Topic nt = createTopicBySI("nameType");
		name.setType(nt);
		for (Variant v : variants) {
			query = "\"" + v.getId() + "\" / by-id / parent otherNameType";
			set = execute(query);
			assertEquals(0, set.size());

			query = "\"" + v.getId() + "\" / by-id / parent nameType";
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

		for (int i = 0; i < 100; i++) {
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

		assertEquals(50, topic.getOccurrences().size());
		assertEquals(50, n.getVariants().size());

		ILiteralIndex index = topicMap.getIndex(ILiteralIndex.class);
		index.open();

		assertEquals(50, index.getDatatypeAwares(xsdInt).size());
		assertEquals(50, index.getDatatypeAwares(xsdString).size());

		String query = null;
		SimpleResultSet set = null;
		/*
		 * test forward
		 */
		for (DatatypeAware d : strings) {
			query = "\"" + d.getId() + "\" / by-id / datatype / value ";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(Namespaces.XSD.STRING, set.get(0, 0));
		}
		for (DatatypeAware d : ints) {
			query = "\"" + d.getId() + "\" / by-id / datatype / value ";
			set = execute(query);
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(Namespaces.XSD.INT, set.get(0, 0));
		}
		/*
		 * test backward
		 */
		query = "\"" + Namespaces.XSD.STRING + "\" / datatyped";
		set = execute(query);
		assertEquals(50, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(strings.contains(r.first()));

		}

		query = "\"" + Namespaces.XSD.INT + "\" / datatyped";
		set = execute(query);
		assertEquals(50, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(ints.contains(r.first()));
		}

		/*
		 * test backward with type
		 */
		query = "\"" + Namespaces.XSD.STRING + "\" / datatyped myType";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(strings.contains(r.first()));

		}

		query = "\"" + Namespaces.XSD.INT + "\" / datatyped myType";
		set = execute(query);
		assertEquals(25, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(ints.contains(r.first()));
		}
	}

}
