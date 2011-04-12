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

public class when_writing_a_sequence_of_tuples extends with_JTMQR2Writer {
	private JsonNode sequenceNode;
	private JsonNode rootNode;

	@Before
	public void given_a_simple_result_set_of_tuples_as_json_node() throws IOException {
		SimpleResultSet resultSet = new SimpleResultSet(null,null);
		resultSet.addResult(new SimpleResult(resultSet) {
			{
				add(getClass().getName());
			}
		});
		resultSet.addResult(new SimpleResult(resultSet) {
			{
				add(getClass().getPackage().getName());
				add(getClass().getSimpleName());
			}
		});

		this.rootNode = writeAndRead(resultSet);
		this.sequenceNode = rootNode.get("tuples");
		assertNotNull(this.sequenceNode);
		System.out.println(this.sequenceNode.toString());
	}

	@Test
	public void it_should_write_the_version() {
		assertEquals(2.0, rootNode.get("version").getNumberValue());
	}

	@Test
	public void it_should_write_all_tuples() {
		assertTrue(sequenceNode.isArray());
		assertEquals(2, sequenceNode.size());
	}

	@Test
	public void it_should_write_sequence_as_arrays() {
		assertTrue(this.sequenceNode.get(0).isArray());
		assertTrue(this.sequenceNode.get(1).isArray());
		assertEquals(1, sequenceNode.get(0).size());
		assertEquals(2, sequenceNode.get(1).size());
	}
}
