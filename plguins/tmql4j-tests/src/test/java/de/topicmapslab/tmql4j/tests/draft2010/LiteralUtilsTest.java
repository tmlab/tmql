package de.topicmapslab.tmql4j.tests.draft2010;

import java.util.Set;

import org.junit.Test;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

public class LiteralUtilsTest extends Tmql4JTestCase {
	@Test
	public void testIntegerLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("+10");
		literals.add("-1");
		literals.add("1");

		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isInteger(literal));
		}
	}

	@Test
	public void testDecimalLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("+10.0");
		literals.add("-.1");
		literals.add(".1");
		literals.add("4.1");
		literals.add(String.valueOf(Double.MAX_VALUE));
		literals.add(String.valueOf(Double.MIN_VALUE));

		for (String literal : literals) {
			System.out.println(literal + "?");
			Assert.assertEquals(true, LiteralUtils.isDecimal(literal));
		}
	}
	@Test
	public void testTimeLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("20:00:00");
		literals.add("20:00:00Z");
		literals.add("20:00:00-08:00");
		literals.add("20:00:00+08:00");
		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isTime(literal));
		}
	}
	@Test
	public void testDateLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("1999-11-31");
		literals.add("20056-09-30");
		literals.add("20056-09-10");
		literals.add("20056-09-01");
		literals.add("20056-09-20");
		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isDate(literal));
		}
	}
	@Test
	public void testDateTimeLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("1999-11-31T99:99:99.99");
		literals.add("1999-11-31T99:99:99.99Z");
		literals.add("1999-11-31T99:99:99.99-99:99");
		literals.add("1999-11-31T99:99:99.99+99:99");
		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isDateTime(literal));
		}
	}
	@Test
	public void testStringLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("\"\"\" abcd  a \"\"\"");
		literals.add("\"\"\" \"abcd\"  a \"\"\"");
		literals.add("\" abc \"");
		;

		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isString(literal));
		}
	}

}
