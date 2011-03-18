package de.topicmapslab.tmql4j.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

public class TestFunctionInvocation extends Tmql4JTestCase {

	@Test
	public void testRegExpFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		query = " fn:regexp ( \"This is a string.\", \".*string.*\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is a string."));

		query = " fn:regexp ( \"This is a integer.\", \".*string.*\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());
	}

	@Test
	public void testSubstringFunction() throws Exception {

		String query;
		SimpleResultSet set = null;

		query = "fn:substring ( \"This is a string.\" , 0 , 7 ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is"));

		query = "fn:substring ( \"This is a string.\" , 0 , 50 ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is a string."));

		query = "fn:substring ( \"This is a string.\" , 7 , 50 ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals(" a string."));
	}

	@Test
	public void testStringConcatFunction() throws Exception {
		String query;
		SimpleResultSet set = null;
		query = "fn:string-concat ( \"This is \", \"a string.\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is a string."));
		
		query = "fn:string-concat ( \"This \" , \"is \", \"a \", \"string.\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is a string."));
		
		Topic t = createTopicBySI("myTopic");
		Topic type1 = createTopicBySI("myType");
		Topic type2 = createTopicBySI("myOtherType");
		t.createName(type1,"V1");
		t.createName(type1,"V2");
		t.createName(type2,"V3");
		t.createName(type2,"V4");
		
		List<String> values = new ArrayList<String>();
		values.add("V1, V3");
		values.add("V1, V4");
		values.add("V2, V3");
		values.add("V2, V4");
		
		query = "myTopic ( fn:string-concat ( . >> characteristics myType , \", \", . >> characteristics myOtherType ) )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(4, set.size());
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertTrue(values.contains(r.first()));
		}
	}

	@Test
	public void testDoubleStringConcatFunction() throws Exception {
		String query;
		SimpleResultSet set = null;
		query = "fn:string-concat ( \"This is \", \"a string.\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("This is a string."));
	}

	@Test
	public void testLengthFunction() throws Exception {
		String query;
		SimpleResultSet set = null;
		String value = "This is a string.";
		query = "fn:length ( \"" + value + "\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(BigInteger.valueOf(value.length()), set.first().first());
	}

	@Test
	public void testGeqFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		query = "fn:string-geq ( \"b\" , \"a\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("b"));

		query = "fn:string-geq ( \"a\" , \"b\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());

		query = "fn:string-geq ( \"aa\" , \"a\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("aa"));

		query = "fn:string-geq ( \"aa\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("aa"));
	}

	@Test
	public void testGtFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		query = "fn:string-gt ( \"b\" , \"a\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("b"));

		query = "fn:string-gt ( \"aa\" , \"a\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("aa"));

		query = "fn:string-gt ( \"aa\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());
	}

	@Test
	public void testLeqFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		query = "fn:string-leq ( \"a\" , \"b\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("a"));

		query = "fn:string-leq ( \"b\" , \"a\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());

		query = "fn:string-leq ( \"a\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("a"));

		query = "fn:string-leq ( \"aa\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("aa"));
	}

	@Test
	public void testLtFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		query = "fn:string-lt ( \"a\" , \"b\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("a"));

		query = "fn:string-lt ( \"a\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals("a"));

		query = "fn:string-lt ( \"aa\" , \"aa\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());
	}

	@Test
	public void testUrlEncodeFunction() throws Exception {
		URL url = new URL("http://psi.ontopia.com?query=lal lulu");
		SimpleResultSet set = null;

		String query = "fn:url-encode ( \"" + url.toString() + "\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals(URLEncoder.encode(url.toString(), "UTF-8")));
	}

	@Test
	public void testUrlDencodeFunction() throws Exception {
		URL url = new URL("http://psi.ontopia.com?query=lal lulu");
		String value = URLEncoder.encode(url.toString(), "UTF-8");
		SimpleResultSet set = null;

		String query = "fn:url-decode ( \"" + value + "\" ) ";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertTrue(set.first().first().equals(URLDecoder.decode(value, "UTF-8")));
	}

	@Test
	public void testCountFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		for (int i = 0; i < 100; i++) {
			topic.createName("Name " + Integer.toString(i));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		query = "fn:count( myTopic / tm:name )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(BigInteger.valueOf(100), set.first().first());

		Topic topicBySL = createTopicBySL("loc");
		query = "// tm:subject [ fn:count(.>>locators)> 0 ]";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicBySL, set.first().first());
	}

	@Test
	public void testCompareFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		for (int i = 0; i < 100; i++) {
			topic.createName("Name " + Integer.toString(i));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		for (int i = 0; i < 100; i++) {
			query = "fn:compare( myTopic / tm:name , \"Name " + Integer.toString(i) + "\")";
			set = execute(new TMQLQuery(topicMap, query));
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(true, set.first().first());
		}
	}

	@Test
	public void testExceptFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name[] names = new Name[100];
		for (int i = 0; i < 100; i++) {
			names[i] = topic.createName("Name " + Integer.toString(i));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		for (int i = 0; i < 100; i++) {
			query = "fn:except(  myTopic / tm:name , \"Name " + Integer.toString(i) + "\")";
			set = execute(new TMQLQuery(topicMap, query));
			assertEquals(99, set.size());
			assertEquals(1, set.first().size());
			for (IResult r : set) {
				if (r.first().equals(names[i])) {
					fail("Name should not be contained!");
				}
			}
		}
	}

	@Test
	public void testConcatFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<String> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name name = topic.createName("Name " + Integer.toString(i));
			names.add(name.getValue());
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		query = "fn:concat(  myTopic / tm:name , \"Name 101\")";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(names.size() + 1, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()) || "Name 101".equalsIgnoreCase(r.first().toString()));
		}
	}

	@Test
	public void testHasDatatypeFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Locator> datatypes = HashUtil.getHashSet();
		datatypes.add(topicMap.createLocator(XmlSchemeDatatypes.XSD_STRING));
		datatypes.add(topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE));
		datatypes.add(topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER));
		datatypes.add(topicMap.createLocator(XmlSchemeDatatypes.XSD_FLOAT));
		for (int i = 0; i < 100; i++) {
			if (i % 4 == 0) {
				Occurrence occ = topic.createOccurrence(createTopic(), "Value", topicMap.createLocator(XmlSchemeDatatypes.XSD_STRING));
				assertEquals(topicMap.createLocator(XmlSchemeDatatypes.XSD_STRING), occ.getDatatype());
			} else if (i % 4 == 1) {
				Occurrence occ = topic.createOccurrence(createTopic(), "1000-10-10", topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE));
				assertEquals(topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE), occ.getDatatype());
			} else if (i % 4 == 2) {
				Occurrence occ = topic.createOccurrence(createTopic(), "1", topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER));
				assertEquals(topicMap.createLocator(XmlSchemeDatatypes.XSD_INTEGER), occ.getDatatype());
			} else {
				Occurrence occ = topic.createOccurrence(createTopic(), "1.0", topicMap.createLocator(XmlSchemeDatatypes.XSD_FLOAT));
				assertEquals(topicMap.createLocator(XmlSchemeDatatypes.XSD_FLOAT), occ.getDatatype());
			}
		}
		assertEquals(100, topic.getOccurrences().size());

		String query;
		SimpleResultSet set = null;
		query = "fn:has-datatype(  myTopic >> characteristics tm:occurrence )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(datatypes.contains(r.first()));
		}
	}

	@Test
	public void testHasVariantFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name name = topic.createName("Name");
		Map<Topic, Variant> variants = HashUtil.getHashMap();
		for (int i = 0; i < 100; i++) {
			Topic theme = createTopicBySI("theme_" + Integer.toString(i));
			variants.put(theme, name.createVariant("Variant " + Integer.toString(i), theme));
		}
		assertEquals(variants.size(), name.getVariants().size());

		String query;
		SimpleResultSet set = null;
		for (Entry<Topic, Variant> entry : variants.entrySet()) {
			query = "fn:has-variant(  myTopic >> characteristics tm:name , " + entry.getKey().getSubjectIdentifiers().iterator().next().getReference() + " )";
			set = execute(new TMQLQuery(topicMap, query));
			assertEquals(1, set.size());
			assertEquals(1, set.first().size());
			assertEquals(entry.getValue(), set.first().first());
		}
	}

	@Test
	public void testSliceFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name " + Integer.toString(i)));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		query = "fn:slice(  myTopic >> characteristics tm:name , 0 , 10 )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:slice(  myTopic >> characteristics tm:name , -10 , 10 )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:slice(  myTopic >> characteristics tm:name , -10 , 100 )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:slice(  myTopic >> characteristics tm:name , -10 , 1000 )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:slice(  myTopic >> characteristics tm:name , 10 , 0 )";
		set = execute(new TMQLQuery(topicMap, query));

	}

	@Test
	public void testUniqueFunction() throws Exception {

		String query;
		SimpleResultSet set = null;
		query = "fn:uniq(  // tm:subject )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.size());

		Topic topic = createTopicBySI("myTopic");
		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name " + Integer.toString(i % 4)));
		}
		assertEquals(100, topic.getNames().size());
		query = "myTopic / tm:name";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof String);
			assertTrue(r.first().equals("Name 0") || r.first().equals("Name 1") || r.first().equals("Name 2") || r.first().equals("Name 3"));
		}

		query = "fn:uniq(  myTopic / tm:name )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(4, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof String);
		}
	}

	@Test
	public void testZigZagFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name " + i));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		query = "myTopic >> characteristics tm:name";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:zigzag(  myTopic >> characteristics tm:name )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		for (IResult r : set) {
			assertEquals(100, r.size());
			for (Object o : r) {
				assertTrue(names.contains(o));
			}
		}

	}

	@Test
	public void testZagZigFunction() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Name> names = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			names.add(topic.createName("Name " + i));
		}
		assertEquals(100, topic.getNames().size());

		String query;
		SimpleResultSet set = null;
		query = "myTopic >> characteristics tm:name";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}

		query = "fn:zigzag(  myTopic >> characteristics tm:name )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		for (IResult r : set) {
			assertEquals(100, r.size());
			for (Object o : r) {
				assertTrue(names.contains(o));
			}
		}

		query = "fn:zagzig ( fn:zigzag (  myTopic >> characteristics tm:name ) )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(names.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFunctionTopicBySubjectIdentifier() throws Exception {
		Topic topic = createTopicBySI("myType");
		Topic otherTopic = createTopicBySI("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " fn:topics-by-subjectidentifier ( \"" + topic.getSubjectIdentifiers().iterator().next().getReference() + "\" , \""
				+ otherTopic.getSubjectIdentifiers().iterator().next().getReference() + "\" ) ";
		set = execute(query);
		assertEquals(2, set.size());
		assertEquals(1, set.get(0).size());
		assertTrue(topic.equals(set.get(0, 0)) || otherTopic.equals(set.get(0, 0)));
		assertEquals(1, set.get(1).size());
		assertTrue(topic.equals(set.get(1, 0)) || otherTopic.equals(set.get(1, 0)));
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFunctionTopicBySubjectLocator() throws Exception {
		Topic topic = createTopicBySL("myType");
		Topic otherTopic = createTopicBySL("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " fn:topics-by-subjectlocator ( \"" + topic.getSubjectLocators().iterator().next().getReference() + "\" , \"" + otherTopic.getSubjectLocators().iterator().next().getReference()
				+ "\" ) ";
		set = execute(query);
		assertEquals(2, set.size());
		assertEquals(1, set.get(0).size());
		assertTrue(topic.equals(set.get(0, 0)) || otherTopic.equals(set.get(0, 0)));
		assertEquals(1, set.get(1).size());
		assertTrue(topic.equals(set.get(1, 0)) || otherTopic.equals(set.get(1, 0)));
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFunctionTopicByItemIdentifier() throws Exception {
		Topic topic = createTopicByII("myType");
		Topic otherTopic = createTopicByII("myOtherTopic");
		String query = null;
		SimpleResultSet set = null;

		query = " fn:topics-by-itemidentifier ( \"" + topic.getItemIdentifiers().iterator().next().getReference() + "\" , \"" + otherTopic.getItemIdentifiers().iterator().next().getReference()
				+ "\" ) ";
		set = execute(query);
		assertEquals(2, set.size());
		assertEquals(1, set.get(0).size());
		assertTrue(topic.equals(set.get(0, 0)) || otherTopic.equals(set.get(0, 0)));
		assertEquals(1, set.get(1).size());
		assertTrue(topic.equals(set.get(1, 0)) || otherTopic.equals(set.get(1, 0)));
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFunctionArrayFunction() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		query = " fn:array ( \"Hallo\", \"Welt\", \"das\",\"sollte\",\"ein\",\"array\",\"werden\")";
		set = execute(query);
		assertEquals(7, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals("Hallo", set.get(0, 0));
		assertEquals(1, set.get(1).size());
		assertEquals("Welt", set.get(1, 0));
		assertEquals(1, set.get(2).size());
		assertEquals("das", set.get(2, 0));
		assertEquals(1, set.get(3).size());
		assertEquals("sollte", set.get(3, 0));
		assertEquals(1, set.get(4).size());
		assertEquals("ein", set.get(4, 0));
		assertEquals(1, set.get(5).size());
		assertEquals("array", set.get(5, 0));
		assertEquals(1, set.get(6).size());
		assertEquals("werden", set.get(6, 0));
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMaxFunction() throws Exception {
		Topic topic = createTopicByII("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			for (int j = 0; j < i; j++) {
				t.createName("name" + j);
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " fn:max ( myType >> instances, fn:count ( . >> characteristics ))";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(BigInteger.valueOf(99), set.get(0,0));
	}
	
	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMinFunction() throws Exception {
		Topic topic = createTopicByII("myType");
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			for (int j = 0; j <= i; j++) {
				t.createName("name" + j);
			}
		}

		String query = null;
		SimpleResultSet set = null;

		query = " fn:min ( myType >> instances, fn:count ( . >> characteristics ))";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.get(0).size());
		assertEquals(BigInteger.valueOf(1), set.get(0,0));
	}


}
