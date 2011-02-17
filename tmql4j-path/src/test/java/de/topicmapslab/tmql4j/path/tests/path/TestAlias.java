/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.path;

import static junit.framework.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestAlias extends Tmql4JTestCase {

	@Test(expected = IllegalArgumentException.class)
	public void testError() throws Exception {
		createTopic();
		IQuery q = runtime.run(topicMap, "// tm:subject ( . )");
		q.getResults().get(0, "topic");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testError2() throws Exception {
		createTopic();
		IQuery q = runtime.run(topicMap, "// tm:subject ( . )");
		q.getResults().get(0).get("topic");
	}
	
	@Test
	public void testAlias() throws Exception {
		createTopic();
		IQuery q = runtime.run(topicMap, "// tm:subject AS \"topic\"");
		q.getResults().get(0).get("topic");
	}

	@Test
	public void testProjectionWithAlias() throws Exception {
		Topic t = createTopic();
		IQuery q = runtime.run(topicMap, "// tm:subject ( . AS \"topic\" )");
		assertEquals(t, q.getResults().get(0, "topic"));
		
		assertEquals(t, q.getResults().get(0).get("topic"));
	}
	
	@Test
	public void testProjectionWithAlias2() throws Exception {
		Topic t = createTopic();
		Set<Name> names = HashUtil.getHashSet();
		for ( int i = 0 ;i < 10 ; i++){
			names.add(t.createName(t, "Name"+i));
		}
		IQuery q = runtime.run(topicMap, "// tm:subject ( . AS \"topic\" , . >> characteristics tm:name AS \"name\" )");
		IResultSet<?> set = q.getResults();
		assertEquals(10, set.size());
		for ( IResult r :set){
			assertEquals(2, r.size());
			assertEquals(t, r.get("topic"));
			assertTrue(names.contains(r.get("name")));
		}
	}
	
	@Test
	public void testProjectionWithAlias3() throws Exception {
		Topic t = createTopic();
		Set<Name> names = HashUtil.getHashSet();
		Set<String> values = HashUtil.getHashSet();
		for ( int i = 0 ;i < 10 ; i++){
			names.add(t.createName(t, "Name"+i));
			values.add("Name"+i);
		}
		IQuery q = runtime.run(topicMap, "// tm:subject >> characteristics tm:name ( . << characteristics AS \"topic\" , . AS \"name\" , . >> atomify AS \"value\" )");
		IResultSet<?> set = q.getResults();
		assertEquals(10, set.size());
		for ( IResult r :set){
			assertEquals(3, r.size());
			assertEquals(t, r.get("topic"));
			assertTrue(names.contains(r.get("name")));
			assertTrue(values.contains(r.get("value")));
		}
	}

}
