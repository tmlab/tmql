package de.topicmapslab.tmql4j.draft2010.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

/**
 * Test class for axis navigation
 * 
 * @author Sven Krosse
 * 
 */
public class AxisTest extends Tmql4JTestCase {

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAssociationAxis() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");

		Set<Association> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Association a = topicMap.createAssociation(type);
			a.createRole(createTopic(), topic);
			results.add(a);
		}

		query = " / association::myType  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / myType  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / association::  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTopicAxis() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		Topic type = createTopicBySI("myType");

		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(type);
			results.add(t);
		}

		query = " / topic::myType  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / myType  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic::  ";
		set = execute(query);
		int nr = results.size() + 1;
		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
			nr += 3;
		}
		results.add(type);
		assertEquals(nr, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDirectTypeAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			results.add(t);
			topic.addType(t);
			topic.createName(t, "Name");
			topic.createOccurrence(t, "Value");
			Association a = createAssociation(t);
			a.createRole(t, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / direct-type:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / name:: / direct-type:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / occurrence:: / direct-type:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		int count = 100;
		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			// 100-times the type-instance-type
			count += 100;
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
		}

		query = "  / association:: / direct-type:: ";
		set = execute(query);
		assertEquals(count, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			// 100-times the type and instance-type
			count += 100;
			results.remove(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
		}
		query = " / association:: / role:: / direct-type:: ";
		set = execute(query);
		assertEquals(count, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTypeAxis() throws Exception {
		Topic supertype = createTopicBySI("mySupertype");
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> results = HashUtil.getHashSet();
		results.add(supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			results.add(t);
			topic.addType(t);
			topic.createName(t, "Name");
			topic.createOccurrence(t, "Value");
			addSupertype(t, supertype);
			Association a = createAssociation(t);
			a.createRole(t, createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myTopic / type:: ";
		set = execute(query);
		assertEquals(200, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / name:: / type:: ";
		set = execute(query);
		assertEquals(200, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " myTopic / occurrence:: / type:: ";
		set = execute(query);
		assertEquals(200, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		int count = 200;

		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)));

			count += 200;
		}

		query = "  / association:: / type:: ";
		set = execute(query);
		assertEquals(count, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			results.remove(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			results.remove(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE)));
			results.add(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE)));

			count += 200;
		}
		query = " / association:: / role:: / type:: ";
		set = execute(query);
		assertEquals(count, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSIAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		createTopicBySL("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " / topic:: / subject-identifier::  ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getSubjectIdentifiers().iterator().next().getReference(), set.first().first());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSLAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		createTopicBySI("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " / topic:: / subject-locator::  ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getSubjectLocators().iterator().next().getReference(), set.first().first());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIIAxis() throws Exception {
		Topic topic = createTopicByII("myTopic");
		createTopicBySI("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " / topic:: / item-identifier::  ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getItemIdentifiers().iterator().next().getReference(), set.first().first());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNameAxis() throws Exception {
		Topic type = createTopicBySI("myType");

		Set<Name> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			results.add(type.createName("Name"));
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myType / name:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVariantAxis() throws Exception {
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");

		Set<Variant> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			results.add(n.createVariant("Name", createTopic()));
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myType / name:: / variant:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOccurrenceAxis() throws Exception {
		Topic type = createTopicBySI("myType");

		Set<Occurrence> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			results.add(type.createOccurrence(createTopic(), "Value"));
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myType / occurrence:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDatatypeAxis() throws Exception {
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		Variant v = n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), createTopic());
		Occurrence o = type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER), createTopic());

		String query = null;
		SimpleResultSet set = null;

		query = " myType / occurrence:: / datatype::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getDatatype().getReference(), set.first().first());

		query = " myType / name:: / variant:: / datatype::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v.getDatatype().getReference(), set.first().first());

	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInstanceAxis() throws Exception {
		Topic supertype = createTopicBySI("mySupertype");
		Topic type = createTopicBySI("myType");
		addSupertype(type, supertype);
		Topic other = createTopicBySI("other");
		addSupertype(other, supertype);

		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(type);
			} else {
				t.addType(other);
			}
			results.add(t);
		}
		String query = null;
		SimpleResultSet set = null;

		query = " mySupertype / instance:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDirectInstanceAxis() throws Exception {
		Topic type = createTopicBySI("myType");

		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(type);
			results.add(t);
		}
		String query = null;
		SimpleResultSet set = null;

		query = " myType / direct-instance:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValueAxis() throws Exception {
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		Variant v = n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), createTopic());
		Occurrence o = type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER), createTopic());

		String query = null;
		SimpleResultSet set = null;

		query = " myType / occurrence:: / value::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getValue(), set.first().first());

		query = " myType / name::  / value::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n.getValue(), set.first().first());

		query = " myType / name:: / variant:: / value::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v.getValue(), set.first().first());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParentAxis() throws Exception {
		Association a = createAssociation();
		a.createRole(createTopic(), createTopic());
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), createTopic());
		type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER), createTopic());

		String query = null;
		SimpleResultSet set = null;

		query = " myType / parent:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicMap, set.first().first());

		query = " / association::  / parent::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicMap, set.first().first());

		query = " / association::  / role:: / parent::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a, set.first().first());

		query = " myType / name:: / parent::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(type, set.first().first());

		query = " myType / occurrence:: / parent::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(type, set.first().first());

		query = " myType / name:: / variant:: / parent::";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n, set.first().first());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPlayerAxis() throws Exception {
		Topic assoType = createTopicBySI("assoType");
		Topic type = createTopicBySI("myType");
		Association a = createAssociation(assoType);
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(type);
			a.createRole(createTopic(), t);
			results.add(t);
		}

		String query = null;
		SimpleResultSet set = null;

		query = " / association::assoType / player:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / association::assoType / player::myType ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		int count = 100;
		if (factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			count += 200;
			results.add(type);
		}

		query = " / association:: / player:: ";
		set = execute(query);
		assertEquals(count, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRoleAxis() throws Exception {
		Topic assoType = createTopicBySI("assoType");
		Topic type = createTopicBySI("myType");
		Association a = createAssociation(assoType);
		Set<Role> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			results.add(a.createRole(type, t));
		}

		String query = null;
		SimpleResultSet set = null;

		query = " / association::assoType / role:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / association::assoType / role::myType ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / association:: / role:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScopeAxis() throws Exception {
		Topic otherTheme = createTopic();
		Association a = createAssociation();
		a.createRole(createTopic(), createTopic());
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		Variant v = n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), otherTheme);
		Occurrence o = type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER));
		Set<Topic> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			results.add(t);
			a.addTheme(t);
			n.addTheme(t);
			o.addTheme(t);
			v.addTheme(t);
		}

		String query = null;
		SimpleResultSet set = null;

		query = " / association:: / scope:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic:: / name:: / scope:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		query = " / topic:: / occurrence:: / scope:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}

		results.add(otherTheme);
		query = " / topic:: / name:: / variant:: / scope:: ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScopedAxis() throws Exception {
		Topic otherTheme = createTopicBySI("theme");
		Topic topic = createTopic();
		Name n = topic.createName("Name");
		Set<Scoped> results = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			if (i % 4 == 0) {
				results.add(topicMap.createAssociation(createTopic(), otherTheme));
			} else if (i % 4 == 1) {
				results.add(topic.createName("Name", otherTheme));
			} else if (i % 4 == 2) {
				results.add(topic.createOccurrence(createTopic(), "Occ", otherTheme));
			} else {
				results.add(n.createVariant("Variant", otherTheme));
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " theme / scoped::  ";
		set = execute(query);
		assertEquals(results.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(results.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReifierAxis() throws Exception {
		Topic reifier = createTopic();
		Association a = createAssociation();
		Role r = a.createRole(createTopic(), createTopic());
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		Variant v = n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), createTopic());
		Occurrence o = type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER));

		String query = null;
		SimpleResultSet set = null;

		topicMap.setReifier(reifier);
		query = " / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		topicMap.setReifier(null);

		a.setReifier(reifier);
		query = " / association:: / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		a.setReifier(null);

		r.setReifier(reifier);
		query = " / association:: / role:: / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		r.setReifier(null);

		n.setReifier(reifier);
		query = " / topic:: / name:: / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		n.setReifier(null);

		o.setReifier(reifier);
		query = " / topic:: / occurrence:: / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		o.setReifier(null);

		v.setReifier(reifier);
		query = " / topic:: / name:: / variant:: / reifier:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(reifier, set.first().first());
		v.setReifier(null);
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReifiedAxis() throws Exception {
		Topic reifier = createTopicBySI("myReifier");
		Association a = createAssociation();
		Role r = a.createRole(createTopic(), createTopic());
		Topic type = createTopicBySI("myType");
		Name n = type.createName("Name");
		Variant v = n.createVariant("Variant", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), createTopic());
		Occurrence o = type.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER));

		String query = null;
		SimpleResultSet set = null;

		topicMap.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicMap, set.first().first());
		topicMap.setReifier(null);

		a.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a, set.first().first());
		a.setReifier(null);

		r.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(r, set.first().first());
		r.setReifier(null);

		n.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n, set.first().first());
		n.setReifier(null);

		o.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o, set.first().first());
		o.setReifier(null);

		v.setReifier(reifier);
		query = " myReifier / reified:: ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v, set.first().first());
		v.setReifier(null);
	}
}
