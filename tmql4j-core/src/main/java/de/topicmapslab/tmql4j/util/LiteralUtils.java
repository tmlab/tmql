/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Utility class for literals. Checks the type of literals and transform string
 * literals to a literal of a specific type.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class LiteralUtils {

	/**
	 * regular expression of date
	 */
	private final static Pattern datePattern = Pattern.compile("[-]?[0-9][0-9][0-9][0-9][0-9]*[-](0[1-9]|1[0-2])[-](0[1-9]|[1-2][0-9]|3[0-1])");
	/**
	 * regular expression of time
	 */
	private final static Pattern timePattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9](\\.[0-9]+)?(Z|[+|-][0-9][0-9]:[0-9][0-9])?");
	/**
	 * regular expression of dateTime
	 */
	private final static Pattern dateTimePattern = Pattern.compile(datePattern.pattern() + "T" + timePattern.pattern());
	/**
	 * regular expression of decimal
	 */
	private final static Pattern decimalPattern = Pattern.compile("[+|-]?([0-9]+[.][0-9]+|[.][0-9]+)(E[+|-]?[0-9]{1,5})?");
	/**
	 * regular expression of integer
	 */
	private final static Pattern integerPattern = Pattern.compile("[+|-]?[0-9]+");

	/**
	 * translation patterns of date
	 */
	private static final List<String> datePatterns = new LinkedList<String>();
	static {
		datePatterns.add("yyyy-MM-dd");
		datePatterns.add("yyyyy-MM-dd");
		datePatterns.add("yyyyyy-MM-dd");
		datePatterns.add("yyyyyyy-MM-dd");
	}
	/**
	 * translation patterns of time
	 */
	private static final List<String> timePatterns = new LinkedList<String>();
	static {
		timePatterns.add("HH:mm:ss");
		timePatterns.add("HH:mm:ss.S");
		timePatterns.add("HH:mm:ss.SS");
		timePatterns.add("HH:mm:ss.SSS");
		timePatterns.add("HH:mm:ss.SSSS");
		timePatterns.add("HH:mm:ss.SSSSS");
		timePatterns.add("HH:mm:ss'Z'");
		timePatterns.add("HH:mm:ss.S'Z'");
		timePatterns.add("HH:mm:ss.SS'Z'");
		timePatterns.add("HH:mm:ss.SSS'Z'");
		timePatterns.add("HH:mm:ss.SSSS'Z'");
		timePatterns.add("HH:mm:ss.SSSSS'Z'");
		timePatterns.add("HH:mm:ss-HH:mm");
		timePatterns.add("HH:mm:ss.S-HH:mm");
		timePatterns.add("HH:mm:ss.SS-HH:mm");
		timePatterns.add("HH:mm:ss.SSS-HH:mm");
		timePatterns.add("HH:mm:ss.SSSS-HH:mm");
		timePatterns.add("HH:mm:ss.SSSSS-HH:mm");
		timePatterns.add("HH:mm:ss+HH:mm");
		timePatterns.add("HH:mm:ss.S+HH:mm");
		timePatterns.add("HH:mm:ss.SS+HH:mm");
		timePatterns.add("HH:mm:ss.SSS+HH:mm");
		timePatterns.add("HH:mm:ss.SSSS+HH:mm");
		timePatterns.add("HH:mm:ss.SSSSS+HH:mm");
	}

	/**
	 * Method checks if the given string literal can be represented as integer
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is an integer literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isInteger(final String literal) {
		return integerPattern.matcher(literal).matches();
	}

	/**
	 * Method checks if the given string literal can be represented as decimal
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a decimal literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isDecimal(final String literal) {
		return decimalPattern.matcher(literal).matches();
	}

	/**
	 * Method checks if the given string literal can be represented as date
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a date literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isDate(final String literal) {
		return datePattern.matcher(literal).matches();
	}

	/**
	 * Method checks if the given string literal can be represented as time
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a time literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isTime(final String literal) {
		return timePattern.matcher(literal).matches();
	}

	/**
	 * Method checks if the given string literal can be represented as dateTime
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a dateTime literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isDateTime(final String literal) {
		return dateTimePattern.matcher(literal).matches();
	}

	/**
	 * Method checks if the given string literal can be represented as quoted
	 * string literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a quoted string literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isQuotedString(final String literal) {
		return literal.startsWith("\"") && literal.endsWith("\"");
	}

	/**
	 * Method checks if the given string literal can be represented as triple
	 * quoted string literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is a triple quoted string
	 *         literal, <code>false</code> otherwise.
	 */
	public static final boolean isTripleQuotedString(final String literal) {
		return literal.startsWith("\"\"\"") && literal.endsWith("\"\"\"");
	}

	/**
	 * Method checks if the given string literal can be represented as integer
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is an integer literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isString(final String literal) {
		return isQuotedString(literal) || isTripleQuotedString(literal);
	}

	/**
	 * Method checks if the given string literal can be represented as IRI
	 * literal.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is an IRI literal,
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isIri(final String literal) {
		try {
			new URI(literal);
		} catch (URISyntaxException e) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if the given string literal can be represented as boolean
	 * value.
	 * 
	 * @param literal
	 *            the literal
	 * @return <code>true</code> if the literal is an "true" or "false",
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isBoolean(final String literal) {
		return literal != null && (literal.equalsIgnoreCase("true") || literal.equalsIgnoreCase("false"));
	}

	/**
	 * Method formats the given literal as integer literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the integer literal
	 * @throws NumberFormatException
	 *             thrown if literal cannot be format as number
	 */
	public static final BigInteger asInteger(final String literal) throws NumberFormatException {
		return BigInteger.valueOf(Long.parseLong(literal));
	}

	/**
	 * Method formats the given literal as decimal literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the decimal literal
	 * @throws NumberFormatException
	 *             thrown if literal cannot be format as number
	 */
	public static final BigDecimal asDecimal(final String literal) throws NumberFormatException {
		return BigDecimal.valueOf(Double.parseDouble(literal));
	}

	/**
	 * Method transform the given object to a double value
	 * 
	 * @param object
	 *            the object
	 * @return the double value and never <code>null</code>
	 * @throws NumberFormatException
	 * @since 2.6.5
	 */
	public static final Double asDouble(final Object object) throws NumberFormatException {
		if (object instanceof Double) {
			return (Double) object;
		} else if (object instanceof BigDecimal) {
			return ((BigDecimal) object).doubleValue();
		} else {
			return Double.parseDouble(object.toString());
		}
	}

	/**
	 * Method formats the given literal as date literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the date literal
	 * @throws ParseException
	 *             thrown if literal cannot be format as date
	 */
	public static final Calendar asDate(final String literal) throws ParseException {
		Date date = null;
		for (String pattern : datePatterns) {
			try {
				date = new SimpleDateFormat(pattern).parse(literal);
			} catch (ParseException e) {
				// VOID
			}
		}
		if (date == null) {
			throw new ParseException("Invalid date pattern", -1);
		}
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c;
	}

	/**
	 * Method formats the given literal as time literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the time literal
	 * @throws ParseException
	 *             thrown if literal cannot be format as time
	 */
	public static final Calendar asTime(final String literal) throws ParseException {
		Date date = null;
		for (String pattern : timePatterns) {
			try {
				date = new SimpleDateFormat(pattern).parse(literal);
			} catch (ParseException e) {
				// VOID
			}
		}
		if (date == null) {
			throw new ParseException("Invalid time pattern", -1);
		}
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c;
	}

	/**
	 * Method formats the given literal as dateTime literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the dateTime literal
	 * @throws ParseException
	 *             thrown if literal cannot be format as dateTime
	 */
	public static final Calendar asDateTime(final String literal) throws ParseException {
		Date date = null;
		for (String dp : datePatterns) {
			for (String tp : timePatterns) {
				try {
					date = new SimpleDateFormat(dp + "'T'" + tp).parse(literal);
				} catch (ParseException e) {
					// VOID
				}
			}
		}
		if (date == null) {
			throw new ParseException("Invalid dateTime pattern", -1);
		}
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c;
	}

	/**
	 * Method formats the given literal as string literal.
	 * 
	 * @param literal
	 *            the quoted string literal
	 * @return the string literal without quotes
	 */
	public static final String asQuotedString(final String literal) {
		return literal.substring(1, literal.length() - 1);
	}

	/**
	 * Method formats the given literal as string literal.
	 * 
	 * @param literal
	 *            the triple quoted string literal
	 * @return the string literal without quotes
	 */
	public static final String asTripleQuotedString(final String literal) {
		return literal.substring(3, literal.length() - 3);
	}

	/**
	 * Method formats the given literal as string literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the string literal without quotes
	 */
	public static final String asString(final String literal) {
		if (isTripleQuotedString(literal)) {
			return unescape(asTripleQuotedString(literal));
		} else if (isQuotedString(literal)) {
			return unescape(asQuotedString(literal));
		} else {
			return unescape(literal);
		}
	}

	/**
	 * Method formats the given literal as boolean literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the boolean literal
	 * @since 3.1.0
	 */
	public static final boolean asBoolean(final String literal) {
		if (isBoolean(literal)) {
			return Boolean.parseBoolean(literal);
		}
		throw new TMQLRuntimeException("Invalid value of a boolean literal: '" + literal + "'");
	}

	/**
	 * Method to clean the given string. The method removes all escaping
	 * characters
	 * 
	 * @param literal
	 *            the literal
	 * @return the cleaned string
	 */
	public static final String unescape(final String literal) {
		StringBuffer buffer = new StringBuffer();
		for (int index = 0; index < literal.length(); index++) {
			char c = literal.charAt(index);
			/*
			 * is escaping sequence
			 */
			if (c == '\\') {
				if (index + 1 == literal.length() || (literal.charAt(index + 1) != '\\' && literal.charAt(index + 1) != '\"')) {
					throw new TMQLRuntimeException("Invalid character sequence at position '" + index + "'. Expected '\"' or '\\' but '" + literal.charAt(index + 1) + "' was found!");
				}
				buffer.append(Character.toString(literal.charAt(index + 1)));
				index++;
			} else {
				buffer.append(Character.toString(c));
			}
		}
		return buffer.toString();
	}

	/**
	 * Method formats the given literal as IRI literal.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the string literal without quotes
	 * @throws URISyntaxException
	 *             thrown if given literal isn't a valid IRI
	 */
	public static final URI asIri(final String literal) throws URISyntaxException {
		return new URI(literal);
	}

	/**
	 * Method transform the given object to its string literal representation. A
	 * topic will be transformed to one of its identifiers.
	 * 
	 * @param o
	 *            the object to transform
	 * @return the string literal
	 */
	public static final String asString(final Object o) {
		final String value;
		/*
		 * object is a topic -> try to set value to one of its identifiers
		 */
		if (o instanceof Topic) {
			if (!((Topic) o).getNames().isEmpty()) {
				value = ((Topic) o).getNames().iterator().next().getValue();
			} else if (!((Topic) o).getSubjectIdentifiers().isEmpty()) {
				value = ((Topic) o).getSubjectIdentifiers().iterator().next().toExternalForm();
			} else if (!((Topic) o).getSubjectLocators().isEmpty()) {
				value = ((Topic) o).getSubjectLocators().iterator().next().toExternalForm();
			} else if (!((Topic) o).getItemIdentifiers().isEmpty()) {
				value = ((Topic) o).getItemIdentifiers().iterator().next().toExternalForm();
			} else {
				value = o.toString();
			}
		}
		/*
		 * object is a name -> get value
		 */
		else if (o instanceof Name) {
			value = ((Name) o).getValue();
		}
		/*
		 * object is an occurrence -> get value
		 */
		else if (o instanceof Occurrence) {
			value = ((Occurrence) o).getValue();
		}
		/*
		 * object is a variant -> get value
		 */
		else if (o instanceof Variant) {
			value = ((Variant) o).getValue();
		}
		/*
		 * object is a locator -> get reference
		 */
		else if (o instanceof Locator) {
			value = ((Locator) o).toExternalForm();
		}
		/*
		 * simple to string
		 */
		else {
			value = o.toString();
		}
		return value;
	}

	/**
	 * Transform the given literal to a literal of the specified type given by
	 * dataType IRI.
	 * 
	 * @param literal
	 *            the literal value to convert
	 * @param datatType
	 *            the IRI of the data-type
	 * @return the transformed literal or a string literal if the value does not
	 *         matches the regular expression of the given type.
	 * @throws Exception
	 *             thrown if transformation fails
	 */
	public static boolean isValid(final String literal, final String datatType) throws Exception {
		if (datatType == null) {
			return true;
		}
		final String dataType_ = XmlSchemeDatatypes.toExternalForm(datatType);
		/*
		 * handle as date?
		 */
		if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATE)) {
			return isDate(literal);
		}
		/*
		 * handle as time?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_TIME)) {
			return isTime(literal);
		}
		/*
		 * handle as dateTime?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATETIME)) {
			return isDateTime(literal);
		}
		/*
		 * handle as integer?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_INTEGER) || dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_INT)) {
			return isInteger(literal);
		}
		/*
		 * handle as decimal?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DECIMAL) || dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_FLOAT)) {
			return isDecimal(literal);
		}
		/*
		 * handle as URI?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_ANYURI)) {
			return isIri(literal);
		}
		return true;
	}

	/**
	 * Transform the given literal to a literal of the specified type given by
	 * dataType IRI.
	 * 
	 * @param literal
	 *            the literal value to convert
	 * @param datatType
	 *            the IRI of the data-type
	 * @return the transformed literal or a string literal if the value does not
	 *         matches the regular expression of the given type.
	 * @throws Exception
	 *             thrown if transformation fails
	 */
	public static Object asLiteral(final String literal, final String datatType) throws Exception {
		final String dataType_ = XmlSchemeDatatypes.toExternalForm(datatType);
		/*
		 * handle as date?
		 */
		if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATE) && isDate(literal)) {
			return asDate(literal);
		}
		/*
		 * handle as time?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_TIME) && isTime(literal)) {
			return asTime(literal);
		}
		/*
		 * handle as dateTime?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATETIME) && isDateTime(literal)) {
			return asDateTime(literal);
		}
		/*
		 * handle as integer?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_INTEGER) && isInteger(literal)) {
			return asInteger(literal);
		}
		/*
		 * handle as decimal?
		 */
		else if ((dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DECIMAL) || dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_FLOAT)) && isDecimal(literal)) {
			return asDecimal(literal);
		}
		/*
		 * handle as URI?
		 */
		else if (dataType_.equalsIgnoreCase(XmlSchemeDatatypes.XSD_ANYURI) && isIri(literal)) {
			return asIri(literal);
		}
		/*
		 * handle as string
		 */
		return literal;
	}

	/**
	 * Method tries to detect the data-type of the given object.
	 * 
	 * @param query
	 *            the query
	 * @param obj
	 *            a atomic item or a tuple sequence
	 * @return a IRI representing the data-type of given item or a sequence of
	 *         IRIs but never <code>null</code>.
	 */
	public static Object getDatatypeOfLiterals(final IQuery query, Object obj) {
		if (obj instanceof Occurrence) {
			return ((Occurrence) obj).getDatatype();
		} else if (obj instanceof Name) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_STRING);
		} else if (obj instanceof Variant) {
			return ((Variant) obj).getDatatype();
		} else if (obj instanceof String) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_STRING);
		} else if (obj instanceof Integer || obj instanceof Long || obj instanceof BigInteger) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_INTEGER);
		} else if (obj instanceof Float || obj instanceof Double || obj instanceof BigDecimal) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_DECIMAL);
		} else if (obj instanceof Calendar) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_DATETIME);
		} else if (obj instanceof URI) {
			return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_ANYURI);
		} else if (obj instanceof Collection<?>) {
			List<Object> seq = HashUtil.getList();
			for (Object o : (Collection<?>) obj) {
				seq.add(getDatatypeOfLiterals(query, o));
			}
			return seq;
		}
		return query.getTopicMap().createLocator(XmlSchemeDatatypes.XSD_ANY);
	}
}
