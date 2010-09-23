package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class LiteralTest extends BaseTest {

	private static final List<String> integers = new LinkedList<String>();
	static {
		integers.add("1");
		integers.add("259");
		integers.add("100");
		integers.add(String.valueOf(Integer.MAX_VALUE));
		integers.add(String.valueOf(Integer.MIN_VALUE));
	}

	private static final List<String> decimals = new LinkedList<String>();
	static {
		decimals.add("1.125");
		decimals.add("555.111");
		decimals.add(".5952");
		decimals.add(String.valueOf(Double.MAX_VALUE));
		decimals.add(String.valueOf(Double.MIN_VALUE));
	}

	private static final List<String> dates = new LinkedList<String>();
	static {
		dates.add("2009-12-05");
		dates.add("0100-01-31");
		dates.add("20009-01-05");
	}

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

	public void testTopicReference() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " o:Composer ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Topic);

		query = prefix + " o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Topic);

		query = prefix + " o:composed_by ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Topic);
	}

	public void testIriReference() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " http://www.google.de ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof URI);

		query = prefix + " http://www.topicmapslab.de ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof URI);

		query = prefix + " http://www.micro.com/abcd?dd ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof URI);
	}

	public void testDateLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String date : dates) {
			System.out.println(date + "?");
			query = prefix + " " + date;
			set = execute(new TMQLQuery(query));
			Assert.assertEquals(1, set.size());
			Assert.assertTrue(set.first().first() instanceof Calendar);
		}
	}

	public void testTimeLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String time : times) {
			System.out.println(time + "?");
			query = prefix + " " + time;
			set = execute(new TMQLQuery(query));
			Assert.assertEquals(1, set.size());
			Assert.assertTrue(set.first().first() instanceof Calendar);
		}
	}

	public void testDateTimeLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String date : dates) {
			for (String time : times) {
				final String dateTime = date + "T" + time;
				System.out.println(dateTime + "?");
				query = prefix + " " + dateTime;
				set = execute(new TMQLQuery(query));
				Assert.assertEquals(1, set.size());
				Assert.assertTrue(set.first().first() instanceof Calendar);
			}
		}
	}

	public void testIntegerLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String integer : integers) {
			System.out.println(integer + "?");
			query = prefix + " " + integer;
			set = execute(new TMQLQuery(query));
			Assert.assertEquals(1, set.size());
			Assert.assertTrue(set.first().first() instanceof BigInteger);
		}
	}

	public void testDecimalLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		for (String decimal : decimals) {
			System.out.println(decimal + "?");
			query = prefix + " " + decimal;
			set = execute(new TMQLQuery(query));
			Assert.assertEquals(1, set.size());
			Assert.assertTrue(set.first().first() instanceof BigDecimal);
		}
	}

	public void testStringLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " \"abcd\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue(((String) set.first().first())
				.equalsIgnoreCase("abcd"));

		query = prefix + " \"\"\"abcd\"\"\"";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue(((String) set.first().first())
				.equalsIgnoreCase("abcd"));
	}

}
