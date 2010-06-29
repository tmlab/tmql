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
package de.topicmapslab.tmql4j.preprocessing.core.moduls;

import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.model.IScreener;

/**
 * Base implementation of {@link IScreener}. The screener remove comments,
 * spaces and other unnecessary content from the given query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLScreener implements IScreener {

	/**
	 * the query instance
	 */
	private final IQuery query;
	/**
	 * the screened query as the result of execution
	 */
	private IQuery screenedQuery = null;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param query
	 *            the query to transform
	 */
	public TMQLScreener(IQuery query) {
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 */
	public void screen() throws TMQLRuntimeException {
		/*
		 * extract string representation of the query
		 */
		String string = query.toString();
		StringBuilder sb = new StringBuilder();
		/*
		 * extract query lines if query is multiline
		 */
		StringTokenizer lines = new StringTokenizer(string, "\n");
		/*
		 * iterate over lines
		 */
		while (lines.hasMoreTokens()) {
			String line = lines.nextToken();
			/*
			 * get first index of comment token #
			 */
			int index = line.indexOf("#");
			while (index != -1) {
				/*
				 * check if $ is in front of # ==> $#
				 */
				if (index == 0 || line.charAt(index - 1) == ' ') {
					/*
					 * remove tokens after comment symbol
					 */
					line = line.substring(0, index);
					break;
				}
				/*
				 * comment token is part of IRI or variable name, get next token
				 */
				index = line.indexOf("#", index + 1);
			}
			/*
			 * add cleaned line
			 */
			line = line.trim();
			line = line.replaceAll("\t", " ");
			sb.append(line);
			sb.append(" ");

		}
		this.screenedQuery = new TMQLQuery(sb.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getScreenedQuery() {
		return this.screenedQuery;
	}

}
