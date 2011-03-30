package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.IJtmQrKeys;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.JTMQRWriterUtils;

/**
 * Serializing a map
 * 
 * @author Sven Krosse
 * 
 */
public class ListSerializer extends JsonSerializer<List<?>> {
	/**
	 * Abstract class for calling JSON generator
	 * 
	 * @author Sven Krosse
	 * 
	 */
	@JsonSerialize(using = ListSerializer.class)
	public abstract class ListMixIn {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(List<?> list, JsonGenerator jgen, SerializerProvider provider) throws IOException {

		// jgen.writeStartObject();
		jgen.writeFieldName(IJtmQrKeys.ARRAY);
		jgen.writeStartArray();
		{
			for (final Object value : list) {
				if (value instanceof List<?>) {
					throw new JsonGenerationException("List cannot contain list again!");
				}

				jgen.writeStartObject();
				/*
				 * write field value
				 */
				JTMQRWriterUtils.handleObjectValue(jgen, value);
				jgen.writeEndObject();
			}
		}
		jgen.writeEndArray();
		// jgen.writeEndObject();
	}

}
