package de.topicmapslab.tmql4j.testsuite.redmine;

import java.io.File;

import junit.framework.TestCase;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapix.io.CTMTopicMapReader;
import org.tmapix.io.LTMTopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.event.model.Event;
import de.topicmapslab.tmql4j.event.model.IEventListener;
import de.topicmapslab.tmql4j.extension.tmml.event.ModificationEvent;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

public class RedmineBugTests extends TestCase implements
		IEventListener<ModificationEvent> {

	private TopicMapSystem topicMapSystem;
	protected ITMQLRuntime runtime;
	private TopicMap topicMap;

	public <T extends IResultSet<?>> T execute(String query) throws Exception {
		return execute("src/test/resources/toyTM_after_spec.ctm", query);
	}

	public <T extends IResultSet<?>> T execute(final String file, String query)
			throws Exception {

		topicMapSystem = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();

		topicMap = topicMapSystem
				.createTopicMap("http://de.topicmapslab/tmql4j/tests/");	
		
		if (file.endsWith(".ctm")) {
			CTMTopicMapReader reader = new CTMTopicMapReader(topicMap,
					new File(file));
			reader.read();
		} else if (file.endsWith(".xtm")) {
			XTMTopicMapReader reader = new XTMTopicMapReader(topicMap,
					new File(file));
			reader.read();
		} else if (file.endsWith(".ltm")) {
			LTMTopicMapReader reader = new LTMTopicMapReader(topicMap,
					new File(file));
			reader.read();
		}

		return execute(topicMap, query);

	}

	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T execute(final TopicMap topicMap,
			String query) throws TMQLRuntimeException {

		runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem,
				topicMap);
		runtime.getEventManager().addEventListener(this);
		runtime.getProperties().enableMaterializeMetaModel(true);
		runtime.getProperties().enableLanguageExtensionTmqlUl(true);

		IQuery q = runtime.run(query);
		return (T) q.getResults();
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

	@Override
	protected void tearDown() throws Exception {
		topicMap.remove();
		topicMapSystem.close();
	}

	public TopicMap getTopicMap() {
		return topicMap;
	}
	
}
