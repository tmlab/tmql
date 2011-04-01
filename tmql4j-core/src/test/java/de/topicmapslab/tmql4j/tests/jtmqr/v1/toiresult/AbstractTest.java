package de.topicmapslab.tmql4j.tests.jtmqr.v1.toiresult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.JTMQRReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * abstract test class
 * 
 * @author Christian Haß
 * 
 */
public abstract class AbstractTest {

	private TopicMapSystem system;
	private ITopicMap topicMap;

	/**
	 * constructor
	 */
	public AbstractTest() {

		try {
			this.system = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs before each test and creates an empty topic map
	 */
	@Before
	public void beforeTest() {
		try {
			this.topicMap = (ITopicMap) this.system.createTopicMap("http://test.tm");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the topic map
	 */
	protected ITopicMap getTopicMap() {
		return this.topicMap;
	}

	/**
	 * @return an new and empty result set
	 */
	protected SimpleResultSet createResultSet() {
		return new SimpleResultSet(this.system, this.topicMap);
	}

	/**
	 * convertes an simple tmql result set via jtmqr to an jtmqr-resultset
	 * 
	 * @param input
	 *            - the simple result set
	 * @return the jtmqr-resultset
	 */
	protected IResultSet<?> convert(SimpleResultSet input) {
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
