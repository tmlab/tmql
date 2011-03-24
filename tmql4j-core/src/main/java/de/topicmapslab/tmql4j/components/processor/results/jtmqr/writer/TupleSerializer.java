package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;

/**
 * Author: mhoyer Created: 28.10.2010 10:26:24
 */
public class TupleSerializer extends JsonSerializer<SimpleResult> {

	/**
	 * Internal class representing the serializer
	 */
	@JsonSerialize(using = TupleSerializer.class)
	public abstract class SimpleTupleResultMixIn {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(SimpleResult tuple, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		/*
		 * start JSON object
		 */
		jgen.writeStartObject();
		jgen.writeFieldName(IJtmQrKeys.TUPLE);
		jgen.writeStartArray();

		/*
		 * add content
		 */
		for (Object result : tuple.getResults()) {
			jgen.writeStartObject();
			/*
			 * write field value
			 */
			JTMQRWriterUtils.handleObjectValue(jgen, result);
			jgen.writeEndObject();
		}

		/*
		 * finish JSON object
		 */
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

}
