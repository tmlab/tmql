package de.topicmapslab.tmql4j.draft2010.tests;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * Test class of numerical expression
 * @author Sven Krosse
 *
 */
public class NumericalExpressionTest extends Tmql4JTestCase {

	/**
	 * TEST METHOD
	 * @throws Exception
	 */
	@Test
	public void testAddition() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query =  " 1 + 2 ";		
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query =  " 1 + 2 + 5 + 10";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(18, ((BigInteger) set.first().first()).longValue());

		query =  " 1 + 2  + 99";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(102, ((BigInteger) set.first().first())
						.longValue());

		query =  " 1.0 + 2.0 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(3.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query =  " .0 + 2.0 + 111.5";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(113.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}
	/**
	 * TEST METHOD
	 * @throws Exception
	 */
	@Test
	public void testSubtraction() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query =  " 1 - 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-1, ((BigInteger) set.first().first()).longValue());

		query =  " 1 - 2 - 5 - 10";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(-16, ((BigInteger) set.first().first())
						.longValue());

		query =  " 99 - 2  - 90";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(7, ((BigInteger) set.first().first()).longValue());

		query =  " 3.0 - 2.0 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query =  " 100 - 2.0 - 11.5";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(86.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}
	/**
	 * TEST METHOD
	 * @throws Exception
	 */
	@Test
	public void testMultiplication() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query =  " 1 * 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query =  " 1 * 2 * 5 * 10";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert
				.assertEquals(100, ((BigInteger) set.first().first())
						.longValue());

		query =  " 90 * -2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-180, ((BigInteger) set.first().first())
				.longValue());

		query =  " 3.0 * 2.0 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(6.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query =  " 100 * 2.0 * 1.5";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(300.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}
	/**
	 * TEST METHOD
	 * @throws Exception
	 */
	@Test
	public void testDivision() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query =  " 1 div 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(0.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query =  " 1.0 div 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(0.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query =  " 90 div -2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(-45, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query =  " 3.0 div 2.0 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.5, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);

		query =  " 100 div 2.0 div 5";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(10.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0.0);
	}
	/**
	 * TEST METHOD
	 * @throws Exception
	 */
	@Test
	public void testModulo() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query =  " 1 mod 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());		
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query =  " 1.0 mod 2 ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1.0, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

	}
}
