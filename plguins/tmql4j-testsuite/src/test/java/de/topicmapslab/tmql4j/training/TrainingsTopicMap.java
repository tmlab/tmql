/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.training;

import static de.topicmapslab.tmql4j.training.TopologyUtils.ako;
import static de.topicmapslab.tmql4j.training.TopologyUtils.association;
import static de.topicmapslab.tmql4j.training.TopologyUtils.baseIRI;
import static de.topicmapslab.tmql4j.training.TopologyUtils.locator;
import static de.topicmapslab.tmql4j.training.TopologyUtils.map;
import static de.topicmapslab.tmql4j.training.TopologyUtils.name;
import static de.topicmapslab.tmql4j.training.TopologyUtils.occurrence;
import static de.topicmapslab.tmql4j.training.TopologyUtils.si;
import static de.topicmapslab.tmql4j.training.TopologyUtils.topicBySI;
import static de.topicmapslab.tmql4j.training.TopologyUtils.variant;

import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.XmlSchemeDatatypes;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TrainingsTopicMap {

	public static void main(String[] args) throws Exception {
		map = TopicMapSystemFactory.newInstance().newTopicMapSystem()
				.createTopicMap(baseIRI);

		Topic topic = null;
		Name name = null;
		Occurrence occurrence = null;
		Variant variant = null;
		Association association = null;
		Role role = null;

		topic = topicBySI("reifier");
		name = name(topic, "reifier");

		topic = topicBySI("Person");
		name = name(topic, "Person");

		topic = topicBySI("Musician");
		name = name(topic, "Musician");
		ako(topic, si("Person"));

		topic = topicBySI("Composer");
		name = name(topic, "Composer");
		ako(topic, si("Musician"));

		topic = topicBySI("Puccini");
		name = name(topic, "Puccjni, Giacomo");
		name = name(topic, "Giacomo Puccjni", "normal-form");
		name = name(topic, "Puccjni", "short-form");
		occurrence = occurrence(topic, "date-of-birth", "1858-12-22",
				XmlSchemeDatatypes.XSD_DATE);
		occurrence = occurrence(topic, "date-of-death", "1924-11-29",
				XmlSchemeDatatypes.XSD_DATE);

		topic = topicBySI("Franco-Alfano");
		name = name(topic, "Alfano, Franco");
		name = name(topic, "Franco Alfano", "normal-form");
		name = name(topic, "Alfano", "short-form");
		occurrence = occurrence(topic, "date-of-birth", "1875-03-08",
				XmlSchemeDatatypes.XSD_DATE);
		occurrence = occurrence(topic, "date-of-death", "1954-10-27",
				XmlSchemeDatatypes.XSD_DATE);

		topic = topicBySI("Country");
		name = name(topic, "Country");

		topic = topicBySI("France");
		name = name(topic, "France");
		topic.addType(si("Country"));

		topic = topicBySI("Germany");
		name = name(topic, "Germany");
		topic.addType(si("Country"));

		topic = topicBySI("Poland");
		name = name(topic, "Poland");
		topic.addType(si("Country"));

		topic = topicBySI("Russia");
		name = name(topic, "Russia");
		topic.addType(si("Country"));

		topic = topicBySI("Place");
		name = name(topic, "Place");

		topic = topicBySI("City");
		name = name(topic, "City");
		ako(topic, si("Place"));

		topic = topicBySI("Paris");
		name = name(topic, "Paris");
		topic.addType(si("City"));

		topic = topicBySI("Berlin");
		name = name(topic, "Berlin");
		topic.addSubjectLocator(locator("http://www.berlin.de/"));
		topic.addType(si("City"));

		topic = topicBySI("Warsaw");
		name = name(topic, "Warsaw");
		variant = variant(name, "Warschau", XmlSchemeDatatypes.XSD_STRING,
				"German");
		topic.addType(si("City"));

		topic = topicBySI("Moscow");
		name = name(topic, "Moscow");
		variant = variant(name, "Moskau", XmlSchemeDatatypes.XSD_STRING,
				"German");
		topic.addType(si("City"));

		association = association("part-of");
		association.createRole(si("City"), si("Paris"));
		role = association.createRole(si("Country"), si("France"));
		role.setReifier(si("reifier"));

		association = association("part-of");
		association.createRole(si("City"), si("Berlin"));
		association.createRole(si("Country"), si("Germany"));

		association = association("part-of");
		association.createRole(si("City"), si("Moscow"));
		association.createRole(si("Country"), si("Russia"));

		association = association("part-of");
		association.createRole(si("City"), si("Warsaw"));
		association.createRole(si("Country"), si("Poland"));

		association = association("born-in");
		association.createRole(si("City"), si("Paris"));
		association.createRole(si("Person"), si("Puccini"));

		association = association("born-in");
		association.createRole(si("City"), si("Berlin"));
		association.createRole(si("Person"), si("Franco-Alfano"));

		topic = topicBySI("Work");
		name = name(topic, "Work");

		topic = topicBySI("Musical-work");
		name = name(topic, "Musical-work");
		ako(topic, si("Work"));

		topic = topicBySI("Theatrical-work");
		name = name(topic, "Theatrical-work");
		ako(topic, si("Work"));

		topic = topicBySI("Opera");
		name = name(topic, "Opera");
		ako(topic, si("Musical-work"));
		ako(topic, si("Theatrical-work"));

		for (int i = 0; i < 15; i++) {
			topic = topicBySI("Opera-" + i);
			topic.addType(si("Opera"));
			
			association = association("composed-by");
			association.createRole(si("Composer"), si("Puccini"));
			association.createRole(si("Work"), topic);
		}
		
		for (int i = 15; i < 25; i++) {
			topic = topicBySI("Opera-" + i);
			topic.addType(si("Opera"));
			
			association = association("composed-by");
			association.createRole(si("Composer"), si("Franco-Alfano"));
			association.createRole(si("Work"), topic);
		}
		
		for (int i = 25; i < 30; i++) {
			topic = topicBySI("Opera-" + i);
			topic.addType(si("Opera"));
			
			association = association("composed-by");
			association.createRole(si("Composer"), si("Franco-Alfano"));
			association.createRole(si("Composer"), si("Puccini"));
			association.createRole(si("Work"), topic);
		}
		
		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(map);
		IQuery query = runtime.run("// tm:subject [ . >> reifier ]");
		System.out.println(query.getResults());
	}

}
