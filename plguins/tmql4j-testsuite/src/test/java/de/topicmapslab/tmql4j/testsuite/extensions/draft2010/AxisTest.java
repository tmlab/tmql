package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class AxisTest extends BaseTest {

	public void testAssociationAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / association::o:composed_by ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(172, set.size());

		query = prefix + " / o:composed_by ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(172, set.size());

		query = prefix + " / association:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3636, set.size());

		query = prefix + " / * ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5648, set.size());
	}

	public void testTopicAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / o:Composer ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2012, set.size());

		query = prefix + " / topic::* ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2012, set.size());
		
		query = prefix + " / topic::*\r\n# ::*";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2012, set.size());

		query = prefix + " / * ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5648, set.size());
	}

	public void testDirectTypeAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + " / topic:: / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(46, set.size());

		query = prefix + " / association:: / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(28, set.size());

		query = prefix + " / topic:: / occurrence:: / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(22, set.size());

		query = prefix + " / association:: / role:: / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(28, set.size());

		query = prefix + " / topic:: / name:: / direct-type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testTypeAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + " / topic:: / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(46, set.size());

		query = prefix + " / association:: / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(28, set.size());

		query = prefix + " / topic:: / occurrence:: / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(22, set.size());

		query = prefix + " / association:: / role:: / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(28, set.size());

		query = prefix + " / topic:: / name:: / type:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSIAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / subject-identifier::  ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(14, set.size());
	}

	public void testSLAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / subject-locator::  ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testIIAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / item-identifier::  ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());
	}

	public void testNameAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / name:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(53, set.size());
	}

	public void testVariantAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / name:: / variant:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());
	}

	public void testOccurrenceAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / occurrence:: ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(123, set.size());

		query = prefix + " / topic::* / occurrence::o:article ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(128, set.size());
	}

	public void testDatatypeAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / occurrence:: / datatype::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(2, set.size());

		query = prefix + " / topic::* / name:: / variant:: / datatype::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
	}

	public void testInstanceAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer / instance::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(16, set.size());
	}

	public void testDirectInstanceAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer / direct-instance::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(16, set.size());
	}

	public void testValueAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / occurrence:: / value::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(123, set.size());

		query = prefix + " / topic::* / occurrence::o:article / value::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(128, set.size());

		query = prefix + " / topic::o:Composer / name:: / variant:: / value::";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());

		query = prefix + " / topic::o:Composer / name:: / value::";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(53, set.size());
	}

	public void testParentAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / parent:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " / topic::o:Composer / occurrence::* / parent::";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer / name:: / parent::";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer / name:: / variant:: / parent::";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());

		query = prefix + " / association:: / parent:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " / association:: / role:: / parent:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3636, set.size());
	}

	public void testPlayerAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / association::o:composed_by / player:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(187, set.size());

		query = prefix + " / association::o:composed_by / player::o:Composer ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / association:: / player:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1848, set.size());
	}

	public void testRoleAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / association::o:composed_by / role:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(344, set.size());

		query = prefix + " / association:: / role:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(7296, set.size());

		query = prefix + " / association:: / role::o:Composer ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(176, set.size());

		query = prefix + " / topic:: / role:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(7296, set.size());
	}

	public void testScopeAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / association::o:composed_by / scope:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " / association:: / scope:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + " / topic:: / * / scope:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(132, set.size());
	}

	public void testScopedAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Web / scoped:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(234, set.size());
	}

	public void testReifierAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / reifier:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " / association:: / reifier:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());

		query = prefix + " / association:: / role:: / reifier:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " / topic:: / * / reifier:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
	}

	public void testReifiedAxis() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Puccini / reified:: ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}
}
