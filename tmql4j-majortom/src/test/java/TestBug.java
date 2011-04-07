//import org.junit.Test;
//import org.tmapi.core.TopicMap;
//import org.tmapi.core.TopicMapSystemFactory;
//
//import de.topicmapslab.jli.jdbc.configuration.IJliPropertyKeys;
//import de.topicmapslab.jli.jdbc.store.JliJdbcStore;
//import de.topicmapslab.majortom.store.TopicMapStoreProperty;
//import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
//import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
//import de.topicmapslab.tmql4j.path.components.processor.runtime.TmqlRuntime2007;
//import de.topicmapslab.tmql4j.query.IQuery;
//
///*
// * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
// * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
// *  
// * @author Sven Krosse
// * @email krosse@informatik.uni-leipzig.de
// *
// */
//
///**
// * @author Sven Krosse
// * 
// */
//public class TestBug {
//
//	@Test
//	public void testname() throws Exception {
//		TopicMapSystemFactory f = TopicMapSystemFactory.newInstance();
//		f.setProperty(TopicMapStoreProperty.TOPICMAPSTORE_CLASS, JliJdbcStore.class.getName());
//		f.setProperty(IJliPropertyKeys.MAPPING_FILE, "src/test/resources/author_database_114.xml");
//		TopicMap tm = f.newTopicMapSystem().createTopicMap("http://psi.lala.com");
//		System.out.println(tm.getTopics());
//		System.out.println("?");
//		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(TmqlRuntime2007.TMQL_2007);
//		String query = "FOR $var IN fn:get-topic-types()  RETURN fn:best-identifier($var, \"true\"), fn:best-label($var) ";
//		IQuery q = runtime.run(tm, query);
//		System.out.println(q.getResults());
//	}
//
// }
