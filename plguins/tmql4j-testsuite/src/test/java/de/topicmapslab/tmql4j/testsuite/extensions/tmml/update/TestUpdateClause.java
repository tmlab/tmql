/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.update;

import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.XmlSchemeDatatypes;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestUpdateClause extends Tmql4JTestCase {

	public void testUpdateClauseWithVariableContext() throws Exception {
		Topic topic = createTopicBySI("type");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopicBySI("value" + i);
		}

		String query = "UPDATE $t occurrences type ADD $l >> atomify + \"value\" WHERE { FOR $t IN // tm:subject RETURN { FOR $l IN $t >> indicators WHERE $l >> atomify =~ \""
				+ base + ".*\" RETURN $t, $l } }";
		execute(query);

		for (int i = 0; i < topics.length; i++) {
			assertEquals(1, topics[i].getOccurrences().size());
			assertEquals(topic, topics[i].getOccurrences().iterator().next()
					.getType());
			assertTrue(topics[i].getOccurrences().iterator().next().getValue()
					.equalsIgnoreCase(base + "value" + i + "value"));
		}
	}

	public void testUpdateClauseWithoutVariableContext() throws Exception {
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopicBySI("value" + i);
		}

		String query = "UPDATE occurrences ADD \"value\" WHERE // tm:subject";
		execute(query);

		for (int i = 0; i < topics.length; i++) {
			assertEquals(1, topics[i].getOccurrences().size());
			assertTrue(topics[i].getOccurrences().iterator().next().getValue()
					.equalsIgnoreCase("value"));
		}
	}

	public void testUpdateClauseWithVariableContextMultipleClause()
			throws Exception {
		String value = "value";
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopicBySI("value" + i);
		}

		String query = null;

		query = "UPDATE $t occurrences ADD $l >> atomify + \"value\", $t names ADD $l >> atomify + \"value\" WHERE { FOR $t IN // tm:subject RETURN { FOR $l IN $t >> indicators WHERE $l >> atomify =~ \""
				+ base + ".*\" RETURN $t, $l } }";
		execute(query);

		for (int i = 0; i < topics.length; i++) {
			assertEquals(1, topics[i].getOccurrences().size());
			assertTrue(topics[i].getOccurrences().iterator().next().getValue()
					.equalsIgnoreCase(base + value + i + "value"));
			assertEquals(1, topics[i].getNames().size());
			assertTrue(topics[i].getNames().iterator().next().getValue()
					.equalsIgnoreCase(base + value + i + "value"));
		}
	}

	public void testUpdateClauseWithDatatype() throws Exception {
		Topic topic = createTopicBySI("type");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopicBySI("value" + i);
		}

		String query = "UPDATE $t occurrences type ADD \"50\" WHERE { FOR $t IN // tm:subject RETURN { FOR $l IN $t >> indicators WHERE $l >> atomify =~ \""
				+ base + ".*\" RETURN $t, $l } }";
		System.out.println(execute(query));

		for (int i = 0; i < topics.length; i++) {
			assertEquals(1, topics[i].getOccurrences().size());
			assertEquals(topic, topics[i].getOccurrences().iterator().next()
					.getType());
			assertTrue(topics[i].getOccurrences().iterator().next().getValue()
					.equalsIgnoreCase("50"));
			assertTrue(topics[i].getOccurrences().iterator().next()
					.getDatatype().getReference().equalsIgnoreCase(
							XmlSchemeDatatypes.XSD_STRING));
		}

	}

	public void testUpdateOccurrenceWithAbsoluteXsdIri() throws Exception {

		/*
		 * create 100 occurrences
		 */
		Topic topic = createTopicBySI("type");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = createTopicBySI("value" + i).createOccurrence(
					topic, "50", new Topic[0]);
		}

		String[] datatypes = new String[] { XmlSchemeDatatypes.XSD_INTEGER,
				XmlSchemeDatatypes.XSD_INT, XmlSchemeDatatypes.XSD_LONG,
				XmlSchemeDatatypes.XSD_DECIMAL, XmlSchemeDatatypes.XSD_DATE,
				XmlSchemeDatatypes.XSD_DATETIME, XmlSchemeDatatypes.XSD_TIME,
				XmlSchemeDatatypes.XSD_ANYURI, XmlSchemeDatatypes.XSD_BOOLEAN,
				XmlSchemeDatatypes.XSD_FLOAT };

		for (String datatype : datatypes) {
			String query = "UPDATE occurrences type SET \"50\"^^"
					+ datatype
					+ " WHERE // tm:subject >> characteristics [ . >> atomify == \"50\" ]";
			execute(query);

			for (int i = 0; i < occurrences.length; i++) {
				assertTrue(occurrences[i].getValue().equalsIgnoreCase("50"));
				assertEquals("Datatype should be " + datatype, datatype,
						occurrences[i].getDatatype().getReference());
			}
		}

	}

	public void testUpdateOccurrenceValuesWithRelativeXsdIri() throws Exception {
		/*
		 * create 100 occurrences
		 */
		Topic topic = createTopicBySI("type");
		Occurrence[] occurrences = new Occurrence[100];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = createTopicBySI("value" + i).createOccurrence(
					topic, "50", new Topic[0]);
		}

		/*
		 * array of relative XSD Iris
		 */
		String[] datatypes = new String[] { XmlSchemeDatatypes.XSD_QINTEGER,
				XmlSchemeDatatypes.XSD_QINT, XmlSchemeDatatypes.XSD_QLONG,
				XmlSchemeDatatypes.XSD_QDECIMAL, XmlSchemeDatatypes.XSD_QDATE,
				XmlSchemeDatatypes.XSD_QDATETIME, XmlSchemeDatatypes.XSD_QTIME,
				XmlSchemeDatatypes.XSD_QANYURI,
				XmlSchemeDatatypes.XSD_QBOOLEAN, XmlSchemeDatatypes.XSD_QFLOAT };

		for (String datatype : datatypes) {

			String query = "UPDATE occurrences type SET \"50\"^^"
					+ datatype
					+ " WHERE // tm:subject >> characteristics [ . >> atomify == \"50\" ]";
			execute(query);

			for (int i = 0; i < occurrences.length; i++) {
				assertTrue(occurrences[i].getValue().equalsIgnoreCase("50"));
				assertEquals("Datatype should be " + datatype,
						XmlSchemeDatatypes.toExternalForm(datatype),
						occurrences[i].getDatatype().getReference());
			}
		}
	}

}
