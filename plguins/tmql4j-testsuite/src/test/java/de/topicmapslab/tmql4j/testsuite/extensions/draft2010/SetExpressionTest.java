package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class SetExpressionTest extends BaseTest {

	public void testUnion() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer union o:Musician ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + " / topic::o:Composer union / topic::o:Musician ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(20, set.size());
	}

	public void testMinus() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer minus o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());

		query = prefix + " / topic::o:Composer minus o:Web ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());
	}

	public void testIntersect() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer intersect o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " / topic::o:Opera intersect o:Puccini / o:composed_by ( o:Composer -> o:Work )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(12, set.size());
	}

}
