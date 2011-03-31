package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;

/**
 * 
 * @author Christian Haß
 *
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
		jgen.writeStartArray();

		/*
		 * add content
		 */
		for (Object result : tuple.getResults()) {
			/*
			 * write field value
			 */
			JTMQR2WriterUtils.handleObjectValue(jgen, result);
		}

		/*
		 * finish JSON object
		 */
		jgen.writeEndArray();
	}

}
