/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime;

import java.net.URI;
import java.util.StringTokenizer;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
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
	public TmqlConstructResolver(final ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final Construct getConstructByIdentifier(final IContext context, final String identifier) throws TMQLRuntimeException {
		/*
		 * try to get element by identifier
		 */
		return tryToGetElementByIdentifier(context, getAbsoluteIdentifier(context, identifier));	
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
	private final Construct tryToGetElementByIdentifier(final IContext context, final String identifier) throws TMQLRuntimeException {
		TopicMap topicMap = context.getQuery().getTopicMap();
	
		try {
			Construct construct;
			/*
			 * Look for construct by item identifier in queried map
			 */
			Locator locator = topicMap.createLocator(identifier);
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
	 * Utility method convert the given identifier to an absolute IRI
	 * identifier. If the given identifier is absolute, it will be returned
	 * unmodified. If it contains a prefix part, it will be replace. Otherwise
	 * the default prefix is used.
	 * 
	 * @param context
	 *            the context
	 * @param identifier
	 *            the identifier
	 * @return the absolute identifier
	 */
	private final String getAbsoluteIdentifier(IContext context, final String identifier) {
		/*
		 * remove <...> if exists
		 */
		String identifier_ = clean(identifier);
		/*
		 * is absolute
		 */
		if (!isAbsolute(context, identifier_)) {
			/*
			 * is system identifier
			 */
			if (runtime.getLanguageContext().getPrefixHandler().isKnownSystemIdentifier(identifier_)) {
				identifier_ = runtime.getLanguageContext().getPrefixHandler().getAbsoluteSystemIdentifier(identifier_);
			}
			/*
			 * contains prefix
			 */
			else if (identifier_.contains(":")) {
				identifier_ = toAbsoluteIRI(context, identifier_);
			}
			/*
			 * check default prefix
			 */
			else {
				final String defaultPrefix = runtime.getLanguageContext().getPrefixHandler().getDefaultPrefix();
				/*
				 * default prefix set
				 */
				if (defaultPrefix != null && !defaultPrefix.isEmpty()) {
					identifier_ = defaultPrefix + identifier;
				} else {
					throw new TMQLRuntimeException("Invalid IRI, non default prefix set, but IRI is relative.");
				}
			}
		}
		return identifier_;
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
		}
		/*
		 * try to create IRI
		 */
		try {
			URI uri = new URI(identifier);
			return uri.isAbsolute();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * {@inheritDoc}
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
		Locator locator = topicMap.createLocator(getAbsoluteIdentifier(context, identifier));
		return topicMap.getConstructByItemIdentifier(locator);
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectIdentifier(IContext context, String identifier) {
		TopicMap topicMap = context.getQuery().getTopicMap();
		Locator locator = topicMap.createLocator(getAbsoluteIdentifier(context, identifier));
		return topicMap.getTopicBySubjectIdentifier(locator);
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicBySubjectLocator(IContext context, String identifier) {
		TopicMap topicMap = context.getQuery().getTopicMap();
		Locator locator = topicMap.createLocator(getAbsoluteIdentifier(context, identifier));
		return topicMap.getTopicBySubjectLocator(locator);
	}

}
