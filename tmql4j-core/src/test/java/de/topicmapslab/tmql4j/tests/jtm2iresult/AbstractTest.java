package de.topicmapslab.tmql4j.tests.jtm2iresult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.JTMQRReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.query.IQuery;


public abstract class AbstractTest {

	private TopicMapSystem system;
	private ITopicMap topicMap;

	public AbstractTest() {
		
		try {
			this.system = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Before
	public void beforeTest(){
		try {
			this.topicMap = (ITopicMap)this.system.createTopicMap("http://test.tm");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected ITopicMap getTopicMap(){
		return this.topicMap;
	}
	
	protected SimpleResultSet createResultSet(){
		return new SimpleResultSet(this.system, this.topicMap);
	}
	
	protected IResultSet<?> convert(SimpleResultSet input){
		try {
			String jtmqrString = input.toJTMQR();
			InputStream in = new ByteArrayInputStream(jtmqrString.getBytes());
			
			JTMQRReader reader = new JTMQRReader(in);
			return reader.readResultSet();
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
		
}
