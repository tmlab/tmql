package de.topicmapslab.tmql4j.tests.jtmqr.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRWriter;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;

/**
 * Author: mhoyer Created: 28.10.2010 10:07:01
 */
public class with_JTMQRWriter {
	protected ByteArrayOutputStream out;
	protected JTMQRWriter jtmqrWriter;
	private ObjectMapper jsonReader;

	@Before
	public void given_the_JTMQRWriter() {
		out = new ByteArrayOutputStream();
		jtmqrWriter = new JTMQRWriter(out);
	}

	@Before
	public void given_JSON_reader() {
		jsonReader = new ObjectMapper();
	}

	protected JsonNode writeAndRead(SimpleResult result) throws IOException {
		jtmqrWriter.write(result);
		jtmqrWriter.flush();
		System.out.println(out.toString());
		return jsonReader.readTree(out.toString());
	}

	protected JsonNode writeAndRead(IResultSet<?> resultSet) throws IOException {
		jtmqrWriter.write(resultSet);
		jtmqrWriter.flush();
		return jsonReader.readTree(out.toString());
	}
}
