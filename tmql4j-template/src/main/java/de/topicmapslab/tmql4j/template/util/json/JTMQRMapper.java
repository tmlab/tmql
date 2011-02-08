package de.topicmapslab.tmql4j.template.util.json;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;

/**
 * Mapper for JTMQR
 * 
 * Author: mhoyer Created: 03.11.10 13:57
 */
public class JTMQRMapper extends ObjectMapper {
	
	/**
	 * constructor
	 */
	public JTMQRMapper() {
		getSerializationConfig().addMixInAnnotations(Map.class, MapSerializer.MapMixIn.class);
		getSerializationConfig().addMixInAnnotations(QueryMatches.class, QueryMatchesSerializer.QueryMatchesMixIn.class);
	}
}
