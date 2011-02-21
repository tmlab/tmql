package de.topicmapslab.tmql4j.tests.jtm2iresult;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.tmapi.core.Association;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.JTMQRReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

public class Test {

	@org.junit.Test
	public void developerTest() throws Exception{
		
		TopicMapSystem tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		TopicMap tm = tms.createTopicMap("http://test.tm");
		SimpleResultSet resultSet = new SimpleResultSet(tms, tm);
		
		// create themes
		Topic theme1 = tm.createTopic();
		Topic theme2 = tm.createTopic();
		Topic theme3 = tm.createTopic();
		Topic theme4 = tm.createTopic();
		Topic theme5 = tm.createTopic();
		Topic type = tm.createTopic();
		
		// create a topic
		Topic topic = tm.createTopic();
		Name name = topic.createName("name value");
		name.addTheme(theme1);
		name.addTheme(theme2);
		Variant variant1 = name.createVariant("variant value 1", theme3);
		variant1.setReifier(tm.createTopic());
		Variant variant2 = name.createVariant("variant value 2", theme4,theme5);

		Name name2 = topic.createName("name value 2");
		name2.setReifier(tm.createTopic());
		
		Occurrence occ = topic.createOccurrence(type, "occurrence value");
		occ.addTheme(theme1);
		occ.addTheme(theme2);
		
		Occurrence occ2 = topic.createOccurrence(type, "occurrence value");
		occ2.setValue(123456);

		Topic assType = tm.createTopic();
		Topic roleType1 = tm.createTopic();
		Topic roleType2 = tm.createTopic();
		
		Topic player1 = tm.createTopic();
		Topic player2 = tm.createTopic();
		Topic player3 = tm.createTopic();
		
		Association ass = tm.createAssociation(assType);
		Role role1 = ass.createRole(tm.createTopic(), tm.createTopic());
		Role role2 = ass.createRole(tm.createTopic(), tm.createTopic());
		role1.setReifier(tm.createTopic());

		ass.addTheme(theme1);
		ass.addTheme(theme2);
		ass.setReifier(tm.createTopic());
		
		Association ass2 = tm.createAssociation(assType);
		ass2.createRole( tm.createTopic(),  tm.createTopic() );
		
		SimpleResult result = new SimpleResult(resultSet);
		result.add(topic);
		resultSet.addResult(result);
		
		result = new SimpleResult(resultSet);
		result.add(variant2);
		resultSet.addResult(result);
		
//		
//		result = new SimpleResult(resultSet);
//		result.add(ass2);
//		resultSet.addResult(result);


		File file = new File("/tmp/test.jtm");
		if(file.exists())
			file.delete();

		OutputStream out = new FileOutputStream(file);
		resultSet.toJTMQR(out);
		
		InputStream in = new FileInputStream(file);
			
		JTMQRReader reader = new JTMQRReader(in);
		reader.readResultSet();
		
		resultSet.toJTMQR(System.out);
	}
	
	
	
}
