package de.topicmapslab.tmql4j.tests.jtmqr.v1.integration;

import java.io.IOException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

/**
 * Author: mhoyer Created: 31.01.11 14:17
 */
public class when_converting_a_construct_with_type_containing_only_an_item_identifier extends with_TMQL_runtime_on_ToyTM {
	private JsonNode rootNode;

	@Before
	public void given_the_query_result() throws IOException {
		String query = "http://toytm%23michael_jackson << players";
		IResultSet<?> result = query(query);
		rootNode = writeAndRead(result);
	}

	@Test
	@Ignore
	public void it_should_write_all_item_identifier_references_in_jtm_style_with_ii_prefix() {
		Assert.assertTrue(rootNode.get("seq").get(0).get("t").get(0).get("i").get("type").getValueAsText().startsWith("ii:"));
	}
}
