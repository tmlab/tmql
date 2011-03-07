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
package de.topicmapslab.tmql4j.path.query.preprocessing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Base implementation of {@link IScreener}. The screener remove comments,
 * spaces and other unnecessary content from the given query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public final class TmqlScreener {

	/**
	 * Utility class to screen a query.
	 * 
	 * @param query
	 *            the query string
	 * @return the screened query string
	 * @throws TMQLRuntimeException
	 */
	public static String screen(final String query) throws TMQLRuntimeException {
		/*
		 * extract string representation of the query
		 */
		String string = query;
		StringBuilder sb = new StringBuilder();
		/*
		 * extract query lines if query is multiple line
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
				String parts[] = line.substring(0, index + 1).split("\"");
				/*
				 * is # part of string literal?
				 */
				if (parts.length % 2 == 0) {
					index = line.indexOf("#", index + 1);
				}
				/*
				 * check if $ is in front of # ==> $#
				 */
				else if ((index == 0 || line.charAt(index - 1) != '$') && !isIRI(line, index)) {

					/*
					 * remove tokens after comment symbol
					 */
					line = line.substring(0, index);
					break;
				}
				/*
				 * comment token is part of IRI or variable name, get next token
				 */
				else {
					index = line.indexOf("#", index + 1);
				}
			}
			/*
			 * add cleaned line
			 */
			line = line.trim();
			line = line.replaceAll("\t", " ");
			sb.append(line);
			sb.append(" ");

		}
		return sb.toString();
	}

	/**
	 * Method checks if the hash is part of an IRI
	 * 
	 * @param string
	 *            the string
	 * @param index
	 *            the location of the URI
	 * @return <code>true</code> if the string is an IRI otherwise
	 *         <code>false</code>
	 */
	private static boolean isIRI(final String string, int index) {
		int whiteSpaceBefore = string.lastIndexOf(' ', index);
		int whiteSpaceAfter = string.indexOf(' ', index);
		if (whiteSpaceBefore != -1 && whiteSpaceAfter != -1) {
			String candidate = string.substring(whiteSpaceBefore + 1, whiteSpaceAfter);
			try {
				new URL(candidate);
				return true;
			} catch (MalformedURLException e) {
				// NOTHING TO DO
			}
		}
		int dataTypeIndex = string.lastIndexOf("^^", index);
		if (dataTypeIndex != -1 && whiteSpaceAfter != -1) {
			String candidate = string.substring(dataTypeIndex + 2, whiteSpaceAfter);
			try {
				new URL(candidate);
				return true;
			} catch (MalformedURLException e) {
				// NOTHING TO DO
			}
		}
		int bracketBefore = string.lastIndexOf('<', index);
		int bracketAfter = string.indexOf('>', index);
		if (bracketBefore != -1 && bracketAfter != -1) {
			String candidate = string.substring(bracketBefore + 1, bracketAfter);
			try {
				new URL(candidate);
				return true;
			} catch (MalformedURLException e) {
				// NOTHING TO DO
			}
		}
		return false;
	}

}
