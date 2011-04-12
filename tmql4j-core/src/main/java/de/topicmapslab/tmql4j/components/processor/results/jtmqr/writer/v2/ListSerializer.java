package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;


public class ListSerializer extends JsonSerializer<List<?>> {
	
	@JsonSerialize(using = ListSerializer.class)
	public abstract class ListMixIn {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(List<?> list, JsonGenerator jgen, SerializerProvider provider) throws IOException {

		jgen.writeStartArray();
		{
			for (final Object value : list) {
				if (value instanceof List<?>) {
					throw new JsonGenerationException("List cannot contain list again!");
				}

				/*
				 * write field value
				 */
				JTMQR2WriterUtils.handleObjectValue(jgen, value);
			}
		}
		jgen.writeEndArray();
	}

}
