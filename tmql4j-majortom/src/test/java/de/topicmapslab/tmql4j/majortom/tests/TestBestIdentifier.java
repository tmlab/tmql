/**
 * 
 */
package de.topicmapslab.tmql4j.majortom.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestBestIdentifier extends Tmql4JTestCase {

	/**
	 * Utility method builds the query, execute them and return results
	 * 
	 * @param topic
	 *            the topic
	 * @param withPrefix
	 *            flag of {@link ITopic#getBestIdentifier(boolean)}
	 * @return the best identifier or <code>null</code>
	 */
	private String getBestIdentifier(Topic topic, boolean withPrefix) {
		String queryString = "fn:best-identifier( \"" + topic.getId() + "\" << id , \"" + Boolean.toString(withPrefix) + "\")";
		SimpleResultSet rs = execute(queryString);

		if (rs.isEmpty())
			return null;
		return rs.get(0, 0);
	}

	@Test
	public void bestIdentifier() {
		String refSi = "si/topic";
		Locator si = createLocator(refSi);
		ITopic topic = (ITopic) topicMap.createTopicBySubjectIdentifier(si);
		topic.removeSubjectIdentifier(si);

		assertEquals(topic.getId(), getBestIdentifier(topic, false));
		assertEquals("id:" + topic.getId(), getBestIdentifier(topic, true));

		String refOSi = "si/opicc";
		Locator oSi = createLocator(refOSi);
		String refSl = "sl/topic";
		Locator sl = createLocator(refSl);
		String refOSl = "sl2/topic";
		Locator oSl = createLocator(refOSl);
		String refIi = "ii/topic";
		Locator ii = createLocator(refIi);
		String refOIi = "ii2/topic";
		Locator oIi = createLocator(refOIi);

		topic.addItemIdentifier(oIi);
		assertEquals(base + refOIi, getBestIdentifier(topic, false));
		assertEquals("ii:" + base + refOIi, getBestIdentifier(topic, true));

		topic.addItemIdentifier(ii);
		assertEquals(base + refIi, getBestIdentifier(topic, false));
		assertEquals("ii:" + base + refIi, getBestIdentifier(topic, true));

		topic.addSubjectLocator(oSl);
		assertEquals(base + refOSl, getBestIdentifier(topic, false));
		assertEquals("sl:" + base + refOSl, getBestIdentifier(topic, true));

		topic.addSubjectLocator(sl);
		assertEquals(base + refSl, getBestIdentifier(topic, false));
		assertEquals("sl:" + base + refSl, getBestIdentifier(topic, true));

		topic.addSubjectIdentifier(si);
		assertEquals(base + refSi, getBestIdentifier(topic, false));
		assertEquals("si:" + base + refSi, getBestIdentifier(topic, true));

		topic.addSubjectIdentifier(oSi);
		assertEquals(base + refOSi, getBestIdentifier(topic, false));
		assertEquals("si:" + base + refOSi, getBestIdentifier(topic, true));
	}
}
