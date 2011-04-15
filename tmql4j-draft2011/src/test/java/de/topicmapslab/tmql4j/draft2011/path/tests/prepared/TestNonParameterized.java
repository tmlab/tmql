/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.tests.prepared;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestNonParameterized extends Tmql4JTestCase {

	@Test
	public void testStringValue() {
		Topic t = createTopic();
		Name n = t.createName("Hans");
		IResultSet<?> rs;

		IPreparedStatement stmt = runtime.preparedStatement("? / by-value");
		stmt.setString(0, n.getValue());
		String query = stmt.getNonParameterizedQueryString();
		assertEquals("\"Hans\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("Han\"s");
		stmt.setString(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"Han\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("Han\"\"\"s");
		stmt.setString(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"Han\\\"\\\"\\\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("H\"an\"\"\"s");
		stmt.setString(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"H\\\"an\\\"\\\"\\\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
	}

	@Test
	public void testConstructValue() {
		Topic t = createTopic();
		Name n = t.createName("Name");
		IResultSet<?> rs;

		IPreparedStatement stmt = runtime.preparedStatement("?");
		stmt.setConstruct(0, n);
		String query = stmt.getNonParameterizedQueryString();
		assertEquals("\"" + n.getId() + "\" / by-id", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		stmt.set(0, n);
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"" + n.getId() + "\" / by-id", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		stmt.setString(0, n.getId());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"" + n.getId() + "\"", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n.getId(), rs.get(0, 0));
	}

	@Test
	public void testObjectValue() {
		Topic t = createTopic();
		Name n = t.createName("Hans");
		IResultSet<?> rs;

		IPreparedStatement stmt = runtime.preparedStatement("? / by-value");
		stmt.set(0, n.getValue());
		String query = stmt.getNonParameterizedQueryString();
		assertEquals("\"Hans\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("Han\"s");
		stmt.set(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"Han\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("Han\"\"\"s");
		stmt.set(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"Han\\\"\\\"\\\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));

		n.setValue("H\"an\"\"\"s");
		stmt.set(0, n.getValue());
		query = stmt.getNonParameterizedQueryString();
		assertEquals("\"\"\"H\\\"an\\\"\\\"\\\"s\"\"\" / by-value", query);
		rs = execute(query);
		assertEquals(1, rs.size());
		assertEquals(1, rs.first().size());
		assertEquals(n, rs.get(0, 0));
	}

	@Test
	public void testQuestionMarksInContent() throws Exception {

		IPreparedStatement stmt = runtime.preparedStatement("\"To be or not to be?\"");
		assertEquals(stmt.getQueryString(), stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("? / by-id [ \"To be or not to be?\" ]");
		stmt.set(0, 1);
		assertEquals("1 / by-id [ \"To be or not to be?\" ]", stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("? / by-id [ \"To be or not to be?\" ] / names [ ? ]");
		stmt.set(0, 1);
		stmt.set(1, 2);
		assertEquals("1 / by-id [ \"To be or not to be?\" ] / names [ 2 ]", stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("<?> / by-id [ \"To be or not to be?\" ] / names [ ? ]");
		stmt.set(0, 1);
		assertEquals("<?> / by-id [ \"To be or not to be?\" ] / names [ 1 ]", stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("?topic / by-id [ \"To be or not to be?\" ]");
		stmt.set("topic", 1);
		assertEquals("1 / by-id [ \"To be or not to be?\" ]", stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("?topic / by-id [ \"To be or not to be?\" ] / names [ ?topic ]");
		stmt.set("topic", 1);
		assertEquals("1 / by-id [ \"To be or not to be?\" ] / names [ 1 ]", stmt.getNonParameterizedQueryString());

		stmt = runtime.preparedStatement("<?> / by-id [ \"To be or not to be?\" ] / names[ ?topic ]");
		stmt.set("topic", 1);
		assertEquals("<?> / by-id [ \"To be or not to be?\" ] / names [ 1 ]", stmt.getNonParameterizedQueryString());
	}

}
