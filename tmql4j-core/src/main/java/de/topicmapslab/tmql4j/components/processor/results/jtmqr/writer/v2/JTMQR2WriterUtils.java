/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;

import de.topicmapslab.jtm.writer.JTMVersion;
import de.topicmapslab.jtm.writer.JTMWriter;

/**
 * 
 * @author Christian Haß
 *
 */
public class JTMQR2WriterUtils {

	/**
	 * Utility method to handle tuple or array value
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param value
	 *            the value to handle
	 * @throws JsonGenerationException
	 *             thrown if generation fails
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static final void handleObjectValue(JsonGenerator jgen, Object value) throws JsonGenerationException, IOException {
		if (value instanceof String) {
			jgen.writeString(value.toString());
		} else if (value instanceof Construct) {
			writeConstruct(jgen, (Construct) value);
		} else if (value instanceof Number) {
			jgen.writeObject(value);
		} else if (value instanceof Boolean) {
			jgen.writeBoolean(Boolean.valueOf(value.toString()));
		} else if (value instanceof List<?>) {
			jgen.writeObject(value);
		} else if (value instanceof Locator) {
			jgen.writeStartObject();
			jgen.writeObjectField(IJtmQr2Keys.REF, ((Locator) value).getReference());
			jgen.writeEndObject();
		} else if (value == null) {
			jgen.writeStartObject();
			jgen.writeEndObject();
		} else {
			throw new JsonGenerationException("Unsupport value for JTMQR writer '" + value.getClass().getSimpleName() + "'");
		}
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
	private static void writeConstruct(JsonGenerator jgen, Construct construct) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMWriter jtmWriter = new JTMWriter(buffer);
		jtmWriter.write(construct, JTMVersion.JTM_1_1).flush();
		jgen.writeStartObject();
		jgen.writeRaw("\"" + IJtmQr2Keys.JTM + "\":" + buffer.toString());
		jgen.writeEndObject();
	}

}
