/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.delete;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

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
		for (int i = 0; i < 100; i++) {
			t.createName("Name");
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics tm:name WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(100L, set.first().first());

		assertEquals(0, t.getNames().size());
	}

	@Test
	public void testDeleteScopedNamesOfAllComposers() throws Exception {
		Topic theme = createTopicBySI("theme");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.addTheme(theme);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics tm:name @theme WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(50L, set.first().first());

		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedOccurrenceOfAllComposers() throws Exception {
		Topic cType = createTopicBySI("cType");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(cType);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics cType WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(50L, set.first().first());

		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedAndScopedOccurrence() throws Exception {
		Topic theme = createTopicBySI("theme");
		Topic cType = createTopicBySI("cType");
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		t.addType(type);
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(cType);
			}
			if (i % 4 == 0) {
				n.addTheme(theme);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE $t >> characteristics cType @theme WHERE $t ISA myType";
		SimpleResultSet set = execute(new TMQLQuery(query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(25L, set.first().first());

		assertEquals(75, t.getNames().size());
	}

}
