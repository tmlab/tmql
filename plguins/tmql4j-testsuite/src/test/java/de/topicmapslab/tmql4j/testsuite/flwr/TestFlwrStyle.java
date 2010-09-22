/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.flwr;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestFlwrStyle extends Tmql4JTestCase {

	public void testEmbeddedQuery() throws Exception {

		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			if (i % 3 == 0) {
				topics[i] = createTopicBySI(base + i);
			} else if (i % 3 == 1){
				topics[i] = createTopicBySL(base + i);
			}else {
				topics[i] = createTopicByII(base + i);
			}
		}

		final String query = "FOR $t IN // tm:subject RETURN { FOR $l IN $t >> indicators ++ $t >> locators ++ $t >> item WHERE $l >> atomify =~ \""
				+ base + ".*\" RETURN ( $t,$l) }";
		IResultSet<?> set = execute(query);
		assertEquals(topics.length, set.size());
	}

}
