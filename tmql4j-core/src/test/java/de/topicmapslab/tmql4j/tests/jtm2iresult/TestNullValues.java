package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * test class to test general handling from null values
 * 
 * @author Sven Krosse
 * 
 */
public class TestNullValues extends AbstractTest {

	/**
	 * checks a null value result
	 */
	@Test
	public void testNullValue() {

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);

		inR.add((Object) null);
		inRS.addResult(inR);
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertTrue(outRS.isNullValue(0, 0));
	}

	/**
	 * checks a null value result with a non value brother
	 */
	@Test
	public void testNullWithOther() {

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);

		double testDouble = 3.1415;

		inR.add((Object) null);
		inR.add(testDouble);
		inRS.addResult(inR);

		IResultSet<?> outRS = convert(inRS);

		assertEquals(1, outRS.size());
		assertEquals(2, outRS.get(0).size());

		assertTrue(outRS.isNullValue(0, 0));
		assertEquals(testDouble, outRS.get(0, 1));
	}

}
