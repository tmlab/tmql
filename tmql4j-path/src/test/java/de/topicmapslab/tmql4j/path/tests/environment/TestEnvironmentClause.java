/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.environment;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestEnvironmentClause extends Tmql4JTestCase {

	@Test
	public void testPrefix() throws Exception {
		Topic topic = topicMap.createTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/myTopic"));
		String query = null;
		SimpleResultSet set = null;

		query = "%prefix o http://psi.example.org/ o:myTopic";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testPragmaOfTypeTransitivity() throws Exception {

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic supertype = createTopicBySI("mySupertype");
		Topic supersupertype = createTopicBySI("supersupertype");

		// topic isa type
		topic.addType(type);
		// type ako supertype
		addSupertype(type, supertype);
		// supertype ako supersupertype
		addSupertype(supertype, supersupertype);

		String query = "%pragma taxonometry tm:intransitive myTopic >> types";
		SimpleResultSet set = execute(query);

		assertEquals(1, set.size());

		query = "%pragma taxonometry tm:transitive myTopic >> types";
		set = execute(query);
		assertEquals(3, set.size());

		// for instances
		query = "%pragma taxonometry tm:intransitive supersupertype >> instances";
		set = execute(query);
		assertEquals(0, set.size());

		query = "%pragma taxonometry tm:transitive supersupertype >> instances";
		set = execute(query);
		assertEquals(1, set.size());

		// for supertypes
		query = "%pragma taxonometry tm:intransitive myType >> supertypes";
		set = execute(query);
		assertEquals(1, set.size());

		query = "%pragma taxonometry tm:transitive myType >> supertypes";
		set = execute(query);
		assertEquals(2, set.size());

		// for subtypes
		query = "%pragma taxonometry tm:intransitive supersupertype >> subtypes";
		set = execute(query);
		assertEquals(1, set.size());

		query = "%pragma taxonometry tm:transitive supersupertype >> subtypes";
		set = execute(query);
		assertEquals(2, set.size());
	}

}
