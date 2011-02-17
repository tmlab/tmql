/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestDeleteAll extends Tmql4JTestCase {

	@Test
	public void testDeleteAll() throws Exception {
		for (int i = 0; i < 100; i++) {
			Topic topic = createTopic();
			Name name = topic.createName("Name");
			name.createVariant("Variant", createTopic());
			topic.createOccurrence(createTopic(), "Occurrence");
			Association a = createAssociation();
			a.createRole(createTopic(), createTopic());
		}

		assertEquals(601, topicMap.getTopics().size());
		assertEquals(100, topicMap.getAssociations().size());

		String query = "DELETE CASCADE ALL";
		SimpleResultSet set = execute(query);
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(1101, ((Integer) set.first().first()).intValue());

		assertEquals(0, topicMap.getTopics().size());
		assertEquals(0, topicMap.getAssociations().size());
	}

}
