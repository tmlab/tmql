package de.topicmapslab.tmql4j.flwr.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import static junit.framework.Assert.*;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

public class TestSetOperations extends Tmql4JTestCase {

	
	@Test
	public void testMinus(){
		Topic type = createTopicBySI("type");
		Topic otype = createTopicBySI("otype");	
		Topic ntype = createTopicBySI("ntype");	
		List<Topic> results = new ArrayList<Topic>();
		for ( int i = 0 ; i < 100 ; i++){
			Topic t = createTopicBySI("topicN" + i);
			t.addType(type);
			if ( i % 2 == 0 ){
				Name n = t.createName("Name");
				if ( i % 3 == 0){
					t.addType(otype);
					results.add(t);
				}else{
					n.setType(ntype);
				}
			}
		}
		
		IResultSet<?> set;
		String query;
		
		query = "FOR $t IN // type RETURN $t";
		set = execute(query);
		assertEquals(100	, set.size());
		
		query = "FOR $t IN // type [ ^ otype ] RETURN $t";
		set = execute(query);
		assertEquals(17	, set.size());
		
		query = "FOR $t IN // type [ . >> characteristics >> types MINUS ntype ] RETURN $t";
		set = execute(query);
		assertEquals(17	, set.size());
		
		Topic t = results.get(0);
		results.remove(t);
		
		
		query = "FOR $t IN // type [ . >> types == otype AND . >> characteristics >> types MINUS ntype ] MINUS \"" + t.getSubjectIdentifiers().iterator().next().getReference() +"\" << indicators RETURN $t";
		set = execute(query);
		assertEquals(results.size()	, set.size());
	}
}
