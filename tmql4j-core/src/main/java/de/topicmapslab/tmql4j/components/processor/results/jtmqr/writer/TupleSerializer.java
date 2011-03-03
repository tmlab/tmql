package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Construct;

import de.topicmapslab.jtm.writer.JTMVersion;
import de.topicmapslab.jtm.writer.JTMWriter;
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

			if (result instanceof Number) {
				jgen.writeObjectField(IJtmQrKeys.NUMBER, result);
			} else if (result instanceof String) {
				jgen.writeStringField(IJtmQrKeys.STRING, result.toString());
			} else if (result instanceof Construct) {
				writeConstruct(jgen, (Construct) result);
			} else if (result instanceof Boolean) {
				jgen.writeBooleanField(IJtmQrKeys.BOOLEAN, Boolean.valueOf(result.toString()));
			}
			jgen.writeEndObject();
		}

		/*
		 * finish JSON object
		 */
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	/**
	 * Internal method to write the construct part
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param construct
	 *            the construct to transform
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	private void writeConstruct(JsonGenerator jgen, Construct construct) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMWriter jtmWriter = new JTMWriter(buffer);
		jtmWriter.write(construct, JTMVersion.JTM_1_1).flush();

		jgen.writeRaw(IJtmQrKeys.QUOTED_ITEM + buffer.toString());
	}

}
