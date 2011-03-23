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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestOrderByClause extends Tmql4JTestCase {

	@Test
	public void testSingleOrderByClause() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			t.createName("Name " + chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
		}

		query = "FOR $var IN // myType  ORDER BY $var / tm:name [0] RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		int index = 0;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index++), r.first());
		}

		query = "FOR $var IN // myType  ORDER BY $var / tm:name [0] DESC RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		index = topics.size() - 1;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index--), r.first());
		}
	}

	@Test
	public void testOrderByClauseForNumbers() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new ArrayList<Topic>();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			for (int j = 0; j < i; j++) {
				t.createName("Name" + j);
			}
			topics.add(t);
		}
		Collections.sort(topics, new Comparator<Topic>() {
			public int compare(Topic o1, Topic o2) {
				return o1.getNames().size() - o2.getNames().size();
			}
		});
		
		query = "FOR $t IN // myType ORDER BY fn:count( $t >> characteristics ) RETURN $t";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		int i = 0;
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertEquals("Should be topic with " + topics.get(i).getNames().size() +" but was with " +((Topic)r.first()).getNames().size(), topics.get(i), r.first());
			i++;
		}
		
		Collections.sort(topics, new Comparator<Topic>() {
			public int compare(Topic o1, Topic o2) {
				return o2.getNames().size() - o1.getNames().size();
			}
		});
		
		query = "FOR $t IN // myType ORDER BY fn:count( $t >> characteristics ) DESC RETURN $t";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		 i = 0;
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertEquals("Should be topic with " + topics.get(i).getNames().size() +" but was with " +((Topic)r.first()).getNames().size(), topics.get(i), r.first());
			i++;
		}
	}

	@Test
	public void testOrderByClause() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		List<Topic> topics = new LinkedList<Topic>();
		String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K" };
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			t.createName("Name " + chars[i / 10]);
			t.createOccurrence(createTopic(),
					chars[i / 10] + Integer.toString(i % 10));
			topics.add(t);
		}

		query = "FOR $var IN // myType  ORDER BY $var / tm:name [0] , $var / tm:occurrence [0] RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		int index = 0;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index++), r.first());
		}

		query = "FOR $var IN // myType  ORDER BY $var / tm:name [0] DESC, $var / tm:occurrence [0] DESC RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		index = topics.size() - 1;
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertEquals(topics.get(index--), r.first());
		}
	}

}
