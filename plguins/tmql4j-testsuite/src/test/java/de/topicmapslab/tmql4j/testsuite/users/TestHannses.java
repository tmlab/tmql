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

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapix.io.XTM20TopicMapReader;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestHannses extends TestCase {

	public void testname() throws Exception {
		TopicMapSystem topicMapSystem = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();
		TopicMap topicMap = topicMapSystem
				.createTopicMap("http://de.topicmapslab/tmql4j/tests/");
		XTM20TopicMapReader reader = new XTM20TopicMapReader(topicMap,
				new File("src/test/resources/reifier.xtm"));
		reader.read();

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);

		final String query = "%prefix tmcl http://psi.topicmaps.org/tmcl/ "
				+ "FOR $c IN http://testmap.de/testoccmayreify << players >> players "
				+ "tmcl:constraint [ ^ tmcl:reifier-constraint ] "
				+ "WHERE ( $c / tmcl:card-min == 0 OR NOT $c / tmcl:card-min )AND $c / tmcl:card-max == 1 "
				+ "RETURN  $c >> traverse tmcl:allowed-reifier , $c / tmcl:card-min , $c / tmcl:card-max";

		IQuery q = runtime.run(query);
		System.out.println(q.getResults());
		System.out.println(q.getQueryString());

	}

	public void testMissingTopic() throws Exception {
		TopicMapSystem topicMapSystem = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();
		TopicMap topicMap = topicMapSystem
				.createTopicMap("http://de.topicmapslab/tmql4j/tests/");
		XTM20TopicMapReader reader = new XTM20TopicMapReader(topicMap,
				new File("src/test/resources/reifier.xtm"));
		reader.read();

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);

		final String query = "%prefix tmcl http://psi.topicmaps.org/tmcl/ "
				+ "FOR $c IN http://testmap.de/testoccmasdasdasdayreify << players >> players "
				+ "tmcl:constraint [ ^ tmcl:reifier-constraint ] "
				+ "RETURN  $c >> traverse tmcl:allowed-reifier , $c / tmcl:card-min , $c / tmcl:card-max";

		IQuery q = runtime.run(query);
		System.out.println(q.getResults());
	}

	public void testCannotReify() throws Exception {
		TopicMapSystem topicMapSystem = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();
		TopicMap topicMap = topicMapSystem
				.createTopicMap("http://de.topicmapslab/tmql4j/tests/");
		XTM20TopicMapReader reader = new XTM20TopicMapReader(topicMap,
				new File("src/test/resources/reifier.xtm"));
		reader.read();

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);

		final String query = "%prefix tmcl http://psi.topicmaps.org/tmcl/ "
				+ "FOR $c IN // tmcl:topic-reifies-constraint "
				+ "	WHERE "
				+ "		tmcl:constrained-topic-type ( tmcl:constraint : $c, tmcl:constrained : http://testmap.de/testreifier ) "
				+ "	AND "
				+ "		( "
				+ "			tmcl:constrained-statement ( tmcl:constraint : $c, tmcl:constrained : $st ) "
				+ "		OR "
				+ "			( "
				+ "				$c / tmcl:card-min == 0 "
				+ "			AND "
				+ "				$c / tmcl:card-max == 0 "
				+ "			) "
				+ "		) "
				+ "RETURN ( $st || \"tmdm:subject\", $c / tmcl:card-min || 0 ,$c / tmcl:card-max || \"*\" ) ";

		IQuery q = runtime.run(query);
		System.out.println(q.getResults());
		System.out.println(q.getQueryString());
	}

}
