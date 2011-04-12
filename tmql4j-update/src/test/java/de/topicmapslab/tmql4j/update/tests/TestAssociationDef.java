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

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
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
		for (int i = 0; i < associations.size(); i++) {
			Association a = createAssociation(type);
			a.createRole(roleType, topic);
			a.createRole(createTopic(), createTopic());
			associations.add(a);
		}

		String query = null;
		SimpleResultSet set = null;

		assertEquals(associations.size(), topicMap.getAssociations().size());

		query = " tm:subject ( tm:subject : myTopic )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(0, set.getResults().size());

		query = " tm:subject ( tm:subject : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(associations.size(), set.getResults().size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(associations.contains(r.first()));
		}

		query = " tm:subject ( roleType : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(associations.size(), set.getResults().size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(associations.contains(r.first()));
		}

		query = " myType ( tm:subject : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(associations.size(), set.getResults().size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(associations.contains(r.first()));
		}

		query = " myType ( roleType : myTopic , ... )";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(associations.size(), set.getResults().size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(associations.contains(r.first()));
		}
	}

	@Test
	public void testIriInBrackets() throws Exception {
		assertEquals(0, topicMap.getAssociations().size());
		String query = "UPDATE associations ADD <http://onotoa.topicmapslab.de#has-methods2> ( <http://fundivers.biow.uni-leipzig.de/dataset> : <http://fundivers.biow.uni-leipzig.de/dataset/a7b1cd7868446b9c693e46238861bd22>,"
				+ "<http://fundivers.biow.uni-leipzig.de/dataset> : <http://fundivers.biow.uni-leipzig.de/datagroup/Scientific+plant+species+name> )";
		IResultSet<?> rs = execute(query);
		System.out.println(rs);
		assertEquals(1, topicMap.getAssociations().size());

	}

}
