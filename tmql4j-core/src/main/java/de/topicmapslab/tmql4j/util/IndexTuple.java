package de.topicmapslab.tmql4j.util;

import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;

/**
 * Tuple enables order in context of an order by clause
 * @author Sven Krosse
 *
 */
public class IndexTuple implements Comparable<IndexTuple> {
	/**
	 * the index at the tuple-sequence
	 */
	public Map<String, Object> origin;
	/**
	 * the tuple
	 */
	public Map<String, Object> tuple;

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IndexTuple o) {
		if (tuple.isEmpty()) {
			if ( !o.tuple.isEmpty()){
				return -1;
			}else{
				return 0;
			}
		}
		if (o.tuple.isEmpty()) {
			return 1;
		}
		Object value = tuple.get(QueryMatches.getNonScopedVariable());
		Object value2 = o.tuple.get(QueryMatches.getNonScopedVariable());
		if (value instanceof Number && value2 instanceof Number) {
			return (int) Math.round(((Number) value).doubleValue()
					- ((Number) value2).doubleValue());
		}
		return value.toString().compareTo(value2.toString());
	}
}
