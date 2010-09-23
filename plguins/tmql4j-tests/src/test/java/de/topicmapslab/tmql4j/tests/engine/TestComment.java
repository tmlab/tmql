/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.engine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

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
		SimpleResultSet set = null;

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
		
		query = "RETURN <Welt> Hallo </Welt>";
		XMLResult xmlSet = execute(query);
		assertEquals(1, xmlSet.size());
		assertEquals(1, xmlSet.first().size());
		assertEquals("<Welt> Hallo </Welt>", xmlSet.get(0, 0));
		
		query = "\"Hallo \\u0022 Welt\"";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Hallo \\u0022 Welt", set.get(0, 0));
		String s = set.get(0, 0);
		System.out.println(s.replaceAll("\\\\u0022", "\""));		
	}

}
