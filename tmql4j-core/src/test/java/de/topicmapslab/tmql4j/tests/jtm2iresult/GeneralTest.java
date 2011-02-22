package de.topicmapslab.tmql4j.tests.jtm2iresult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;
import org.tmapi.core.Topic;

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

	@org.junit.Test
	public void testTest() throws Exception{
		
		ITopicMap map = getTopicMap();
		
		Topic t = map.createTopicBySubjectIdentifier(map.createLocator("http://lala/topic"));
		t.createName("Lala");
		
		SimpleResultSet rs = createResultSet();
		
		SimpleResult r = new SimpleResult(rs);

		r.add(t);
		rs.addResult(r);

		System.out.println(convert(rs));
		
		
	}
	
	public IResultSet<?> convert(SimpleResultSet input) throws Exception{
		String jtmqrString = input.toJTMQR();
		InputStream in = new ByteArrayInputStream(jtmqrString.getBytes());
		
		JTMQRReader reader = new JTMQRReader(in);
		return reader.readResultSet();
		
		
	}
}
