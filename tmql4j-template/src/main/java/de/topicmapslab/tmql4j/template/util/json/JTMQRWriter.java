package de.topicmapslab.tmql4j.template.util.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.map.ObjectMapper;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * A JTMQR writer 
 *
 */
public class JTMQRWriter {

	/**
	 * the UTF-8 constant
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * Writes the given matches as JTMQR
	 * 
	 * @param matches
	 *            the matches
	 * @return the JTMQR string
	 * @throws TMQLRuntimeException
	 *             thrown if any IO error occurs
	 */
	public static String write(QueryMatches matches) throws TMQLRuntimeException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			write(stream, matches);
			stream.flush();
			return stream.toString(UTF_8);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to JTMQR", e);
		}
	}

	/**
	 * Writes the given matches as JTMQR to the output stream
	 * 
	 * @param outputStream
	 *            the stream
	 * @param matches
	 *            the matches
	 * @throws TMQLRuntimeException
	 *             thrown if any IO error occurs
	 */
	public static void write(OutputStream outputStream, QueryMatches matches) throws TMQLRuntimeException {
		try {
			ObjectMapper mapper = new JTMQRMapper();
			mapper.writeValue(outputStream, matches);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to JTMQR", e);
		}
	}

}
