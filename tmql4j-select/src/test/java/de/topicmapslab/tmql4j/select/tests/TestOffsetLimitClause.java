/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.tests;

import static junit.framework.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestOffsetLimitClause extends Tmql4JTestCase {

	@Test
	public void testOffsetClause() throws Exception{
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			addSupertype(t, topic);
			t.createName("Name " + chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
		}

		query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] OFFSET 10";
		set = execute(query);
		assertEquals(topics.size()-10, set.size());
		int index = 10;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index++), r.first());
		}
		
		query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] DESC OFFSET 10";
		set = execute(query);
		assertEquals(topics.size()-10, set.size());
		index = topics.size()-11;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index--), r.first());
		}
	}
	
	@Test
	public void testLimitClause() throws Exception{
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			addSupertype(t, topic);
			t.createName("Name " + chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
		}

		query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] LIMIT 10";
		set = execute(query);
		assertEquals(10, set.size());
		int index = 0;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index++), r.first());
		}
		
		query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] DESC LIMIT 10";
		set = execute(query);
		assertEquals(10, set.size());
		index = topics.size()-1;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index--), r.first());
		}
	}
	
	@Test
	public void testOffsetLimitClause() throws Exception{
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			addSupertype(t, topic);
			t.createName("Name " + chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
		}

		for ( int index = 0 ; index < 10; index++){
			query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] OFFSET " + Integer.toString(index*10) + " LIMIT 10";
			set = execute(query);
			assertEquals(10, set.size());
			int i = index*10;
			for (IResult r : set.getResults()) {
				assertEquals(1, r.size());
				assertEquals(topics.get(i++), r.first());
			}
		}
		
		for ( int index = 0 ; index < 10; index++){
			query = "SELECT $var WHERE $var AKO myType ORDER BY $var / tm:name [0] DESC OFFSET " + Integer.toString(index*10) + " LIMIT 10";
			set = execute(query);
			assertEquals(10, set.size());
			int i = topics.size() - ( 1 + index*10 );
			for (IResult r : set.getResults()) {
				assertEquals(1, r.size());
				assertEquals(topics.get(i--), r.first());
			}
		}
	}
	
}
