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
package de.topicmapslab.tmql4j.path.grammar.lexical;

import java.net.URI;
import java.util.regex.Pattern;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class XmlStartTag extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isToken(ITMQLRuntime runtime, String literal) {
		return literal.matches("<[^/].+>") && !isIRI(literal) && !literal.equalsIgnoreCase("<->");
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return "<...>";
	}

	/**
	 * Internal method to check if the given token is an IRI prefixed by the
	 * symbol < and postponed by the symbol >.
	 * 
	 * @param pattern
	 *            the token to check
	 * @return <code>true</code> if the token is an IRI, <code>false</code>
	 *         otherwise.
	 */
	private static boolean isIRI(final String pattern) {
		try {
			/*
			 * create regular expression
			 */
			Pattern regExp = Pattern
					.compile("((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\.&]*)");
			/*
			 * remove prefixed and postponed symbols
			 */
			final String candidate = pattern.substring(1, pattern.length() - 1);
			/*
			 * check if token matches the regular expression
			 */
			if (regExp.matcher(candidate).matches()) {
				/*
				 * check if token is a real IRI
				 */
				new URI(candidate);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}	
}
