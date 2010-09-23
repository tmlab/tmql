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

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MergeExpressionPathStyleTest extends
		BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMergeTwoTopicAsTupleExpression() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " MERGE o:Puccini , o:Franco_Alfano";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());
	}

	public void testMergeAllComposers() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = null;
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " MERGE o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testMergeAllComposerWithATypedOccurrenceInSpecificScope()
			throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " MERGE o:Composer >> instances [ . >> characteristics o:webpage >> scope == o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(6, set.size());
	}

	public void testMergeTwoComposerByOccurrenceValue() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Composer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " MERGE o:Composer >> instances [ . / o:date_of_birth == \"1858-12-22\" OR . / o:date_of_birth == \"1875-03-08\" ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());
	}

}
