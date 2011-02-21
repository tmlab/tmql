/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.prepared;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
/**
 * @author Sven Krosse
 *
 */
public class TestNonParameterized extends Tmql4JTestCase {

	@Test
	public void testStringValue(){
		Topic t = createTopic();
		Name n = t.createName("Hans");
		IResultSet<?> rs;
		
		IPreparedStatement stmt = runtime.preparedStatement("? << atomify");
		stmt.setString(0, n.getValue());
		String query = stmt.getNonParametrizedQueryString();
		assertEquals("\"Hans\" << atomify",query );
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("Han\"s");
		stmt.setString(0,  n.getValue());		
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"Han\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("Han\"\"\"s");
		stmt.setString(0,  n.getValue());
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"Han\\\"\\\"\\\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("H\"an\"\"\"s");
		stmt.setString(0,  n.getValue());
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"H\\\"an\\\"\\\"\\\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
	}
	
	@Test
	public void testConstructValue(){
		Topic t = createTopic();
		IResultSet<?> rs;
		
		IPreparedStatement stmt = runtime.preparedStatement("?");
		stmt.setConstruct(0, t);
		String query = stmt.getNonParametrizedQueryString();
		assertEquals("\"" + t.getId() + "\" << id",query );
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(t, rs.get(0, 0));
		
		stmt.set(0, t);
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"" + t.getId() + "\" << id",query );
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(t, rs.get(0, 0));
		
		stmt.setString(0, t.getId());
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"" + t.getId() + "\"",query );
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(t.getId(), rs.get(0, 0));
	}
	
	
	@Test
	public void testObjectValue(){
		Topic t = createTopic();
		Name n = t.createName("Hans");
		IResultSet<?> rs;
		
		IPreparedStatement stmt = runtime.preparedStatement("? << atomify");
		stmt.set(0, n.getValue());
		String query = stmt.getNonParametrizedQueryString();
		assertEquals("\"Hans\" << atomify",query );
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("Han\"s");
		stmt.set(0,  n.getValue());		
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"Han\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("Han\"\"\"s");
		stmt.set(0,  n.getValue());
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"Han\\\"\\\"\\\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
		
		n.setValue("H\"an\"\"\"s");
		stmt.set(0,  n.getValue());
		query = stmt.getNonParametrizedQueryString();
		assertEquals("\"\"\"H\\\"an\\\"\\\"\\\"s\"\"\" << atomify", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
	}
	
}
