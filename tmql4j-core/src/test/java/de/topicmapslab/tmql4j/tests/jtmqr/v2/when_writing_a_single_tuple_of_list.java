package de.topicmapslab.tmql4j.tests.jtmqr.v2;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.tests.jtmqr.v2.with_TopicMap;

public class when_writing_a_single_tuple_of_list extends with_TopicMap {

	@Test
	public void it_should_write_an_array_of_strings() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.isArray());
		assertEquals("a", first.get(0).getValueAsText());
		assertEquals("b", first.get(1).getValueAsText());
	}

	@Test
	public void it_should_write_an_array_of_numbers() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.isArray());
		assertEquals(1L, first.get(0).getLongValue());
		assertEquals(2L, first.get(1).getLongValue());
	}

	@Test
	public void it_should_write_an_array_of_locators() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Locator> list = new ArrayList<Locator>();
		Locator l1 = createLocator("http://psi.example.org");
		Locator l2 = createLocator("http://psi.example.org");
		list.add(l1);
		list.add(l2);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.isArray());
		
		assertTrue(first.get(0).has("ref"));
		assertTrue(first.get(1).has("ref"));
		
		assertEquals(l1.getReference(), first.get(0).get("ref").getValueAsText());
		assertEquals(l2.getReference(), first.get(1).get("ref").getValueAsText());
	}

	@Test
	public void it_should_write_an_array_of_booleans() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(true);
		list.add(false);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.isArray());
		assertEquals(true, first.get(0).getBooleanValue());
		assertEquals(false, first.get(1).getBooleanValue());
	}

	@Test
	public void it_should_write_an_array_of_constructs() throws IOException {
		
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Construct> list = new ArrayList<Construct>();
		list.add(createSimpleNamedTopic("class:" + getClass().getName()));
		list.add(createName("Name"));
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		
		JsonNode array = tupleNode.get(0);

		JsonNode a = array.get(0);
		assertTrue(a.has("jtm"));
		assertEquals("topic", a.get("jtm").get("item_type").getTextValue());
		
		JsonNode b = array.get(1);
		assertTrue(b.has("jtm"));
		assertEquals("name", b.get("jtm").get("item_type").getTextValue());
	}

	@Test
	public void it_should_write_an_array_of_nulls() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(null);
		list.add(null);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple);
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode array = tupleNode.get(0);
		JsonNode a1 = array.get(0);
		assertTrue(a1.size() == 0);
		JsonNode a2 = array.get(1);
		assertTrue(a2.size() == 0);
	}

	@Test(expected = JsonGenerationException.class)
	public void it_should_throw_an_error() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<List<?>> list = new ArrayList<List<?>>();
		list.add(new ArrayList<String>());
		tuple.add(list);
		writeAndRead(tuple);
	}
}
