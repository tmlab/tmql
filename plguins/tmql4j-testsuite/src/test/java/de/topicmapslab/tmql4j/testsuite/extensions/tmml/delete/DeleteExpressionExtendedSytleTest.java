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
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteExpressionExtendedSytleTest extends BaseTest {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testDeleteFirstNameOfAllComposer() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:name";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

		query = prefix
				+ " DELETE $t >> characteristics tm:name [ 0 ] WHERE $t ISA o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / tm:name";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testDeleteScopedNamesOfAllComposers() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name >> scope";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " DELETE $t >> characteristics tm:name [ @ o:short_name ] WHERE $t ISA o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name >> scope";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testDeleteTypedOccurrenceOfAllComposers() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:occurrence";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());

		query = prefix
				+ " DELETE $t >> characteristics tm:occurrence [ ^ o:webpage ] WHERE $t ISA o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

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
				+ " DELETE $t >> characteristics tm:occurrence [ . >> types == o:webpage AND . >> scope == o:Web ] WHERE $t ISA o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

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
				+ " DELETE $t WHERE o:composed_by( o:Composer : $t , o:Work : o:La_Boheme )";
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
				+ " DELETE CASCADE $t WHERE o:composed_by( o:Composer : $t , o:Work : o:La_Boheme )";

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

		query = prefix + " DELETE CASCADE $t WHERE $t ISA o:Composer";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(13, set.size());

		query = prefix + "o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testDeleteAllAssociationsOfSpecificTopicWithCascade() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " DELETE CASCADE $t >>traverse WHERE o:composed_by( o:Composer : $t , o:Work : o:La_Boheme ) ";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Puccini << players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}
	
	public void testDeleteFilteredAssociationsOfSpecificTopicWithCascade() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " DELETE CASCADE $opera WHERE o:composed_by( o:Composer : o:Puccini , o:Work : $opera ) AND $opera == o:La_Boheme  ";

		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "o:Puccini << players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(17, set.size());
	}

}
