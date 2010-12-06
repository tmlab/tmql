/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.merge.tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestMergeExpressionExtendedStyle extends Tmql4JTestCase {

	@Test
	public void testMerge() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic t = createTopicBySI("myTopic");
		t.addType(type);
		Topic o = createTopicBySI("other");
		o.addType(type);

		int n = 3;
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			n += 3;
		}

		String query = " MERGE $t, $o WHERE $t ISA myType AND $o ISA myType ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());

		assertEquals(n - 1, topicMap.getTopics().size());
	}

	@Test
	public void testMergeAll() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic t = createTopicBySI("myTopic");
		t.addType(type);
		Topic o = createTopicBySI("other");
		o.addType(type);

		int n = 3;
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			n += 3;
		}

		String query = " MERGE ALL $t WHERE $t ISA myType ";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());

		assertEquals(n - 1, topicMap.getTopics().size());
	}

}
