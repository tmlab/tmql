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

	public JTMQRWriter(OutputStream outputStream) {
		this(outputStream, JTMQRFormat.JTMQR_1);
	}
	
	public JTMQRWriter(OutputStream outputStream, JTMQRFormat format) {
		out = outputStream;
        mapper = getMapper(format);
    }

    private static ObjectMapper getMapper(JTMQRFormat format) {
        if(format.equals(JTMQRFormat.JTMQR_1)) return new JTMQRMapper();
        if(format.equals(JTMQRFormat.JTMQR_2)) return new JTMQR2Mapper();

        throw new RuntimeException("Unexpected JTMQR Format");
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
	 * @param result the result to transform
	 * @param format the JTMQR version to be used for serialization.
     * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static String getJson(IResult result, JTMQRFormat format) throws IOException {
		return getJsonAsStream(result, format).toString(UTF_8);
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJson(de.topicmapslab.tmql4j.components.processor.results.model.IResult, JTMQRFormat)
     */
	public static String getJson(IResult result) throws IOException {
		return getJson(result, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Static method to transform the given result to a byte array containing
	 * JSON string.
	 * 
	 * @param result
	 *            the result to transform
     * @param format the JTMQR version to be used for serialization.
	 * @return the JSON string as byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static byte[] getJsonAsByteArray(IResult result, JTMQRFormat format) throws IOException {
		return getJsonAsStream(result, format).toByteArray();
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJsonAsByteArray(de.topicmapslab.tmql4j.components.processor.results.model.IResult, JTMQRFormat)
     */
	public static byte[] getJsonAsByteArray(IResult result) throws IOException {
		return getJsonAsByteArray(result, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Static method to transform the given result to a
	 * {@link ByteArrayOutputStream} containing JSON string.
	 * 
	 * @param result
	 *            the result to transform
     * @param format the JTMQR version to be used for serialization.
	 * @return the JSON string as {@link ByteArrayOutputStream}
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static ByteArrayOutputStream getJsonAsStream(IResult result, JTMQRFormat format) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMQRWriter writer = new JTMQRWriter(buffer, format);
		writer.write(result);
		writer.flush();
		return buffer;
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJson(de.topicmapslab.tmql4j.components.processor.results.model.IResult, JTMQRFormat)
     */
	public static ByteArrayOutputStream getJsonAsStream(IResult result) throws IOException {
		return getJsonAsStream(result, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Static method to transform the given result set to an JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
     * @param format the JTMQR version to be used for serialization.
	 * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static String getJson(IResultSet<?> resultSet, JTMQRFormat format) throws IOException {
		return getJsonAsStream(resultSet, format).toString();
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJson(de.topicmapslab.tmql4j.components.processor.results.model.IResultSet, JTMQRFormat) 
     */
	public static String getJson(IResultSet<?> resultSet) throws IOException {
		return getJson(resultSet, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Static method to transform the given result set to a byte array
	 * containing JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
     * @param format the JTMQR version to be used for serialization.
	 * @return the JSON string as byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static byte[] getJsonAsByteArray(IResultSet<?> resultSet, JTMQRFormat format) throws IOException {
		return getJsonAsStream(resultSet, format).toByteArray();
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJsonAsByteArray(de.topicmapslab.tmql4j.components.processor.results.model.IResultSet, JTMQRFormat) 
     */
	public static byte[] getJsonAsByteArray(IResultSet<?> resultSet) throws IOException {
		return getJsonAsByteArray(resultSet, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Static method to transform the given result set to a
	 * {@link ByteArrayOutputStream} containing JSON string.
	 * 
	 * @param resultSet
	 *            the result set to transform
     * @param format the JTMQR version to be used for serialization.
	 * @return the JSON string as {@link ByteArrayOutputStream}
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static ByteArrayOutputStream getJsonAsStream(IResultSet<?> resultSet, JTMQRFormat format) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JTMQRWriter writer = new JTMQRWriter(buffer, format);
		writer.write(resultSet);
		writer.flush();
		return buffer;
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJsonAsStream(de.topicmapslab.tmql4j.components.processor.results.model.IResultSet, JTMQRFormat) 
     */
	public static ByteArrayOutputStream getJsonAsStream(IResultSet<?> resultSet) throws IOException {
		return getJsonAsStream(resultSet, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Writes the given matches as JTMQR
	 * 
	 * @param matches
	 *            the matches
     * @param format the JTMQR version to be used for serialization.
	 * @return the JTMQR string
	 * @throws TMQLRuntimeException
	 *             thrown if any IO error occurs
	 */
	public static String getJson(QueryMatches matches, JTMQRFormat format) throws TMQLRuntimeException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			getJsonAsStream(stream, matches, format);
			stream.flush();
			return stream.toString(UTF_8);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to JTMQR", e);
		}
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJson(de.topicmapslab.tmql4j.components.processor.core.QueryMatches, JTMQRFormat) 
     */
	public static String getJson(QueryMatches matches) throws TMQLRuntimeException {
		return getJson(matches, JTMQRFormat.JTMQR_1);
	}

	/**
	 * Writes the given matches as JTMQR to the output stream
	 * 
	 * @param outputStream
	 *            the stream
	 * @param matches
	 *            the matches
     * @param format the JTMQR version to be used for serialization.
	 * @throws TMQLRuntimeException
	 *             thrown if any IO error occurs
	 */
	public static void getJsonAsStream(OutputStream outputStream, QueryMatches matches, JTMQRFormat format) throws TMQLRuntimeException {
		try {
			ObjectMapper mapper = getMapper(format);
			mapper.writeValue(outputStream, matches);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to JTMQR", e);
		}
	}

    /**
     * @deprecated
     * @see JTMQRWriter#getJsonAsStream(java.io.OutputStream, de.topicmapslab.tmql4j.components.processor.core.QueryMatches, JTMQRFormat)
     */
	public static void getJsonAsStream(OutputStream outputStream, QueryMatches matches) throws TMQLRuntimeException {
		getJsonAsStream(outputStream, matches, JTMQRFormat.JTMQR_1);
	}

}
