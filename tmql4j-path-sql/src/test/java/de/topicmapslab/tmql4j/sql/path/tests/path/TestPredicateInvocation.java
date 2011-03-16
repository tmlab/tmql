package de.topicmapslab.tmql4j.sql.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

public class TestPredicateInvocation extends Tmql4JTestCase {

	

	@Test	
	@Ignore
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

	@Test
	@Ignore
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
	
	@Test
	@Ignore
	public void testAkoPredicateInvocation(){		
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			topics.add(t);
			addSupertype(t, supertype);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject [ tm:subclass-of ( tm:subclass : . , tm:superclass : mySupertype ) ]";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof Topic);
			assertTrue(topics.contains(r.first() ));			
		}
	}
	
	@Test
	public void testAkoPredicateInvocationNCL(){		
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			topics.add(t);
			addSupertype(t, supertype);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject [ . AKO mySupertype ]";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof Topic);
			assertTrue(topics.contains(r.first() ));			
		}
	}
	
	@Test
	@Ignore
	public void testIsaPredicateInvocation(){		
		Topic type = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			topics.add(t);
			t.addType(type);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject [ tm:type-instance ( tm:instance : . , tm:type : myType ) ]";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof Topic);
			assertTrue(topics.contains(r.first() ));			
		}
	}
	
	@Test
	public void testIsaPredicateInvocationNCL(){		
		Topic type = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			topics.add(t);
			t.addType(type);
		}
		String query = null;
		SimpleResultSet set = null;

		query = "// tm:subject [ . ISA myType ]";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		
		for ( IResult r : set){
			assertEquals(1, r.size());
			assertTrue(r.first() instanceof Topic);
			assertTrue(topics.contains(r.first() ));			
		}
	}

}
