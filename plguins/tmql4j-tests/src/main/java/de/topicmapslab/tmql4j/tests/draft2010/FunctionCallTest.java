package de.topicmapslab.tmql4j.tests.draft2010;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

public class FunctionCallTest extends Tmql4JTestCase {

	@Test
	public void testFunctionTopicmap() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query = " topicmap() ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicMap, set.first().first());
	}

	@Test
	public void testFunctionCount() throws Exception {
		Topic topic = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			topics.add(t);
		}

		String query = null;
		SimpleResultSet set = null;

		query = " count( / topic::myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(topics.size(),
				((BigInteger) set.first().first()).longValue());
	}

	@Test
	public void testFunctionBoolean() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query = " boolean ( \"true\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertEquals(true, ((Boolean) set.first().first()).booleanValue());

		query = " boolean ( \"false\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertEquals(false, ((Boolean) set.first().first()).booleanValue());
	}

	@Test
	public void testFunctionString() throws Exception {
		Topic topic = createTopicBySI("myType");
		Name n = topic.createName("Name");
		Variant v = n.createVariant("Variant", createTopic());
		Occurrence o = topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " string ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);

		query = " string ( myType / subject-identifier:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(topic.getSubjectIdentifiers().iterator().next()
				.getReference(), set.first().first());

		query = " string ( 1234 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("1234", set.first().first());

		query = " string ( myType / name:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(n.getValue(), set.first().first());

		query = " string ( myType / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(o.getValue(), set.first().first());

		query = " string ( myType / name:: / variant::  ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(v.getValue(), set.first().first());
	}

	@Test
	public void testFunctionNumber() throws Exception {
		createTopicBySI("myType");
		String query = null;
		SimpleResultSet set = null;

		query = " number ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(0, set.size());

		query = " number ( count ( myType ) ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(1, ((BigInteger) set.first().first()).longValue());

		query = " number ( \"1234\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(1234, ((BigInteger) set.first().first()).longValue());

		query = " number ( 1234 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(1234, ((BigInteger) set.first().first()).longValue());

		query = " number ( \"1234.1\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigDecimal);
		assertEquals(1234.1, ((BigDecimal) set.first().first()).doubleValue(),
				0);

		query = " number ( 1234.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigDecimal);
		assertEquals(1234.1, ((BigDecimal) set.first().first()).doubleValue(),
				0);
	}

	@Test
	public void testFunctionRound() throws Exception {
		createTopicBySI("myType");
		String query = null;
		SimpleResultSet set = null;

		query = " round ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(0, set.size());

		query = " round ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = " round ( -2.5 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(-2, ((BigInteger) set.first().first()).longValue());

		query = " round ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(2, ((BigInteger) set.first().first()).longValue());
	}

	@Test
	public void testFunctionCeiling() throws Exception {
		createTopicBySI("myType");
		String query = null;
		SimpleResultSet set = null;

		query = " ceiling ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(0, set.size());

		query = " ceiling ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = " ceiling ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(3, ((BigInteger) set.first().first()).longValue());

		query = " ceiling ( -2.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(-2, ((BigInteger) set.first().first()).longValue());
	}

	@Test
	public void testFunctionFloor() throws Exception {
		createTopicBySI("myType");
		String query = null;
		SimpleResultSet set = null;

		query = " floor ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(0, set.size());

		query = " floor ( 2.5 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query = " floor ( 2.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(2, ((BigInteger) set.first().first()).longValue());

		query = " floor ( -2.1 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(-3, ((BigInteger) set.first().first()).longValue());
	}

	@Test
	public void testFunctionStringLength() throws Exception {
		Topic topic = createTopicBySI("myType");
		Name n = topic.createName("Name");
		Occurrence o = topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " string-length ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);

		query = " string-length ( myType / name::) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(n.getValue().length(),
				((BigInteger) set.first().first()).longValue());

		query = " string-length ( myType / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o.getValue().length(),
				((BigInteger) set.first().first()).longValue());

		query = " string-length ( \"abcdg\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(5, ((BigInteger) set.first().first()).longValue());

		query = " string-length ( \"\"\"abcdg\"\"\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(5, ((BigInteger) set.first().first()).longValue());

	}

	@Test
	public void testFunctionNormalizeSpace() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name           1");
		topic.createOccurrence(createTopic(), "value        1");
		String query = null;
		SimpleResultSet set = null;

		query = " normalize-space ( myType ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);

		query = " normalize-space ( myType / name::) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue("Name 1".equalsIgnoreCase(((String) set.first().first())));

		query = " normalize-space ( myType / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue("value 1".equalsIgnoreCase(((String) set.first().first())));

		query = " normalize-space ( \"abc dg\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue("abc dg".equalsIgnoreCase(((String) set.first().first())));

		query = " normalize-space ( \"abc  dg\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue("abc dg".equalsIgnoreCase(((String) set.first().first())));

	}

	@Test
	public void testFunctionFind() throws Exception {
		Topic topic = createTopicBySI("myType");
		Name n = topic.createName("Name");
		Occurrence o = topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " find ( myType , \"myType\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);

		query = " find ( myType / name:: , \"a\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(n.getValue().indexOf("a"), ((BigInteger) set.first()
				.first()).longValue());

		query = " find ( myType / occurrence:: , \"a\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof BigInteger);
		assertEquals(o.getValue().indexOf("a"), ((BigInteger) set.first()
				.first()).longValue());

	}

	@Test
	public void testFunctionConcat() throws Exception {
		Topic topic = createTopicBySI("myType");
		Name n = topic.createName("Name");
		Occurrence o = topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " concat ( myType , \"/PG\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue(((String) set.first().first()).matches(".*PG"));

		query = " concat ( \"si:\"  , myType) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertTrue(((String) set.first().first()).matches("si:.*"));

		query = " concat ( \"si:\"  , myType / name:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("si:" + n.getValue(), set.first().first());

		query = " concat ( myType / name::,  \"si:\"   ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(n.getValue() + "si:", set.first().first());

		query = " concat ( \"si:\"  , myType / occurrence:: ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("si:" + o.getValue(), set.first().first());

		query = " concat ( myType / occurrence::,  \"si:\"   ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals(o.getValue() + "si:", set.first().first());

	}

	@Test
	public void testFunctionContains() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " contains ( myType , \"T\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " contains ( myType / name:: , \"a\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " contains ( myType / occurrence:: , \"a\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());
	}

	@Test
	public void testFunctionStartsWith() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " starts-with ( myType , \"U\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertFalse(((Boolean) set.first().first()).booleanValue());

		query = " starts-with ( myType / name:: , \"N\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " starts-with ( myType / occurrence:: , \"v\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());
	}

	@Test
	public void testFunctionEndsWith() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " ends-with ( myType , \"U\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertFalse(((Boolean) set.first().first()).booleanValue());

		query = " ends-with ( myType / name:: , \"e\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " ends-with ( myType / occurrence:: , \"e\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());
	}

	@Test
	public void testFunctionSubstring() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " substring ( myType , 0 , 4 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);

		query = " substring ( myType / name:: , 0 , 2 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("Na", set.first().first());

		query = " substring ( myType / name:: , 0 , 50 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("Name", set.first().first());

		query = " substring ( myType / occurrence:: , 0 , 2 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("va", set.first().first());

		query = " substring ( myType / occurrence:: , 0 , 50 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("value", set.first().first());

		query = " substring ( \"abcd\" , 0 , 50 ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("abcd".equalsIgnoreCase((String) set.first().first()));
	}

	@Test
	public void testFunctionSubstringBefore() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " substring-before ( myType , \"myType\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);

		query = " substring-before ( myType / name:: , \"m\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("Na", set.first().first());

		query = " substring-before ( myType / occurrence:: , \"ue\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("val", set.first().first());

		query = " substring-before ( \"abcdhehe\" , \"he\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("abcd".equalsIgnoreCase((String) set.first().first()));

		query = " substring-before ( \"abcdhehe\" , \"o\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("".equalsIgnoreCase((String) set.first().first()));
	}

	@Test
	public void testFunctionSubstringAfter() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " substring-after ( myType , \"T\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);

		query = " substring-after ( myType / name:: , \"m\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("e", set.first().first());

		query = " substring-after ( myType / occurrence:: , \"u\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals("e", set.first().first());

		query = " substring-after ( \"abcdhehe\" , \"he\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("he".equalsIgnoreCase((String) set.first().first()));

		query = " substring-after ( \"abcdhehe\" , \"o\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("".equalsIgnoreCase((String) set.first().first()));
	}

	@Test
	public void testFunctionMatchesRegexp() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " matches-regexp ( myType , \".*T.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);

		query = " matches-regexp ( myType / name:: , \".*m.*\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " matches-regexp ( myType / occurrence:: , \".*u.*\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());

		query = " matches-regexp ( \"foo bar\" , \".*bar\" ) ";
		set = execute(new TMQLQuery(query));
		System.out.println(set);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof Boolean);
		assertTrue(((Boolean) set.first().first()).booleanValue());
	}

	@Test
	public void testFunctionExtractRegexp() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		String query = null;
		SimpleResultSet set = null;

		query = " extract-regexp ( myType , \".*my.*\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("myType", set.first().first());

		query = " extract-regexp ( myType , \"xyz\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("".equalsIgnoreCase((String) set.first().first()));

		query = " extract-regexp ( myType / name:: , \".*m\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("Nam", set.first().first());

		query = " extract-regexp ( myType / occurrence:: , \".*u\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("valu", set.first().first());

		query = " extract-regexp ( \"foo bar\" , \".*bar\" ) ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("foo bar".equalsIgnoreCase((String) set.first()
				.first()));
	}

	@Test
	public void testFunctionTranslate() throws Exception {
		Topic topic = createTopicBySI("myType");
		topic.createName("Name");
		topic.createOccurrence(createTopic(), "value");
		;
		String query = null;
		SimpleResultSet set = null;

		query = " translate ( myType , \"yT\" , \"iS\") ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("miSipe", set.first().first());

		query = " translate ( myType / name:: , \"ae\" , \"ui\") ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("Numi", set.first().first());

		query = " translate ( myType / occurrence:: , \"ae\" , \"ui\") ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		assertEquals("vului", set.first().first());

		query = " translate ( \"pppppsssssiiiii\" , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("xxxxxyyyyyzzzzz".equalsIgnoreCase((String) set
				.first().first()));

		query = " translate ( \"uuu\" , \"psi\" , \"xyz\") ";
		set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first() instanceof String);
		Assert.assertTrue("uuu".equalsIgnoreCase((String) set.first().first()));
	}

}
