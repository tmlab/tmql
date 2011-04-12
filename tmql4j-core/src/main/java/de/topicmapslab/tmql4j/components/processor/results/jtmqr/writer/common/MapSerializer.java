package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.IJtmQrKeys;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.JTMQRWriterUtils;
import de.topicmapslab.tmql4j.util.CollectionsUtility;

/**
 * Serializing a map
 * 
 * @author Sven Krosse
 * 
 */
public class MapSerializer extends JsonSerializer<Map<?, ?>> {
	/**
	 * Abstract class for calling JSON generator
	 * 
	 * @author Sven Krosse
	 * 
	 */
	@JsonSerialize(using = MapSerializer.class)
	public abstract class MapMixIn {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Map<?, ?> tuple, JsonGenerator jgen, SerializerProvider provider) throws IOException {

		jgen.writeStartObject();
		jgen.writeFieldName(IJtmQrKeys.TUPLE);
		jgen.writeStartArray();
		{
			for (final String key : CollectionsUtility.getOrderedKeys((Map<String, Object>) tuple)) {
				Object value = tuple.get(key);

				jgen.writeStartObject();
				/*
				 * write field value
				 */
				JTMQRWriterUtils.handleObjectValue(jgen, value);
				jgen.writeEndObject();
			}
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

}
