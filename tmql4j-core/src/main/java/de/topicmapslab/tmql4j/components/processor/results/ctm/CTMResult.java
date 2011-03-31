/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.ctm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapix.io.CTMTopicMapReader;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * 
 */
public class CTMResult extends ResultSet<CTMValue> {

	/**
	 * 
	 */
	private static final String NEWLINE = "\r\n";
	/**
	 * UTF-8 encoding
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * constructor create an empty result set
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	public CTMResult(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new CTMValue(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.CTM.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if (isEmpty()) {
			return "";
		}
		return first().first().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toCTM() throws UnsupportedOperationException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		toCTM(os);
		try {
			return os.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new TMQLRuntimeException("Content cannot be transformed to UTF-8", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void toCTM(OutputStream os) throws UnsupportedOperationException {
		PrintWriter writer = new PrintWriter(os);
		for (CTMValue value : getResults()) {
			writer.write(value.first().toString());
			writer.write(NEWLINE);
		}
		writer.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap toTopicMap() throws TMQLRuntimeException {
		try {
			/*
			 * create a new topic map system
			 */
			TopicMapSystem topicMapSystem = getTopicMapSystem();
			TopicMap topicMap = getTopicMap();
			if (topicMapSystem == null || topicMap == null) {
				throw new TMQLRuntimeException("Missing topic map system or topic map!");
			}
			/*
			 * create a temporary topic map as container of all CTM-fragments
			 */
			final String baseIRI = getTopicMap().getLocator().getReference() + "ctm/" + System.nanoTime() + "/";
			TopicMap full = topicMapSystem.createTopicMap(baseIRI);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			toCTM(os);
			os.flush();
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			CTMTopicMapReader reader = new CTMTopicMapReader(full, is, baseIRI);
			reader.read();
			return full;
		} catch (Exception e) {
			throw new TMQLRuntimeException("An I/O error occurred during transformation of topic map from CTM!", e);
		}
	}

}