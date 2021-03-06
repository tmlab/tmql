/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.UUID;

import org.tmapi.core.Construct;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapix.io.XTM2TopicMapWriter;
import org.tmapix.io.XTMTopicMapReader;
import org.tmapix.io.XTMVersion;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Utility class to transform topic map fragments to XML or parse XTM fragments
 * an create the corresponding topic map fragment.
 * 
 * <p>
 * The class internal use the {@link XTM20TopicMapReader} and
 * {@link XTM20TopicMapWriter}.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XTMConverter {

	public static final String encoding = "UTF-8";

	/**
	 * private and hidden constructor
	 */
	private XTMConverter() {
	}

	/**
	 * Method transform the given XTM fragment to the corresponding topic map
	 * fragment. The method parse the given XTM string with the
	 * {@link XTM20TopicMapReader} and create a new instance of {@link TopicMap}
	 * by using the given topic map system.
	 * 
	 * @param tms
	 *            the topic map system used to create a new topic map
	 * 
	 * @param xtm
	 *            the XTM fragment
	 * @return the new topic map fragment and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if XTM parsing fails
	 */
	public static TopicMap toTopicMap(TopicMapSystem tms, final String xtm) throws TMQLRuntimeException {
		try {
			/*
			 * create a temporary file containing the CTM fragment
			 */
			File file = File.createTempFile("xtm-snippet", ".tmp");
			FileWriter writer = new FileWriter(file);
			writer.write(xtm);
			writer.flush();
			writer.close();

			/*
			 * create a new topic map
			 */
			TopicMap map = tms.createTopicMap("http://xtm-snippet-" + UUID.randomUUID().toString());
			/*
			 * parse the XTM fragment
			 */
			XTMTopicMapReader reader = new XTMTopicMapReader(map, file);
			reader.read();

			/*
			 * delete temporary file
			 */
			file.deleteOnExit();
			return map;
		} catch (Exception ex) {
			throw new TMQLRuntimeException("Cannot convert construct to XTM", ex);
		}
	}

	/**
	 * Method transform the given topic map items to the corresponding XTM
	 * fragment. The method serialize the given topic map with the
	 * {@link XTM20TopicMapWriter}.
	 * 
	 * @param tms
	 *            the topic map system used to create a new topic map
	 * @param values
	 *            set containing all values to convert to XTM
	 * @return the string-represented XTM-fragment
	 * @throws TMQLRuntimeException
	 *             thrown if items can not be serialized
	 */
	public static String toXTMString(TopicMapSystem tms, Collection<?> values) throws TMQLRuntimeException {
		StringBuilder builder = new StringBuilder();

		for (Object o : values) {
			builder.append(getReplacement(tms, o));
		}
		return builder.toString();
	}

	/**
	 * Internal method which transform the given value of the matches to their
	 * XML-string-representation.
	 * <p>
	 * If value is a topic, the XTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an occurrence, the XTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an name, the XTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an association, the XTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is a sequence, the method will called incremental for each
	 * element
	 * </p>
	 * <p>
	 * If value is an atom, the string-representation will be returned.
	 * </p>
	 * 
	 * @param tms
	 *            the topic map system used to create a new topic map
	 * @param value
	 *            the value to transform
	 * @return the XTM-string-representation
	 * @throws TMQLRuntimeException
	 *             thrown if serialization fails
	 */
	private static final String getReplacement(TopicMapSystem tms, Object value) throws TMQLRuntimeException {
		StringBuilder builder = new StringBuilder();
		/*
		 * check if value is a tuple sequence
		 */
		if (value instanceof Collection<?>) {
			for (Object object : (Collection<?>) value) {
				builder.append(getReplacement(tms, object) + "\r\n");
			}
		}
		/*
		 * check if value is a topic map item
		 */
		else if (value instanceof Construct) {
			try {
				/*
				 * create temporary output stream
				 */
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				/*
				 * serialize topic map construct
				 */
				XTM2TopicMapWriter writer = new XTM2TopicMapWriter(stream, "www.topicmapslab.de", XTMVersion.XTM_2_0);
				TopicMap map = tms.createTopicMap("http://xtm-conversion-" + UUID.randomUUID().toString());
				writer.write(new TMAPICloner(map).clone((Construct) value));
				builder.append(cleanXTM2(stream.toString(encoding)));
				map.remove();
			} catch (Exception e) {
				throw new TMQLRuntimeException("Cannot convert construct to XTM.", e);
			}
		} else if (value != null) {
			builder.append(value.toString());
		} else {
			builder.append("un-def");
		}
		return builder.toString();
	}

	/**
	 * Method cleans the serialized XTM-string representation of the topic map
	 * by removing the XTM-head information from the XML file.
	 * 
	 * @param xtm2
	 *            the XTM-20 content
	 * @return the cleaned XTM content
	 */
	private static String cleanXTM2(final String xtm2) {
		int fromIndex = 0;
		fromIndex = xtm2.indexOf("<topicMap");
		fromIndex = xtm2.indexOf(">", fromIndex) + 1;

		int toIndex = xtm2.lastIndexOf("</");

		if (fromIndex != -1 && toIndex != -1) {
			return xtm2.substring(fromIndex, toIndex);
		}
		return xtm2;
	}

}
