package de.topicmapslab.tmql4j.tests.jtmqr.integration;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.tests.jtmqr.with_TopicMap;

import org.junit.Before;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapExistsException;
import org.tmapix.io.XTMTopicMapReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: mhoyer Created: 28.10.2010 02:31:51
 */
public class with_TMQL_runtime_on_ToyTM extends with_TopicMap {
	protected ITMQLRuntime runtime;
	protected TopicMap toytm;

	@Before
	public void given_a_TQML_runtime() throws TopicMapExistsException, IOException {
		toytm = tms.createTopicMap("http://" + getClass().getSimpleName());
		InputStream toytmStream = ClassLoader.getSystemResourceAsStream("toytm.xtm");
		new XTMTopicMapReader(toytm, toytmStream, "http://toytm").read();

		runtime = TMQLRuntimeFactory.newFactory().newRuntime();
	}

	protected IResultSet<?> query(String query) {
		IQuery queryResult = runtime.run(toytm, query);
		return queryResult.getResults();
	}
}
