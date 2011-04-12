package de.topicmapslab.tmql4j.tests.jtmqr.v1;

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
 * Author: mhoyer Created: 28.10.2010 11:29
 */
public class when_writing_a_sequence_of_tuples extends with_JTMQRWriter {
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

		rootNode = writeAndRead(resultSet);
		sequenceNode = rootNode.get("seq");
		assertNotNull(sequenceNode);
	}

	@Test
	public void it_should_write_the_version() {
		assertEquals(1.0, rootNode.get("version").getNumberValue());
	}

	@Test
	public void it_should_write_all_tuples() {
		assertTrue(sequenceNode.isArray());
		assertEquals(2, sequenceNode.size());
	}

	@Test
	public void it_should_write_sequence_as_set_of_tuples() {
		assertEquals(1, sequenceNode.get(0).get("t").size());
		assertEquals(2, sequenceNode.get(1).get("t").size());
	}
}
