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
package de.topicmapslab.tmql4j.components.processor.results.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Result set implementation containing XML fragments. The results are generated
 * by the interpretation of XML-content expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XMLResult extends ResultSet<XMLValue> {

	/**
	 * 
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * base constructor create an empty result set
	 * 
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	public XMLResult(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}

	/**
	 * Method convert all contained XML fragments to one XML document and return
	 * the generated instance of the document.
	 * 
	 * @return the XML document
	 * @throws TMQLRuntimeException
	 *             thrown if generation of the XML document fails
	 */
	public Document toXMLDocument() throws TMQLRuntimeException {
		try {
			/*
			 * create a new string builder instance
			 */
			final StringBuilder builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
			/*
			 * add root node if necessary
			 */
			if (size() > 1) {
				builder.append("<xml-root>\r\n");
			}

			/*
			 * iterate over results
			 */
			for (XMLValue value : getResults()) {
				/*
				 * transform stream to string
				 */
				builder.append(value.first().toString());
				builder.append("\r\n");
			}

			/*
			 * close root node if necessary
			 */
			if (size() > 1) {
				builder.append("</xml-root>\r\n");
			}

			/*
			 * create a new XML document
			 */
			ByteArrayInputStream stream = new ByteArrayInputStream(builder.toString().getBytes());
			InputSource source = new InputSource(new BufferedReader(new InputStreamReader(stream)));
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);
		} catch (Exception ex) {
			throw new TMQLRuntimeException("Cannot convert to XML document", ex);
		}
	}

	/**
	 * Convert the contained XTM fragments to one topic map and return the
	 * XML-representation of the whole topic map.
	 * 
	 * @return the XML-representation of a temporary topic map containing all
	 *         XML-fragments
	 * @throws TMQLRuntimeException
	 *             thrown if topic map cannot be generated or cannot be
	 *             serialized to XML
	 */
	public String toXML() throws TMQLRuntimeException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		toXML(os);
		try {
			return os.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new TMQLRuntimeException("Content cannot be transformed to UTF-8", e);
		}
	}

	/**
	 * Convert the contained XTM fragments to one topic map and return the
	 * XML-representation of the whole topic map.
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws TMQLRuntimeException
	 *             thrown if topic map cannot be generated or cannot be
	 *             serialized to XML
	 */
	public void toXML(OutputStream stream) throws TMQLRuntimeException {
		try {
			Document doc = toXMLDocument();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(doc);
			Result output = new StreamResult(stream);
			transformer.transform(source, output);
			stream.flush();
		} catch (TransformerException e) {
			throw new TMQLRuntimeException("Cannot convert to XML document because the document cannot transform to stream", e);
		} catch (IOException e) {
			throw new TMQLRuntimeException("Cannot convert to XML document because an I/O error occur.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.XML.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new XMLValue(this);
	}

}
