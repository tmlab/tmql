/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.tests;

import junit.framework.Assert;

import org.junit.Test;
import org.tmapi.core.Topic;


/**
 * @author Sven Krosse
 *
 */
public class TestRemoveDuplicates extends Tmql4JTestCase {

	@Test
	public void testRemoveDuplicates() throws Exception {
		Topic t = createTopic();
		for ( int i = 0 ; i < 100 ; i++){
			t.createName("Name");
		}
		Assert.assertEquals(100, t.getNames().size());
		execute("fn:remove-duplicates()");
		Assert.assertEquals(1, t.getNames().size());
	}
	
}
