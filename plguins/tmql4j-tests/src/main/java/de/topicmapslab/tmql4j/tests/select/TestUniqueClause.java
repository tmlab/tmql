/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.select;

import static junit.framework.Assert.assertEquals;


import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.tuplesequence.MultipleTupleSequence;
import de.topicmapslab.tmql4j.common.core.tuplesequence.UniqueTupleSequence;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestUniqueClause extends Tmql4JTestCase {

	@Test
	public void testUniqueClause() throws Exception {
		runtime.getProperties().setProperty(TMQLRuntimeProperties.TUPLE_SEQUENCE_CLASS, MultipleTupleSequence.class.getName());
		
		Topic topic = createTopicBySI("myTopic");

		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			t.createName("Name");
		}

		String query = "SELECT $t / tm:name WHERE $t ISA myTopic ";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals("Name",r.first());
		}
		
		query = "SELECT $t / tm:name WHERE $t ISA myTopic  UNIQUE";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Name",set.first().first());		
		runtime.getProperties().setProperty(TMQLRuntimeProperties.TUPLE_SEQUENCE_CLASS, UniqueTupleSequence.class.getName());	
	}
	
}
