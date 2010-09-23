/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.select;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.identifier.TmdmSubjectIdentifier;
import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestFromClause extends Tmql4JTestCase {

	@Test
	public void testFromClause() throws Exception {
		Set<Topic> others = HashUtil.getHashSet();

		Topic topic = createTopicBySI("myType");
		others.add(topic);
		Topic other = createTopicBySI("myOtherType");
		others.add(other);
		Set<Topic> instances = HashUtil.getHashSet();
		Set<Topic> otherInstances = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				instances.add(t);
			} else {
				t.addType(other);
				otherInstances.add(t);
			}
		}

		int cnt = 102;
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
			cnt += 3;
		}

		String query = "SELECT // tm:subject";
		SimpleResultSet set = execute(query);
		assertEquals(cnt, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(instances.contains(r.first())
					|| otherInstances.contains(r.first())
					|| others.contains(r.first()));
		}
		
		query = "SELECT // tm:subject FROM // myType";
		set = execute(query);
		assertEquals(50, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(instances.contains(r.first()));
		}
		
		query = "SELECT // tm:subject FROM // myOtherType";
		set = execute(query);
		assertEquals(50, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherInstances.contains(r.first()));
		}
		
		query = "SELECT // tm:subject FROM // myOtherType UNION // myType";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherInstances.contains(r.first()) || instances.contains(r.first()));
		}
	}

}
