package de.topicmapslab.tmql4j.tests.jtmqr.v1.toiresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.LocatorStub;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * 
 * @author Sven Krosse
 * 
 */
public class TestListValues extends AbstractTest {

	@Test
	public void testSingleListOfStrings() throws IOException {
		SimpleResultSet inRes = new SimpleResultSet(getTopicMap().getTopicMapSystem(), getTopicMap());
		SimpleResult tuple = new SimpleResult(inRes);
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		tuple.add(list);
		inRes.addResult(tuple);
		IResultSet<?> outRS = convert(inRes);
		assertEquals(1, outRS.size());
		assertEquals(1, outRS.get(0).size());
		assertTrue(outRS.get(0, 0) instanceof List<?>);
		List<?> list_ = outRS.get(0, 0);
		assertEquals(list, list_);
	}

	@Test
	public void testSingleListOfNumbers() throws IOException {
		SimpleResultSet inRes = new SimpleResultSet(getTopicMap().getTopicMapSystem(), getTopicMap());
		SimpleResult tuple = new SimpleResult(inRes);
		List<BigInteger> list = new ArrayList<BigInteger>();
		list.add(BigInteger.valueOf(1L));
		list.add(BigInteger.valueOf(2L));
		tuple.add(list);
		inRes.addResult(tuple);
		IResultSet<?> outRS = convert(inRes);
		assertEquals(1, outRS.size());
		assertEquals(1, outRS.get(0).size());
		assertTrue(outRS.get(0, 0) instanceof List<?>);
		List<?> list_ = outRS.get(0, 0);
		assertEquals(list, list_);
	}

	@Test
	public void testSingleListOfLocators() throws IOException {
		SimpleResultSet inRes = new SimpleResultSet(getTopicMap().getTopicMapSystem(), getTopicMap());
		SimpleResult tuple = new SimpleResult(inRes);
		List<Locator> list = new ArrayList<Locator>();
		Locator l1 = new LocatorStub("http://psi.example.org");
		Locator l2 = new LocatorStub("http://psi.example.org");
		list.add(l1);
		list.add(l2);
		tuple.add(list);
		inRes.addResult(tuple);
		IResultSet<?> outRS = convert(inRes);
		assertEquals(1, outRS.size());
		assertEquals(1, outRS.get(0).size());
		assertTrue(outRS.get(0, 0) instanceof List<?>);
		List<?> list_ = outRS.get(0, 0);
		assertEquals(list, list_);
	}

	@Test
	public void testSingleListOfBooleans() throws IOException {
		SimpleResultSet inRes = new SimpleResultSet(getTopicMap().getTopicMapSystem(), getTopicMap());
		SimpleResult tuple = new SimpleResult(inRes);
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(true);
		list.add(false);
		tuple.add(list);
		inRes.addResult(tuple);
		IResultSet<?> outRS = convert(inRes);
		assertEquals(1, outRS.size());
		assertEquals(1, outRS.get(0).size());
		assertTrue(outRS.get(0, 0) instanceof List<?>);
		List<?> list_ = outRS.get(0, 0);
		assertEquals(list, list_);
	}

	@Test
	public void testSingleListOfConstructs() throws IOException {
		SimpleResultSet inRes = new SimpleResultSet(getTopicMap().getTopicMapSystem(), getTopicMap());
		SimpleResult tuple = new SimpleResult(inRes);
		List<Construct> list = new ArrayList<Construct>();
		Topic t = getTopicMap().createTopicBySubjectIdentifier(getTopicMap().createLocator("http://psi.example.org"));
		list.add(t);
		list.add(t.createName("Name"));
		tuple.add(list);
		inRes.addResult(tuple);
		IResultSet<?> outRS = convert(inRes);
		assertEquals(1, outRS.size());
		assertEquals(1, outRS.get(0).size());
		assertTrue(outRS.get(0, 0) instanceof List<?>);
		List<?> list_ = outRS.get(0, 0);
		Object o1 = list_.get(0);
		assertTrue(o1 instanceof Topic);
		assertEquals(1, ((Topic) o1).getSubjectIdentifiers().size());
		assertEquals("http://psi.example.org", ((Topic) o1).getSubjectIdentifiers().iterator().next().getReference());
		Object o2 = list_.get(1);
		assertTrue(o2 instanceof Name);
		assertEquals("Name", ((Name) o2).getValue());
	}

}
