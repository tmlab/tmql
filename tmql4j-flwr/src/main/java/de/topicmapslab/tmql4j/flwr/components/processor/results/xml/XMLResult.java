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
package de.topicmapslab.tmql4j.flwr.components.processor.results.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultType;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.flwr.util.XTMConverter;

/**
 * Result set implementation containing XML fragments. The results are generated
 * by the interpretation of XML-content expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XMLResult extends ResultSet<XMLFragment> {

	/**
	 * base constructor create an empty result set
	 */
	public XMLResult() {
		// VOID
	}

	/**
	 * base constructor to create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public XMLResult(Collection<XMLFragment> results) {
		addResults(results);
	}

	/**
	 * base constructor to create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public XMLResult(XMLFragment... results) {
		addResults(results);
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
			for (XMLFragment stream : getResults()) {
				/*
				 * transform stream to string
				 */
				builder.append(stream.toString() + "\r\n");
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

			ByteArrayInputStream stream = new ByteArrayInputStream(builder
					.toString().getBytes());
			
			InputSource source = new InputSource(new BufferedReader(
					new InputStreamReader(stream)));

			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(source);
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
	public String resultsAsMergedXML() throws TMQLRuntimeException {
		try {
			Document doc = toXMLDocument();
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			Source source = new DOMSource(doc);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Result output = new StreamResult(stream);
			transformer.transform(source, output);
			return stream.toString(XTMConverter.encoding);
		} catch (TransformerException e) {
			throw new TMQLRuntimeException("Cannot convert to XML document", e);
		} catch (UnsupportedEncodingException e) {
			throw new TMQLRuntimeException(
					"Cannot convert to XML document because of unknown encoding.",
					e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.XML.name();
	}

}
