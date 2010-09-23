/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.delete;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.extension.tmml.exception.DeletionException;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteExpressionPathSytleTest extends Tmql4JTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testDeleteTopic() throws Exception {
		createTopicBySI("topic");
		assertNotNull(topicMap
				.getTopicBySubjectIdentifier(createLocator("topic")));
		SimpleResultSet set = execute("topic");
		assertEquals(1, set.size());
		
		set = execute("DELETE topic");
		assertEquals(1, set.size());
		assertEquals(1L, set.first().first());
	}

	public void testDeleteOneName() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:name";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

		query = prefix + " DELETE o:Puccini >> characteristics tm:name [ 0 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / tm:name";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testDeleteScopedName() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name >> scope";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " DELETE o:Puccini >> characteristics tm:name [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name >> scope";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testDeleteTypedOccurrence() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:occurrence";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());

		query = prefix
				+ " DELETE o:Puccini >> characteristics tm:occurrence [ ^ o:webpage ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / tm:occurrence";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(11, set.size());
	}

	public void testDeleteTypedAndScopedOccurrence() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:occurrence";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());

		query = prefix
				+ " DELETE o:Puccini >> characteristics tm:occurrence [ . >> types == o:webpage AND . >> scope == o:Web ]";
		set = execute(query);
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / tm:occurrence";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(12, set.size());
	}

	public void testDeleteTopicWithTraversedConditionWithoutCascade()
			throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " DELETE o:Composer >> instances [ . >> traverse == o:La_Boheme ]";
		try {
			set = execute(new TMQLQuery(query));
			Assert.assertEquals(1, set.size());
		} catch (Exception e) {
			Assert.assertEquals(DeletionException.class, e.getClass());
		}

		query = prefix + "o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());
	}

	public void testDeleteTopicWithTraversedConditionWithCascade()
			throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " DELETE CASCADE o:Composer >> instances [ . >> traverse == o:La_Boheme ]";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());
	}

	public void testDeleteAllInstancesWithCascade() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " DELETE CASCADE o:Composer >> instances ";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testDeleteAllAssociationsOfSpecificTopicWithCascade()
			throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " DELETE CASCADE o:Puccini << players ";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Puccini << players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testDeleteFilteredAssociationsOfSpecificTopicWithCascade()
			throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix
				+ " DELETE CASCADE o:Puccini << players [ . >> players == o:La_Boheme ] ";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Puccini << players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(17, set.size());
	}

}
