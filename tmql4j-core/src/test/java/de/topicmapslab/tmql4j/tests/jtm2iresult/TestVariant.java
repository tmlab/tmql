package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.core.LocatorImpl;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * test class for variant created form jtmqr result
 * 
 * @author Christian Haß
 * 
 */
public class TestVariant extends AbstractTest {

	/**
	 * checks if the id is correct
	 */
	@Test
	public void testId() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");
		String value = "lala";
		Construct construct = name.createVariant(value, map.createTopic());

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(construct);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Construct c = outR.get(0);
		assertNotNull(c);
		assertNull(c.getParent());
		assertEquals(construct.getId(), c.getId());
	}

	/**
	 * tests if the value is correct
	 */
	@org.junit.Test
	public void testValue() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");
		String value = "lala";
		Variant variant = name.createVariant(value, map.createTopic());

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);

		inR.add(variant);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);

		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());

		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());

		Variant v = outR.get(0);
		assertNotNull(v);
		assertNull(v.getParent());
		assertEquals(value, v.getValue());

	}

	/**
	 * checks item identifier
	 */
	@Test
	public void testItemIdentifier() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");

		Variant variant = name.createVariant("lala", map.createTopic());
		String ii1 = "http://variant/ii/one";
		String ii2 = "http://variant/ii/two";
		variant.addItemIdentifier(map.createLocator(ii1));
		variant.addItemIdentifier(map.createLocator(ii2));

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(variant);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Variant v = outR.get(0);
		assertNotNull(v);
		assertNull(v.getParent());

		assertEquals(2, v.getItemIdentifiers().size());
		assertTrue(v.getItemIdentifiers().contains(new LocatorImpl(ii1)));
		assertTrue(v.getItemIdentifiers().contains(new LocatorImpl(ii2)));

	}

	/**
	 * checks the scope
	 */
	@Test
	public void checkScope() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");
		String themeSi = "http://theme";
		Variant variant = name.createVariant("lala", map.createTopicBySubjectIdentifier(map.createLocator(themeSi)));

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(variant);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());

		Variant v = outR.get(0);
		assertNotNull(v);
		assertNull(v.getParent());

		Set<Topic> themes = v.getScope();
		assertNotNull(themes);
		assertEquals(1, themes.size());
		assertTrue(themes.iterator().next().getSubjectIdentifiers().contains(new LocatorImpl(themeSi)));

	}

	/**
	 * checks reifier
	 */
	@Test
	public void checkReifier() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");
		Variant variant = name.createVariant("lala", map.createTopic());

		String reifierSi = "http://reifier";

		variant.setReifier(map.createTopicBySubjectIdentifier(map.createLocator(reifierSi)));

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(variant);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Variant o = outR.get(0);
		assertNotNull(o);
		assertNull(o.getParent());

		Topic reifier = o.getReifier();
		assertNotNull(reifier);

		assertEquals(1, reifier.getSubjectIdentifiers().size());
		assertTrue(reifier.getSubjectIdentifiers().contains(new LocatorImpl(reifierSi)));

	}

	/**
	 * check datatype
	 */
	@Test
	public void checkDatatype() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("name");
		Variant variant1 = name.createVariant("lala", map.createTopic());
		Variant variant2 = name.createVariant("lala2", map.createTopic());
		variant2.setValue(666);

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(variant1);
		inR.add(variant2);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(2, outR.size());

		Variant o1 = outR.get(0);
		assertNotNull(o1);
		assertNull(o1.getParent());

		assertEquals(new LocatorImpl("http://www.w3.org/2001/XMLSchema#string"), o1.getDatatype());

		Variant o2 = outR.get(1);
		assertNotNull(o2);
		assertNull(o2.getParent());

		assertEquals(new LocatorImpl("http://www.w3.org/2001/XMLSchema#int"), o2.getDatatype());

	}
}
