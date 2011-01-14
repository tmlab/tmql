/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQR;
import de.topicmapslab.tmql4j.template.grammar.lexical.Template;
import de.topicmapslab.tmql4j.template.util.json.JTMQRWriter;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;
import static junit.framework.Assert.*;
/**
 * @author Sven Krosse
 * 
 */
public class TestUse extends Tmql4JTestCase {

	@Test
	public void testUse() throws Exception {
		Set<String> values = new HashSet<String>();
		Topic topic = createTopic();
		for ( int i = 0 ; i < 100 ; i++){
			topic.createName("Topic with name " + i);
			values.add("<div>Topic with name " + i + "</div>");
		}
		final String query 	= "%pragma TEMPLATE \"\"\"<div>?name?</div>\"\"\" " 
							+ " // tm:subject / tm:name AS \"name\" USE TEMPLATE";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for ( IResult r : rs){
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}
		assertEquals(Template.TOKEN, rs.getResultType());
	}
	
	@Test
	public void testUse2() throws Exception {
		Set<String> values = new HashSet<String>();
		values.add("<div>"+ TmdmSubjectIdentifier.TMDM_DEFAULT_NAME_TYPE + " with name NULL</div>");
		Topic topic = createTopicBySI("myTopic");
		for ( int i = 0 ; i < 100 ; i++){
			topic.createName("Name " + i);
			values.add("<div>"+ base + "myTopic with name Name " + i + "</div>");
		}
		final String query 	= "%pragma TEMPLATE \"\"\"<div>?si? with name ?name?</div>\"\"\" " 
							+ " // tm:subject ( . >> indicators AS \"si\" , . / tm:name AS \"name\" ) USE TEMPLATE";
		IResultSet<?> rs = execute(query);
		assertEquals(101, rs.size());
		for ( IResult r : rs){
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}
		assertEquals(Template.TOKEN, rs.getResultType());
	}

	@Test
	public void testUseJTMQR() throws Exception {
		for ( int i = 0 ; i < 1; i++){
			createTopic();
		}
		final String query 	= "// tm:subject USE JTMQR";
		IResultSet<?> rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(String.class, rs.get(0,0).getClass());
		assertEquals(JTMQR.TOKEN, rs.getResultType());
	}
	@Test
	public void testJTMQR() throws Exception {
		final String jtmqr = JTMQRWriter.write(QueryMatches.emptyMatches());
		final String query 	= "// tm:subject USE JTMQR";
		IResultSet<?> rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(String.class, rs.get(0,0).getClass());
		assertEquals(JTMQR.TOKEN, rs.getResultType());
		assertEquals(jtmqr, rs.get(0,0));
	}
}
