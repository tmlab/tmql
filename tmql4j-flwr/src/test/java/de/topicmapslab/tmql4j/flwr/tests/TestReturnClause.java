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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.topicmapslab.tmql4j.components.processor.results.ctm.CTMResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.components.processor.results.xml.XMLResult;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
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
	public void testBoolean() throws Exception {
		String query;
		SimpleResultSet set = null;
		query = "RETURN fn:compare( " + Integer.toString(0) + ", " + Integer.toString(0) + ") AND fn:compare( " + Integer.toString(1) + ", " + Integer.toString(0) + ")";
		set = execute(new TMQLQuery(topicMap, query));		
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(false, set.first().first());
		
		query = "RETURN fn:compare( " + Integer.toString(0) + ", " + Integer.toString(0) + ") OR fn:compare( " + Integer.toString(1) + ", " + Integer.toString(0) + ")";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(true, set.first().first());
		
		query = "RETURN NOT fn:compare( " + Integer.toString(0) + ", " + Integer.toString(0) + ")";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(false, set.first().first());
		
		query = "RETURN NOT fn:compare( " + Integer.toString(0) + ", " + Integer.toString(1) + ")";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(true, set.first().first());
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
	public void testToXml() throws Exception {		
		XMLResult set;

		Topic topic = createTopicBySI("myTopic");
		Set<String> nodes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topic.createName("Name" + Integer.toString(i));
			nodes.add("Name" + Integer.toString(i));
		}
		assertEquals(100, topic.getNames().size());
		String query = "FOR $n IN // tm:subject >> characteristics RETURN <a> { $n >> atomify } </a>";
		set = execute(query);
		assertEquals(nodes.size(), set.size());		
		/*
		 * test with document
		 */
		Document doc = set.toXMLDocument();
		NodeList roots = doc.getChildNodes();
		assertEquals(1, roots.getLength());
		Node root = roots.item(0);
		assertEquals("xml-root", root.getNodeName());
		NodeList as = root.getChildNodes();
		int cnt = 0;
		for ( int item = 0 ; item < as.getLength() ; item++ ){
			Node a = as.item(item);
			if ( "a".equalsIgnoreCase(a.getNodeName())){
				cnt++;
				String value = a.getTextContent().trim();
				assertTrue(nodes.contains(value));
			}
		}
		assertEquals(100, cnt);
		
		/*
		 * test with output stream
		 */
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		set.toXML(os);
		os.flush();
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(os.toByteArray()));
		roots = doc.getChildNodes();
		assertEquals(1, roots.getLength());
		root = roots.item(0);
		assertEquals("xml-root", root.getNodeName());
		as = root.getChildNodes();
		cnt = 0;
		for ( int item = 0 ; item < as.getLength() ; item++ ){
			Node a = as.item(item);
			if ( "a".equalsIgnoreCase(a.getNodeName())){
				cnt++;
				String value = a.getTextContent().trim();
				assertTrue(nodes.contains(value));
			}
		}
		assertEquals(100, cnt);
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
