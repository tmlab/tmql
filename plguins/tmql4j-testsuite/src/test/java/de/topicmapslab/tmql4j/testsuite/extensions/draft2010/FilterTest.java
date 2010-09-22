package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class FilterTest extends BaseTest {

	public void testScopeFilter() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Puccini / name:: @o:short_name  ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / name:: @o:normal  ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / occurrence:: @o:Local  ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(3, set.size());

		query = prefix
				+ " o:Puccini / occurrence:: @http://www.topicmaps.org/xtm/1.0/language.xtm#it @o:Web  ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(2, set.size());
	}

	public void testBooleanFilterWithExpression() throws TMQLRuntimeException {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer / name:: [ . / variant:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());

		query = prefix + " / topic::o:Composer [ . / subject-identifier:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());
	}

	public void testBooleanFilterWithConjunction() throws TMQLRuntimeException {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix
				+ " / topic::o:Composer [ . / name:: / variant:: AND . / subject-identifier:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " / topic::o:Composer [ . / subject-identifier:: AND . / name:: / variant:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " / topic::o:Composer [ . / subject-identifier:: ][. / name:: / variant:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testBooleanFilterWithDisjunction() throws TMQLRuntimeException {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix
				+ " / topic::o:Composer [ . / name:: / variant:: OR . / subject-identifier:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());

		query = prefix
				+ " / topic::o:Composer [ . / subject-identifier:: OR . / name:: / variant:: ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());
	}

	public void testBooleanFilterWithNegation() throws TMQLRuntimeException {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix
				+ " / topic::o:Composer [ not ( . / name:: / variant:: ) ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());

		query = prefix
				+ " / topic::o:Composer [ not ( . / subject-identifier:: OR . / name:: / variant:: ) ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " / topic::o:Composer [ not ( . / subject-identifier:: AND . / name:: / variant:: ) ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());

		query = prefix
				+ " / topic::o:Composer [ not ( . / subject-identifier:: =~ \"http://.+\" ) ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testBooleanFilterComparison() throws TMQLRuntimeException {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " / topic::o:Composer [ 1 = 1 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer [ 1 != 0 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer [ 1 > 0 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer [ 1 >= 0 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer [ 1 < 2 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix + " / topic::o:Composer [ 1 <= 2 ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());

		query = prefix
				+ " / topic::o:Composer [ . / subject-identifier:: =~ \"http://.+\" ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());
	}
}
