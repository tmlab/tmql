/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Arnim Bleier
 * @email bleier@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.resultprocessing.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONWriter;
import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;


/**
 * A experimental TMQLResultsJSONWriter that serializes a {@link IResultSet} into
 * a <a href="http://www.topicmapslab.de/publications/tmql-json-result/">TMQL Query Results in JSON</a>
 * representation.
 */
public class TMQLResultsJSONWriter {

	private Iterator<SimpleTupleResult> resultIterator;
	private JSONWriter writer;
	private Writer w;

	/**
	 * 
	 * @param out
	 * @param queryResult
	 * @throws IOException
	 */
	public TMQLResultsJSONWriter(OutputStream out,
			IResultSet<SimpleTupleResult> queryResult) throws IOException {
		w = new OutputStreamWriter(out, Charset.forName("UTF-8"));
		w = new BufferedWriter(w, 1024);
		writer = new JSONWriter(w);
		this.resultIterator = queryResult.iterator();
	}

	/**
	 * Serializes the {@link IResultSet}
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public void write() throws JSONException, IOException {
		SimpleTupleResult simpleTupleResult;
		JSONWriter o = writer.object();
		o = o.key("results");
		o.array();
		while (resultIterator.hasNext()) {
			simpleTupleResult = (SimpleTupleResult) resultIterator.next();
			handleSolution(simpleTupleResult, o);
		}
		o.endArray();
		w.flush();
	}

	/**
	 * 
	 * @param result
	 * @param jsonObject
	 * @throws JSONException
	 */
	public void handleSolution(SimpleTupleResult result, JSONWriter jsonObject)
			throws JSONException {
		Iterator<Object> rowIterator = result.iterator();
		// System.err.println("row size" + result.size());
		Object singleResult;
		JSONWriter oa = jsonObject.object().key("result");
		JSONWriter arr = oa.array();
		while (rowIterator.hasNext()) {
			singleResult = rowIterator.next();
			if (singleResult instanceof Topic) {
				// System.out.println("doing Topic");
				writeTopic((Topic) singleResult, jsonObject);
			} else if (singleResult instanceof Variant) {
				// System.out.println("doing Variant");
				writeVariant((Variant) singleResult, jsonObject);
			} else if (singleResult instanceof Name) {
				// System.out.println("doing Name");
				writeName((Name) singleResult, jsonObject);
			} else if (singleResult instanceof Association) {
				// System.out.println("doing Association");
				writeAssociation((Association) singleResult, jsonObject);
			} else if (singleResult instanceof Occurrence) {
				// System.out.println("doing Occurrence");
				writeOccurrence((Occurrence) singleResult, jsonObject);
			} else if (singleResult instanceof String) {
				// System.out.println("doing Atom");
				writeAtom((String) singleResult, jsonObject);
			} else if (singleResult instanceof Locator) {
				// System.out.println("doing Locator");
				writeLocator((Locator) singleResult, jsonObject);
			} else {
				System.err.println("missed" + singleResult.getClass());
			}
		}
		arr.endArray();
		oa.endObject();
	}

	/**
	 * 
	 * @param t
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeAtom(String t, JSONWriter jsonObject) throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("Atom");
		o.key("value").value(t);
		o.endObject();
	}

	/**
	 * 
	 * @param l
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeLocator(Locator l, JSONWriter jsonObject) throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("Locator");
		o.key("value").value(l.toExternalForm());
		o.endObject();
	}

	/**
	 * 
	 * @param t
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeAssociation(Association t, JSONWriter jsonObject)
			throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("Association");
		writeType(t, o);
		o.key("scope");
		o.array();
		Iterator<Topic> i = t.getScope().iterator();
		while (i.hasNext()) {
			o.value(topicRef((Topic) i.next()));
		}
		o.endArray();
		o.key("players");
		o.array();
		Iterator<Role> ir = t.getRoles().iterator();
		while (ir.hasNext()) {
			o.value(topicRef(ir.next().getPlayer()));
		}
		o.endArray();
		o.endObject();
	}

	/**
	 * 
	 * @param t
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeTopic(Topic t, JSONWriter jsonObject) throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("Topic");
		o.key("reference").value(topicRef(t));
		o.endObject();
	}

	/**
	 * 
	 * @param t
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeType(Typed t, JSONWriter jsonObject) throws JSONException {
		jsonObject.key("type").value(topicRef(t.getType()));
	}

	/**
	 * 
	 * @param t
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeParent(Topic t, JSONWriter jsonObject) throws JSONException {
		jsonObject.key("parent").value(topicRef(t));
	}

	/**
	 * 
	 * @param name
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeName(Name name, JSONWriter jsonObject) throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("name");
		o.key("value").value(name.getValue());
		writeType(name, o);
		writeParent(name.getParent(), o);
		o.endObject();
	}

	/**
	 * 
	 * @param name
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeVariant(Variant name, JSONWriter jsonObject)
			throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("variant");
		o.key("value").value(name.getValue());
		o.key("datatype").value(name.getDatatype().toExternalForm());
		o.endObject();
	}

	/**
	 * 
	 * @param occurence
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void writeOccurrence(Occurrence occurence, JSONWriter jsonObject)
			throws JSONException {
		JSONWriter o = jsonObject.object();
		o.key("item_type").value("occurence");
		o.key("value").value(occurence.getValue());
		o.key("datatype").value(occurence.getDatatype().toExternalForm());
		writeType(occurence, o);
		writeParent(occurence.getParent(), o);
		o.endObject();
	}

	/**
	 * Returns an IRI which is usable to as reference to the specified topic.
	 * 
	 * Modified version of JTMTopicMapWriter from Lars Heuer
	 * (heuer[at]semagia.com)
	 * 
	 * @param topic
	 *            The topic.
	 * @return An IRI.
	 */
	private String topicRef(Topic topic) {
		Set<Locator> locs = topic.getSubjectIdentifiers();
		if (!locs.isEmpty()) {
			return "si:" + locs.iterator().next().toExternalForm();
		}
		locs = topic.getSubjectLocators();
		if (!locs.isEmpty()) {
			return "sl:" + locs.iterator().next().toExternalForm();
		} else
			try {
				return "ii:"
						+ topic.getItemIdentifiers().iterator().next()
								.toExternalForm();
			} catch (Exception e) {
				throw new TMQLRuntimeException("Topic " + topic
						+ "has no SI nor SL nor an II, thus violates the TMDM");
			}
	}

}
