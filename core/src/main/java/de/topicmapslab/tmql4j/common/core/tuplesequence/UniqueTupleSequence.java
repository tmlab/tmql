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
package de.topicmapslab.tmql4j.common.core.tuplesequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.CollectionsUtility;

/**
 * Implementation of {@link ITupleSequence} to represent a tuple sequence
 * containing items only one times.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <E>
 *            the type of contained items
 */
public class UniqueTupleSequence<E> extends ArrayList<E> implements
		ITupleSequence<E> {

	private static final long serialVersionUID = 1L;
	/**
	 * internal flag if tuple sequence is unique
	 */
	private boolean unified = false;

	/**
	 * {@inheritDoc}
	 */
	public boolean isUniqueSet() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean wasUnified() {
		return unified;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean add(E o) {
		unified = false;
		return super.add(o);
	};

	/**
	 * {@inheritDoc}
	 */
	public void unify() {
		/*
		 * temporary cache of new elements
		 */
		List<E> list = new ArrayList<E>();
		/*
		 * iterate over all contained items
		 */
		for (E arg : this) {
			/*
			 * check if item is not contained
			 */
			if (!list.contains(arg)) {
				list.add(arg);
			}
		}
		/*
		 * set new values
		 */
		clear();
		addAll(list);
		/*
		 * set flag
		 */
		unified = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean retainAll(final ITMQLRuntime runtime, Collection<?> c) {
		unified = false;
		if (c instanceof ITupleSequence<?>) {
			/*
			 * call special retain method of CollectionsUtility
			 */
			return CollectionsUtility.retainAll(runtime,
					(ITupleSequence<Object>) this, (ITupleSequence<Object>) c);
		} else {
			return super.retainAll(c);
		}
	}
}