/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestUpdateExpression extends Tmql4JTestCase {

	@Test
	public void testAddLocator() throws Exception {
		Topic topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getSubjectLocators().size());

		String query = " UPDATE locators ADD \"http://psi.example.org/loc\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getId(), set.first().first());
		assertEquals(topic.getId(), set.get(0,"topics"));
		assertEquals(1, topic.getSubjectLocators().size());
		assertEquals("http://psi.example.org/loc", topic.getSubjectLocators().iterator().next().getReference());

	}

	@Test
	public void testAddIndicator() throws Exception {
		Topic topic = createTopicBySL("myTopic");

		assertEquals(0, topic.getSubjectIdentifiers().size());

		String query = " UPDATE indicators ADD \"http://psi.example.org/loc\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getId(), set.first().first());
		assertEquals(topic.getId(), set.get(0,"topics"));
		assertEquals(1, topic.getSubjectIdentifiers().size());
		assertEquals("http://psi.example.org/loc", topic.getSubjectIdentifiers().iterator().next().getReference());
	}

	@Test
	public void testAddItemIdentifier() throws Exception {
		Topic topic = createTopicBySL("myTopic");

		assertEquals(0, topic.getItemIdentifiers().size());

		String query = " UPDATE item ADD \"http://psi.example.org/loc\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getId(), set.first().first());
		assertEquals(topic.getId(), set.get(0,"topics"));
		assertEquals(1, topic.getItemIdentifiers().size());
		assertEquals("http://psi.example.org/loc", topic.getItemIdentifiers().iterator().next().getReference());
	}

	@Test
	public void testAddNameWithoutType() throws Exception {
		Topic topic = createTopicBySL("myTopic");

		assertEquals(0, topic.getNames().size());

		String query = " UPDATE names ADD \"Name\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic.getId(), set.get(0,"topics"));
		
		assertEquals(1, topic.getNames().size());
		assertEquals(topic.getNames().iterator().next().getId(), set.get(0,"names"));
		
		assertEquals("Name", topic.getNames().iterator().next().getValue());
	}

	@Test
	public void testAddNameWithType() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");

		assertEquals(0, topic.getNames().size());

		String query = " UPDATE names myType ADD \"Name\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic.getId(), set.get(0,"topics"));
		
		assertEquals(1, topic.getNames().size());
		assertEquals(topic.getNames().iterator().next().getId(), set.get(0,"names"));
		assertEquals("Name", topic.getNames().iterator().next().getValue());
		assertEquals(type, topic.getNames().iterator().next().getType());
	}
	
	@Test
	public void testAddRemoveName() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Name name = topic.createName("Name");
		assertEquals(1, topic.getNames().size());

		String query = " UPDATE names REMOVE \"" + name.getId() + "\" << id WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic.getId(), set.get(0,"topics"));
		
		assertEquals(1, topic.getNames().size());
		assertEquals(topic.getNames().iterator().next().getId(), set.get(0,"names"));
		assertEquals("Name", topic.getNames().iterator().next().getValue());
		assertEquals(type, topic.getNames().iterator().next().getType());
	}

	@Test
	public void testSetNameValue() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		topic.createName("Name");
		assertEquals(1, topic.getNames().size());
		Name name = topic.getNames().iterator().next();
		assertEquals("Name", name.getValue());

		String query = " UPDATE names SET \"New Name\" WHERE myTopic >> characteristics tm:name ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals(1, topic.getNames().size());
		assertEquals("New Name", name.getValue());

		query = " UPDATE names SET \"\"\"\\\"Abra\\\"\"\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("\"Abra\"", name.getValue());

		query = " UPDATE names SET \"\\\"Abra\\\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("\"Abra\"", name.getValue());

		query = " UPDATE names SET \"Abr\\\"a\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abr\"a", name.getValue());

		query = " UPDATE names SET \"\"\"Abr\\\"a\"\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abr\"a", name.getValue());

		query = " UPDATE names SET \"Abra\\\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abra\"", name.getValue());

		query = " UPDATE names SET \"\"\"Abra\\\"\"\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abra\"", name.getValue());

		query = " UPDATE names SET \"Abra\\\\\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abra\\", name.getValue());

		query = " UPDATE names SET \"\"\"Abra\\\\\"\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("Abra\\", name.getValue());

		query = " UPDATE names SET \"\\\\\\\"Abra\\\\\\\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("\\\"Abra\\\"", name.getValue());

		query = " UPDATE names SET \"\"\"\\\\\\\"Abra\\\\\\\"\"\"\" WHERE \"" + name.getId() + "\" << id";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(name.getId(), set.first().first());
		assertEquals(name.getId(), set.get(0,"names"));
		assertEquals("\\\"Abra\\\"", name.getValue());
	}

	@Test
	public void testAddOccurrence() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");

		assertEquals(0, topic.getOccurrences().size());

		String query = " UPDATE occurrences myType ADD \"Value\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic.getId(), set.get(0,"topics"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));		
		assertEquals("Value", topic.getOccurrences().iterator().next().getValue());
		assertEquals(type, topic.getOccurrences().iterator().next().getType());
	}

	@Test
	public void testSetOccurrenceValue() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		topic.createOccurrence(createTopic(), "Value");
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Value", topic.getOccurrences().iterator().next().getValue());

		String query = " UPDATE occurrences SET \"New Value\" WHERE myTopic >> characteristics tm:occurrence ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("New Value", topic.getOccurrences().iterator().next().getValue());

		query = " UPDATE occurrences SET \"\"\"New\"'Value\"\"\" WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id  ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("New\"'Value", topic.getOccurrences().iterator().next().getValue());

		query = " UPDATE occurrences SET \"\"\"New\"'Value\"\"\"\" WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id  ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("New\"'Value\"", topic.getOccurrences().iterator().next().getValue());

		query = " UPDATE occurrences SET \"\"\"\\\"New\"'Value\"\"\"\" WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id  ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("\"New\"'Value\"", topic.getOccurrences().iterator().next().getValue());

		query = " UPDATE occurrences SET \"New Value\"^^" + XmlSchemeDatatypes.XSD_BOOLEAN + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("New Value", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_BOOLEAN, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"2'11\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("2'11", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"\"\"2'11\"\"\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("2'11", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"Abra\"\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Abra\"", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"\"\"Abra\"\"\"\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Abra\"", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"Abra\\\\\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Abra\\", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());

		query = " UPDATE occurrences SET \"\"\"Abra\\\\\"\"\"^^" + XmlSchemeDatatypes.XSD_STRING + " WHERE \"" + topic.getOccurrences().iterator().next().getId() + "\" << id ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Abra\\", topic.getOccurrences().iterator().next().getValue());
		assertEquals(XmlSchemeDatatypes.XSD_STRING, topic.getOccurrences().iterator().next().getDatatype().getReference());
	}

	@Test
	public void testAddScopeToOccurrence() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic theme = createTopicBySL("theme");
		topic.createOccurrence(createTopic(), "Value");
		assertEquals(1, topic.getOccurrences().size());
		assertEquals(0, topic.getOccurrences().iterator().next().getScope().size());

		String query = " UPDATE scope ADD theme WHERE myTopic >> characteristics tm:occurrence ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getOccurrences().iterator().next().getId(), set.get(0,"occurrences"));
		assertEquals(1, topic.getOccurrences().size());
		assertEquals(1, topic.getOccurrences().iterator().next().getScope().size());
		assertTrue(topic.getOccurrences().iterator().next().getScope().contains(theme));
	}

	@Test
	public void testAddScopeToName() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic theme = createTopicBySL("theme");
		topic.createName(createTopic(), "Value");
		assertEquals(1, topic.getNames().size());
		assertEquals(0, topic.getNames().iterator().next().getScope().size());

		String query = " UPDATE scope ADD theme WHERE myTopic >> characteristics tm:name ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getNames().iterator().next().getId(), set.get(0,"names"));
		assertEquals(1, topic.getNames().size());
		assertEquals(1, topic.getNames().iterator().next().getScope().size());
		assertTrue(topic.getNames().iterator().next().getScope().contains(theme));
	}

	@Test
	public void testAddScopeToAssociation() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		Topic theme = createTopicBySL("theme");
		Association a = createAssociation(type);
		a.createRole(createTopic(), topic);
		assertEquals(0, a.getScope().size());

		String query = " UPDATE scope ADD theme WHERE myType ( tm:subject : myTopic )";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a.getId(), set.get(0,"associations"));
		assertEquals(1, a.getScope().size());
		assertTrue(a.getScope().contains(theme));
	}

	@Test
	public void testAddTypeToTopic() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		assertEquals(0, topic.getTypes().size());

		String query = " UPDATE types ADD myType WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.get(0,"topics") instanceof Collection<?>);
		assertEquals(1,((Collection<?>)set.get(0,"topics")).size());
		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(type));
	}

	@Test
	public void testSetTypeToOccurrence() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		Topic other = createTopicBySL("other");
		Occurrence o = topic.createOccurrence(type, "Value");
		assertEquals(type, o.getType());

		String query = " UPDATE types SET other WHERE myTopic >> characteristics tm:occurrence ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getId(), set.get(0,"occurrences"));
		assertEquals(other, o.getType());
	}

	@Test
	public void testSetTypeToNames() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		Topic other = createTopicBySL("other");
		Name n = topic.createName(type, "Value");
		assertEquals(type, n.getType());

		String query = " UPDATE types SET other WHERE myTopic >> characteristics tm:name ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n.getId(), set.get(0,"names"));
		assertEquals(other, n.getType());
	}

	@Test
	public void testSetTypeToAssociation() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		Topic other = createTopicBySL("other");
		Association a = createAssociation(type);
		a.createRole(createTopic(), topic);
		assertEquals(type, a.getType());

		String query = " UPDATE types SET other WHERE myType ( tm:subject : myTopic )";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a.getId(), set.get(0,"associations"));
		assertEquals(other, a.getType());
	}

	@Test
	public void testAddInstance() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		assertEquals(0, topic.getTypes().size());

		String query = " UPDATE instances ADD myTopic WHERE myType ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.get(0,"topics") instanceof Collection<?>);
		assertEquals(1,((Collection<?>)set.get(0,"topics")).size());
		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(type));
	}

	@Test
	public void testAddSupertypes() throws Exception {
		Topic subtype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		Topic supertype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		assertEquals(0, topic.getRolesPlayed(subtype).size());

		String query = " UPDATE supertypes ADD myType WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.get(0,"topics") instanceof Collection<?>);
		assertEquals(1,((Collection<?>)set.get(0,"topics")).size());
		assertEquals(1, topic.getRolesPlayed(subtype).size());
		assertEquals(type, topic.getRolesPlayed(subtype).iterator().next().getParent().getRoles(supertype).iterator().next().getPlayer());
	}

	@Test
	public void testAddSubtypes() throws Exception {
		Topic subtype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		Topic supertype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		assertEquals(0, topic.getRolesPlayed(subtype).size());

		String query = " UPDATE subtypes ADD myTopic WHERE  myType";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.get(0,"topics") instanceof Collection<?>);
		assertEquals(1,((Collection<?>)set.get(0,"topics")).size());
		assertEquals(1, topic.getRolesPlayed(subtype).size());
		assertEquals(type, topic.getRolesPlayed(subtype).iterator().next().getParent().getRoles(supertype).iterator().next().getPlayer());
	}

	@Test
	public void testSetPlayers() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic other = createTopicBySL("other");
		Topic type = createTopicBySL("myType");
		Association a = createAssociation(type);
		Role r = a.createRole(createTopic(), topic);
		assertEquals(topic, r.getPlayer());

		String query = " UPDATE players SET other WHERE myTopic << players << roles";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.get(0,"roles") instanceof Collection<?>);
		assertEquals(other, r.getPlayer());
	}

	@Test
	public void testAddRoles() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic type = createTopicBySL("myType");
		Topic otherType = createTopicBySL("otherType");
		Topic other = createTopicBySL("other");
		Association a = createAssociation(type);
		Role r = a.createRole(createTopic(), topic);
		assertEquals(1, a.getRoles().size());
		assertEquals(0, a.getRoles(otherType).size());

		String query = " UPDATE roles other ADD otherType WHERE myType ( tm:subject : myTopic )";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(a.getId(),set.get(0,"associations"));
		assertNotNull(set.get(0,"roles"));
		Iterator<Role> it = a.getRoles(otherType).iterator();
		Role r2 = it.next();
		if ( r2.equals(r)){
			r2 = it.next();
		}
		assertEquals(r2.getId(),set.get(0,"roles"));
		assertEquals(2, a.getRoles().size());
		assertEquals(1, a.getRoles(otherType).size());
		assertEquals(other, a.getRoles(otherType).iterator().next().getPlayer());
	}

	@Test
	public void testSetReifier() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Topic reifier = createTopicBySL("myReifier");
		Topic type = createTopicBySL("myType");
		Association a = createAssociation(type);
		a.createRole(createTopic(), topic);
		Name n = topic.createName("Name");
		Occurrence o = topic.createOccurrence(createTopic(), "Value");

		String query = null;
		SimpleResultSet set = null;

		// for association
		assertNull(a.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET myReifier WHERE myType ( tm:subject : myTopic )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a.getId(), set.get(0, "associations"));
		assertEquals(reifier, a.getReifier());
		assertEquals(a, reifier.getReified());

		a.setReifier(null);
		assertNull(a.getReifier());
		assertNull(reifier.getReified());

		assertNull(a.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET myType ( tm:subject : myTopic ) WHERE myReifier";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a.getId(), set.get(0, "associations"));
		assertEquals(reifier, a.getReifier());
		assertEquals(a, reifier.getReified());

		a.setReifier(null);
		assertNull(a.getReifier());
		assertNull(reifier.getReified());

		// for name
		assertNull(n.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET myReifier WHERE myTopic >> characteristics tm:name ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n.getId(), set.get(0, "names"));
		assertEquals(reifier, n.getReifier());
		assertEquals(n, reifier.getReified());

		n.setReifier(null);
		assertNull(n.getReifier());
		assertNull(reifier.getReified());

		assertNull(n.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET  myTopic >> characteristics tm:name WHERE myReifier";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n.getId(), set.get(0, "names"));
		assertEquals(reifier, n.getReifier());
		assertEquals(n, reifier.getReified());

		n.setReifier(null);
		assertNull(n.getReifier());
		assertNull(reifier.getReified());

		// for occurrence
		assertNull(o.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET myReifier WHERE myTopic >> characteristics tm:occurrence ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getId(), set.get(0, "occurrences"));
		assertEquals(reifier, o.getReifier());
		assertEquals(o, reifier.getReified());

		o.setReifier(null);
		assertNull(o.getReifier());
		assertNull(reifier.getReified());

		assertNull(o.getReifier());
		assertNull(reifier.getReified());
		query = " UPDATE reifier SET myTopic >> characteristics tm:occurrence WHERE myReifier";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getId(), set.get(0, "occurrences"));
		assertEquals(reifier, o.getReifier());
		assertEquals(o, reifier.getReified());

		o.setReifier(null);
		assertNull(o.getReifier());
		assertNull(reifier.getReified());
	}

	@Test
	public void testMultipleUpdates() throws Exception {
		Topic topic = createTopicBySL("myTopic");

		assertEquals(0, topic.getSubjectIdentifiers().size());
		assertEquals(0, topic.getItemIdentifiers().size());

		String query = " UPDATE indicators ADD \"http://psi.example.org/loc\", item ADD \"http://psi.example.org/ii\" WHERE myTopic ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap, query));
		assertEquals(2, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic.getId(), set.get(0, "topics"));
		assertEquals(topic.getId(), set.get(1, "topics"));
		assertEquals(1, topic.getSubjectIdentifiers().size());
		assertEquals("http://psi.example.org/loc", topic.getSubjectIdentifiers().iterator().next().getReference());
		assertEquals(1, topic.getItemIdentifiers().size());
		assertEquals("http://psi.example.org/ii", topic.getItemIdentifiers().iterator().next().getReference());
	}
	
	@Test
	public void testRemoveTopic() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		
		Topic topicBySi = createTopicBySI("topicBySi");
		final String idTopicBySi = topicBySi.getId(); 
		Topic topicBySi2 = createTopicBySI("topicBySi2");
		final String idTopicBySi2 = topicBySi2.getId();
		Topic topicBySi3 = createTopicBySI("topicBySi3");
		final String idTopicBySi3 = topicBySi3.getId();
		Topic topicBySl = createTopicBySL("topicBySl");
		final String idTopicBySl = topicBySl.getId();
		Topic topicBySl2 = createTopicBySL("topicBySl2");
		final String idTopicBySl2 = topicBySl2.getId();
		Topic topicByIi = createTopicByII("topicByIi");
		final String idTopicByIi = topicByIi.getId();
		Topic topicByIi2 = createTopicByII("topicByIi2");
		final String idTopicByIi2 = topicByIi2.getId();
		int size = topicMap.getTopics().size();
		
		/*
		 * default identifier type
		 */
		query = " UPDATE topics REMOVE \"" + base + "topicBySi\"";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicBySi, set.get(0, IUpdateAlias.TOPICS));
		
		/*
		 * subject-identifier
		 */
		query = " UPDATE topics REMOVE \"" + base + "topicBySi2\" << indicators ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicBySi2, set.get(0, IUpdateAlias.TOPICS));
		
		query = " UPDATE topics REMOVE \"" + base + "topicBySi3\" ~ ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicBySi3, set.get(0, IUpdateAlias.TOPICS));
		
		/*
		 * subject-locator
		 */
		query = " UPDATE topics REMOVE \"" + base + "topicBySl\" << locators ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicBySl, set.get(0, IUpdateAlias.TOPICS));
		
		query = " UPDATE topics REMOVE \"" + base + "topicBySl2\" = ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicBySl2, set.get(0, IUpdateAlias.TOPICS));
		
		/*
		 * item-identifier
		 */
		query = " UPDATE topics REMOVE \"" + base + "topicByIi\" << item ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicByIi, set.get(0, IUpdateAlias.TOPICS));
		
		query = " UPDATE topics REMOVE \"" + base + "topicByIi2\" ! ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(--size, topicMap.getTopics().size());
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(idTopicByIi2, set.get(0, IUpdateAlias.TOPICS));
	}

	@Test
	public void testAddTopic() throws Exception {

		int size = topicMap.getTopics().size();

		String query = null;
		SimpleResultSet set = null;

		/*
		 * default identifier type
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicDefaultIdentifier\"";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/topicDefaultIdentifier")));

		/*
		 * canonical subject-identifier
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicSI\" << indicators";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/topicSI")));

		/*
		 * non-canonical subject-identifier
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicSINCL\" ~";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/topicSINCL")));

		/*
		 * canonical subject-locator
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicSL\" << locators";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap.createLocator("http://psi.example.org/topicSL")));

		/*
		 * non-canonical subject-locator
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicSLNCL\" =";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap.createLocator("http://psi.example.org/topicSLNCL")));

		/*
		 * canonical item-identifier
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicII\" << item";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap.createLocator("http://psi.example.org/topicII")));

		/*
		 * non-canonical item-identifier
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/topicIINCL\" !";
		set = execute(new TMQLQuery(topicMap, query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap.createLocator("http://psi.example.org/topicIINCL")));

		/*
		 * create multiple count of topics
		 */
		query = " UPDATE topics ADD \"http://psi.example.org/1\" ! , topics ADD \"http://psi.example.org/2\" = , topics ADD \"http://psi.example.org/3\" ~";
		set = execute(new TMQLQuery(topicMap, query));
		size += 3;

		assertEquals(3, set.size());		
		assertEquals(size, topicMap.getTopics().size());
		
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap.createLocator("http://psi.example.org/1")));
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap.createLocator("http://psi.example.org/2")));
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/3")));
	}

	@Test
	public void testAddAssociation() throws Exception {

		int size = topicMap.getAssociations().size();
		String query = null;

		query = "%prefix e http://psi.example.org/ UPDATE associations ADD e:association-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		IResultSet<?> rs = execute(query);		
		assertEquals(1,rs.size());
		assertEquals(3,rs.first().size());
		assertTrue(rs.get(0, "associations") instanceof String);
		assertTrue(rs.get(0, "roles") instanceof Collection);
		assertTrue(rs.get(0, "topics") instanceof Collection);
		assertEquals(2,((Collection<?>)rs.get(0, "roles")).size());
		assertEquals(5,((Collection<?>)rs.get(0, "topics")).size());
		
		size++;

		Topic associationType = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/association-type"));
		Topic roleType1 = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/role-type-1"));
		Topic player1 = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/player1"));
		Topic roleType2 = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/role-type-2"));
		Topic player2 = topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/player2"));

		assertNotNull(associationType);
		assertNotNull(roleType1);
		assertNotNull(player1);
		assertNotNull(roleType2);
		assertNotNull(player2);

		assertEquals(size, topicMap.getAssociations().size());
		Association association = topicMap.getAssociations().iterator().next();
		assertEquals(associationType, association.getType());
		assertEquals(2, association.getRoles().size());
		assertEquals(1, association.getRoles(roleType1).size());
		assertEquals(player1, association.getRoles(roleType1).iterator().next().getPlayer());
		assertEquals(1, association.getRoles(roleType2).size());
		assertEquals(player2, association.getRoles(roleType2).iterator().next().getPlayer());
	}

	@Test
	public void testAddAssociationWithWhere() throws Exception {
		Topic t = createTopicBySI("myTopic");
		String query = null;

		query = "UPDATE associations ADD e:association-type ( http://psi.example.org/role-type-1 : . ) WHERE myTopic ";
		IResultSet<?> rs = execute(query);	
		assertEquals(1, rs.size());
		
		assertEquals(1, topicMap.getAssociations().size());
		Association a = topicMap.getAssociations().iterator().next();
		assertEquals(1, a.getRoles().size());
		assertEquals(t, a.getRoles().iterator().next().getPlayer());
	}
}
