/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.users;

import java.io.File;

import junit.framework.TestCase;

import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapi.index.TypeInstanceIndex;
import org.tmapix.io.CTMTopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.converter.QueryFactory;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestCaseXML extends TestCase {

	public void testLettersFromTMAPI() throws Exception {

		TopicMap topicMap = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem().createTopicMap("a:b");

		Topic type = topicMap.createTopicBySubjectIdentifier(topicMap
				.createLocator("a:b#letter"));

		for (char c : new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm' }) {
			Topic l = topicMap.createTopicBySubjectIdentifier(topicMap
					.createLocator("a:b#" + String.valueOf(c)));
			l.createName(String.valueOf(c), new Topic[0]);
			l.addType(type);
		}

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);
		IQuery q = QueryFactory.getFactory().getTmqlQuery(
				"a:b#letter >> instances");
		runtime.run(q);
		System.out.println(q.getResults());

		q = QueryFactory
				.getFactory()
				.getTmqlQuery(
						"RETURN <letters>{FOR $l in // a:b#letter RETURN <letter>{$l / tm:name}</letter>}</letters>");
		runtime.run(q);
		System.out.println(q.getResults());
		TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		for (Topic t : index.getTopics(topicMap
				.getTopicBySubjectIdentifier(topicMap
						.createLocator("a:b#letter")))) {
			for (Name n : t.getNames()) {
				System.out.println(n.getValue());
			}
		}
	}

	public void testLettersFromXTM() throws Exception {

		TopicMap topicMap = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem().createTopicMap("a:b");

		XTMTopicMapReader reader = new XTMTopicMapReader(topicMap, new File(
				"src/test/resources/letters.xtm"));
		reader.read();

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);
		IQuery q = QueryFactory.getFactory().getTmqlQuery(
				"a:bletter >> instances");
		runtime.run(q);
		System.out.println(q.getResults());

		q = QueryFactory
				.getFactory()
				.getTmqlQuery(
						"RETURN <letters>{FOR $l in // a:bletter RETURN <letter>{$l / tm:name}</letter>}</letters>");
		runtime.run(q);
		System.out.println(q.getResults());
	}

	public void testLettersFromCTM() throws Exception {

		TopicMap topicMap = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem().createTopicMap("a:b");

		CTMTopicMapReader reader = new CTMTopicMapReader(topicMap, new File(
				"src/test/resources/lettermap.ctm"));
		reader.read();

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);
		IQuery q = QueryFactory.getFactory().getTmqlQuery(
				"a:bletter >> instances");
		runtime.run(q);
		System.out.println(q.getResults());

		q = QueryFactory
				.getFactory()
				.getTmqlQuery(
						"RETURN <letters>{FOR $l in // a:bletter RETURN <letter>{$l / tm:name}</letter>}</letters>");
		runtime.run(q);
		System.out.println(q.getResults());
	}

}
