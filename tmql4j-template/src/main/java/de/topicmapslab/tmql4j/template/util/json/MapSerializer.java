package de.topicmapslab.tmql4j.template.util.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Construct;

import de.topicmapslab.JTMWriter;
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
		jgen.writeFieldName("t");
		jgen.writeStartArray();
		{
			for (final String key : CollectionsUtility.getOrderedKeys((Map<String, Object>) tuple)) {
				Object value = tuple.get(key);

				jgen.writeStartObject();

				if (value instanceof String) {
					jgen.writeStringField("s", value.toString());
				} else if (value instanceof Construct) {
					writeConstruct(jgen, (Construct) value);
				} else if (value instanceof Long) {
					jgen.writeNumberField("n", (Long) value);
				} else if (value instanceof Integer) {
					jgen.writeNumberField("n", (Integer) value);
				} else if (value instanceof Double) {
					jgen.writeNumberField("n", (Double) value);
				} else if (value instanceof Float) {
					jgen.writeNumberField("n", (Float) value);
				} else {
					jgen.writeStringField("s", value.toString());
				}
				jgen.writeEndObject();
			}
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	/**
	 * Utility method to write the construct to JSON
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param construct
	 *            the construct
	 * @throws IOException
	 */
	private void writeConstruct(JsonGenerator jgen, Construct construct) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMWriter jtmWriter = new JTMWriter(buffer);
		jtmWriter.write(construct).flush();

		jgen.writeRaw("\"i\":" + buffer.toString());
	}

}
