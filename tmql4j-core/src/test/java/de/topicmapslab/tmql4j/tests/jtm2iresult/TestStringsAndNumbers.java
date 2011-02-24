package de.topicmapslab.tmql4j.tests.jtm2iresult;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

import static junit.framework.Assert.*;

/**
 * test class to test general handling from string and number results
 * @author Christian
 *
 */
public class TestStringsAndNumbers extends AbstractTest {

	/**
	 * checks a single string result
	 */
	@Test
	public void testSingleStringResult(){
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		String testString = "Test String";
		
		inR.add(testString);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		
		assertEquals(testString, outRS.get(0).get(0));
		
	}
	
	/**
	 * checks a single double result
	 */
	@Test
	public void testSingleDoubleResult(){
	
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		double testDouble = 3.1415;
		
		inR.add(testDouble);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		
		assertEquals(testDouble, outRS.get(0).get(0));
	}
	
	/**
	 * checks a single integer result
	 */
	@Test
	public void testSingleIntegerResult(){
	
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		int testInt = 666;
		
		inR.add(testInt);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		
		assertEquals(testInt, outRS.get(0).get(0));
	}
	
	/**
	 * checks a combined result
	 */
	@Test
	public void testCombinationResult(){
		
		String testString = "Test String";
		double testDouble = 3.1415;
		int testInt = 666;
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		
		inR.add(testString);
		inR.add(testString);
		inRS.addResult(inR);
		
		inR = new SimpleResult(inRS);
		inR.add(testDouble);
		inR.add(testInt);
		inR.add(testString);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		
		assertEquals(testString, outRS.get(0).get(0));
		assertEquals(testString, outRS.get(0).get(1));
		
		assertEquals(testDouble, outRS.get(1).get(0));
		assertEquals(testInt, outRS.get(1).get(1));
		assertEquals(testString, outRS.get(1).get(2));
	}
	
	/**
	 * checks the empty result set
	 */
	@Test
	public void testEmptyResultSet(){
		
		SimpleResultSet inRS = createResultSet();
		IResultSet<?> outRS = convert(inRS);
		assertTrue(outRS.isEmpty());
	}
	
	/**
	 * checks an result set with an single empty result
	 */
	@Test
	public void testEmptyResult(){
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		
		assertFalse(outRS.isEmpty());
		
		IResult r = outRS.get(0);
		assertEquals(0, r.size());
	}
	
	/**
	 * checks aliasess
	 */
	@Test
	public void testAlias(){

		String testString1 = "Test String 1";
		String testString2 = "Test String 2";
		
		SimpleResultSet inRS = createResultSet();
		
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(testString1);
		inR.add(testString2);
		inRS.addResult(inR);
		
		Map<String,Integer> alias = new HashMap<String, Integer>();
		alias.put("string 1", 0);
		alias.put("string 2", 1);
		
		Map<Integer, String> indexes = new HashMap<Integer, String>();
		indexes.put(0, "string 1");
		indexes.put(1, "string 2");
		
		inRS.setAlias(alias);
		inRS.setIndexes(indexes);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertTrue(outRS.hasAlias());
		
		assertEquals(testString2, outRS.get(0).get("string 2"));
		assertEquals(testString1, outRS.get(0).get("string 1"));
		
	}
	
	
}
