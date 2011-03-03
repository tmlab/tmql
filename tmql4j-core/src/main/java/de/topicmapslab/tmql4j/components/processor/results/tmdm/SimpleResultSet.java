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
package de.topicmapslab.tmql4j.components.processor.results.tmdm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;
import org.tmapix.io.XTM2TopicMapWriter;
import org.tmapix.io.XTMVersion;

import de.topicmapslab.ctm.writer.core.CTMTopicMapWriter;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRWriter;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.ConstructCopy;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class representing a simple result set similar to a table result of a data
 * base. The result set only contains a number of simple tuple results.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleResultSet extends ResultSet<SimpleResult> {

	/**
	 * 
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * base constructor create an empty result set
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	public SimpleResultSet(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new SimpleResult(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toJTMQR() throws UnsupportedOperationException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		toJTMQR(os);
		try {
			return os.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new TMQLRuntimeException("Content cannot be transformed to UTF-8", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void toJTMQR(OutputStream os) throws UnsupportedOperationException {
		JTMQRWriter writer = new JTMQRWriter(os);
		try {
			writer.write(this);
			writer.flush();
		} catch (IOException e) {
			throw new TMQLRuntimeException("An I/O error occured during transformation!", e);
		}
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
		TopicMap topicMap = getTopicMap();
		if (topicMap == null) {
			throw new TMQLRuntimeException("Missing topic map!");
		}
		CTMTopicMapWriter writer = new CTMTopicMapWriter(os, topicMap.getLocator().getReference());
		try {
			writer.write(getConstruct());
			os.flush();
		} catch (IOException e) {
			throw new TMQLRuntimeException("An I/O eror occur!", e);
		}
	}

	/**
	 * Utility method to extract the result representing a topic or association
	 * 
	 * @return the construct
	 */
	private Collection<Construct> getConstruct() {
		Collection<Construct> constructs = HashUtil.getHashSet();
		for (IResult r : getResults()) {
			for (Object o : r.getResults()) {
				if (o instanceof Topic || o instanceof Association) {
					constructs.add((Construct) o);
				}
			}
		}
		return constructs;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toXTM() throws UnsupportedOperationException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		toXTM(os);
		try {
			return os.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new TMQLRuntimeException("Content cannot be transformed to UTF-8", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void toXTM(OutputStream os) throws UnsupportedOperationException {
		TopicMapSystem topicMapSystem = getTopicMapSystem();
		TopicMap topicMap = getTopicMap();
		if (topicMap == null || topicMapSystem == null) {
			throw new TMQLRuntimeException("Missing topic map system or topic map!");
		}
		final String baseIRI = topicMap.getLocator().getReference() + "xtm/" + System.nanoTime() + "/";
		final TopicMap xtm;
		try {
			xtm = topicMapSystem.createTopicMap(baseIRI);
		} catch (TopicMapExistsException e) {
			throw new TMQLRuntimeException("An internal error occur. The base IRI already exists in topic map system!", e);
		}
		ConstructCopy.copyIn(xtm, getConstruct());
		try {
			XTM2TopicMapWriter writer = new XTM2TopicMapWriter(os, baseIRI, XTMVersion.XTM_2_1);
			writer.write(xtm);
			os.flush();
		} catch (IOException e) {
			throw new TMQLRuntimeException("An I/O eror occur!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMap toTopicMap() throws UnsupportedOperationException {
		TopicMapSystem topicMapSystem = getTopicMapSystem();
		TopicMap topicMap = getTopicMap();
		if (topicMap == null || topicMapSystem == null) {
			throw new TMQLRuntimeException("Missing topic map system or topic map!");
		}
		final String baseIRI = topicMap.getLocator().getReference() + "copy/" + System.nanoTime() + "/";
		final TopicMap copy;
		try {
			copy = topicMapSystem.createTopicMap(baseIRI);
		} catch (TopicMapExistsException e) {
			throw new TMQLRuntimeException("An internal error occur. The base IRI already exists in topic map system!", e);
		}
		ConstructCopy.copyIn(copy, getConstruct());
		return copy;
	}
}
