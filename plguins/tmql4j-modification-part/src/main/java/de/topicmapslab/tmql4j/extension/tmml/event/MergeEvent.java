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

import java.util.Set;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.event.model.EventType;

/**
 * Special modification event fired if items of the current map will be merged
 * using the TMQL merge-expression or because of internal merging rules.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MergeEvent extends ModificationEvent {

	/**
	 * set containing all items which will be merged
	 */
	private final Set<? extends Construct> mergedItems;

	/**
	 * base constructor to create a new event
	 * 
	 * @param modifiedConstructOrLocator
	 *            the item which caused the merging process
	 * @param mergedItems
	 *            all item which will be merged with the given item
	 * @param origin
	 *            the origin which fired the event
	 */
	public MergeEvent(Construct modifiedConstructOrLocator,
			Set<? extends Construct> mergedItems, Object origin) {
		super(modifiedConstructOrLocator, modifiedConstructOrLocator,
				EventType.MERGE, origin);
		this.mergedItems = mergedItems;
	}

	/**
	 * Method returns the list of items which will be merged or which were
	 * merged.
	 * 
	 * @return the mergedItems the set containing all items which are merged
	 */
	public Set<? extends Construct> getMergedItems() {
		return mergedItems;
	}

}
