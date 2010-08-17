/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.associationdef;

import java.util.Collection;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestAssociationDef extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAssociationDefAsResult() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		Topic topic = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.ontopedia.net/Puccini"));
		assertNotNull(topic);

		int size = topic.getRolesPlayed().size();

		query = prefix
				+ " tm:subject ( tm:subject : http://psi.ontopedia.net/Puccini , ... )";
		set = execute(new TMQLQuery(query));
		assertEquals(size, set.getResults().size());

		query = prefix
				+ " RETURN tm:subject ( tm:subject : http://psi.ontopedia.net/Puccini , ... )";
		set = execute(new TMQLQuery(query));
		assertEquals(size, set.getResults().size());

		query = prefix
				+ " SELECT tm:subject ( tm:subject : http://psi.ontopedia.net/Puccini , ... )";
		set = execute(new TMQLQuery(query));
		assertEquals(size, ((Collection<?>)set.getResults().get(0).first()).size());
	}

}
