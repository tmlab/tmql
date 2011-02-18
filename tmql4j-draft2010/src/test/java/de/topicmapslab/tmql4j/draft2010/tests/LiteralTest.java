package de.topicmapslab.tmql4j.draft2010.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * Test class of literals
 * 
 * @author Sven Krosse
 * 
 */
public class LiteralTest extends Tmql4JTestCase {

	/**
	 * integer values
	 */
	private static final List<String> integers = new LinkedList<String>();
	static {
		integers.add("1");
		integers.add("259");
		integers.add("100");
		integers.add(String.valueOf(Integer.MAX_VALUE));
		integers.add(String.valueOf(Integer.MIN_VALUE));
	}

	/**
	 * decimal values
	 */
	private static final List<String> decimals = new LinkedList<String>();
	static {
		decimals.add("1.125");
		decimals.add("555.111");
		decimals.add(".5952");
		decimals.add(String.valueOf(Double.MAX_VALUE));
		decimals.add(String.valueOf(Double.MIN_VALUE));
	}

	/**
	 * dates
	 */
	private static final List<String> dates = new LinkedList<String>();
	static {
		dates.add("2009-12-05");
		dates.add("0100-01-31");
		dates.add("20009-01-05");
	}

	/**
	 * times
	 */
	private static final List<String> times = new LinkedList<String>();
	static {
		times.add("20:00:00");
		times.add("00:59:59");
		times.add("00:59:59.1234");
		times.add("00:59:59-08:00");
		times.add("00:59:59+08:00");
		times.add("00:59:59.1234Z");
		times.add("00:59:59.1234-08:00");
		times.add("00:59:59.1234+08:00");
		times.add("00:59:59.1234Z");
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTopicReference() throws Exception {
		createTopicBySI("myTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " myTopic ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIriReference() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " http://www.google.de ";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof URI);

		query = prefix + " http://www.topicmapslab.de ";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof URI);

		query = prefix + " http://www.micro.com/abcd?dd ";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof URI);
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDateLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String date : dates) {
			query = prefix + " " + date;
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.first().first() instanceof Calendar);
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTimeLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String time : times) {
			query = prefix + " " + time;
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.first().first() instanceof Calendar);
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDateTimeLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String date : dates) {
			for (String time : times) {
				final String dateTime = date + "T" + time;
				query = prefix + " " + dateTime;
				set = execute(query);
				assertEquals(1, set.size());
				assertTrue(set.first().first() instanceof Calendar);
			}
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIntegerLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String integer : integers) {
			query = prefix + " " + integer;
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.first().first() instanceof BigInteger);
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDecimalLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String decimal : decimals) {
			query = prefix + " " + decimal;
			set = execute(query);
			assertEquals(1, set.size());
			assertTrue(set.first().first() instanceof BigDecimal);
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStringLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " \"abcd\"";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof String);
		assertTrue(((String) set.first().first()).equalsIgnoreCase("abcd"));

		query = prefix + " \"\"\"abcd\"\"\"";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof String);
		assertTrue(((String) set.first().first()).equalsIgnoreCase("abcd"));
	}

}
