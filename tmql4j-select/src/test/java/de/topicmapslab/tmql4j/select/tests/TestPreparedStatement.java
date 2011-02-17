/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.tests;

import static junit.framework.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestPreparedStatement extends Tmql4JTestCase {

	@Test
	public void testOffsetLimitClause() throws Exception {
		IResultSet<?> set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			addSupertype(t, topic);
			t.createName("Name " + chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
			assertFalse(t.getNames().isEmpty());
		}

		IPreparedStatement stmt = runtime.preparedStatement("SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] OFFSET ? LIMIT 10");
		stmt.setTopicMap(topicMap);
		for (int index = 0; index < 10; index++) {
			stmt.setLong(0, index * 10);
			stmt.run();
			set = stmt.getResults();
			assertEquals(10, set.size());
			int i = index * 10;
			for (IResult r : set.getResults()) {
				assertEquals(1, r.size());
				assertEquals(topics.get(i++), r.first());
			}
		}

		stmt = runtime.preparedStatement("SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] DESC OFFSET ? LIMIT ?");
		stmt.setTopicMap(topicMap);
		for (int index = 0; index < 10; index++) {
			stmt.setLong(0, index * 10);
			stmt.setLong(1, 10);
			stmt.run();
			set = stmt.getResults();
			assertEquals(10, set.size());
			int i = topics.size() - (1 + index * 10);
			for (IResult r : set.getResults()) {
				assertEquals(1, r.size());
				assertEquals(topics.get(i--), r.first());
			}
		}
	}

}
