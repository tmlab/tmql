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
import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestDeleteExpressionPathSytle extends Tmql4JTestCase {

	@Test
	public void testDeleteTopic() throws Exception {
		createTopicBySI("myTopic");
		assertNotNull(topicMap
				.getTopicBySubjectIdentifier(createLocator("myTopic")));
		SimpleResultSet set = execute("myTopic");
		assertEquals(1, set.size());

		set = execute("DELETE myTopic");
		assertEquals(1, set.size());
		assertEquals(1L, set.first().first());
	}

	@Test
	public void testDeleteOneName() throws Exception {
		Topic t = createTopicBySI("myTopic");
		for (int i = 0; i < 100; i++) {
			t.createName("Name");
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(100L, set.first().first());

		assertEquals(0, t.getNames().size());
	}

	@Test
	public void testDeleteScopedName() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.addTheme(theme);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name @theme";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(50L, set.first().first());

		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTyped() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(type);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name [ ^ myType ]";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(50L, set.first().first());

		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedAndScoped() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Topic type = createTopicBySI("myType");
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(type);
			}
			if (i % 4 == 0) {
				n.addTheme(theme);
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics myType @theme";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(25L, set.first().first());

		assertEquals(75, t.getNames().size());
	}

}
