package de.topicmapslab.tmql4j.tests.jtmqr.v1;

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

/**
 * 
 * @author Sven Krosse
 * 
 */
public class when_writing_a_single_tuple_of_list extends with_TopicMap {

	@Test
	public void it_should_write_an_array_of_strings() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		assertTrue(a1.has("s"));
		assertEquals("a", a1.get("s").getValueAsText());
		JsonNode a2 = a.get(1);
		assertTrue(a2.has("s"));
		assertEquals("b", a2.get("s").getValueAsText());
	}

	@Test
	public void it_should_write_an_array_of_numbers() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		assertTrue(a1.has("n"));
		assertEquals(1, a1.get("n").getNumberValue());
		JsonNode a2 = a.get(1);
		assertTrue(a2.has("n"));
		assertEquals(2, a2.get("n").getNumberValue());
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
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		String key = "l";
		assertTrue(a1.has(key));
		assertEquals(l1.getReference(), a1.get(key).getValueAsText());
		JsonNode a2 = a.get(1);
		assertTrue(a2.has(key));
		assertEquals(l2.getReference(), a2.get(key).getValueAsText());
	}

	@Test
	public void it_should_write_an_array_of_booleans() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(true);
		list.add(false);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		String key = "b";
		assertTrue(a1.has(key));
		assertEquals(true, a1.get(key).getBooleanValue());
		JsonNode a2 = a.get(1);
		assertTrue(a2.has(key));
		assertEquals(false, a2.get(key).getBooleanValue());
	}

	@Test
	public void it_should_write_an_array_of_constructs() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Construct> list = new ArrayList<Construct>();
		list.add(createSimpleNamedTopic("class:" + getClass().getName()));
		list.add(createName("Name"));
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		String key = "i";
		assertTrue(a1.has(key));
		assertEquals("topic", a1.get("i").get("item_type").getTextValue());
		JsonNode a2 = a.get(1);
		assertTrue(a2.has(key));
		assertEquals("name", a2.get("i").get("item_type").getTextValue());
	}

	@Test
	public void it_should_write_an_array_of_nulls() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms, topicMap));
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(null);
		list.add(null);
		tuple.add(list);
		JsonNode tupleNode = writeAndRead(tuple).get("t");
		assertTrue(tupleNode.isArray());
		assertEquals(1, tupleNode.size());
		JsonNode first = tupleNode.get(0);
		assertTrue(first.has("a"));
		JsonNode a = first.get("a");
		assertTrue(a.isArray());
		JsonNode a1 = a.get(0);
		assertTrue(a1.size() == 0);
		JsonNode a2 = a.get(1);
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
