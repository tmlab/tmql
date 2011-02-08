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
import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestGroupByClause extends Tmql4JTestCase {
	@Test
	public void testSingleGroupByVariable() throws Exception {

		Topic topic = createTopicBySI("myTopic");
		Set<Topic> types = HashUtil.getHashSet();
		for (int j = 0; j < 100; j++) {
			Topic type = createTopicBySI("myType" + j);
			topic.addType(type);
			types.add(type);
		}
		String query = "SELECT myTopic , myTopic >> types";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult result : set) {
			assertEquals(2, result.size());
			assertEquals(topic, result.first());
			assertTrue(types.contains(result.get(1)));
		}
		
		query = "SELECT myTopic , myTopic >> types GROUP BY $0";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic, set.first().first());
		assertTrue(set.first().get(1) instanceof Collection<?>);
		Collection<?> values = (Collection<?>)set.first().get(1);
		assertEquals(100, values.size());
		assertTrue(types.containsAll((Collection<?>)set.first().get(1)));
	}
	
	@Test
	public void testSingleGroupByVariable2() throws Exception {

		Topic topic = createTopicBySI("myTopic");
		Set<Topic> types = HashUtil.getHashSet();
		for (int j = 0; j < 100; j++) {
			Topic type = createTopicBySI("myType" + j);
			topic.addType(type);
			types.add(type);
		}
		String query = "SELECT myTopic >>  types, myTopic";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult result : set) {
			assertEquals(2, result.size());			
			assertTrue(types.contains(result.get(0)));
			assertEquals(topic, result.get(1));
		}
		
		query = "SELECT myTopic >>  types, myTopic GROUP BY $1 ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(topic, set.first().get(1));
		assertTrue(set.first().get(0) instanceof Collection<?>);
		Collection<?> values = (Collection<?>)set.first().get(0);
		assertEquals(100, values.size());
		assertTrue(types.containsAll((Collection<?>)set.first().get(0)));
	}
	
	@Test(expected=TMQLRuntimeException.class)
	public void testInvalidGroupByVariable() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Topic> types = HashUtil.getHashSet();
		for (int j = 0; j < 100; j++) {
			Topic type = createTopicBySI("myType" + j);
			topic.addType(type);
			types.add(type);
		}
		String query = "SELECT myTopic >> types, myTopic GROUP BY $3";
		execute(query);
	}
	
	@Test
	public void testDoubleGroupByVariable() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		topic.addSubjectIdentifier(createLocator("otherSI"));
		Set<Topic> types = HashUtil.getHashSet();
		for (int j = 0; j < 100; j++) {
			Topic type = createTopicBySI("myType" + j);
			topic.addType(type);
			types.add(type);
		}
		String query = "SELECT myTopic, myTopic >> indicators, myTopic >> types";
		SimpleResultSet set = execute(query);
		assertEquals(200, set.size());
		for (IResult result : set) {
			assertEquals(3, result.size());			
			assertEquals(topic, result.get(0));
			assertTrue(topic.getSubjectIdentifiers().contains(result.get(1)));
			assertTrue(types.contains(result.get(2)));
		}
		
		query = "SELECT myTopic, myTopic >> indicators, myTopic >> types GROUP BY $0 ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(3, set.first().size());
		assertEquals(topic, set.first().get(0));
		assertTrue(set.first().get(1) instanceof Collection<?>);
		assertTrue(set.first().get(2) instanceof Collection<?>);
		assertEquals(2, ((Collection<?>)set.first().get(1)).size());
		assertTrue(topic.getSubjectIdentifiers().containsAll((Collection<?>)set.first().get(1)));
		assertEquals(100, ((Collection<?>)set.first().get(2)).size());
		assertTrue(types.containsAll((Collection<?>)set.first().get(2)));
		
		query = "SELECT myTopic, myTopic >> indicators, myTopic >> types GROUP BY $0, $1 ";
		set = execute(query);
		assertEquals(2, set.size());
		for ( IResult r : set){
			assertEquals(3, r.size());
			assertEquals(topic, r.get(0));
			assertTrue(topic.getSubjectIdentifiers().contains(r.get(1)));
			assertTrue(r.get(2) instanceof Collection<?>);
			assertEquals(100, ((Collection<?>)r.get(2)).size());
			assertTrue(types.containsAll((Collection<?>)r.get(2)));
		}
	}

}
