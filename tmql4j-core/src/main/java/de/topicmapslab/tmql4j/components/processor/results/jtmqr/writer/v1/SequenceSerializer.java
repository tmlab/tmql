package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

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
		addMetadata(sequence, jgen);
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

	/**
	 * Utility method to write meta data field
	 * 
	 * @param sequence
	 *            the sequence
	 * @param jgen
	 *            the generator
	 * @throws IOException
	 *             thrown if anything fails
	 */
	public void addMetadata(SimpleResultSet sequence, JsonGenerator jgen) throws IOException {
		jgen.writeFieldName(IJtmQrKeys.METADATA);
		jgen.writeStartObject();

		/*
		 * write number of rows
		 */
		jgen.writeNumberField(IJtmQrKeys.ROWS, sequence.size());
		/*
		 * write number of columns
		 */
		int cols = sequence.isEmpty() ? 0 : sequence.get(0).size();
		jgen.writeNumberField(IJtmQrKeys.COLUMNS, cols);
		/*
		 * write alias
		 */
		if (sequence.hasAlias()) {
			jgen.writeFieldName(IJtmQrKeys.ALIASES);
			jgen.writeStartObject();
			for (int col = 0; col < cols; col++) {
				String alias = sequence.getAlias(col);
				if (alias != null) {
					jgen.writeStringField(Integer.toString(col), alias);
				} else {
					jgen.writeNullField(Integer.toString(col));
				}
			}
			jgen.writeEndObject();			
		}
		/*
		 * end meta data
		 */
		jgen.writeEndObject();
	}

}
