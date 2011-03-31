/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.tests;

import java.util.Set;

import org.junit.Test;
import static junit.framework.Assert.*;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestPrepared extends Tmql4JTestCase {

	@Test
	public void testPrepared() throws Exception {
		IPreparedStatement stmt = runtime.preparedStatement("DELETE ? << id");
		stmt.setTopicMap(topicMap);
		Set<String> ids = HashUtil.getHashSet();
		for ( int i = 0 ; i < 100 ; i++ ){
			ids.add(createTopic().getId());
		}
		int size = 100;
		assertEquals(size, topicMap.getTopics().size());		
		for ( String id : ids ){
			stmt.setString(0, id);
			stmt.run();
			assertEquals(--size, topicMap.getTopics().size());
		}
	}

}
