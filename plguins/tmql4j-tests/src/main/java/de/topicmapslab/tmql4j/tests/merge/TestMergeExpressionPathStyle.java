/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.merge;

import static junit.framework.Assert.assertEquals;


import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestMergeExpressionPathStyle extends Tmql4JTestCase {

	@Test
	public void testMergeTwoTopicAsTupleExpression() throws Exception {
		createTopicBySI("myTopic");
		createTopicBySI("other");

		assertEquals(2, topicMap.getTopics().size());

		String query;
		SimpleResultSet set;

		query = " MERGE myTopic , other";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());
		set = execute(new TMQLQuery(query));
		assertEquals(1, topicMap.getTopics().size());
	}

	@Test
	public void testMergeAllComposers() throws Exception {
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

		assertEquals(n, topicMap.getTopics().size());

		String query;
		SimpleResultSet set;

		query = " MERGE myType >> instances";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());
		set = execute(new TMQLQuery(query));
		assertEquals(n - 1, topicMap.getTopics().size());
	}

	@Test
	public void testMergeWithFilter() throws Exception {
		String val1 = "1858-12-22";
		String val2 = "1858-12-10";

		Topic type = createTopicBySI("myType");
		Topic t = createTopicBySI("myTopic");
		t.addType(type);
		t.createOccurrence(type, val1);
		Topic o = createTopicBySI("other");
		o.addType(type);
		o.createOccurrence(type, val2);

		Topic t2 = createTopicBySI("t2");
		t2.addType(type);

		int n = 4;
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			n += 3;
		}

		assertEquals(n, topicMap.getTopics().size());

		String query;
		SimpleResultSet set;

		query = " MERGE myType >> instances [ . / myType =~ \"" + val1
				+ "\" OR . / myType =~ \"" + val2 + "\" ]";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());
		set = execute(new TMQLQuery(query));
		assertEquals(n - 1, topicMap.getTopics().size());
	}

}
