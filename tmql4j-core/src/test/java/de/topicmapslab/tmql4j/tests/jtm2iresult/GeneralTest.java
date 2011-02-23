package de.topicmapslab.tmql4j.tests.jtm2iresult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.junit.Test;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.JTMQRReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

public class GeneralTest extends AbstractTest {

	public GeneralTest() throws Exception {
		super();
	}

	@Test
	public void testTest() throws Exception{
		
		ITopicMap map = getTopicMap();
		
		Topic t = map.createTopicBySubjectIdentifier(map.createLocator("http://lala/topic/si/1"));
		Name n = t.createName("Lala");
		
		t.addSubjectIdentifier(map.createLocator("http://lala/topic/si/2"));
		
		t.addSubjectLocator(map.createLocator("http://lala/topic/sl/1"));
		
		t.addItemIdentifier(map.createLocator("http://lala/topic/ii/1"));
		t.addItemIdentifier(map.createLocator("http://lala/topic/ii/2"));
		
		SimpleResultSet rs = createResultSet();
		
		SimpleResult r = new SimpleResult(rs);
		r.add(t);
		r.add(n);
		r.add("Test String");
		rs.addResult(r);
		
		
		Map<String, Integer> alias = new HashMap<String, Integer>();
		alias.put("topic", 0);
		alias.put("string", 2);
				
		Map<Integer, String> indexes = new HashMap<Integer, String>();
		indexes.put(0, "topic");
		indexes.put(2, "string");
		
		
		rs.setAlias(alias);
		rs.setIndexes(indexes);
		
		System.out.println(rs);

		System.out.println(convert(rs));
		
		
	}
	
	
}
