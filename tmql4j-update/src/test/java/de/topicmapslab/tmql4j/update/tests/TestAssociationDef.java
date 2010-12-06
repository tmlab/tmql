/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.util.HashUtil;
/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestAssociationDef extends Tmql4JTestCase {

	@Test
	public void testAssociationDefAsResult() throws Exception {
		
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic roleType = createTopicBySI("roleType");
		
		Set<Association> associations = HashUtil.getHashSet();
		for ( int i = 0 ; i < associations.size() ; i++){
			Association a = createAssociation(type);
			a.createRole(roleType, topic);
			a.createRole(createTopic(), createTopic());
			associations.add(a);
		}
				
		String query = null;
		SimpleResultSet set = null;

		assertEquals(associations.size(), topicMap.getAssociations().size());
		
		query = " tm:subject ( tm:subject : myTopic )";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(0, set.getResults().size());
		
		query = " tm:subject ( tm:subject : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(associations.size(), set.getResults().size());
		for ( IResult r : set){
			assertEquals(1,r.size());
			assertTrue(associations.contains(r.first()));
		}
		
		query = " tm:subject ( roleType : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(associations.size(), set.getResults().size());
		for ( IResult r : set){
			assertEquals(1,r.size());
			assertTrue(associations.contains(r.first()));
		}
		
		query = " myType ( tm:subject : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(associations.size(), set.getResults().size());
		for ( IResult r : set){
			assertEquals(1,r.size());
			assertTrue(associations.contains(r.first()));
		}
		
		query = " myType ( roleType : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(associations.size(), set.getResults().size());
		for ( IResult r : set){
			assertEquals(1,r.size());
			assertTrue(associations.contains(r.first()));
		}
	}

}
