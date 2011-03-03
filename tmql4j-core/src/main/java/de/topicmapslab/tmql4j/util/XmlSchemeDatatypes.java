/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.util;

/**
 * Utility class for XSD data-types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XmlSchemeDatatypes {

	/**
	 * hidden constructor
	 */
	private XmlSchemeDatatypes() {

	}

	/**
	 * prefix colon
	 */
	private static final String COLON = ":";

	/**
	 * Base identifier of all XML Scheme Definition data-types <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#</code>
	 */
	public static final String XSD_BASE = "http://www.w3.org/2001/XMLSchema#";

	/**
	 * QName of all XML Scheme Definition data-types <br />
	 * <br />
	 * <code>xsd</code>
	 */
	public static final String XSD_QNAME = "xsd";

	/**
	 * XML Scheme Definition data-types of string <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#string</code>
	 */
	public static final String XSD_STRING = XSD_BASE + "string";

	/**
	 * QNamed XML Scheme Definition data-types of string <br />
	 * <br />
	 * <code>xsd:string</code>
	 */
	public static final String XSD_QSTRING = XSD_QNAME + COLON + "string";

	/**
	 * XML Scheme Definition data-types of URI <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#anyURI</code>
	 */
	public static final String XSD_ANYURI = XSD_BASE + "anyURI";

	/**
	 * QNamed XML Scheme Definition data-types of URI <br />
	 * <br />
	 * <code>xsd:anyURI</code>
	 */
	public static final String XSD_QANYURI = XSD_QNAME + COLON + "anyURI";

	/**
	 * XML Scheme Definition data-types of decimal <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#decimal</code>
	 */
	public static final String XSD_DECIMAL = XSD_BASE + "decimal";

	/**
	 * QNamed XML Scheme Definition data-types of decimal <br />
	 * <br />
	 * <code>xsd:decimal</code>
	 */
	public static final String XSD_QDECIMAL = XSD_QNAME + COLON + "decimal";
	
	/**
	 * XML Scheme Definition data-types of integer <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#integer</code>
	 */
	public static final String XSD_INT = XSD_BASE + "int";
		
	/**
	 * QNamed XML Scheme Definition data-types of integer <br />
	 * <br />
	 * <code>xsd:integer</code>
	 */
	public static final String XSD_QINT = XSD_QNAME + COLON + "int";
	
	/**
	 * XML Scheme Definition data-types of integer <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#integer</code>
	 */
	public static final String XSD_INTEGER = XSD_BASE + "integer";
		
	/**
	 * QNamed XML Scheme Definition data-types of integer <br />
	 * <br />
	 * <code>xsd:integer</code>
	 */
	public static final String XSD_QINTEGER = XSD_QNAME + COLON + "integer";

	/**
	 * XML Scheme Definition data-types of long numbers <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#long</code>
	 */
	public static final String XSD_LONG = XSD_BASE + "long";

	/**
	 * QNamed XML Scheme Definition data-types of long numbers <br />
	 * <br />
	 * <code>xsd:long</code>
	 */
	public static final String XSD_QLONG = XSD_QNAME + COLON + "long";
	
	/**
	 * XML Scheme Definition data-types of floating point numbers <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#float</code>
	 */
	public static final String XSD_FLOAT = XSD_BASE + "float";

	/**
	 * QNamed XML Scheme Definition data-types of floating point numbers <br />
	 * <br />
	 * <code>xsd:float</code>
	 */
	public static final String XSD_QFLOAT = XSD_QNAME + COLON + "float";	

	/**
	 * XML Scheme Definition data-types of boolean <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#boolean</code>
	 */
	public static final String XSD_BOOLEAN = XSD_BASE + "boolean";

	/**
	 * QNamed XML Scheme Definition data-types of boolean <br />
	 * <br />
	 * <code>xsd:boolean</code>
	 */
	public static final String XSD_QBOOLEAN = XSD_QNAME + COLON + "boolean";

	/**
	 * XML Scheme Definition data-types of date <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#date</code>
	 */
	public static final String XSD_DATE = XSD_BASE + "date";
	
	/**
	 * QNamed XML Scheme Definition data-types of date <br />
	 * <br />
	 * <code>xsd:date</code>
	 */
	public static final String XSD_QDATE = XSD_QNAME + COLON + "date";
	
	/**
	 * XML Scheme Definition data-types of time <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#time</code>
	 */
	public static final String XSD_TIME = XSD_BASE + "time";

	/**
	 * QNamed XML Scheme Definition data-types of time <br />
	 * <br />
	 * <code>xsd:time</code>
	 */
	public static final String XSD_QTIME = XSD_QNAME + COLON + "time";
	
	/**
	 * XML Scheme Definition data-types of date-time <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#dateTime</code>
	 */
	public static final String XSD_DATETIME = XSD_BASE + "dateTime";

	/**
	 * QNamed XML Scheme Definition data-types of date-time <br />
	 * <br />
	 * <code>xsd:dateTime</code>
	 */
	public static final String XSD_QDATETIME = XSD_QNAME + COLON + "dateTime";

	/**
	 * XML Scheme Definition data-types of any <br />
	 * <br />
	 * <code>http://www.w3.org/2001/XMLSchema#any</code>
	 */
	public static final String XSD_ANY = XSD_BASE + "any";

	/**
	 * QNamed XML Scheme Definition data-types of any <br />
	 * <br />
	 * <code>xsd:any</code>
	 */
	public static final String XSD_QANY = XSD_QNAME + COLON + "any";

	/**
	 * Method transform the given identifier to its absolute IRI if it starts
	 * with the XML scheme definition prefix 'xsd' otherwise the string returned
	 * unmodified.
	 * 
	 * @param identifier
	 *            the identifier to transform
	 * @return the transformed or unmodified string-represented IRI.
	 */
	public static String toExternalForm(final String identifier) {
		if (identifier.startsWith(XSD_QNAME + COLON)) {
			return XSD_BASE.concat(identifier.substring(4));
		}
		return identifier;
	}

}
