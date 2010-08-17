/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j;

import java.util.Set;

import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.testsuite.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestMultipleCalls extends Tmql4JTestCase {

	public void testNCLAtomifyAxisTmName() throws Exception {

		TopicMapSystem system = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();
		TopicMap environment = system
				.createTopicMap("http://psi.example.org/enivronment");
		runtime.setEnvironmentMap(environment);

		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[100];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		SimpleResultSet set = null;

		for (int j = 1; j < 100000; j++) {

			System.out.print("Start " + j + ". iteration ... ");

			query = "myTopic / tm:name";
			set = execute(query);
			assertEquals(names.length, set.size());

			Set<String> result = HashUtil.getHashSet();
			for (IResult r : set.getResults()) {
				assertTrue(r.first() instanceof String);
				result.add((String) r.first());
			}

			for (int i = 0; i < names.length; i++) {
				assertTrue(result.contains(names[i].getValue()));
			}

			assertEquals("Environment map changed!", environment,
					((TMQLRuntime) runtime).getEnvironmentMap());
			assertEquals("Environment changed!", environment,
					((TMQLRuntime) runtime).getEnvironment().getTopicMap());

			System.out.println("Finished!");
		}
	}

}
