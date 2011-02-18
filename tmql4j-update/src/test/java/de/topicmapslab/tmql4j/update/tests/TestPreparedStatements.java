/*
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

import org.junit.Test;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

/**
 * @author Sven Krosse
 * 
 */
public class TestPreparedStatements extends Tmql4JTestCase {

	@Test
	public void testAddTopic() throws Exception {

		int size = topicMap.getTopics().size();
		IResultSet<?> set = null;

		/*
		 * default identifier type
		 */
		String reference = "http://psi.example.org/topicDefaultIdentifier";
		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE topics ADD ?");
		stmt.setTopicMap(topicMap);
		stmt.run(reference);
		set = stmt.getResults();
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/topicDefaultIdentifier")));

		/*
		 * canonical subject-identifier
		 */
		reference = "http://psi.example.org/topicSI";
		stmt = runtime.preparedStatement(" UPDATE topics ADD ? << indicators");
		stmt.setTopicMap(topicMap);
		stmt.run(reference);
		set = stmt.getResults();
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator("http://psi.example.org/topicSI")));

		/*
		 * canonical subject-identifier
		 */
		reference = "http://psi.example.org/topicSL";
		stmt = runtime.preparedStatement(" UPDATE topics ADD ? << locators");
		stmt.setTopicMap(topicMap);
		stmt.run(reference);
		set = stmt.getResults();
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap.createLocator("http://psi.example.org/topicSL")));

		/*
		 * canonical ii-identifier
		 */
		reference = "http://psi.example.org/topicII";
		stmt = runtime.preparedStatement(" UPDATE topics ADD ? << item");
		stmt.setTopicMap(topicMap);
		stmt.run(reference);
		set = stmt.getResults();
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap.createLocator("http://psi.example.org/topicII")));
	}

	@Test
	public void testAddAssociation() throws Exception {

		Topic at = createTopicBySI("atype");
		Topic rt = createTopicBySI("rtype");
		Topic player = createTopicBySI("player");

		assertEquals(0, topicMap.getAssociations().size());

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE associations ADD ? ( ? : ? )");
		stmt.setTopicMap(topicMap);
		stmt.run(at, rt, player);

		assertEquals(1, topicMap.getAssociations().size());
	}

	@Test
	public void testAddAssociation2() throws Exception {

		Topic at = createTopicBySI("atype");
		Topic rt = createTopicBySI("rtype");
		String player = base + "player";

		assertEquals(0, topicMap.getAssociations().size());
		assertEquals(2, topicMap.getTopics().size());

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE associations ADD ? ( ? : ? << indicators )");
		stmt.setTopicMap(topicMap);
		stmt.run(at, rt, player);

		assertEquals(1, topicMap.getAssociations().size());
		assertEquals(3, topicMap.getTopics().size());
	}

	@Test
	public void testAddAssociation3() throws Exception {

		String at = base + "atype";
		Topic rt = createTopicBySI("rtype");
		String player = base + "player";

		assertEquals(0, topicMap.getAssociations().size());
		assertEquals(1, topicMap.getTopics().size());

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE associations ADD ? ( ? : ? << indicators )");
		stmt.setTopicMap(topicMap);
		stmt.run(at, rt, player);

		assertEquals(1, topicMap.getAssociations().size());
		assertEquals(3, topicMap.getTopics().size());
	}

	@Test()
	public void testAddAssociationErr() throws Exception {

		String at = base + "atype";
		Topic rt = createTopicBySI("rtype");
		String player = base + "player";

		assertEquals(0, topicMap.getAssociations().size());
		assertEquals(1, topicMap.getTopics().size());

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE associations ADD ? ( ? : ? )");
		stmt.setTopicMap(topicMap);
		stmt.run(at, rt, player);

		assertEquals(1, topicMap.getAssociations().size());
		assertEquals(3, topicMap.getTopics().size());
	}

	@Test()
	public void testAddName() throws Exception {
		Topic topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getNames().size());
		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE names ADD ? WHERE ?");
		stmt.setTopicMap(topicMap);
		stmt.run("Name", topic);

		assertEquals(1, topic.getNames().size());
		assertEquals("Name", topic.getNames().iterator().next().getValue());

		topic.remove();

		Topic type = createTopicBySI("myType");
		topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getNames().size());
		stmt = runtime.preparedStatement(" UPDATE names ? ADD ? WHERE ?");
		stmt.setTopicMap(topicMap);
		stmt.run(type, "Name", topic);

		assertEquals(1, topic.getNames().size());
		assertEquals("Name", topic.getNames().iterator().next().getValue());
		assertEquals(type, topic.getNames().iterator().next().getType());

		topic.remove();
		topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getNames().size());
		stmt = runtime.preparedStatement(" UPDATE names ? ADD ? WHERE ?");
		stmt.setTopicMap(topicMap);
		stmt.run(base + "myType", "Name", topic);

		assertEquals(1, topic.getNames().size());
		assertEquals("Name", topic.getNames().iterator().next().getValue());
		assertEquals(type, topic.getNames().iterator().next().getType());

		topic.remove();
		type.remove();
		topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getNames().size());
		stmt = runtime.preparedStatement(" UPDATE names ? ADD ? WHERE ?");
		stmt.setTopicMap(topicMap);
		stmt.run(base + "myType", "Name", topic);

		assertEquals(1, topic.getNames().size());
		assertEquals("Name", topic.getNames().iterator().next().getValue());

		topic.remove();
		topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getOccurrences().size());
		stmt = runtime.preparedStatement(" UPDATE occurrences ? ADD ? WHERE ?");
		stmt.setTopicMap(topicMap);
		stmt.run(base + "myType2", "Name", topic);

		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Name", topic.getOccurrences().iterator().next().getValue());
		assertEquals(topicMap.getTopicBySubjectIdentifier(createLocator("myType2")), topic.getOccurrences().iterator().next().getType());

		topic.remove();
		topic = createTopicBySI("myTopic");

		assertEquals(0, topic.getOccurrences().size());
		IPreparedStatement stmt2 = runtime.preparedStatement(" UPDATE occurrences " + base + "myType3 ADD ? WHERE ?");
		stmt2.setTopicMap(topicMap);
		stmt2.run("Name", topic);

		assertEquals(1, topic.getOccurrences().size());
		assertEquals("Name", topic.getOccurrences().iterator().next().getValue());
		assertEquals(topicMap.getTopicBySubjectIdentifier(createLocator("myType3")), topic.getOccurrences().iterator().next().getType());

	}

	@Test()
	public void testAddOccurrence() throws Exception {

		Topic t = createTopicBySI("myTopic");
		Topic ty = createTopicBySI("myType");

		assertEquals(0, t.getOccurrences().size());

		final String value = "2011-01-31^^xsd:dateTime";

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE occurrences myType ADD ? WHERE myTopic");
		stmt.setTopicMap(topicMap);
		stmt.run(value);

		assertEquals(1, t.getOccurrences().size());
		Occurrence o = t.getOccurrences().iterator().next();
		assertEquals("2011-01-31", o.getValue());

		assertEquals(ty, o.getType());
		assertEquals(XmlSchemeDatatypes.XSD_DATETIME, o.getDatatype().getReference());
	}

	@Test()
	public void testAddOccurrence2() throws Exception {

		Topic t = createTopicBySI("myTopic");
		Topic ty = createTopicBySI("myType");

		assertEquals(0, t.getOccurrences().size());

		final String value = "2011-01-31";

		IPreparedStatement stmt = runtime.preparedStatement(" UPDATE occurrences myType ADD ? ^^xsd:dateTime WHERE myTopic");
		stmt.setTopicMap(topicMap);
		stmt.run(value);

		assertEquals(1, t.getOccurrences().size());
		Occurrence o = t.getOccurrences().iterator().next();
		assertEquals("2011-01-31", o.getValue());

		assertEquals(ty, o.getType());
		assertEquals(XmlSchemeDatatypes.XSD_DATETIME, o.getDatatype().getReference());
	}
}
