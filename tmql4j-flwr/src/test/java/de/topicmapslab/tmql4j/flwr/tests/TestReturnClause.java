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
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.flwr.components.processor.results.ctm.CTMResult;
import de.topicmapslab.tmql4j.flwr.components.processor.results.xml.XMLResult;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestReturnClause extends Tmql4JTestCase {

	@Test
	public void testSingleReturnClause() throws Exception {

		Topic topic = createTopicBySI("myTopic");

		String query = "RETURN myTopic";
		SimpleResultSet set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testDoubleReturnClause() throws Exception {

		Topic topic = createTopicBySI("myTopic");

		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name" + Integer.toString(i)));
		}
		assertEquals(100, topic.getNames().size());

		String query = "RETURN myTopic , myTopic >> characteristics tm:name";
		SimpleResultSet set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(names.contains(r.getResults().get(1)));
		}

		query = "RETURN ( myTopic , myTopic >> characteristics tm:name )";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertEquals(topic, r.getResults().get(0));
			assertTrue(names.contains(r.getResults().get(1)));
		}
	}

	@Test
	public void testReturnXml() throws Exception {
		String query = "RETURN <yml> Name </yml>";
		XMLResult set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("<yml> Name </yml>", set.first().first());

		Topic topic = createTopicBySI("myTopic");
		Set<String> nodes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topic.createName("Name" + Integer.toString(i));
			nodes.add("<yml> Name" + Integer.toString(i) + "</yml>");
		}
		assertEquals(100, topic.getNames().size());
		query = "FOR $name IN myTopic >> characteristics tm:name RETURN <yml> { $name >> atomify } </yml>";
		set = execute(query);
		assertEquals(nodes.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(nodes.contains(r.first()));
		}
	}

	@Test
	public void testReturnXmlWithAttribute() throws Exception {
		String query = "RETURN <yml type=\"name\"> Name </yml>";
		XMLResult set = null;
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("<yml type=\"name\"> Name </yml>", set.first().first());

		Topic topic = createTopicBySI("myTopic");
		Set<String> nodes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topic.createName("Name" + Integer.toString(i));
			nodes.add("<yml type=\"name\"> Name" + Integer.toString(i) + "</yml>");
		}
		assertEquals(100, topic.getNames().size());
		query = "FOR $name IN myTopic >> characteristics tm:name RETURN <yml type=\"name\"> { $name >> atomify }</yml>";
		set = execute(query);
		assertEquals(nodes.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(nodes.contains(r.first()));
		}

		topic.remove();
		topic = createTopicBySI("myTopic");
		nodes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topic.createName("Name" + Integer.toString(i));
			nodes.add("<yml type=\" Name" + Integer.toString(i) + "\"> Name </yml>");
		}
	}

	@Test
	public void testReturnCtm() throws Exception {
		String query = "RETURN ''' Name '''";
		CTMResult set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Name ", set.first().first());

		Topic topic = createTopicBySI("myTopic");
		Set<String> nodes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topic.createName("Name" + Integer.toString(i));
			nodes.add("Name" + Integer.toString(i) + "");
		}
		assertEquals(100, topic.getNames().size());
		query = "FOR $name IN myTopic >> characteristics tm:name RETURN ''' { $name >> atomify } '''";
		set = execute(query);
		assertEquals(nodes.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(nodes.contains(r.first()));
		}
	}

	@Test
	public void testReturnOrder() throws Exception{
		List<String> results = new ArrayList<String>();
		StringBuilder builder = new StringBuilder("RETURN ");
		boolean first = true;
		for ( int i = 0 ; i <100 ; i++ ){
			String s = String.valueOf(i);
			if ( !first ){
				builder.append(", ");
			}
			builder.append("\"");
			builder.append(s);
			builder.append("\"");
			results.add(s);
			first = false;
		}
		
		IResultSet<?> rs = execute(builder.toString());
		assertEquals(1, rs.size());
		for ( int i = 0 ; i < rs.size() ; i++ ){
			assertEquals(results.get(i), rs.get(0, i));
		}
	}
	
}
