package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class AssociationPatternTest extends BaseTest {

	public void testFullQualifiedAssociationPattern() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix
				+ " o:Puccini / o:composed_by ( o:Composer -> o:Work )  ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(12, set.size());
	}

	public void testAssociationPatternWithoutOtherRole() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Puccini / o:composed_by ( o:Composer -> ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(12, set.size());

		query = prefix + " o:Puccini / o:exponent_of ( o:Person -> ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / o:pupil_of ( o:Pupil -> ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
	}

	public void testAssociationPatternWithoutRoles() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Puccini / o:composed_by ( -> ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(12, set.size());
	}

	public void testEmptyAssociationPattern() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Puccini / * ( -> )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " o:Puccini / * ( * -> )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " o:Puccini / * ( * -> * )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());

		query = prefix + " o:Puccini / * ( -> * )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(18, set.size());
	}

}
