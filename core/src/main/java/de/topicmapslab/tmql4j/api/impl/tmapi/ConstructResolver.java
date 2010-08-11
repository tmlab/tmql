/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.impl.tmapi;

import java.util.StringTokenizer;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.api.model.IConstructResolver;
import de.topicmapslab.tmql4j.common.context.PrefixHandler;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;

/**
 * Utility class to resolve the items of the topic map content which will be
 * identified using item references specified in the current TMQL draft.
 * <p>
 * If the item reference is an identifier then this identifier is interpreted as
 * a topic item identifier ([TMDM], Clause 5.1) for a topic in the effective
 * map. The result is then this topic item; if no such topic exists, an error
 * will be flagged.
 * </p>
 * <p>
 * If the item reference is a QName, then first that is expanded into an
 * absolute IRI according to 4.2. That absolute IRI is interpreted as subject
 * indicator for a topic in the effective map. If no such topic exist an error
 * will be flagged.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ConstructResolver implements IConstructResolver {

	/**
	 * private and hidden constructor
	 */
	protected ConstructResolver() {
	}

	/**
	 * Static method which tries to extract the construct of the topic map which
	 * identified by the given identifier.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param identifier
	 *            a string-represented identifier to identify the topic map item
	 * @return the topic map item and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if no topic map element can be found
	 */
	protected final Construct getElementByIdentifier(final TMQLRuntime runtime,
			final String identifier) throws TMQLRuntimeException {

		try {
			/*
			 * try to get element by identifier
			 */
			return tryToGetElementByIdentifier(runtime, identifier);
		} catch (TMQLRuntimeException ex) {
			/*
			 * check if identifier contains QNames
			 */
			if (!isAbsolute(runtime, identifier)) {
				/*
				 * try to get item by absolute IRI
				 */
				return tryToGetElementByIdentifier(runtime, toAbsoluteIRI(
						runtime, identifier));
			} else {
				final String defaultPrefix = runtime.getLanguageContext()
						.getPrefixHandler().getDefaultPrefix();
				/*
				 * no default prefix set
				 */
				if (defaultPrefix == null || defaultPrefix.isEmpty()) {
					/*
					 * throw exception
					 */
					throw ex;
				} else {
					/*
					 * try to get element by identifier
					 */
					return tryToGetElementByIdentifier(runtime, defaultPrefix
							+ identifier);
				}
			}
		}

	}

	/**
	 * Static method which tries to extract the construct of the topic map which
	 * identified by the given identifier.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param identifier
	 *            a string-represented identifier to identify the topic map item
	 * @return the topic map item and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if no topic map element can be found
	 */
	protected final Construct tryToGetElementByIdentifier(
			final TMQLRuntime runtime, final String identifier)
			throws TMQLRuntimeException {

		/*
		 * the temporary instance of identifier
		 */
		String identifier_ = identifier;
		/*
		 * Check if identifier is known symbolic constant like tm:name or
		 * tm:subject
		 */
		PrefixHandler handler = runtime.getLanguageContext().getPrefixHandler();
		if (handler.isKnownSystemIdentifier(identifier_)) {
			/*
			 * translate to absolute IRI
			 */
			identifier_ = handler.getAbsoluteSystemIdentifier(identifier);
		}

		try {

			/*
			 * Get current queried topic map
			 */
			Object object = runtime.getInitialContext()
					.getPredefinedVariableSet().getValue(
							VariableNames.CURRENT_MAP);
			/*
			 * Get current environment map
			 */
			Object object2 = runtime.getInitialContext()
					.getPredefinedVariableSet().getValue(
							VariableNames.ENVIRONMENT_MAP);
			/*
			 * both variable values must be an instance of TopicMap
			 */
			if (object instanceof TopicMap && object2 instanceof TopicMap) {
				TopicMap map = (TopicMap) object;
				TopicMap environment = (TopicMap) object2;
				/*
				 * Look for construct by Id in queried map
				 */
				Construct construct = map.getConstructById(identifier_);
				if (construct != null) {
					return construct;
				}
				/*
				 * Look for construct by Id in environment map
				 */
				construct = environment.getConstructById(identifier_);
				if (construct != null) {
					return construct;
				}
				/*
				 * Look for construct by item identifier in queried map
				 */
				Locator locator = map.createLocator(clean(identifier_));
				Locator envLocator = environment.createLocator(clean(identifier_));
				construct = map.getConstructByItemIdentifier(locator);
				if (construct != null) {
					return construct;
				}
				/*
				 * Look for construct by item identifier in environment map
				 */				
				construct = environment.getConstructByItemIdentifier(envLocator);
				if (construct != null) {
					return construct;
				}
				/*
				 * Look for topic by subject identifier in queried map
				 */
				Topic topic = map.getTopicBySubjectIdentifier(locator);
				if (topic != null) {
					return topic;
				}
				/*
				 * Look for topic by subject identifier in environment map
				 */
				topic = environment.getTopicBySubjectIdentifier(envLocator);
				if (topic != null) {
					return topic;
				}
				/*
				 * Look for topic by subject locator in queried map
				 */
				topic = map.getTopicBySubjectLocator(locator);
				if (topic != null) {
					return topic;
				}
				/*
				 * Look for topic by subject locator in environment map
				 */
				topic = environment.getTopicBySubjectLocator(envLocator);
				if (topic != null) {
					return topic;
				}

				/*
				 * if identifier is a system topic but does not exists, try to
				 * materialize it
				 */
				if (topic == null && !identifier.equalsIgnoreCase(identifier_)) {
					/*
					 * materialyzation is allowed
					 */
					if (runtime.getProperties().isMaterializeMetaModel()) {
						return map.createTopicBySubjectIdentifier(map
								.createLocator(identifier_));
					} else {
						return environment.createTopicBySubjectIdentifier(map
								.createLocator(identifier_));
					}
				}
			}
		} catch (MalformedIRIException e) {
			throw new TMQLRuntimeException(
					"Cannot resolve topic map construct " + identifier_, e);
		}
		throw new TMQLRuntimeException("Cannot resolve topic map construct "
				+ identifier_);

	}

	/**
	 * Internal method to clean the string-representation of the identifier if
	 * it is encapsulate by &gt; and &lt;
	 * 
	 * @param identifier
	 *            the identifier to clean
	 * @return the clean identifier and never <code>null</code>
	 */
	private String clean(final String identifier) {
		if (identifier.startsWith("<")) {
			return identifier.substring(1, identifier.length() - 1);
		}
		return identifier;
	}

	/**
	 * Method checks if the given identifier is an absolute IRI or if it
	 * contains QNames.
	 * 
	 * @param runtime
	 *            the TMQL4jRuntime
	 * @param identifier
	 *            the identifier to check
	 * @return <code>true</code> if the identifier is an absolute IRI,
	 *         <code>false</code> otherwise.
	 * @throws TMQLRuntimeException
	 *             thrown if QNames cannot be read from properties
	 */
	public static boolean isAbsolute(final TMQLRuntime runtime,
			final String identifier) throws TMQLRuntimeException {
		/*
		 * to speed up, first check if the character ":" contained in the
		 * identifier
		 */
		if (identifier.contains(":")) {
			/*
			 * Split potential QNames from the IRI
			 */
			StringTokenizer tokenizer = new StringTokenizer(identifier, ":");
			PrefixHandler handler = runtime.getLanguageContext()
					.getPrefixHandler();
			while (tokenizer.hasMoreTokens()) {
				/*
				 * check if QName is known by runtime
				 */
				if (handler.isKnownPrefix(tokenizer.nextToken())) {
					/*
					 * is known QName >> is relative URI
					 */
					return false;
				}
			}
			/*
			 * IRI doesn't contain any known QName
			 */
			return true;
		}
		/*
		 * IRI doesn't contain a QName
		 */
		return true;
	}

	/**
	 * method transform the given identifier to an absolute IRI by replacing the
	 * QNames with the defined prefixes. The prefixes will be defined in the
	 * {@link TMQLRuntimeProperties}.
	 * 
	 * @param runtime
	 *            the TMQL4jRuntime
	 * @param identifier
	 *            the identifier to transform
	 * @return the absolute IRI and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if QNames cannot be read from properties
	 */
	public static String toAbsoluteIRI(final TMQLRuntime runtime,
			final String identifier) throws TMQLRuntimeException {
		String identifier_ = identifier;

		while (!isAbsolute(runtime, identifier_)) {
			String prefix = identifier_.substring(0, identifier_.indexOf(":"));
			String namespace = runtime.getLanguageContext().getPrefixHandler()
					.getPrefix(prefix);
			if (namespace == null) {
				throw new TMQLRuntimeException("Unknown namespace for prefix "
						+ prefix);
			}
			identifier_ = namespace
					+ identifier_.substring(identifier_.indexOf(":") + 1);
		}
		return identifier_;
	}

	/**
	 * {@inheritDoc}
	 */
	public Construct getConstructByIdentifier(final TMQLRuntime runtime,
			String identifier, TopicMap topicMap) throws Exception {
		Construct construct = getTopicBySubjectIdentifier(identifier, topicMap);
		if (construct == null) {
			construct = getConstructByItemIdentifier(identifier, topicMap);
			if (construct == null) {
				construct = getConstructByItemIdentifier(identifier, topicMap);
				if (construct == null) {
					construct = getElementByIdentifier(runtime, identifier);
				}
			}
		}
		return construct;
	}

	/**
	 * {@inheritDoc}
	 */
	public Construct getConstructByItemIdentifier(String identifier,
			TopicMap topicMap) throws Exception {
		return topicMap.getConstructByItemIdentifier(topicMap
				.createLocator(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectIdentifier(String identifier,
			TopicMap topicMap) throws Exception {
		return topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectLocator(String identifier, TopicMap topicMap)
			throws Exception {
		return topicMap.getTopicBySubjectLocator(topicMap
				.createLocator(identifier));
	}

}
