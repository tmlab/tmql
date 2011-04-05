package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.JTMQRMapper;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2.JTMQR2Mapper;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: mhoyer Created: 28.10.2010 02:26:45
 */
public class JTMQRWriter {
	private static final String UTF_8 = "UTF-8";
	private OutputStream out;
	private ObjectMapper mapper;
	
	private final JTMQRFormat format;

	public JTMQRWriter(OutputStream outputStream) {
		this(outputStream, JTMQRFormat.JTMQR_1);
	}
	
	public JTMQRWriter(OutputStream outputStream, JTMQRFormat format) {
		out = outputStream;
		this.format = format;
		
		if(this.format.equals(JTMQRFormat.JTMQR_1)){
			this.mapper = new JTMQRMapper();
		}else if(this.format.equals(JTMQRFormat.JTMQR_2)){
			this.mapper = new JTMQR2Mapper();
		}else{
			throw new RuntimeException("Unexpected JTMQR Format");
		}
		
	}

	/**
	 * Writes the given result to JTMQR
	 * 
	 * @param result
	 *            the result to write
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public void write(IResult result) throws IOException {
		mapper.writeValue(out, result);
	}

	/**
	 * Writes the given result set to JTMQR
	 * 
	 * @param resultSet
	 *            the result set to write
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public void write(IResultSet<?> resultSet) throws IOException {
		if (resultSet instanceof SimpleResultSet) {
			mapper.writeValue(out, resultSet);
		}else{
			throw new JsonGenerationException("Only simple query results are supported by now. (no CTM or XTM).");
		}
	}

	/**
	 * Flush the internal stream
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/**
	 * Static method to transform the given result to an JSON string.
	 * 
	 * @param result
	 *            the result to transform
	 * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static String getJson(IResult result) throws IOException {
		return getJsonAsStream(result).toString(UTF_8);
	}

	/**
	 * Static method to transform the given result to a byte array containing
	 * JSON string.
	 * 
	 * @param result
	 *            the result to transform
	 * @return the JSON string as byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static byte[] getJsonAsByteArray(IResult result) throws IOException {
		return getJsonAsStream(result).toByteArray();
	}

	/**
	 * Static method to transform the given result to a
	 * {@link ByteArrayOutputStream} containing JSON string.
	 * 
	 * @param result
	 *            the result to transform
	 * @return the JSON string as {@link ByteArrayOutputStream}
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static ByteArrayOutputStream getJsonAsStream(IResult result) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMQRWriter writer = new JTMQRWriter(buffer);
		writer.write(result);
		writer.flush();
		return buffer;
	}

	/**
	 * Static method to transform the given result set to an JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
	 * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static String getJson(IResultSet<?> resultSet) throws IOException {
		return getJsonAsStream(resultSet).toString();
	}

	/**
	 * Static method to transform the given result set to a byte array
	 * containing JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
	 * @return the JSON string as byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static byte[] getJsonAsByteArray(IResultSet<?> resultSet) throws IOException {
		return getJsonAsStream(resultSet).toByteArray();
	}

	/**
	 * Static method to transform the given result set to a
	 * {@link ByteArrayOutputStream} containing JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
	 * @return the JSON string as {@link ByteArrayOutputStream}
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static ByteArrayOutputStream getJsonAsStream(IResultSet<?> resultSet) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMQRWriter writer = new JTMQRWriter(buffer);
		writer.write(resultSet);
		writer.flush();
		return buffer;
	}

	/**
	 * Writes the given matches as JTMQR
	 * 
	 * @param matches
	 *            the matches
	 * @return the JTMQR string
	 * @throws TMQLRuntimeException
	 *             thrown if any IO error occurs
	 */
	public static String getJson(QueryMatches matches) throws TMQLRuntimeException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			getJsonAsStream(stream, matches);
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
	public static void getJsonAsStream(OutputStream outputStream, QueryMatches matches) throws TMQLRuntimeException {
		try {
			ObjectMapper mapper = new JTMQRMapper();
			mapper.writeValue(outputStream, matches);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to JTMQR", e);
		}
	}

}
