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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.UUID;

import org.tmapi.core.Construct;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapix.io.CTMTopicMapReader;

import de.topicmapslab.ctm.writer.core.CTMTopicMapWriter;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Utility class to transform topic map fragments to CTM or parse CTM fragments
 * an create the corresponding topic map fragment.
 * 
 * <p>
 * The class internal use the {@link CTMTopicMapWriter} and
 * {@link CTMTopicMapReader}.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CTMConverter {

	/**
	 * property line of the CTM-writer
	 */
	private static final String line = "writer.features.export.itemidentifier = false, "
			+ "writer.features.prefixDetection.enabled = false, "
			+ "writer.features.templateDetection.enabled = false, "
			+ "writer.features.templateDetection.topicTemplates = false , "
			+ "writer.features.templateDetection.associationTemplates = false, "
			+ "writer.features.templateMerger.enabled = false";

	/**
	 * private and hidden constructor
	 */
	private CTMConverter() {
	}

	/**
	 * Method transform the given CTM fragment to the corresponding topic map
	 * fragment. The method parse the given CTM string with the
	 * {@link CTMTopicMapReader} and create a new instance of {@link TopicMap}
	 * by using the given topic map system.
	 * 
	 * @param tms
	 *            the topic map system used to create a new topic map
	 * @param ctm
	 *            the CTM fragment
	 * @return the new topic map fragment and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if CTM parsing fails
	 */
	public static TopicMap toTopicMap( final TopicMapSystem tms, final String ctm)
			throws TMQLRuntimeException {
		try {
			/*
			 * create a byte stream containing the CTM fragment
			 */
			ByteArrayInputStream stream = new ByteArrayInputStream(ctm
					.getBytes());

			/*
			 * create a new topic map
			 */
			final String baseLocator = "file://ctm-snippet-"
					+ UUID.randomUUID().toString();
			TopicMap map = tms.createTopicMap(baseLocator);
			/*
			 * parse the CTM fragment
			 */
			CTMTopicMapReader reader = new CTMTopicMapReader(map, stream,
					baseLocator);
			reader.read();

			return map;
		} catch (Exception ex) {
			throw new TMQLRuntimeException("Cannot convert construct to CTM",
					ex);
		}
	}

	/**
	 * Method transform the given topic map fragment to the corresponding CTM
	 * fragment. The method serialize the given topic map with the
	 * {@link CTMTopicMapWriter}.
	 * 
	 * @param topicMap
	 *            the topic map to serialize
	 * @return the string-represented CTM-fragment
	 * @throws TMQLRuntimeException
	 *             thrown if topic map fragment can not be serialized
	 */
	public static String toCTMString(TopicMap topicMap)
			throws TMQLRuntimeException {
		try {
			/*
			 * create temporary output-stream
			 */
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			/*
			 * serialize the topic map
			 */
			CTMTopicMapWriter writer = new CTMTopicMapWriter(stream,
					"www.topicmapslab.de", line);
			writer.write(topicMap);
			return stream.toString();
		} catch (Exception e) {
			throw new TMQLRuntimeException("Cannot convert construct to CTM.",
					e);
		}
	}

	/**
	 * Method transform the given topic map items to the corresponding CTM
	 * fragment. The method serialize the given topic map with the
	 * {@link CTMTopicMapWriter}.
	 * 
	 * @param values
	 *            a collection containing all values, to convert as CTM
	 * @return the string-represented CTM-fragment
	 * @throws TMQLRuntimeException
	 *             thrown if items can not be serialized
	 */
	public static String toCTMString(Collection<?> values)
			throws TMQLRuntimeException {

		StringBuilder builder = new StringBuilder();

		for ( Object o : values){
			String content = getReplacement(o);
			builder.append(content);
		}		
		return builder.toString();
	}	

	/**
	 * Internal method which transform the given value of the matches to their
	 * CTM-string-representation.
	 * <p>
	 * If value is a topic, the CTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an occurrence, the CTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an name, the CTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is an association, the CTM-specific pattern will be returned.
	 * </p>
	 * <p>
	 * If value is a sequence, the method will called incremental for each
	 * element
	 * </p>
	 * <p>
	 * If value is an atom, the string-representation will be returned.
	 * </p>
	 * 
	 * @param value
	 *            the value to transform
	 * @return the CTM-string-representation
	 * @throws TMQLRuntimeException
	 *             thrown if serialization fails
	 */
	private static final String getReplacement(Object value)
			throws TMQLRuntimeException {
		StringBuilder builder = new StringBuilder();
		/*
		 * check if value is a tuple sequence
		 */
		if (value instanceof Collection<?>) {
			for (Object object : (Collection<?>) value) {
				builder.append(getReplacement(object) + "\r\n");
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
				CTMTopicMapWriter writer = new CTMTopicMapWriter(stream,
						"www.topicmapslab.de", line);
				writer.write((Construct) value);
				builder.append(stream.toString());
			} catch (Exception e) {
				throw new TMQLRuntimeException(
						"Cannot convert construct to CTM.", e);
			}
		} else {
			builder.append(value.toString());
		}
		return builder.toString();
	}
}
