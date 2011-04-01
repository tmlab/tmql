/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.prepared;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.lexer.TMQLTokenizer;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Utility class for {@link IPreparedStatement}
 * 
 * @author Sven Krosse
 * 
 */
public class PreparedUtils {

	/**
	 * constant for ID axis navigation
	 */
	private static final String IDNAVIGATION = " << id";
	/**
	 * constant for whitespace navigation
	 */
	private static final String WHITESPACE = " ";

	/**
	 * Transform the given prepared statement to a non parameterized TMQL query
	 * 
	 * @param stmt
	 *            the statement
	 * @return the TMQL query
	 */
	public static String toNonParameterized(IPreparedStatement stmt) {
		TMQLTokenizer tokenizer = new TMQLTokenizer(stmt.getQueryString());
		StringBuilder builder = new StringBuilder();
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.startsWith(Wildcard.TOKEN)) {
				Object value = stmt.get(index++);
				builder.append(asQueryPart(value));
			} else {
				builder.append(token);
			}
			builder.append(WHITESPACE);
		}
		return builder.toString().trim();
	}

	/**
	 * Utility method to transform the given value to a query part. The
	 * transformation depends on the type of the given value.
	 * <p>
	 * {@link String}: returns the quoted literal <br />
	 * {@link Calendar}: returns the quoted string representation of the
	 * calendar.<br />
	 * {@link Construct}: return the query part: <code> "12345" << id </code><br />
	 * default: the string representation
	 * </p>
	 * 
	 * @param value
	 *            the value
	 * @return the query part
	 */
	public static String asQueryPart(Object value) {
		/*
		 * value is a string
		 */
		if (value instanceof String) {
			return LiteralUtils.asEscapedString(value.toString());
		}
		/*
		 * value is a topic
		 */
		else if (value instanceof Topic) {
			Topic topic = (Topic) value;
			Set<Locator> set = topic.getSubjectIdentifiers();
			if (!set.isEmpty()) {
				return LiteralUtils.asEscapedString(set.iterator().next().getReference()) + " << indicators";
			}
			set = topic.getSubjectLocators();
			if (!set.isEmpty()) {
				return LiteralUtils.asEscapedString(set.iterator().next().getReference()) + " << locators";
			}
			set = topic.getItemIdentifiers();
			if (!set.isEmpty()) {
				return LiteralUtils.asEscapedString(set.iterator().next().getReference()) + " << item";
			}
			return LiteralUtils.asEscapedString(topic.getId()) + IDNAVIGATION;
		}
		/*
		 * value is a construct
		 */
		else if (value instanceof Construct) {
			return LiteralUtils.asEscapedString(((Construct) value).getId()) + IDNAVIGATION;
		}
		/*
		 * is a calendar
		 */
		else if (value instanceof Calendar) {
			return LiteralUtils.asEscapedString(new SimpleDateFormat().format(((Calendar) value).getTime()));
		}
		/*
		 * is anything else
		 */
		return value.toString();
	}
}
