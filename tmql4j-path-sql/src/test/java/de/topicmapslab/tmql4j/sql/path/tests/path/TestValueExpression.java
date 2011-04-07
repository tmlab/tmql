package de.topicmapslab.tmql4j.sql.path.tests.path;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;

public class TestValueExpression extends Tmql4JTestCase {

	@Test
	public void testUndef() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " undef  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(0, set.size());
	}

	@Test
	public void testBoolean() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " true  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(true, set.get(0, 0));

		query = prefix + " false  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(false, set.get(0, 0));
	}

	@Test
	public void testSign() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " -1 * 1  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(-1, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(-1, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " - -1 * 1 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(1, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(1, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testAddition() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 + 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(3, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(3, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 1 + 2 + 5 + 10 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(18, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(18, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 1 + 2 + 99 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(102, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(102, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 1.0 + 2.0  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(3.0, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(3.0, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}

		query = prefix + " \"abc\" + \"def\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof String) {
			Assert.assertTrue("abcdef".equalsIgnoreCase(o.toString()));
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue("abcdef".equalsIgnoreCase(((Collection<?>) o).iterator().next().toString()));
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testSubtraction() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 - 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(-1, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(-1, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 20 - 20 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(0, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(0, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 99 - 2 - 99 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(196, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(196, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 3.0 - 2.0  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(1.0, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(1.0, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}

	}

	@Test
	public void testMultiplication() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 * 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(2, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(2, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 20 * 20 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(400, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(400, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " - 20 * 20 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(-400, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(-400, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 3.0 * 2.0  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(6.0, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(6.0, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testDivision() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1.0 % 2.0 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(0.5, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(0.5, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}

		query = prefix + " 20.0 % 20.0 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(1.0, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(1.0, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}

		query = prefix + " - 20.0 % 20.0 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(-1.0, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(-1.0, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}

		query = prefix + " 3.0 % 2.0  ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigDecimal) {
			Assert.assertEquals(1.5, ((BigDecimal) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(1.5, ((BigDecimal) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testModulo() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 mod 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(1, ((BigInteger) o).longValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(1, ((BigInteger) ((Collection<?>) o).iterator().next()).longValue());
		} else {
			Assert.fail();
		}

		query = prefix + " 20 mod 20 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof BigInteger) {
			Assert.assertEquals(0, ((BigInteger) o).doubleValue(), 0);
		} else if (o instanceof Collection<?>) {
			Assert.assertEquals(0, ((BigInteger) ((Collection<?>) o).iterator().next()).doubleValue(), 0);
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testLowerThan() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 < 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}

		query = prefix + " \"a\" < \"b\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testLowerEquals() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 1 <= 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}

		query = prefix + " \"a\" <= \"b\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testGreaterThan() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 10 > 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}

		query = prefix + " \"c\" > \"B\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testGreaterEquals() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " 10 >= 2 ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}

		query = prefix + " \"c\" >= \"B\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testMatchesRegExp() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " \"aaaa\" =~ \".*a.*\" ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if (o instanceof Boolean) {
			Assert.assertTrue(((Boolean) o).booleanValue());
		} else if (o instanceof Collection<?>) {
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testEquals() throws Exception {
		Topic t = createTopicBySI("myTopic");
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " // tm:subject [ 1 == 1 ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(t, set.first().first());

		query = prefix + " // tm:subject [ 1 == 2 ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(0, set.size());

		query = prefix + "  // tm:subject [ \"a\" == \"a\" ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(t, set.first().first());

		query = prefix + "  // tm:subject [ \"a\" == \"b\" ] ";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(0, set.size());
	}

	@Test
	public void testUnEquals() throws Exception {
		Topic t = createTopicBySI("myTopic");
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " // tm:subject [ 1 != 2 ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(t, set.first().first());

		query = prefix + " // tm:subject [ 1 != 1 ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(0, set.size());

		query = prefix + "  // tm:subject [ \"a\" != \"b\"  ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(1, set.size());
		Assert.assertEquals(1, set.first().size());
		Assert.assertEquals(t, set.first().first());

		query = prefix + "  // tm:subject [ \"a\" != \"a\"  ]";
		set = execute(new TMQLQuery(topicMap, query));
		Assert.assertEquals(0, set.size());
	}
}
