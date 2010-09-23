package de.topicmapslab.tmql4j.testsuite.path;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

public class TestPredicateInvocation extends Tmql4JTestCase {

	public void testPredicateStrictAsSelectPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);
			assocaition.createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = "SELECT $var WHERE myTopic ( roleType : $var )";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}
	
	public void testPredicateStrictAsPathPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);
			assocaition.createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = " // tm:subject [ myTopic ( roleType : . ) ]";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}
	
	public void testPredicateStrictAsFlwrPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);
			assocaition.createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = "FOR $var IN // tm:subject WHERE myTopic ( roleType : $var ) RETURN $var";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	public void testPredicateNonStrictAsSelectPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			if (i % 2 == 0) {
				assocaition.createRole(createTopic(), createTopic());
			}
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;
		
		query = "SELECT $var WHERE myTopic ( roleType : $var , ... )";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}
	
	public void testPredicateNonStrictAsPathPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			if (i % 2 == 0) {
				assocaition.createRole(createTopic(), createTopic());
			}
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;
		
		query = " // tm:subject [ myTopic ( roleType : . , ...) ]";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}
	
	public void testPredicateNonStrictAsFlwrPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			if (i % 2 == 0) {
				assocaition.createRole(createTopic(), createTopic());
			}
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = "FOR $var IN // tm:subject WHERE myTopic ( roleType : $var , ... ) RETURN $var";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}		
	}

}
