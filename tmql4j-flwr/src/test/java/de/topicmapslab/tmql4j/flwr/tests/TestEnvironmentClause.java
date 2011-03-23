/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;


/**
 * @author Sven Krosse
 * 
 */
public class TestEnvironmentClause extends Tmql4JTestCase {

	

	@Test
	@Ignore
	public void testPragmaOfTypeTransitivity() throws Exception {

		fromXtm("src/test/resources/toytm.xtm");

		String query = "%pragma taxonometry tm:transitive http://en.wikipedia.org/wiki/Country >> instances";
		IResultSet<?> set = execute(query);
		assertEquals(10, set.size());
		
		query = "%pragma taxonometry tm:inttransitive http://en.wikipedia.org/wiki/Country >> instances";
		set = execute(query);
		assertEquals(0, set.size());
	}

}
