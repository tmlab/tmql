package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * 
 * @author Christian Haß
 *
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
		jgen.writeNumberField(IJtmQr2Keys.VERSION, 2.0);
		addMetadata(sequence, jgen);
		jgen.writeFieldName(IJtmQr2Keys.TUPLES);
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
		jgen.writeFieldName(IJtmQr2Keys.METADATA);
		jgen.writeStartObject();

		/*
		 * write number of rows
		 */
		jgen.writeNumberField(IJtmQr2Keys.ROWS, sequence.size());
		/*
		 * write number of columns
		 */
		int cols = sequence.isEmpty() ? 0 : sequence.get(0).size();
		jgen.writeNumberField(IJtmQr2Keys.COLUMNS, cols);
		
		/*
		 * write header, i.e. alias
		 */
		if (sequence.hasAlias()) {
			jgen.writeFieldName(IJtmQr2Keys.HEADERS);
			jgen.writeStartArray();
			for (int col = 0; col < cols; col++) {
				String alias = sequence.getAlias(col);
				if (alias != null) {
					jgen.writeString(alias);
				} else {
					jgen.writeNull();
				}
			}
			jgen.writeEndArray();		
		}
		/*
		 * end meta data
		 */
		jgen.writeEndObject();
	}

}
