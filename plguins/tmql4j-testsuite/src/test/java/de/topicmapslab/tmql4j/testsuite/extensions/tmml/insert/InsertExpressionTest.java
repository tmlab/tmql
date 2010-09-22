/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.insert;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertExpressionTest extends BaseTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_leipzig.topicmapslab.tmql.testsuite.base.BaseTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void testInsertSimpleTopic() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix
				+ " INSERT \"\"\" http://psi.ontopedia.net/new_Composer . \"\"\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:new_Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testInsertTopicWithType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " INSERT \"\"\"http://psi.ontopedia.net/new_Composer isa http://psi.ontopedia.net/Composer. \"\"\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:new_Composer >> types";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(set.first().first(), execute(
				new TMQLQuery("o:Composer")).first().first());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(17, set.size());
	}

	public void testInsertTopicTypeAndATopic() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " INSERT \"\"\" http://psi.ontopedia.net/New_Type ako http://psi.ontopedia.net/Composer. http://psi.ontopedia.net/new_Composer isa http://psi.ontopedia.net/New_Type. \"\"\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:New_Type >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(set.first().first(), execute(
				new TMQLQuery("o:new_Composer")).first().first());

		query = prefix + " o:Composer >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());
	}

	public void testInsertAnAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:La_Boheme << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(set.first().first(), execute(
				new TMQLQuery("o:Puccini")).first().first());

		query = prefix
				+ " INSERT \"\"\" http://psi.ontopedia.net/composed_by ( http://psi.ontopedia.net/Work : http://psi.ontopedia.net/La_Boheme , http://psi.ontopedia.net/Composer : http://psi.ontopedia.net/Franco_Alfano ) \"\"\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:La_Boheme << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

}
