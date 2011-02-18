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
	 * 
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
		jgen.writeNumberField(IJtmQrKeys.VERSION, 1.0);

		addMetadata(matches, jgen);
		
		jgen.writeFieldName(IJtmQrKeys.SEQUENCE);
		jgen.writeStartArray();
		{
			for (Map<String, Object> tuple : ProjectionUtils.asTwoDimensionalMap(matches)) {
				jgen.writeObject(tuple);
			}
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	/**
	 * Utility method to write meta data field
	 * 
	 * @param matches
	 *            the matches
	 * @param jgen
	 *            the generator
	 * @throws IOException
	 *             thrown if anything fails
	 */
	public void addMetadata(QueryMatches matches, JsonGenerator jgen) throws IOException {
		jgen.writeFieldName(IJtmQrKeys.METADATA);
		jgen.writeStartObject();

		/*
		 * write number of rows
		 */
		jgen.writeNumberField(IJtmQrKeys.ROWS, matches.size());
		/*
		 * write number of columns
		 */
		jgen.writeNumberField(IJtmQrKeys.COLUMNS, matches.getOrderedKeys().size());
		/*
		 * write alias
		 */
		if (matches.getColumnAlias() != null) {
			Map<Integer, String> aliases = matches.getColumnAlias();
			jgen.writeFieldName(IJtmQrKeys.ALIASES);
			jgen.writeStartObject();
			for (int col = 0; col < matches.getOrderedKeys().size(); col++) {
				String alias = aliases.get(col);
				if (alias != null) {
					jgen.writeStringField(Integer.toString(col), alias);
				} else {
					jgen.writeNullField(Integer.toString(col));
				}
			}
			jgen.writeEndObject();
		}
		jgen.writeEndObject();
	}

}
