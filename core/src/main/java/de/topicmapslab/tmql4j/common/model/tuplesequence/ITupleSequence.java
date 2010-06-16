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
package de.topicmapslab.tmql4j.common.model.tuplesequence;

import java.util.List;

/**
 * Class representing a tuple sequence during the interpretation process of the
 * TMQL engine. A tuple sequence is a unsorted list of tuples. Every tuple can
 * be contained multiple times. A tuple containing a unsorted list of atoms or
 * literals.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <E>
 *            the type of contained items
 */
public interface ITupleSequence<E> extends List<E> {

	/**
	 * Method checks if the tuple sequence contains same items multiple times or
	 * not. After calling the method {@link ITupleSequence#unify()} this method
	 * has to return always <code>true</code>.
	 * 
	 * @return <code>true</code> if each item contained in the sequence are
	 *         unique and will not contain multiple times, <code>false</code>
	 *         otherwise.
	 */
	public boolean isUniqueSet();

	/**
	 * Method is calling to unify the tuple sequence. After calling this method,
	 * the sequence contains each item only one times.
	 */
	public void unify();

	/**
	 * Method checks if tuple sequence was unified.
	 * 
	 * @see ITupleSequence#wasUnified()
	 * @return <code>true</code> if the method {@link ITupleSequence#unify()}
	 *         was called before, <code>false</code> otherwise.
	 */
	public boolean wasUnified();
}
