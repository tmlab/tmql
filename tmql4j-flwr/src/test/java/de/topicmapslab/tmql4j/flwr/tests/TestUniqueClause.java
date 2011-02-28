/*

 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestUniqueClause extends Tmql4JTestCase {

	@Test
	public void testUniqueClause() throws Exception {
		
		
		Topic topic = createTopicBySI("myTopic");

		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			t.createName("Name");
		}

		String query = "FOR $t IN // myTopic RETURN $t / tm:name";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals("Name",r.first());
		}
		
		query = "FOR $t IN // myTopic UNIQUE RETURN $t / tm:name";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Name",set.first().first());		
			
	}
	
}
