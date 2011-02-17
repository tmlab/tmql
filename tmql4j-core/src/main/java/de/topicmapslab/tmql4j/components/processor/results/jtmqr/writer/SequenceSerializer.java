package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

/**
 * Author: mhoyer Created: 28.10.2010 11:27
 */
public class SequenceSerializer extends JsonSerializer<SimpleResultSet> {
	/**
	 * Internal class representing the content serializer
	 */
	@JsonSerialize(using = SequenceSerializer.class)
	public abstract class SimpleResultSetMixIn {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(SimpleResultSet sequence, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		/*
		 * start the JSON object
		 */
		jgen.writeStartObject();
		jgen.writeNumberField(IJtmQrKeys.VERSION, 1.0);
		jgen.writeFieldName(IJtmQrKeys.SEQUENCE);
		jgen.writeStartArray();

		/*
		 * write content
		 */
		for (SimpleResult result : sequence.getResults()) {
			jgen.writeObject(result);
		}

		/*
		 * finish JSON content
		 */
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

}
