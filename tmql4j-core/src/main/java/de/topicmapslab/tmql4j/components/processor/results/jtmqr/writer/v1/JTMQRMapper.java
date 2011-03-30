package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common.ListSerializer;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common.MapSerializer;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.common.QueryMatchesSerializer;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * Author: mhoyer Created: 03.11.10 13:57
 */
public class JTMQRMapper extends ObjectMapper {
	/**
	 * constructor
	 */
	public JTMQRMapper() {
		/*
		 * register new serializer instances
		 */
		getSerializationConfig().addMixInAnnotations(SimpleResult.class, TupleSerializer.SimpleTupleResultMixIn.class);
		getSerializationConfig().addMixInAnnotations(SimpleResultSet.class, SequenceSerializer.SimpleResultSetMixIn.class);
		getSerializationConfig().addMixInAnnotations(Map.class, MapSerializer.MapMixIn.class);
		getSerializationConfig().addMixInAnnotations(QueryMatches.class, QueryMatchesSerializer.QueryMatchesMixIn.class);
		getSerializationConfig().addMixInAnnotations(List.class, ListSerializer.ListMixIn.class);
	}
}
