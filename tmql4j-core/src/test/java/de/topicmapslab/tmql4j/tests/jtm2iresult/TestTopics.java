package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.*;

import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

public class TestTopics extends AbstractTest {

	@org.junit.Test
	public void testSubjectIdentifier(){
		
		ITopicMap map = getTopicMap();
		
		Locator si1 = map.createLocator("http://lala/topic/si/1");
		Locator si2 = map.createLocator("http://lala/topic/si/2");
		
		Topic topic = map.createTopicBySubjectIdentifier(si1);
		topic.addSubjectIdentifier(si2);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(topic);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Topic t = outR.get(0);
		assertNotNull(t);
		
		Set<Locator> locators = t.getSubjectIdentifiers();
		assertNotNull(locators);
		assertEquals(2, locators.size());
		assertTrue(locators.contains(si1));
		assertTrue(locators.contains(si2));
	}
	
	@org.junit.Test
	public void testSubjectLocator(){
		
		ITopicMap map = getTopicMap();
		
		Locator sl1 = map.createLocator("http://lala/topic/sl/1");
		Locator sl2 = map.createLocator("http://lala/topic/sl/2");
		
		Topic topic = map.createTopicBySubjectLocator(sl1);
		topic.addSubjectLocator(sl2);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(topic);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Topic t = outR.get(0);
		assertNotNull(t);
		
		Set<Locator> locators = t.getSubjectLocators();
		assertNotNull(locators);
		assertEquals(2, locators.size());
		assertTrue(locators.contains(sl1));
		assertTrue(locators.contains(sl2));
	}
	
	@org.junit.Test
	public void testItemIdentifier(){
		
		ITopicMap map = getTopicMap();
		
		Locator ii1 = map.createLocator("http://lala/topic/ii/1");
		Locator ii2 = map.createLocator("http://lala/topic/ii/2");
		
		Topic topic = map.createTopicByItemIdentifier(ii1);
		topic.addItemIdentifier(ii2);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(topic);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Topic t = outR.get(0);
		assertNotNull(t);
		
		Set<Locator> locators = t.getItemIdentifiers();
		assertNotNull(locators);
		assertEquals(2, locators.size());
		assertTrue(locators.contains(ii1));
		assertTrue(locators.contains(ii2));
	}

	@org.junit.Test
	public void testName(){
	
		String value = "lala";
		ITopicMap map = getTopicMap();
		
		Topic topic = map.createTopic();
		Name n = topic.createName(value);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(topic);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Topic t = outR.get(0);
		assertNotNull(t);
		
		Set<Name> names = t.getNames();
		assertNotNull(names);
		assertEquals(1, names.size());
		assertEquals(value, names.iterator().next().getValue());
		
	}
	
	@org.junit.Test
	public void testOccurrence(){
	
		String value = "lala";
		ITopicMap map = getTopicMap();
		
		Topic topic = map.createTopic();
		Occurrence o = topic.createOccurrence(map.createTopic(),value);
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(topic);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		
		Topic t = outR.get(0);
		assertNotNull(t);
		
		Set<Occurrence> occurrences = t.getOccurrences();
		assertNotNull(occurrences);
		assertEquals(1, occurrences.size());
		assertEquals(value, occurrences.iterator().next().getValue());
		
	}
	
	
}
