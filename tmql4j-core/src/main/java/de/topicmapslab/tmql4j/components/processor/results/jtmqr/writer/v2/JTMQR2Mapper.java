package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common.MapSerializer;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common.QueryMatchesSerializer;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2.ListSerializer;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * 
 * @author Christian Haß
 *
 */
public class JTMQR2Mapper extends ObjectMapper {

	/**
	 * constructor
	 */
	public JTMQR2Mapper() {
		
		/*
		 * register serializer
		 */
		getSerializationConfig().addMixInAnnotations(SimpleResult.class, TupleSerializer.SimpleTupleResultMixIn.class);
		getSerializationConfig().addMixInAnnotations(SimpleResultSet.class, SequenceSerializer.SimpleResultSetMixIn.class);
		getSerializationConfig().addMixInAnnotations(Map.class, MapSerializer.MapMixIn.class);
		getSerializationConfig().addMixInAnnotations(QueryMatches.class, QueryMatchesSerializer.QueryMatchesMixIn.class);
		getSerializationConfig().addMixInAnnotations(List.class, ListSerializer.ListMixIn.class);
	}
}
