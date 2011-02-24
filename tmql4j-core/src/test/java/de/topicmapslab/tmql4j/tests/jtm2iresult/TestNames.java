package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
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
 * test name constructs created from jtmqr
 * @author Christian Haß
 *
 */
public class TestNames extends AbstractTest {

	/**
	 * tests if the value is correnct
	 */
	@org.junit.Test
	public void testValue(){

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		
		String value = "lala";
		Name name = topic.createName(value);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(name);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Name n = outR.get(0);
		assertNotNull(n);
		assertNull(n.getParent());
		assertEquals(value, n.getValue());
		
	}
	
	/**
	 * checks if the type is correct
	 */
	@org.junit.Test
	public void testType(){
		
		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		
		String typeSi = "http://test/name/type";
		
		Topic type = map.createTopicBySubjectIdentifier(map.createLocator(typeSi));
		
		Name name = topic.createName(type, "lala");
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(name);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Name n = outR.get(0);
		assertNotNull(n);
		assertNull(n.getParent());
		
		Topic t = n.getType();
		assertEquals(1, t.getSubjectIdentifiers().size());
		assertTrue(t.getSubjectIdentifiers().contains(new LocatorImpl(typeSi)));
	}
	
	/**
	 * checks item identifier
	 */
	@Test
	public void testItemIdentifier(){
		
		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("lala");
		String ii1 = "http://name/ii/one";
		String ii2 = "http://name/ii/two";
		name.addItemIdentifier(map.createLocator(ii1));
		name.addItemIdentifier(map.createLocator(ii2));
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(name);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Name n = outR.get(0);
		assertNotNull(n);
		assertNull(n.getParent());
		
		assertEquals(2, n.getItemIdentifiers().size());
		assertTrue(n.getItemIdentifiers().contains(new LocatorImpl(ii1)));
		assertTrue(n.getItemIdentifiers().contains(new LocatorImpl(ii2)));
		
	}
	
	/**
	 * checks the scope
	 */
	@Test
	public void checkScope(){

		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		
		// test one theme
		Name name = topic.createName("lala");
		String themeSi = "http://theme";
		name.addTheme(map.createTopicBySubjectIdentifier(map.createLocator(themeSi)));
		
		// test 2 themes
		Name name2 = topic.createName("lala");
		name2.addTheme(map.createTopic());
		name2.addTheme(map.createTopic());
			
		// test no scope
		Name name3 = topic.createName("lala3");
			
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(name);
		inR.add(name2);
		inR.add(name3);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(3, outR.size());
		
		Name n1 = outR.get(0);
		Name n2 = outR.get(1);
		Name n3 = outR.get(2);
		
		assertNotNull(n1);
		assertNotNull(n2);
		assertNotNull(n3);
		
		assertNull(n1.getParent());
		assertNull(n2.getParent());
		assertNull(n3.getParent());
		
		Set<Topic> themes1 = n1.getScope();
		assertNotNull(themes1);
		assertEquals(1, themes1.size());
		assertTrue(themes1.iterator().next().getSubjectIdentifiers().contains(new LocatorImpl(themeSi)));

		Set<Topic> themes2 = n2.getScope();
		assertNotNull(themes2);
		assertEquals(2, themes2.size());
		
		Set<Topic> themes3 = n3.getScope();
		assertNotNull(themes3);
		assertEquals(0, themes3.size());
		
	}
	
	/**
	 * checks reifier
	 */
	@Test
	public void checkReifier(){
		
		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("lala");
		
		String reifierSi = "http://reifier";
		
		name.setReifier(map.createTopicBySubjectIdentifier(map.createLocator(reifierSi)));
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(name);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Name n = outR.get(0);
		assertNotNull(n);
		assertNull(n.getParent());
		
		Topic reifier = n.getReifier();
		assertNotNull(reifier);
		
		assertEquals(1, reifier.getSubjectIdentifiers().size());
		assertTrue(reifier.getSubjectIdentifiers().contains(new LocatorImpl(reifierSi)));
		
	}
	
	/**
	 * checks variants
	 */
	@Test
	public void checkVariants(){
		
		ITopicMap map = getTopicMap();
		Topic topic = map.createTopic();
		Name name = topic.createName("lala");
		
		name.createVariant("variant 1", map.createTopic());
		name.createVariant("variant 2", map.createTopic());
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(name);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Name n = outR.get(0);
		assertNotNull(n);
		assertNull(n.getParent());
		
		Set<Variant> variants = n.getVariants();
		assertNotNull(variants);
		assertEquals(2, variants.size());
		
	}
	
	
}
