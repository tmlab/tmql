package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

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
 * class to test roles created from jtmqr result
 * @author Christian Haß
 *
 */
public class TestRoles extends AbstractTest {

	/**
	 * checks if the type is correct
	 */
	@Test
	public void testType(){
		
		ITopicMap map = getTopicMap();
		String typeSi = "http://test/role/type";
		Association assoc = map.createAssociation(map.createTopic());
		Role role = assoc.createRole(map.createTopicBySubjectIdentifier(map.createLocator(typeSi)), map.createTopic());
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(role);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Role r = outR.get(0);
		assertNotNull(r);
		assertNull(r.getParent());
		
		Topic t = r.getType();
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
		Role role = assoc.createRole(map.createTopic(), map.createTopic());
				
		String ii1 = "http://role/ii/one";
		String ii2 = "http://role/ii/two";
		role.addItemIdentifier(map.createLocator(ii1));
		role.addItemIdentifier(map.createLocator(ii2));
		
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(role);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Role r = outR.get(0);
		assertNotNull(r);
		assertNull(r.getParent());
		
		assertEquals(2, r.getItemIdentifiers().size());
		assertTrue(r.getItemIdentifiers().contains(new LocatorImpl(ii1)));
		assertTrue(r.getItemIdentifiers().contains(new LocatorImpl(ii2)));
		
	}
	
	/**
	 * checks reifier
	 */
	@Test
	public void checkReifier(){
		
		ITopicMap map = getTopicMap();
		
		Association assoc = map.createAssociation(map.createTopic());
		Role role = assoc.createRole(map.createTopic(), map.createTopic());
		
		String reifierSi = "http://reifier";
		
		role.setReifier(map.createTopicBySubjectIdentifier(map.createLocator(reifierSi)));
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(role);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Role r = outR.get(0);
		assertNotNull(r);
		assertNull(r.getParent());
		
		Topic reifier = r.getReifier();
		assertNotNull(reifier);
		
		assertEquals(1, reifier.getSubjectIdentifiers().size());
		assertTrue(reifier.getSubjectIdentifiers().contains(new LocatorImpl(reifierSi)));
		
	}
	
	/**
	 * check player
	 */
	@Test
	public void checkPlayer(){
		
		ITopicMap map = getTopicMap();
		Association assoc = map.createAssociation(map.createTopic());
		String playerSi = "http://test/player";
		Role role = assoc.createRole(map.createTopic(), map.createTopicBySubjectIdentifier(map.createLocator(playerSi)));
				
		SimpleResultSet inRS = createResultSet();
		SimpleResult inR = new SimpleResult(inRS);
		inR.add(role);
		inRS.addResult(inR);
		
		IResultSet<?> outRS = convert(inRS);
		assertFalse(outRS.isEmpty());
		assertEquals(1, outRS.size());
		IResult outR = outRS.get(0);
		assertEquals(1, outR.size());
		Role r = outR.get(0);
		assertNotNull(r);
		assertNull(r.getParent());
		
		Topic p = r.getPlayer();
		assertEquals(1, p.getSubjectIdentifiers().size());
		assertTrue(p.getSubjectIdentifiers().contains(new LocatorImpl(playerSi)));
	}
	
}
