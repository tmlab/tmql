package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

public class LiteralUtilsTest extends TestCase {

	public void testIntegerLiteral() throws Exception {
		final Set<String> literals = HashUtil.getHashSet();
		literals.add("+10");
		literals.add("-1");
		literals.add("1");

		for (String literal : literals) {
			Assert.assertEquals(true, LiteralUtils.isInteger(literal));
		}
	}

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
