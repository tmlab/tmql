/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.engine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestResultSets extends Tmql4JTestCase {

	@Test
	public void testCellAccess() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topics.add(createTopic());
		}
		String query = null;
		SimpleResultSet set = null;

		query = "tm:subject >> instances";
		set = execute(query);
		assertEquals(topics.size(), set.size());

		int index = 0;
		for (IResult r : set.getResults()) {
			assertEquals(r, set.get(index));
			assertEquals(r.first(), set.get(index, 0));
			assertTrue(r.first() instanceof Topic);
			assertTrue(topics.contains(r.first()));
			index++;
		}
	}

	@Test
	public void testIsNull() throws Exception {
		SimpleResultSet set = new SimpleResultSet(null, null);
		SimpleResult result = new SimpleResult(set);
		result.add((Object) null);
		result.add("Topic");
		result.add((Object) null);
		set.addResult(result);

		assertTrue(result.isNullValue(0));
		assertFalse(result.isNullValue(1));
		assertTrue(result.isNullValue(2));

		assertTrue(set.isNullValue(0, 0));
		assertFalse(set.isNullValue(0, 1));
		assertTrue(set.isNullValue(0, 2));
	}
}
