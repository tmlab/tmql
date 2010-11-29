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
package de.topicmapslab.tmql4j.components.processor.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;

/**
 * Utility class to extends functionality of {@link Collections}
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public final class CollectionsUtility {

	/**
	 * private and hidden constructor
	 */
	private CollectionsUtility() {
	}

	/**
	 * Method checks if the given collections contain the same content. Please
	 * note that the method only check if each item of one collection is
	 * contained at least one times in the other collection. Method does not
	 * check if the number of containments of each element are equal. For
	 * example this means that { A , B , B } and { A , A , B } are contained
	 * equal content.
	 * 
	 * @param collectionA
	 *            the first collection
	 * @param collectionB
	 *            the other collection
	 * @return <code>true</code> if the content are equal, <code>false</code>
	 *         otherwise.
	 */
	public static boolean isContentEqual(final Collection<?> collectionA, final Collection<?> collectionB) {
		/*
		 * check if at least one parameter are null
		 */
		if (collectionA == null || collectionB == null) {
			throw new IllegalArgumentException("parameters can not be null.");
		}

		/*
		 * check if both collection have the same size
		 */
		if (collectionA.size() != collectionB.size()) {
			return false;
		}

		/*
		 * check if each item of collection A contained in collection B
		 */
		for (Object obj : collectionA) {
			if (!collectionB.contains(obj)) {
				return false;
			}
		}

		/*
		 * check if each item of collection B contained in collection A
		 */
		for (Object obj : collectionB) {
			if (!collectionA.contains(obj)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Special retainAll implementation for tuple sequences.
	 * 
	 * @see Collection#retainAll(Collection)
	 * @param runtime
	 *            the TMQL4JRuntime
	 * @param sequenceA
	 *            the first sequence
	 * @param sequenceB
	 *            the other sequence
	 * @return <code>true</code> if at least one element are contained in the
	 *         retained sequence, <code>false</code> otherwise.
	 */
	public static boolean retainAll(ITMQLRuntime runtime, List<Object> sequenceA, final List<Object> sequenceB) {

		/*
		 * create temporary sequences
		 */
		List<Object> sequenceA_ = HashUtil.getList();
		List<Object> sequenceB_ = HashUtil.getList();

		/*
		 * check if sequences containing sequences
		 */
		if (hasToAtomify(sequenceA)) {
			/*
			 * atomify sequences
			 */
			sequenceA_.addAll(atomify(runtime, sequenceA));
		} else {
			sequenceA_.addAll(sequenceA);
		}

		/*
		 * check if sequences containing sequences
		 */
		if (hasToAtomify(sequenceB)) {
			/*
			 * atomify sequences
			 */
			sequenceB_.addAll(atomify(runtime, sequenceB));
		} else {
			sequenceB_.addAll(sequenceB);
		}

		sequenceA.clear();

		/*
		 * iterate over temporary sequences
		 */
		boolean modified = false;
		Iterator<?> iterator = sequenceA_.iterator();
		while (iterator.hasNext()) {
			/*
			 * add item which are contained by both sequences
			 */
			Object objectA = iterator.next();
			if (sequenceB_.contains(objectA)) {
				sequenceA.add(objectA);
				modified = true;
			}
		}

		return modified;
	}

	/**
	 * 
	 * Method reduce the sequence to 2D, this means all contained collections
	 * will be reduced to there atomic items.
	 * 
	 * 
	 * @param <E>
	 *            the type of contained items
	 * @param runtime
	 *            the TMQL4JRuntime
	 * @param sequence
	 *            the sequence to atomify
	 * @return the atomified sequence
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Object> List<E> atomify(final ITMQLRuntime runtime, List<E> sequence) {
		/*
		 * check if sequence is empty
		 */
		if (sequence.isEmpty()) {
			return sequence;
		} else {
			/*
			 * create new sequence
			 */
			List<E> seq = HashUtil.getList();

			/*
			 * iterate over elements
			 */
			for (E obj : sequence) {
				/*
				 * if item is tuple sequence call atomify() against
				 */
				if (obj instanceof List<?>) {
					seq.addAll(atomify(runtime, (List<E>) obj));
				}
				/*
				 * if item is atomic value
				 */
				else {
					seq.add(obj);
				}
			}
			return seq;
		}
	}

	/**
	 * 
	 * Internal method to check if a tuple sequence contains sequences or only
	 * atomic values.
	 * 
	 * @param <E>
	 *            the type of contained items
	 * @param sequence
	 *            the sequence
	 * @return <code>true</code> if sequence has to atomfiy, <code>false</code>
	 *         otherwise
	 */
	private static <E extends Object> boolean hasToAtomify(List<E> sequence) {
		if (sequence.isEmpty()) {
			return false;
		}
		for (E obj : sequence) {
			if (obj instanceof List<?>) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method checks if one map contain at least the same entries like the
	 * other. The third parameter are used to enable a mapping the the keys of
	 * the first and the second map.
	 * 
	 * @param map
	 *            the map to check
	 * @param containment
	 *            the entries which has to contain
	 * @param mapping
	 *            the key-mapping between the both maps
	 * @return <code>true</code> if the first map contain at least the same
	 *         entries like the other, <code>false</code> otherwise.
	 */
	public static final boolean containsAll(Map<String, Object> map, Map<String, Object> containment, final Map<String, String> mapping) {
		/*
		 * iterate over entries, which should be contained
		 */
		for (Entry<String, Object> entry : containment.entrySet()) {
			String key = entry.getKey();
			/*
			 * check if key has to map
			 */
			if (mapping.containsKey(key)) {
				key = mapping.get(key);
			}
			/*
			 * check if key is contained or if key is fn:count
			 */
			if (map.containsKey(key) || entry.getKey().equalsIgnoreCase("fn:count")) {
				if (map.get(key).equals(entry.getValue())) {
					continue;
				}
			}
			return false;
		}
		return true;
	}

}
