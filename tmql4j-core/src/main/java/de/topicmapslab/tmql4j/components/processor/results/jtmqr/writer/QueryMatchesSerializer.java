package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.model.ProjectionUtils;

/**
 * Serializer for {@link QueryMatches}
 * 
 * @author Sven Krosse
 *
 */
public class QueryMatchesSerializer extends JsonSerializer<QueryMatches> {
	
	/**
	 * Abstract class for {@link QueryMatches} mix in
	 * @author Sven Krosse
	 *
	 */
	@JsonSerialize(using = QueryMatchesSerializer.class)
	public abstract class QueryMatchesMixIn {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(QueryMatches matches, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
		jgen.writeNumberField("version", 1.0);
		jgen.writeFieldName("seq");
		jgen.writeStartArray();
		{
			for (Map<String, Object> tuple : ProjectionUtils.asTwoDimensionalMap(matches)) {
				jgen.writeObject(tuple);
			}
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

}
