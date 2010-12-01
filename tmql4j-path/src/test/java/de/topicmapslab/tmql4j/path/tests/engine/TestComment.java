/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.engine;

import static junit.framework.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestComment extends Tmql4JTestCase {

	@Test
	public void testCommentInString() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topics.add( createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "#tm:subject >> instances";
		set = execute(query);
		assertEquals(0, set.size());
		
		query = "\"Hallo Welt\"#tm:subject >> instances";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Hallo Welt", set.get(0, 0));
		
		query = "\"Hallo #Welt\"#tm:subject >> instances";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Hallo #Welt", set.get(0, 0));
		
		query = "\"Hallo #################Welt\"#tm:subject >> instances";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Hallo #################Welt", set.get(0, 0));
		
		query = "\"Hallo <Welt>\"#tm:subject >> instances";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Hallo <Welt>", set.get(0, 0));	
	}

}
