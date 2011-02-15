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
import static junit.framework.Assert.assertNotNull;

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
public class TestDeleteExpressionPathSytle extends Tmql4JTestCase {

	@Test
	public void testDeleteTopic() throws Exception {
		Topic t = createTopicBySI("myTopic");
		assertNotNull(topicMap
				.getTopicBySubjectIdentifier(createLocator("myTopic")));
		SimpleResultSet set = execute("myTopic");
		assertEquals(1, set.size());

		Set<String> ids = HashUtil.getHashSet();
		ids.add(t.getId());
		
		set = execute("DELETE myTopic");
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(ids, set.first().get(1));
		assertEquals(1, set.first().first());
	}

	@Test
	public void testDeleteOneName() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			ids.add(t.createName("Name").getId());
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(100, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(0, t.getNames().size());
	}

	@Test
	public void testDeleteScopedName() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.addTheme(theme);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name @theme";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(50, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTyped() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(type);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics tm:name [ ^ myType ]";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(50, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(50, t.getNames().size());
	}

	@Test
	public void testDeleteTypedAndScoped() throws Exception {
		Topic t = createTopicBySI("myTopic");
		Topic theme = createTopicBySI("theme");
		Topic type = createTopicBySI("myType");
		Set<String> ids = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Name n = t.createName("Name");
			if (i % 2 == 0) {
				n.setType(type);
			}
			if (i % 4 == 0) {
				n.addTheme(theme);
				ids.add(n.getId());
			}
		}
		assertEquals(100, t.getNames().size());

		String query = " DELETE myTopic >> characteristics myType @theme";
		SimpleResultSet set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(2, set.first().size());
		assertEquals(25, set.first().first());
		assertEquals(ids, set.first().get(1));
		assertEquals(75, t.getNames().size());
	}

}
