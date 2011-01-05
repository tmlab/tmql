/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.results.SimpleResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import static junit.framework.Assert.*;
/**
 * @author Sven Krosse
 * 
 */
public class TestResultSet {

	/**
	 * Test method for
	 * {@link de.topicmapslab.tmql4j.components.processor.results.ResultSet#unify()}
	 * .
	 */
	@Test
	public void testUnify() {

		SimpleResultSet resultSet = new SimpleResultSet();

		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 5; j++) {
				SimpleResult result = new SimpleResult(resultSet);
				result.add(i);
				result.add(i);
				result.add(i);
				resultSet.addResult(result);
			}
		}
		
		assertEquals(5000, resultSet.size());
		resultSet.unify();
		assertEquals(1000, resultSet.size());
	}

}
