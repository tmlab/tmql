/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.other;

import junit.framework.TestCase;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.TmdmSubjectIdentifier;


/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
public class TestPragma extends TestCase{

	public void testPragmaOfTypeTransitivity() throws Exception {
		TopicMap map = TopicMapSystemFactory.newInstance().newTopicMapSystem().createTopicMap("http://psi.example.org");
		
		Topic akoType = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
		Topic rSupertype = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		Topic rSubtype = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		
		Topic topic = map.createTopicBySubjectIdentifier(map.createLocator("http://psi.example.org/topic"));
		Topic type = map.createTopicBySubjectIdentifier(map.createLocator("http://psi.example.org/type"));
		Topic supertype = map.createTopicBySubjectIdentifier(map.createLocator("http://psi.example.org/supertype"));
		Topic supersupertype = map.createTopicBySubjectIdentifier(map.createLocator("http://psi.example.org/supersupertype"));
		
		// topic isa type
		topic.addType(type);
		// type ako supertype
		Association a = map.createAssociation(akoType, new Topic[0]);
		a.createRole(rSubtype, type);
		a.createRole(rSupertype, supertype);
		// supertype ako supersupertype
		a = map.createAssociation(akoType, new Topic[0]);
		a.createRole(rSubtype, supertype);
		a.createRole(rSupertype, supersupertype);
		
		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(map);
		// for types
		IQuery query = null;
		query = runtime.run("%pragma taxonometry tm:intransitive http://psi.example.org/topic >> types");
		assertEquals(1, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(type));
		
		query = runtime.run("%pragma taxonometry tm:transitive http://psi.example.org/topic >> types");
		assertEquals(3, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(type));
//		assertTrue(query.getResults().getResults().contains(supertype));
//		assertTrue(query.getResults().getResults().contains(supersupertype));
		
		// for instances
		query = runtime.run("%pragma taxonometry tm:intransitive http://psi.example.org/supersupertype >> instances");
		assertEquals(0, query.getResults().size());
		
		query = runtime.run("%pragma taxonometry tm:transitive http://psi.example.org/supersupertype >> instances");
		assertEquals(1, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(topic));
		
		//for supertypes
		query = runtime.run("%pragma taxonometry tm:intransitive http://psi.example.org/type >> supertypes");
		assertEquals(1, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(supertype));
		
		query = runtime.run("%pragma taxonometry tm:transitive http://psi.example.org/type >> supertypes");
		assertEquals(2, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(supersupertype));
//		assertTrue(query.getResults().getResults().contains(supertype));
		
		//for subtypes
		query = runtime.run("%pragma taxonometry tm:intransitive http://psi.example.org/supersupertype >> subtypes");
		assertEquals(1, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(supertype));
		
		query = runtime.run("%pragma taxonometry tm:transitive http://psi.example.org/supersupertype >> subtypes");
		assertEquals(2, query.getResults().size());
//		assertTrue(query.getResults().getResults().contains(type));
//		assertTrue(query.getResults().getResults().contains(supertype));
	}
	
}
