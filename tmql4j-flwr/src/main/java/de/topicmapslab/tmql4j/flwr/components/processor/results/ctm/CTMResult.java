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
package de.topicmapslab.tmql4j.flwr.components.processor.results.ctm;

import java.util.Collection;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultType;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.flwr.util.CTMConverter;

/**
 * Result set implementation containing CTM strings. The results are generated
 * by the interpretation of tm-content expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CTMResult extends ResultSet<CTMFragment> {

	/**
	 * constructor create an empty result set
	 */
	public CTMResult() {
		// VOID
	}

	/**
	 * base constructor to create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public CTMResult(Collection<CTMFragment> results) {
		addResults(results);
	}

	/**
	 * base constructor to create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public CTMResult(CTMFragment... results) {
		addResults(results);
	}

	/**
	 * Convert the contained CTM fragments to one topic map and return the
	 * CTM-representation of the whole topic map.
	 * 
	 * @return the CTM-representation of a temporary topic map containing all
	 *         CTM-fragments
	 * @throws TMQLRuntimeException
	 *             thrown if topic map cannot be generated or cannot be
	 *             serialized to CTM
	 */
	public String resultsAsMergedCTM() throws TMQLRuntimeException {
		TopicMap map = resultsAsTopicMap();
		final String ctm = CTMConverter.toCTMString(map);
		map.remove();
		return ctm;
	}

	/**
	 * Method create a temporary topic map containing all CTM-fragments
	 * contained as results of the result set.
	 * 
	 * @return a temporary topic map containing all CTM-fragments
	 * @throws TMQLRuntimeException
	 *             thrown if topic map cannot be generated
	 */
	public TopicMap resultsAsTopicMap() throws TMQLRuntimeException {
		try {
			/*
			 * create a new topic map system
			 */
			TopicMapSystem topicMapSystem = TopicMapSystemFactory.newInstance()
					.newTopicMapSystem();
			/*
			 * create a temporary topic map as container of all CTM-fragments
			 */
			TopicMap full = null;
			/*
			 * iterate over CTM-fragments
			 */
			for (CTMFragment stream : getResults()) {
				topicMapSystem = TopicMapSystemFactory.newInstance()
						.newTopicMapSystem();
				
				/*
				 * convert fragment to a topic map
				 */
				TopicMap fragment = stream.toTopicMap(topicMapSystem);
				/*
				 * merge the topic maps
				 */
				if (full == null) {
					full = fragment;
				} else {
					full.mergeIn(fragment);
					fragment.remove();
				}
			}
			return full;
		} catch (Exception e) {
			throw new TMQLRuntimeException("Cannot convert to Topicmap", e);
		}
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
	public IResult createResult() {
		return new CTMFragment(this);
	}

}
