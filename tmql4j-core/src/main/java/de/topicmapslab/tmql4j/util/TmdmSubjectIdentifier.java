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
 * Utility class for TMDM default subject-identifier.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TmdmSubjectIdentifier {

	/**
	 * the tm:subject
	 */
	public static final String TM_SUBJECT = "tm:subject";
	public static final String TMDM_SUBJECT = "tmdm:subject";
	/**
	 * the tm:name
	 */
	public static final String TM_NAME = "tm:name";
	public static final String TMDM_NAME = "tmdm:name";
	/**
	 * the tm:occurrence
	 */
	public static final String TM_OCCURRENCE = "tm:occurrence";
	public static final String TMDM_OCCURRENCE = "tmdm:occurrence";
	/**
	 * the tm:association
	 */
	public static final String TM_ASSOCIATION = "tm:association";
	public static final String TMDM_ASSOCIATION = "tmdm:association";
	/**
	 * the tm:role
	 */
	public static final String TM_ROLE = "tm:role";
	public static final String TMDM_ROLE = "tmdm:role";
	/**
	 * subject-identifier of the topic-type of the topic maps data model
	 */
	public static final String TMDM_TOPIC_TYPE = "http://psi.topicmaps.org/iso13250/glossary/topic-type";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_NAME_TYPE = "http://psi.topicmaps.org/iso13250/glossary/topic-name-type";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_OCCURRENCE_TYPE = "http://psi.topicmaps.org/iso13250/glossary/occurrence-type";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_TYPE_ROLE_TYPE = "http://psi.topicmaps.org/iso13250/model/type";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_INSTANCE_ROLE_TYPE = "http://psi.topicmaps.org/iso13250/model/instance";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_SUBTYPE_ROLE_TYPE = "http://psi.topicmaps.org/iso13250/model/subtype";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_SUPERTYPE_ROLE_TYPE = "http://psi.topicmaps.org/iso13250/model/supertype";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE = "http://psi.topicmaps.org/iso13250/model/type-instance";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION = "http://psi.topicmaps.org/iso13250/model/supertype-subtype";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_ASSOCIATION_TYPE = "http://psi.topicmaps.org/iso13250/glossary/association-type";
	/**
	 * subject-identifier of the name-type of the topic maps data model
	 */
	public static final String TMDM_ASSOCIATION_ROLE_TYPE = "http://psi.topicmaps.org/iso13250/glossary/association-role-type";
	/**
	 * subject-identifier of the default name-type of the topic maps data model
	 */
	public static final String TMDM_DEFAULT_NAME_TYPE = "http://psi.topicmaps.org/iso13250/model/topic-name";

	/**
	 * Checks if the given identifier is known as subject-identifier of the
	 * topic maps meta model
	 * 
	 * @param identifier
	 *            the identifier
	 * @return <code>true</code> if the given identifier is a predefined
	 *         subject-identifier of the topic maps meta model,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isTmdmSubjectIdentifier(final String identifier) {
		return identifier.equalsIgnoreCase(TMDM_ASSOCIATION_ROLE_TYPE) || identifier.equalsIgnoreCase(TMDM_ASSOCIATION_TYPE) || identifier.equalsIgnoreCase(TMDM_DEFAULT_NAME_TYPE)
				|| identifier.equalsIgnoreCase(TMDM_INSTANCE_ROLE_TYPE) || identifier.equalsIgnoreCase(TMDM_NAME_TYPE) || identifier.equalsIgnoreCase(TMDM_OCCURRENCE_TYPE)
				|| identifier.equalsIgnoreCase(TMDM_SUBTYPE_ROLE_TYPE) || identifier.equalsIgnoreCase(TMDM_SUPERTYPE_ROLE_TYPE) || identifier.equalsIgnoreCase(TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)
				|| identifier.equalsIgnoreCase(TMDM_TOPIC_TYPE) || identifier.equalsIgnoreCase(TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE) || identifier.equalsIgnoreCase(TMDM_TYPE_ROLE_TYPE);
	}

	/**
	 * Utility method to check if the given object is a string with the value
	 * "tm:subject" or "tmdm:subject"
	 * 
	 * @param obj
	 *            the object
	 * @return <code>true</code> if the object is the string "tm:subject" or
	 *         "tmdm:subject", <code>false</code> otherwise
	 */
	public static boolean isTmdmSubject(Object obj) {
		if (obj instanceof String) {
			String identifier = (String) obj;
			return identifier.equalsIgnoreCase(TM_SUBJECT) || identifier.equalsIgnoreCase(TMDM_SUBJECT);
		}
		return false;
	}

	/**
	 * Utility method to check if the given object is a string with the value
	 * "tm:occurrence" or "tmdm:occurrence"
	 * 
	 * @param obj
	 *            the object
	 * @return <code>true</code> if the object is the string "tm:occurrence" or
	 *         "tmdm:occurrence", <code>false</code> otherwise
	 */
	public static boolean isTmdmOccurrence(Object obj) {
		if (obj instanceof String) {
			String identifier = (String) obj;
			return identifier.equalsIgnoreCase(TMDM_OCCURRENCE) || identifier.equalsIgnoreCase(TM_OCCURRENCE);
		}
		return false;
	}

	/**
	 * Utility method to check if the given object is a string with the value
	 * "tm:name" or "tmdm:name"
	 * 
	 * @param obj
	 *            the object
	 * @return <code>true</code> if the object is the string "tm:name" or
	 *         "tmdm:name", <code>false</code> otherwise
	 */
	public static boolean isTmdmName(Object obj) {
		if (obj instanceof String) {
			String identifier = (String) obj;
			return identifier.equalsIgnoreCase(TMDM_NAME) || identifier.equalsIgnoreCase(TM_NAME);
		}
		return false;
	}

	/**
	 * Utility method to check if the given object is a string with the value
	 * "tm:role" or "tmdm:role"
	 * 
	 * @param obj
	 *            the object
	 * @return <code>true</code> if the object is the string "tm:role" or
	 *         "tmdm:role", <code>false</code> otherwise
	 * @since 3.1.0
	 */
	public static boolean isTmdmRole(Object obj) {
		if (obj instanceof String) {
			String identifier = (String) obj;
			return identifier.equalsIgnoreCase(TMDM_ROLE) || identifier.equalsIgnoreCase(TM_ROLE);
		}
		return false;
	}

	/**
	 * Utility method to check if the given object is a string with the value
	 * "tm:association" or "tmdm:association"
	 * 
	 * @param obj
	 *            the object
	 * @return <code>true</code> if the object is the string "tm:association" or
	 *         "tmdm:association", <code>false</code> otherwise
	 * @since 3.1.0
	 */
	public static boolean isTmdmAssociation(Object obj) {
		if (obj instanceof String) {
			String identifier = (String) obj;
			return identifier.equalsIgnoreCase(TM_ASSOCIATION) || identifier.equalsIgnoreCase(TMDM_ASSOCIATION);
		}
		return false;
	}
}
