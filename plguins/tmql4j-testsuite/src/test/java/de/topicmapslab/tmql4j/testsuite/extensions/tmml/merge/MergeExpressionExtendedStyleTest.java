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
public class MergeExpressionExtendedStyleTest extends BaseTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_leipzig.topicmapslab.tmql.testsuite.base.BaseTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMergeAllWritersBornInParis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Writer >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(70, set.size());

		query = prefix
				+ " MERGE $t, $person WHERE o:born_in ( o:Place : o:Paris , o:Person : $t ) AND o:born_in ( o:Place : o:Paris , o:Person : $person ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(2, set.size());

		query = prefix + " o:Writer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(59, set.size());
	}

}
