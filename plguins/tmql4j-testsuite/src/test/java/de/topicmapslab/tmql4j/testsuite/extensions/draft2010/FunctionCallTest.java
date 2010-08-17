package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class FunctionCallTest extends BaseTest {

	public void testFunctionTopicmap() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " topicmap() ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof TopicMap);
	}

	public void testFunctionCount() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " count( / topic::o:Composer ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(16, ((BigInteger) set.first().first()).longValue());

		query = prefix
				+ " count( / topic::o:Composer union / topic::o:Musician ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(20, ((BigInteger) set.first().first()).longValue());
	}

	public void testFunctionBoolean() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " boolean ( \"true\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		Assert.assertEquals(true, ((Boolean) set.first().first())
				.booleanValue());

		query = prefix + " boolean ( \"false\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		Assert.assertEquals(false, ((Boolean) set.first().first())
				.booleanValue());
	}

	public void testFunctionString() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " string ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " string ( o:Puccini / subject-identifier:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("http://psi.ontopedia.net/Puccini"
				.equalsIgnoreCase((String) set.first().first()));

		query = prefix + " string ( 1234 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
				.assertTrue("1234".equalsIgnoreCase((String) set.first()
						.first()));

		query = prefix + " string ( o:Puccini / name:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " string ( o:Puccini / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix
				+ " string ( / topic::o:Composer / name:: / variant::  ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
	}

	public void testFunctionNumber() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " number ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " number ( count ( o:Puccini ) ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(1, ((BigInteger) set.first().first()).longValue());

		query = prefix + " number ( \"1234\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(1234, ((BigInteger) set.first().first())
				.longValue());

		query = prefix + " number ( 1234 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(1234, ((BigInteger) set.first().first())
				.longValue());

		query = prefix + " number ( \"1234.1\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1234.1, ((BigDecimal) set.first().first())
				.doubleValue(), 0);

		query = prefix + " number ( 1234.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigDecimal);
		Assert.assertEquals(1234.1, ((BigDecimal) set.first().first())
				.doubleValue(), 0);
	}

	public void testFunctionRound() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " round ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " round ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(3, ((BigInteger) set.first().first()).longValue());
		
		query = prefix + " round ( -2.5 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-2, ((BigInteger) set.first().first()).longValue());

		query = prefix + " round ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(2, ((BigInteger) set.first().first()).longValue());
	}

	public void testFunctionCeiling() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " ceiling ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " ceiling ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = prefix + " ceiling ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = prefix + " ceiling ( -2.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-2, ((BigInteger) set.first().first()).longValue());
	}

	public void testFunctionFloor() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " floor ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " floor ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query = prefix + " floor ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query = prefix + " floor ( -2.1 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(-3, ((BigInteger) set.first().first()).longValue());
	}

	public void testFunctionStringLength() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " string-length ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);

		query = prefix + " string-length ( o:Puccini / name::) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);

		query = prefix + " string-length ( o:Puccini / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(13, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);

		query = prefix + " string-length ( \"abcdg\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(5, ((BigInteger) set.first().first()).longValue());

		query = prefix + " string-length ( \"\"\"abcdg\"\"\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		Assert.assertEquals(5, ((BigInteger) set.first().first()).longValue());

	}

	public void testFunctionNormalizeSpace() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " normalize-space ( o:Puccini ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " normalize-space ( o:Puccini / name::) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " normalize-space ( o:Puccini / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " normalize-space ( \"abc dg\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("abc dg".equalsIgnoreCase(((String) set.first()
				.first())));

		query = prefix + " normalize-space ( \"abc  dg\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("abc dg".equalsIgnoreCase(((String) set.first()
				.first())));

	}

	public void testFunctionFind() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " find ( o:Puccini , \"Puc\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);

		query = prefix + " find ( o:Puccini / name:: , \"P\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof BigInteger);
		System.out.println(set);

	}

	public void testFunctionConcat() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " concat ( o:Puccini , \"/PG\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue(((String) set.first().first()).matches(".*PG"));

		query = prefix + " concat ( \"si:\"  , o:Puccini) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert.assertTrue(((String) set.first().first()).matches("si:.*"));

		query = prefix + " concat ( \"si:\"  , o:Puccini / name:: ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

		query = prefix + " concat ( o:Puccini / name::,  \"si:\"   ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);

	}

	public void testFunctionContains() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " contains ( o:Puccini , \"Puc\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		Assert.assertTrue(((Boolean) set.first().first()).booleanValue());

		query = prefix + " contains ( o:Puccini / name:: , \"Giacomo\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		System.out.println(set);
	}

	public void testFunctionStartsWith() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " starts-with ( o:Puccini , \"P\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);

		query = prefix + " starts-with ( o:Puccini / name:: , \"Puccini\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		System.out.println(set);
	}

	public void testFunctionEndsWith() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " ends-with ( o:Puccini , \"Puccini\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);

		query = prefix + " ends-with ( o:Puccini / name:: , \"Puccini\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		System.out.println(set);
	}

	public void testFunctionSubstring() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " substring ( o:Puccini , 0 , 4 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " substring ( o:Puccini / name:: , 0 , 4 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " substring ( o:Puccini / name:: , 0 , 50 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " substring ( \"abcd\" , 0 , 50 ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
		.assertTrue("abcd".equalsIgnoreCase((String) set.first()
				.first()));
	}

	public void testFunctionSubstringBefore() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " substring-before ( o:Puccini , \"psi\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " substring-before ( o:Puccini / name:: , \"ucc\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		System.out.println(set);
		
		query = prefix + " substring-before ( \"abcdhehe\" , \"he\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
				.assertTrue("abcd".equalsIgnoreCase((String) set.first()
						.first()));
		
		query = prefix + " substring-before ( \"abcdhehe\" , \"o\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
				.assertTrue("".equalsIgnoreCase((String) set.first()
						.first()));
	}

	public void testFunctionSubstringAfter() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " substring-after ( o:Puccini , \"psi\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " substring-after ( o:Puccini / name:: , \"ucc\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
		System.out.println(set);
		
		query = prefix + " substring-after ( \"abcdhehe\" , \"he\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
				.assertTrue("he".equalsIgnoreCase((String) set.first()
						.first()));
		
		query = prefix + " substring-after ( \"abcdhehe\" , \"o\" ) ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
				.assertTrue("".equalsIgnoreCase((String) set.first()
						.first()));
	}

	public void testFunctionMatchesRegexp() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " matches-regexp ( o:Puccini , \".*psi.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		
		query = prefix + " matches-regexp ( o:Puccini / name:: , \".*ucc.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		Assert.assertTrue(((Boolean) set.first().first()).booleanValue());
		
		query = prefix + " matches-regexp ( o:Puccini / name:: , \".*G.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		
		query = prefix + " matches-regexp ( \"foo bar\" , \".*bar\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof Boolean);
		Assert.assertTrue(((Boolean) set.first().first()).booleanValue());
	}
	
	public void testFunctionExtractRegexp() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;
		
		query = prefix + " extract-regexp ( o:Puccini , \".*Pucc.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " extract-regexp ( o:Puccini , \"xyz\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
		.assertTrue("".equalsIgnoreCase((String) set.first()
				.first()));
		
		query = prefix + " extract-regexp ( o:Puccini / name:: , \".*ucc\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " extract-regexp ( o:Puccini / name:: , \".*G\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " extract-regexp ( \"foo bar\" , \".*bar\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
		.assertTrue("foo bar".equalsIgnoreCase((String) set.first()
				.first()));
	}
	
	public void testFunctionTranslate() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " translate ( o:Puccini , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " translate ( o:Puccini / name:: , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(3, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		
		query = prefix + " translate ( \"pppppsssssiiiii\" , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
		.assertTrue("xxxxxyyyyyzzzzz".equalsIgnoreCase((String) set.first()
				.first()));
		
		query = prefix + " translate ( \"uuu\" , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.first().first() instanceof String);
		Assert
		.assertTrue("uuu".equalsIgnoreCase((String) set.first()
				.first()));
	}

}
