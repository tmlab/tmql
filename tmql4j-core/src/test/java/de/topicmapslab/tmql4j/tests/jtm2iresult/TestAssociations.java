package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.core.LocatorImpl;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * test class for associations created from a jtmqr result
 * @author Christian Haß
 *
 */
public class TestAssociations extends AbstractTest {

	/**
	 * checks if the type is correct
	 */
	@Test
	public void testType(){
		
		ITopicMap map = getTopicMap();
		String typeSi = "http://test/association/type";
		Association assoc = map.createAssociation(map.createTopicBySubjectIdentifier(map.createLocator(typeSi)));
		assoc.createRole(map.createTopic(), map.createTopic());
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(assoc);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Association a = outR.get(0);
		assertNotNull(a);
		assertNull(a.getParent());
		
		Topic t = a.getType();
		assertEquals(1, t.getSubjectIdentifiers().size());
		assertTrue(t.getSubjectIdentifiers().contains(new LocatorImpl(typeSi)));
	}
	
	/**
	 * checks item identifier
	 */
	@Test
	public void testItemIdentifier(){
		
		ITopicMap map = getTopicMap();
		
		Association assoc = map.createAssociation(map.createTopic());
		assoc.createRole(map.createTopic(), map.createTopic());
				
		String ii1 = "http://name/ii/one";
		String ii2 = "http://name/ii/two";
		assoc.addItemIdentifier(map.createLocator(ii1));
		assoc.addItemIdentifier(map.createLocator(ii2));
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(assoc);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Association a = outR.get(0);
		assertNotNull(a);
		assertNull(a.getParent());
		
		assertEquals(2, a.getItemIdentifiers().size());
		assertTrue(a.getItemIdentifiers().contains(new LocatorImpl(ii1)));
		assertTrue(a.getItemIdentifiers().contains(new LocatorImpl(ii2)));
		
	}
	
	/**
	 * checks the scope
	 */
	@Test
	public void checkScope(){

		ITopicMap map = getTopicMap();
				
		// test one theme
		Association assoc1 = map.createAssociation(map.createTopic());
		assoc1.createRole(map.createTopic(), map.createTopic());
		
		String themeSi = "http://theme";
		assoc1.addTheme(map.createTopicBySubjectIdentifier(map.createLocator(themeSi)));
		
		// test 2 themes
		Association assoc2 = map.createAssociation(map.createTopic());
		assoc2.createRole(map.createTopic(), map.createTopic());
		assoc2.addTheme(map.createTopic());
		assoc2.addTheme(map.createTopic());
			
		// test no scope
		Association assoc3 = map.createAssociation(map.createTopic());
		assoc3.createRole(map.createTopic(), map.createTopic());
			
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(assoc1);
		inR.add(assoc2);
		inR.add(assoc3);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(3, outR.size());
		
		Association a1 = outR.get(0);
		Association a2 = outR.get(1);
		Association a3 = outR.get(2);
		
		assertNotNull(a1);
		assertNotNull(a2);
		assertNotNull(a3);
		
		assertNull(a1.getParent());
		assertNull(a2.getParent());
		assertNull(a3.getParent());
		
		Set<Topic> themes1 = a1.getScope();
		assertNotNull(themes1);
		assertEquals(1, themes1.size());
		assertTrue(themes1.iterator().next().getSubjectIdentifiers().contains(new LocatorImpl(themeSi)));

		Set<Topic> themes2 = a2.getScope();
		assertNotNull(themes2);
		assertEquals(2, themes2.size());
		
		Set<Topic> themes3 = a3.getScope();
		assertNotNull(themes3);
		assertEquals(0, themes3.size());
		
	}
	
	/**
	 * checks reifier
	 */
	@Test
	public void checkReifier(){
		
		ITopicMap map = getTopicMap();
		
		Association assoc = map.createAssociation(map.createTopic());
		assoc.createRole(map.createTopic(), map.createTopic());
		
		String reifierSi = "http://reifier";
		
		assoc.setReifier(map.createTopicBySubjectIdentifier(map.createLocator(reifierSi)));
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(assoc);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Association a = outR.get(0);
		assertNotNull(a);
		assertNull(a.getParent());
		
		Topic reifier = a.getReifier();
		assertNotNull(reifier);
		
		assertEquals(1, reifier.getSubjectIdentifiers().size());
		assertTrue(reifier.getSubjectIdentifiers().contains(new LocatorImpl(reifierSi)));
		
	}
	
	/**
	 * check roles
	 */
	@Test
	public void checkRoles(){
			
		ITopicMap map = getTopicMap();
		Association assoc = map.createAssociation(map.createTopic());
		
		String roleType1Si = "http://roletype/one";
		String player1Si = "http://player/one";

		Topic roletype1 = map.createTopicBySubjectIdentifier(map.createLocator(roleType1Si));
		Topic player1 = map.createTopicBySubjectIdentifier(map.createLocator(player1Si));

		assoc.createRole(roletype1, player1);

		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(assoc);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Association a = outR.get(0);
		assertNotNull(a);
		assertNull(a.getParent());
			
		Set<Role> roles = a.getRoles();
		assertNotNull(roles);
		assertEquals(1, roles.size());
		
		assertTrue(roles.iterator().next().getType().getSubjectIdentifiers().contains(new LocatorImpl(roleType1Si)));
		assertTrue(roles.iterator().next().getPlayer().getSubjectIdentifiers().contains(new LocatorImpl(player1Si)));

	}
	
	
	
	
}
