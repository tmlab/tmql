/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.merge;

import org.junit.Assert;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestMergeExpressionAssociation extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMergeAssociations() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		Topic topic = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.ontopedia.net/Puccini"));
		assertNotNull(topic);

		int size = topic.getRolesPlayed().size();
		assertTrue(size > 1);

		query = prefix
				+ " MERGE tm:subject ( tm:subject : http://psi.ontopedia.net/Puccini )";
		set = execute(new TMQLQuery(query));
		assertEquals(0L, set.getResults().get(0).first());

		try {
			query = prefix
					+ " MERGE tm:subject ( tm:subject : http://psi.ontopedia.net/Puccini , ... )";
			set = execute(new TMQLQuery(query));
			fail("At least one association is of the wrong type!");
		} catch (Exception e) {
			// NOTHING TO DO
		}

		query = prefix
				+ " MERGE http://psi.ontopedia.net/composed_by ( tm:subject : http://psi.ontopedia.net/Puccini , ... )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		System.out.println(set);
		size = size - 11;
		assertEquals(size, topic.getRolesPlayed().size());
	}

}
