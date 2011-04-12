package de.topicmapslab.tmql4j.tests.jtmqr.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRFormat;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRWriter;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2.JTMQR2WriterUtils;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;


public class with_JTMQR2Writer {
	
	protected ByteArrayOutputStream out;
	protected JTMQRWriter jtmqrWriter;
	private ObjectMapper jsonReader;

	@Before
	public void before() {
		this.out = new ByteArrayOutputStream();
		this.jtmqrWriter = new JTMQRWriter(this.out, JTMQRFormat.JTMQR_2);
	}

	@Before
	public void initReader() {
		this.jsonReader = new ObjectMapper();
	}

	protected JsonNode writeAndRead(SimpleResult result) throws IOException {
		this.jtmqrWriter.write(result);
		this.jtmqrWriter.flush();
		return this.jsonReader.readTree(this.out.toString());
	}

	protected JsonNode writeAndRead(IResultSet<?> resultSet) throws IOException {
		this.jtmqrWriter.write(resultSet);
		this.jtmqrWriter.flush();
		return this.jsonReader.readTree(this.out.toString());
	}
}
