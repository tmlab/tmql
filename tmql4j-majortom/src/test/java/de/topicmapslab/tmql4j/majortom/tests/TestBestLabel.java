/**
 * 
 */
package de.topicmapslab.tmql4j.majortom.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;

/**
 * @author Hannes Niederhausen
 * 
 */
public class TestBestLabel extends Tmql4JTestCase {

	@Test
	public void smalltest() {
		Topic t = createTopicBySI("testtopic");
		Topic t2 = createTopicBySI("testtopic2");
		t.createName("Hans Wurst");
		t2.createName("Meyer");

		getBestLabel(t);

		System.out.println(execute("fn:best-label( // tm:subject )"));
		
		
	}

	private String getBestLabel(Topic topic) {
		String queryString = "fn:best-label( \"" + topic.getId() + "\" << id )";
		SimpleResultSet rs = execute(queryString);
		
		if (rs.isEmpty())
			return null;
		return rs.get(0, 0);
	}

	private String getBestLabel(Topic topic, Topic theme) {
		String queryString = "fn:best-label( \"" + topic.getId() + "\" << id, \"" + theme.getId() + "\" << id )";
		SimpleResultSet rs = execute(queryString);
		if (rs.isEmpty())
			return null;
		return rs.get(0, 0);
	}

	private String getBestLabel(Topic topic, Topic theme, boolean strict) {
		String queryString = "fn:best-label( \"" + topic.getId() + "\" << id, \"" + theme.getId() + "\" << id, \"" + Boolean.toString(strict) + "\" )";
		SimpleResultSet rs = execute(queryString);
		if (rs.isEmpty())
			return null;
			
		return rs.get(0, 0);
	}

	@Test
	public void bestLabel() {
		Locator si = createLocator("http://psi.example.org/si/testtopic");
		Locator oSi = createLocator("http://psi.example.org/si/testtopic");
		Locator sl = createLocator("http://psi.example.org/sl/testtopic");
		Locator oSl = createLocator("http://psi.example.org/sl/testtopic");
		Locator ii = createLocator("http://psi.example.org/ii/testtopic");
		Locator oIi = createLocator("http://psi.example.org/ii/testtopic");
		ITopic topic = (ITopic) topicMap.createTopicBySubjectIdentifier(si);
		topic.removeSubjectIdentifier(si);

		assertEquals("Best label should be the id", topic.getId(), getBestLabel(topic));

		topic.addItemIdentifier(ii);
		assertEquals("Best label should be the item-identifier", ii.getReference(), getBestLabel(topic));
		topic.addItemIdentifier(oIi);
		assertEquals("Best label should be the lexicographically smallest item-identifier", ii.getReference(), getBestLabel(topic));

		topic.addSubjectLocator(sl);
		assertEquals("Best label should be the subject-locator", sl.getReference(), getBestLabel(topic));
		topic.addSubjectLocator(oSl);
		assertEquals("Best label should be the lexicographically smallest subject-locator", sl.getReference(), getBestLabel(topic));

		topic.addSubjectIdentifier(si);
		assertEquals("Best label should be the subject-identifier", si.getReference(), getBestLabel(topic));
		topic.addSubjectIdentifier(oSi);
		assertEquals("Best label should be the lexicographically smallest subject-identifier", si.getReference(), getBestLabel(topic));

		Topic type = createTopic();
		Topic theme = createTopic();
		Topic otherTheme = createTopic();

		Name name1 = topic.createName("Name");
		assertEquals("Best label should be the name", name1.getValue(), getBestLabel(topic));
		Name name2 = topic.createName("NameZZZ");
		assertEquals("Best label should be the the lexicographically smallest name value", name1.getValue(), getBestLabel(topic));

		name1.setType(type);
		assertEquals("Best label should be the default name", name2.getValue(), getBestLabel(topic));
		name2.setType(type);
		assertEquals("Best label should be the default name", name1.getValue(), getBestLabel(topic));

		name1.addTheme(theme);
		assertEquals("Best label should be the name with the unconstained scope", name2.getValue(), getBestLabel(topic));
		name2.addTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));
		name2.setValue(name1.getValue());
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));

		name2.setValue("NameZZZ");
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));

		name2.removeTheme(otherTheme);
		name2.addTheme(theme);
		assertEquals("Best label should be the name with the unconstained scope", name1.getValue(), getBestLabel(topic));

		name1.addTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name2.getValue(), getBestLabel(topic));
		name2.addTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));

		name1.removeTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));
		name2.removeTheme(theme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));
		name2.setValue("A");
		assertEquals("Best label should be the name with the scope with the smallest number of themes and shortest value", name2.getValue(), getBestLabel(topic));
	}

	@Test
	public void BestLabelWithTheme() {
		Topic theme = createTopic();
		Topic otherTheme = createTopic();
		Topic newTheme = createTopic();
		Locator si = createLocator("http://psi.example.org/si/testtopic");
		Locator oSi = createLocator("http://psi.example.org/si/testtopic");
		Locator sl = createLocator("http://psi.example.org/sl/testtopic");
		Locator oSl = createLocator("http://psi.example.org/sl/testtopic");
		Locator ii = createLocator("http://psi.example.org/ii/testtopic");
		Locator oIi = createLocator("http://psi.example.org/ii/testtopic");
		ITopic topic = (ITopic) topicMap.createTopicBySubjectIdentifier(si);
		topic.removeSubjectIdentifier(si);

		assertEquals("Best label should be the id", topic.getId(), getBestLabel(topic));

		topic.addItemIdentifier(ii);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the item-identifier", ii.getReference(), getBestLabel(topic));
		topic.addItemIdentifier(oIi);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the lexicographically smallest item-identifier", ii.getReference(), getBestLabel(topic));

		topic.addSubjectLocator(sl);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the subject-locator", sl.getReference(), getBestLabel(topic));
		topic.addSubjectLocator(oSl);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the lexicographically smallest subject-locator", sl.getReference(), getBestLabel(topic));

		topic.addSubjectIdentifier(si);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the subject-identifier", si.getReference(), getBestLabel(topic));
		topic.addSubjectIdentifier(oSi);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the lexicographically smallest subject-identifier", si.getReference(), getBestLabel(topic));

		Topic type = createTopic();

		Name name1 = topic.createName("Name");
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the name", name1.getValue(), getBestLabel(topic));
		Name name2 = topic.createName("NameZZZ");
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the the lexicographically smallest name value", name1.getValue(), getBestLabel(topic));

		name1.setType(type);
		assertEquals("Best label should be the default name", name2.getValue(), getBestLabel(topic));
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		name2.setType(type);
		assertNull("Strict mode for best label should return null.", getBestLabel(topic, theme, true));
		assertEquals("Best label should be the default name", name1.getValue(), getBestLabel(topic));

		name1.addTheme(theme);
		assertEquals("Best label should be the name with the unconstained scope", name2.getValue(), getBestLabel(topic));
		assertEquals("Best label should be the name with the given theme scope", name1.getValue(), getBestLabel(topic, theme));
		name2.addTheme(theme);
		assertEquals("Best label should be the name with the unconstained scope", name1.getValue(), getBestLabel(topic));

		name1.addTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name2.getValue(), getBestLabel(topic));
		name2.addTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));

		name1.removeTheme(otherTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));
		name2.removeTheme(theme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes", name1.getValue(), getBestLabel(topic));
		name2.setValue("A");
		assertEquals("Best label should be the name with the scope with the smallest number of themes and shortest value", name2.getValue(), getBestLabel(topic));

		name1.addTheme(newTheme);
		name2.addTheme(newTheme);
		assertEquals("Best label should be the name with the scope with the smallest number of themes and shortest value", name2.getValue(), getBestLabel(topic, newTheme));
	}
}
