/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.base;

import java.io.File;

import junit.framework.TestCase;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapix.io.LTMTopicMapReader;
import org.tmapix.io.XTM20TopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.event.model.Event;
import de.topicmapslab.tmql4j.event.model.IEventListener;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BaseTest extends TestCase implements
		IEventListener<Event> {

	private static TopicMapSystem topicMapSystem;
	protected static ITMQLRuntime runtime;

	protected static TopicMap topicMap;
	
	static {		
		try {
			topicMapSystem = TopicMapSystemFactory.newInstance()
					.newTopicMapSystem();
			topicMap = topicMapSystem
					.createTopicMap("http://de.topicmapslab/tmql4j/tests/");
			XTMTopicMapReader reader = new XTMTopicMapReader(topicMap,
					new File("src/test/resources/opera.xtm"));
//			LTMTopicMapReader reader = new LTMTopicMapReader(topicMap,
//					new File("src/test/resources/ItalianOpera.ltm"));
			reader.read();

			runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMap);
			runtime.getProperties().enableLanguageExtensionTmqlUl(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseTest() {
		runtime.getEventManager().addEventListener(this);
	}

	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T execute(String query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		IQuery q = runtime.run(query);
		return (T) q.getResults();
	}

	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T execute(IQuery query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		runtime.run(query);
		return (T) query.getResults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.topicmapslab.tmql4j.event.model.IEventListener#event(de.topicmapslab
	 * .tmql4j.event.model.Event)
	 */
	public void event(Event event) {
		System.out.println(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.topicmapslab.tmql4j.event.model.IEventListener#isInterested(de.
	 * topicmapslab.tmql4j.event.model.Event)
	 */
	public boolean isInterested(Event event) {
		return true;
	}
}
