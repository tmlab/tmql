package de.topicmapslab.tmql4j.tests.jtmqr.integration;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

/**
 * Author: mhoyer Created: 28.10.2010 11:55:43
 */
public class when_converting_simple_query_on_ToyTM extends with_TMQL_runtime_on_ToyTM {
	private JsonNode rootNode;

	@Before
	public void given_the_query_result() throws IOException {
		String query = "tm:subject >> instances";
		IResultSet<?> result = query(query);
		rootNode = writeAndRead(result);
	}

	@Test
	@Ignore
	public void it_should_write_all_tuples() {
		int topicsCount = toytm.getTopics().size();
		assertTrue(rootNode.get("seq").isArray());
		assertEquals(topicsCount, rootNode.get("seq").size());
	}

	@Test
	@Ignore
	public void it_should_write_all_topics() {
		int topicsCount = toytm.getTopics().size();

		for (int i = 0; i < topicsCount; i++) {
			assertEquals(1, rootNode.get("seq").get(i).get("t").size());
			assertEquals("topic", rootNode.get("seq").get(i).get("t").get(0).get("i").get("item_type").getTextValue());
		}
	}
}
