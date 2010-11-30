/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime;

import java.util.StringTokenizer;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.IConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.PrefixHandler;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

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
public class TmqlConstructResolver implements IConstructResolver {

	private final ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime called this constructor
	 */
	protected TmqlConstructResolver(final ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final Construct getConstructByIdentifier(final IContext context, final String identifier) throws TMQLRuntimeException {
		Construct construct = getTopicBySubjectIdentifier(context, identifier);
		if (construct == null) {
			construct = getConstructByItemIdentifier(context, identifier);
			if (construct == null) {
				construct = getConstructByItemIdentifier(context, identifier);
				if (construct == null) {
					try {
						/*
						 * try to get element by identifier
						 */
						construct = tryToGetElementByIdentifier(context, identifier);
					} catch (TMQLRuntimeException ex) {
						/*
						 * check if identifier contains QNames
						 */
						if (!isAbsolute(context, identifier)) {
							/*
							 * try to get item by absolute IRI
							 */
							construct = tryToGetElementByIdentifier(context, toAbsoluteIRI(context, identifier));
						} else {
							final String defaultPrefix = runtime.getLanguageContext().getPrefixHandler().getDefaultPrefix();
							/*
							 * no default prefix set
							 */
							if (defaultPrefix != null && !defaultPrefix.isEmpty()) {
								/*
								 * try to get element by identifier
								 */
								construct = tryToGetElementByIdentifier(context, defaultPrefix + identifier);
							}
						}
					}
				}
			}
		}
		return construct;

	}

	/**
	 * Static method which tries to extract the construct of the topic map which
	 * identified by the given identifier.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifier
	 *            a string-represented identifier to identify the topic map item
	 * @return the topic map item and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if no topic map element can be found
	 */
	protected final Construct tryToGetElementByIdentifier(final IContext context, final String identifier) throws TMQLRuntimeException {
		TopicMap topicMap = context.getQuery().getTopicMap();
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
			Construct construct;
			/*
			 * Look for construct by item identifier in queried map
			 */
			Locator locator = topicMap.createLocator(clean(identifier_));
			construct = topicMap.getConstructByItemIdentifier(locator);
			if (construct != null) {
				return construct;
			}
			/*
			 * Look for topic by subject identifier in queried map
			 */
			Topic topic = topicMap.getTopicBySubjectIdentifier(locator);
			if (topic != null) {
				return topic;
			}
			/*
			 * Look for topic by subject locator in queried map
			 */
			topic = topicMap.getTopicBySubjectLocator(locator);
			if (topic != null) {
				return topic;
			}
		} catch (MalformedIRIException e) {
			// NOTHING TO DO
		}
		return null;

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
	 * @param context
	 *            the context
	 * @param identifier
	 *            the identifier to check
	 * @return <code>true</code> if the identifier is an absolute IRI,
	 *         <code>false</code> otherwise.
	 * @throws TMQLRuntimeException
	 *             thrown if QNames cannot be read from properties
	 */
	public boolean isAbsolute(final IContext context, final String identifier) throws TMQLRuntimeException {
		/*
		 * to speed up, first check if the character ":" contained in the
		 * identifier
		 */
		if (identifier.contains(":")) {
			/*
			 * Split potential QNames from the IRI
			 */
			StringTokenizer tokenizer = new StringTokenizer(identifier, ":");
			PrefixHandler handler = runtime.getLanguageContext().getPrefixHandler();
			if (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				/*
				 * check current context
				 */
				if (context.getPrefix(token) != null) {
					return false;
				}
				/*
				 * check if QName is known by runtime
				 */
				if (handler.isKnownPrefix(tokenizer.nextToken())) {
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
	 * @param context
	 *            the context
	 * @param identifier
	 *            the identifier to transform
	 * @return the absolute IRI and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if QNames cannot be read from properties
	 */
	public String toAbsoluteIRI(final IContext context, final String identifier) throws TMQLRuntimeException {
		String identifier_ = identifier;

		while (!isAbsolute(context, identifier_)) {
			String prefix = identifier_.substring(0, identifier_.indexOf(":"));
			/*
			 * check current context
			 */
			String reference = context.getPrefix(prefix);
			if (reference == null) {
				/*
				 * check runtime prefixes
				 */
				reference = runtime.getLanguageContext().getPrefixHandler().getPrefix(prefix);
			}
			/*
			 * no prefix found
			 */
			if (reference == null) {
				throw new TMQLRuntimeException("Unknown namespace for prefix " + prefix);
			}
			identifier_ = reference + identifier_.substring(identifier_.indexOf(":") + 1);
		}
		return identifier_;
	}

	/**
	 * {@inheritDoc}
	 */
	public Construct getConstructByItemIdentifier(IContext context, String identifier) {
		TopicMap topicMap = context.getQuery().getTopicMap();
		return topicMap.getConstructByItemIdentifier(topicMap.createLocator(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectIdentifier(IContext context, String identifier) {
		TopicMap topicMap = context.getQuery().getTopicMap();
		return topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectLocator(IContext context, String identifier) {
		TopicMap topicMap = context.getQuery().getTopicMap();
		return topicMap.getTopicBySubjectLocator(topicMap.createLocator(identifier));
	}

}
