/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Topic;
import org.tmapi.core.Typed;

import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SupertypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Base implementation of a navigation axis to implement some core functionality of each TMQL axis.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class Axis implements IAxis {

	/**
	 * the internal enumeration value representing the axis type
	 */
	private final Class<? extends IToken> axisToken;

	/**
	 * constructor
	 * 
	 * @param axisToken
	 *            the axis token
	 */
	protected Axis(final Class<? extends IToken> axisToken) {
		this.axisToken = axisToken;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IToken> getIdentifier() {
		return axisToken;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IToken> getNavigationType() {
		return axisToken;
	}

	/**
	 * Returns all transitive subtypes and the topic itself
	 * 
	 * @param context
	 *            the context
	 * @param type
	 *            the topic type
	 * @return the transitive subtypes and the type
	 */
	@SuppressWarnings("unchecked")
	protected Collection<Topic> getTypes(IContext context, Topic type) {
		SupertypesAxis axis = new SupertypesAxis();
		Set<Topic> topics = HashUtil.getHashSet();
		topics.addAll((Collection<Topic>) axis.navigate(context, type, null));
		topics.add(type);
		return topics;
	}

	/**
	 * Checks if the given topic matches the given topic type.
	 * 
	 * @param context
	 *            the context
	 * @param topic
	 *            the topic
	 * @param type
	 *            the type
	 * @return <code>true</code> if the topic matches the type argument, <code>false</code> otherwise.
	 */
	protected boolean matches(IContext context, Topic topic, Topic type) {
		if (context.isTransitive()) {
			Collection<Topic> topics = getTypes(context, type);
			Set<Topic> types = HashUtil.getHashSet(topic.getTypes());
			types.retainAll(topics);
			return !types.isEmpty();
		} else {
			return topic.getTypes().contains(type);
		}
	}

	/**
	 * Checks if the given typed matches the given topic type.
	 * 
	 * @param context
	 *            the context
	 * @param typed
	 *            the typed
	 * @param type
	 *            the type
	 * @return <code>true</code> if the typed matches the type argument, <code>false</code> otherwise.
	 */
	protected boolean matches(IContext context, Typed typed, Topic type) {
		if (context.isTransitive()) {
			Collection<Topic> topics = getTypes(context, type);
			return topics.contains(typed.getType());
		} else {
			return typed.getType().equals(type);
		}
	}

}
