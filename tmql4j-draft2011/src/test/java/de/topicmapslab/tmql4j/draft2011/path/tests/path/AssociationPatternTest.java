package de.topicmapslab.tmql4j.draft2011.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Test class for association pattern
 * 
 * @author Sven Krosse
 * 
 */
public class AssociationPatternTest extends Tmql4JTestCase {

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFullQualifiedAssociationPattern() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic rType1 = createTopicBySI("rType1");
		Topic rType2 = createTopicBySI("rType2");

		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			Association a = topicMap.createAssociation(type);
			a.createRole(rType1, topic);
			a.createRole(rType2, t);
			topics.add(t);
		}

		query = " myTopic / myType ( rType1 -> rType2 )  ";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAssociationPatternWithoutOtherRole() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");
		Topic rType1 = createTopicBySI("rType1");

		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			Association a = topicMap.createAssociation(type);
			a.createRole(rType1, topic);
			a.createRole(createTopic(), t);
			topics.add(t);
		}

		query = " myTopic / myType ( rType1 -> )  ";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	/**
	 * TEST METHOD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAssociationPatternWithoutRoles() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");

		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			Association a = topicMap.createAssociation(type);
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), t);
			topics.add(t);
		}

		query = " myTopic / myType ( -> )  ";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

}
