package de.topicmapslab.tmql4j.tests.jtmqr;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Author: mhoyer Created: 28.10.2010 02:36:50
 */
public class when_writing_a_single_tuple_of_constructs extends with_TopicMap {
	private JsonNode tupleNode;

	@Before
	public void given_a_simple_tuple_of_literals_as_json_node() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(tms,topicMap));
		tuple.add(createSimpleNamedTopic("class:" + getClass().getName()));
		tuple.add(createName(getClass().getSimpleName()));

		tupleNode = writeAndRead(tuple).get("t");
		assertNotNull(tupleNode);
	}

	@Test
	public void it_should_write_all_tuple_items() {
		assertTrue(tupleNode.isArray());
		assertEquals(2, tupleNode.size());
	}

	@Test
	public void it_should_write_tuple_items_as_JTM_construct_nodes() {
		assertEquals("topic", tupleNode.get(0).get("i").get("item_type").getTextValue());
		assertEquals("name", tupleNode.get(1).get("i").get("item_type").getTextValue());
	}
}
