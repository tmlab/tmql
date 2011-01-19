/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module;

import java.util.Map;
import java.util.Properties;

import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Class representing the prefix handler to add and read the defined prefixes of
 * the current runtime
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PrefixHandler {

	/**
	 * the default prefix
	 */
	private String defaultPrefix = null;

	/**
	 * internal store of all known prefixes
	 */
	private final Map<String, String> prefixes = HashUtil.getHashMap();

	/**
	 * store of all known IRI shortcuts of the TMDM
	 */
	private static Map<String, String> knownSystemIdentifiers = HashUtil
			.getHashMap();
	/**
	 * insert all known IRI shortcuts of the TMDM
	 */
	static {
		knownSystemIdentifiers.put("tm:subject",
				TmdmSubjectIdentifier.TM_SUBJECT);
		knownSystemIdentifiers.put("tm:name",
				TmdmSubjectIdentifier.TMDM_NAME_TYPE);
		knownSystemIdentifiers.put("tm:occurrence",
				TmdmSubjectIdentifier.TMDM_OCCURRENCE_TYPE);
		knownSystemIdentifiers.put("tm:type",
				TmdmSubjectIdentifier.TMDM_TOPIC_TYPE);
		knownSystemIdentifiers.put("tm:instance",
				TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE);
		knownSystemIdentifiers.put("tm:subtype",
				TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE);
		knownSystemIdentifiers.put("tm:subclass",
				TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE);
		knownSystemIdentifiers.put("tm:superclass",
				TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE);
		knownSystemIdentifiers.put("tm:supertype",
				TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE);
		knownSystemIdentifiers.put("tm:type-instance",
				TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE);
		knownSystemIdentifiers.put("tm:supertype-subtype",
				TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION);
		knownSystemIdentifiers.put("tm:subclass-of",
				TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION);
		knownSystemIdentifiers.put("tm:association",
				TmdmSubjectIdentifier.TMDM_ASSOCIATION_TYPE);
		knownSystemIdentifiers.put("tm:association-role",
				TmdmSubjectIdentifier.TMDM_ASSOCIATION_ROLE_TYPE);
	}

	public PrefixHandler() throws TMQLInitializationException {
		/*
		 * try to load predefined prefixes from file
		 */
		try {
			Properties prefixProperties = new Properties();
			prefixProperties.load(getClass().getResourceAsStream(
					"prefix.properties"));
			/*
			 * add all prefixes to map
			 */
			for (Map.Entry<Object, Object> prefix : prefixProperties.entrySet()) {
				prefixes.put(prefix.getKey().toString(), prefix.getValue()
						.toString());
			}
		} catch (Exception ex) {
			// IGNORE
		}
	}

	/**
	 * Register a new prefix definition in the TMQL engine. Please note that
	 * only registered prefixes can used in context of a TMQL query.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param uri
	 *            the absolute IRI
	 */
	public void registerPrefix(final String prefix, final String uri) {
		prefixes.put(prefix, uri);
	}

	/**
	 * Method return the IRI of the given identifier.
	 * 
	 * @param prefix
	 *            the identifier of the prefix
	 * @return the stored prefix or <code>null</code> if the identifiers is
	 *         unknown
	 */
	public String getPrefix(final String prefix) {
		return prefixes.get(prefix);
	}

	/**
	 * method returns all registered prefixes as mapping between prefix and
	 * absolute IRI.
	 * 
	 * @return the prefix map
	 */
	public Map<String, String> getPrefixMap() {
		return prefixes;
	}

	/**
	 * Method checks if the given token represents a TMDM specified IRI, like
	 * tm:name or tm:subject.
	 * 
	 * @param token
	 *            the token to check
	 * @return <code>true</code> if the token is a TMDM specified IRI,
	 *         <code>false</code> otherwise.
	 */
	public boolean isKnownSystemIdentifier(final String token) {
		return knownSystemIdentifiers.containsKey(token);
	}

	/**
	 * Method returns the absolute IRI of the given relative system identifier.
	 * 
	 * @param token
	 *            the relative IRI
	 * @return the corresponding absolute IRI
	 */
	public String getAbsoluteSystemIdentifier(final String token) {
		return knownSystemIdentifiers.get(token);
	}

	/**
	 * Returns the stored default prefix
	 * 
	 * @return the default prefix
	 */
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	/**
	 * Changes the stored default prefix
	 * 
	 * @param defaultPrefix
	 *            the default prefix
	 */
	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	/**
	 * Method checks if the given prefix is known by the handler.
	 * 
	 * @param prefix
	 *            the prefix to check
	 * @return <code>true</code> if the prefix is known, <code>false</code>
	 *         otherwise.
	 */
	public boolean isKnownPrefix(final String prefix) {
		return prefixes.containsKey(prefix);
	}

	/**
	 * Transform the given relative to its absolute IRI reference. If the given
	 * reference is already an absolute IRI, it will be returned unmodified. If
	 * the prefix is unknown, an exception will be thrown.
	 * 
	 * @param reference
	 *            the reference
	 * @return the absolute IRI
	 * @throws TMQLRuntimeException
	 *             thrown if the prefix is unknown
	 */
	private String toAbsoluteIRI(final String reference)
			throws TMQLRuntimeException {
		int index = reference.indexOf(":");
		if (index != -1) {
			String id = reference.substring(0, index);
			if (id.equalsIgnoreCase("http") || id.equalsIgnoreCase("file")
					|| id.equalsIgnoreCase("ftp")
					|| id.equalsIgnoreCase("https")) {
				return reference;
			}
			if (prefixes.containsKey(id)) {
				String prefix = prefixes.get(id);
				if (!prefix.endsWith("/") && !prefix.endsWith("#")) {
					prefix += "/";
				}
				return prefix + reference.substring(index + 1);
			}
			throw new TMQLRuntimeException("Unknown prefix '" + id + "'!");
		} else if (reference.indexOf("://") == -1) {
			return defaultPrefix
					+ (defaultPrefix.endsWith("/")
							|| defaultPrefix.endsWith("#") ? "" : "/")
					+ reference;
		}
		return reference;
	}

}
