package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class NumericalExpressionTest extends BaseTest {

	public void testAddition() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 + 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = prefix + " 1 + 2 + 5 + 10";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(18, ((BigInteger) set.first().first()).longValue());

		query = prefix + " 1 + 2  + 99";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(102, ((BigInteger) set.first().first())
						.longValue());

		query = prefix + " 1.0 + 2.0 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(3.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query = prefix + " .0 + 2.0 + 111.5";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(113.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}

	public void testSubtraction() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 - 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-1, ((BigInteger) set.first().first()).longValue());

		query = prefix + " 1 - 2 - 5 - 10";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(-16, ((BigInteger) set.first().first())
						.longValue());

		query = prefix + " 99 - 2  - 90";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(7, ((BigInteger) set.first().first()).longValue());

		query = prefix + " 3.0 - 2.0 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query = prefix + " 100 - 2.0 - 11.5";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(86.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}

	public void testMultiplication() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 * 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query = prefix + " 1 * 2 * 5 * 10";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(100, ((BigInteger) set.first().first())
						.longValue());

		query = prefix + " 90 * -2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-180, ((BigInteger) set.first().first())
				.longValue());

		query = prefix + " 3.0 * 2.0 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(6.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query = prefix + " 100 * 2.0 * 1.5";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(300.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}

	public void testDivision() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 div 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(0.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query = prefix + " 1.0 div 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(0.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query = prefix + " 90 div -2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(-45, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query = prefix + " 3.0 div 2.0 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query = prefix + " 100 div 2.0 div 5";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(10.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}

	public void testModulo() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 mod 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());		
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query = prefix + " 1.0 mod 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

	}
}
