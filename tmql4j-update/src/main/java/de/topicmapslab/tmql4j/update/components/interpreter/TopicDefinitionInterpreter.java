/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.components.interpreter;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.update.grammar.productions.TopicDefinition;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Special implementation of {@link ExpressionInterpreterImpl} representing the
 * interpreter of a topic-definition as a kind of update-clauses.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * topic-definition ::= string-literal ( ! | ~ | = )
 * topic-definition ::= string-literal << ( item | locators | indicators )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TopicDefinitionInterpreter extends ExpressionInterpreterImpl<TopicDefinition> {
	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public TopicDefinitionInterpreter(TopicDefinition ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		TopicMap topicMap = context.getQuery().getTopicMap();
		Class<? extends IToken> identifierType = getExpression().getIdentifierType();
		/*
		 * extract string-represented reference
		 */
		Locator locator = topicMap.createLocator(runtime.getLanguageContext().getPrefixHandler().toAbsoluteIRI(LiteralUtils.asString(getTokens().get(0))));

		Topic topic = createTopic(topicMap, identifierType, locator);
		return QueryMatches.asQueryMatchNS(runtime, topic == null ? 0 : 1);
	}

	/**
	 * Utility method to create a topic
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifierType
	 *            the token represent the type of used identity
	 * @param locator
	 *            the locator used as identity
	 * @return the topic or <code>null</code>
	 */
	public static Topic createTopic(TopicMap topicMap, Class<? extends IToken> identifierType, Locator locator) {
		long count = 0;
		if (identifierType.equals(AxisIndicators.class) || identifierType.equals(ShortcutAxisIndicators.class)) {
			/*
			 * check topic by subject-identifier
			 */
			Topic topic = topicMap.getTopicBySubjectIdentifier(locator);
			if (topic != null) {
				return topic;
			}
			/*
			 * check construct by item-identifier
			 */
			Construct construct = topicMap.getConstructByItemIdentifier(locator);
			if (construct != null) {
				if (construct instanceof Topic) {
					return (Topic) construct;
				}
				throw new TMQLRuntimeException("Topic expected but identifier is used by anything else than a topic!");
			}
			/*
			 * create topic by subject-identifier
			 */
			count++;
			return topicMap.createTopicBySubjectIdentifier(locator);
		} else if (identifierType.equals(AxisItem.class) || identifierType.equals(ShortcutAxisItem.class)) {
			/*
			 * check topic by subject-identifier
			 */
			Topic topic = topicMap.getTopicBySubjectIdentifier(locator);
			if (topic != null) {
				return topic;
			}
			/*
			 * check construct by item-identifier
			 */
			Construct construct = topicMap.getConstructByItemIdentifier(locator);
			if (construct != null) {
				if (construct instanceof Topic) {
					return (Topic) construct;
				}
				throw new TMQLRuntimeException("Topic expected but identifier is used by anything else than a topic!");
			}
			/*
			 * create topic by item-identifier
			 */
			count++;
			return topicMap.createTopicByItemIdentifier(locator);
		} else if (identifierType.equals(AxisLocators.class) || identifierType.equals(ShortcutAxisLocators.class)) {
			/*
			 * check topic by subject-locator
			 */
			Topic topic = topicMap.getTopicBySubjectLocator(locator);
			if (topic != null) {
				return topic;
			}
			/*
			 * create topic by subject-locator
			 */
			count++;
			return topicMap.createTopicBySubjectLocator(locator);
		}
		return null;
	}
}
