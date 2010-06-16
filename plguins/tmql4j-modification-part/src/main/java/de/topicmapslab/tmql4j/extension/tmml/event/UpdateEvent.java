/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.event;

import de.topicmapslab.tmql4j.event.model.EventType;
import de.topicmapslab.tmql4j.lexer.model.IToken;

/**
 * Special modification event fired during a topic map construct was changed
 * using the update-expression of TMQL.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateEvent extends ModificationEvent {

	/**
	 * the token representing the kind of modification
	 */
	private final Class<? extends IToken> modifiedKey;
	/**
	 * the modified element of the topic map construct
	 */
	private final Object modifiedValue;

	/**
	 * Base constructor to create a new event
	 * 
	 * @param modifiedConstructOrLocator
	 *            the affected topic map construct
	 * @param origin
	 *            the origin which fire the event
	 * @param modifiedKey
	 *            the token representing the kind of modification
	 * @param modifiedValue
	 *            the modified element of the topic map construct
	 */
	public UpdateEvent(Object modifiedConstructOrLocator, Object origin,
			Class<? extends IToken> modifiedKey, Object modifiedValue) {
		super(modifiedConstructOrLocator, modifiedConstructOrLocator,
				EventType.UPDATE, origin);

		this.modifiedKey = modifiedKey;
		this.modifiedValue = modifiedValue;
	}

	/**
	 * Method return the token representing the kind of modification. The token
	 * is a {@link IToken} similar to the TMQL axes which describe the kind of
	 * modification.
	 * 
	 * @return the modifiedKey a token representing the kind of modification
	 */
	public Class<? extends IToken> getModifiedKey() {
		return modifiedKey;
	}

	/**
	 * Method return the modified element of the affected construct.
	 * 
	 * @return the modifiedValue the modified element
	 */
	public Object getModifiedValue() {
		return modifiedValue;
	}

}
