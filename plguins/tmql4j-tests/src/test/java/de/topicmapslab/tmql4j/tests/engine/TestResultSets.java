/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.engine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestResultSets extends Tmql4JTestCase {

	@Test
	public void testCellAccess() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			topics.add( createTopic());
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

}
