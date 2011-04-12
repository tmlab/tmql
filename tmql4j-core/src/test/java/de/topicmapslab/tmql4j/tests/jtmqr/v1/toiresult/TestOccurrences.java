package de.topicmapslab.tmql4j.tests.jtmqr.v1.toiresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.core.LocatorImpl;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * test class for occurrences created from jtmqr tmql result
 * 
 * @author Christian Haß
 */
public class TestOccurrences extends AbstractTest {

	/**
	 * checks if the id is correct
	 */
	@Test
	public void testId() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();

		String value = "lala";
		Construct construct = topic.createOccurrence(map.createTopic(), value);

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
	 * tests if the value is correnct
	 */
	@org.junit.Test
	public void testValue() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();

		String value = "lala";
		Occurrence occurrence = topic.createOccurrence(map.createTopic(), value);

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);

		inR.add(occurrence);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);

		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());

		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());

		Occurrence o = outR.get(0);
		assertNotNull(o);
		assertNull(o.getParent());
		assertEquals(value, o.getValue());

	}

	/**
	 * checks if the type is correct
	 */
	@org.junit.Test
	public void testType() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();

		String typeSi = "http://test/occurrence/type";

		Topic type = map.createTopicBySubjectIdentifier(map.createLocator(typeSi));

		Occurrence occurrence = topic.createOccurrence(type, "lala");

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(occurrence);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Occurrence o = outR.get(0);
		assertNotNull(o);
		assertNull(o.getParent());

		Topic t = o.getType();
		assertEquals(1, t.getSubjectIdentifiers().size());
		assertTrue(t.getSubjectIdentifiers().contains(new LocatorImpl(typeSi)));
	}

	/**
	 * checks item identifier
	 */
	@Test
	public void testItemIdentifier() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Occurrence occurrence = topic.createOccurrence(map.createTopic(), "lala");
		String ii1 = "http://occurrence/ii/one";
		String ii2 = "http://occurrence/ii/two";
		occurrence.addItemIdentifier(map.createLocator(ii1));
		occurrence.addItemIdentifier(map.createLocator(ii2));

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(occurrence);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Occurrence o = outR.get(0);
		assertNotNull(o);
		assertNull(o.getParent());

		assertEquals(2, o.getItemIdentifiers().size());
		assertTrue(o.getItemIdentifiers().contains(new LocatorImpl(ii1)));
		assertTrue(o.getItemIdentifiers().contains(new LocatorImpl(ii2)));

	}

	/**
	 * checks the scope
	 */
	@Test
	public void checkScope() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();

		// test one theme
		Occurrence occurrence1 = topic.createOccurrence(map.createTopic(), "lala");
		String themeSi = "http://theme";
		occurrence1.addTheme(map.createTopicBySubjectIdentifier(map.createLocator(themeSi)));

		// test 2 themes
		Occurrence occurrence2 = topic.createOccurrence(map.createTopic(), "lala");
		occurrence2.addTheme(map.createTopic());
		occurrence2.addTheme(map.createTopic());

		// test no scope
		Occurrence occurrence3 = topic.createOccurrence(map.createTopic(), "lala3");

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(occurrence1);
		inR.add(occurrence2);
		inR.add(occurrence3);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(3, outR.size());

		Occurrence o1 = outR.get(0);
		Occurrence o2 = outR.get(1);
		Occurrence o3 = outR.get(2);

		assertNotNull(o1);
		assertNotNull(o2);
		assertNotNull(o3);

		assertNull(o1.getParent());
		assertNull(o2.getParent());
		assertNull(o3.getParent());

		Set<Topic> themes1 = o1.getScope();
		assertNotNull(themes1);
		assertEquals(1, themes1.size());
		assertTrue(themes1.iterator().next().getSubjectIdentifiers().contains(new LocatorImpl(themeSi)));

		Set<Topic> themes2 = o2.getScope();
		assertNotNull(themes2);
		assertEquals(2, themes2.size());

		Set<Topic> themes3 = o3.getScope();
		assertNotNull(themes3);
		assertEquals(0, themes3.size());

	}

	/**
	 * checks reifier
	 */
	@Test
	public void checkReifier() {

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Occurrence occurrence = topic.createOccurrence(map.createTopic(), "lala");

		String reifierSi = "http://reifier";

		occurrence.setReifier(map.createTopicBySubjectIdentifier(map.createLocator(reifierSi)));

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(occurrence);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Occurrence o = outR.get(0);
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
		Occurrence occurrence1 = topic.createOccurrence(map.createTopic(), "lala");
		Occurrence occurrence2 = topic.createOccurrence(map.createTopic(), "lala2");
		occurrence2.setValue(666);

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(occurrence1);
		inR.add(occurrence2);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(2, outR.size());

		Occurrence o1 = outR.get(0);
		assertNotNull(o1);
		assertNull(o1.getParent());

		assertEquals(new LocatorImpl("http://www.w3.org/2001/XMLSchema#string"), o1.getDatatype());

		Occurrence o2 = outR.get(1);
		assertNotNull(o2);
		assertNull(o2.getParent());

		assertEquals(new LocatorImpl("http://www.w3.org/2001/XMLSchema#int"), o2.getDatatype());

	}

}
