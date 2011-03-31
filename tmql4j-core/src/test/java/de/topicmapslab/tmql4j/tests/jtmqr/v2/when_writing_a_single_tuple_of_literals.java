package de.topicmapslab.tmql4j.tests.jtmqr.v2;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class when_writing_a_single_tuple_of_literals extends with_JTMQR2Writer {
	private JsonNode tupleNode;

	@Before
	public void given_a_simple_tuple_of_literals_as_json_node() throws IOException {
		SimpleResult tuple = new SimpleResult(new SimpleResultSet(null,null));
		tuple.add(getClass().getName());
		tuple.add(getClass().getSimpleName());

		this.tupleNode = writeAndRead(tuple);
		assertNotNull(this.tupleNode);
	}

	@Test
	public void it_should_write_all_tuple_items() {
		assertTrue(tupleNode.isArray());
		assertEquals(2, tupleNode.size());
	}

	@Test
	public void it_should_write_tuple_items_as_string_literal() {
		assertEquals(getClass().getName(), tupleNode.get(0).getTextValue());
		assertEquals(getClass().getSimpleName(), tupleNode.get(1).getTextValue());
	}
}
