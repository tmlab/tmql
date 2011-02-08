/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.tests;

import static junit.framework.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestDeleteExpressionExtendedSytle extends Tmql4JTestCase {

	@Test
	public void testDeleteFirstNameOfAllComposer() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			ids.add(t.createName("Name").getId());
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics tm:name WHERE $t ISA myType";
		SimpleResultSet set = execute(query);
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(100, set.first().first());
		assertEquals(ids, set.first().get(1));

		assertEquals(0, t.getNames().size());
	}

	@Test
	public void testDeleteScopedNamesOfAllComposers() throws Exception {
		Topic theme = createTopicBySI("theme");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.addTheme(theme);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics tm:name @theme WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(50, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedOccurrenceOfAllComposers() throws Exception {
		Topic cType = createTopicBySI("cType");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(cType);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics cType WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(50, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedAndScopedOccurrence() throws Exception {
		Topic theme = createTopicBySI("theme");
		Topic cType = createTopicBySI("cType");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(cType);
			}
			if (i % 4 == 0) {
				n.addTheme(theme);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics cType @theme WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(25, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(75, t.getNames().size());
	}

}
