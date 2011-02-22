package de.topicmapslab.tmql4j.tests.jtm2iresult;

import org.junit.Before;
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.query.IQuery;


public abstract class AbstractTest {

	private TopicMapSystem system;
	private ITopicMap topicMap;

	public AbstractTest() throws Exception {
		this.system = TopicMapSystemFactory.newInstance().newTopicMapSystem();
	}
	
	@Before
	public void beforeTest() throws Exception{
		this.topicMap = (ITopicMap)this.system.createTopicMap("http://test.tm");
	}
	
	protected ITopicMap getTopicMap(){
		return this.topicMap;
	}
	
	protected SimpleResultSet createResultSet(){
		return new SimpleResultSet(this.system, this.topicMap);
	}
	
		
}
