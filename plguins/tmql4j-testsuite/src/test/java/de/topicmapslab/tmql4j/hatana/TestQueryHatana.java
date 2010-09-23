/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.tmapix.io.CTMTopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.hatana.ondemand.MergedTopicMap;
import de.topicmapslab.hatana.ondemand.MergedTopicMapSystemFactory;
import de.topicmapslab.majortom.core.TopicMapSystemFactoryImpl;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;


/**
 * @author Sven Krosse
 *
 */
public class TestQueryHatana {

	MergedTopicMap hatanaMap;
	ITopicMap tm1, tm2,tm3, env;
	
	@Before
	public void init() throws Exception{
		hatanaMap = (MergedTopicMap)MergedTopicMapSystemFactory.newInstance().newTopicMapSystem().createTopicMap("http://psi.hatana.test/");
		
		tm1 = (ITopicMap)TopicMapSystemFactoryImpl.newInstance().newTopicMapSystem().createTopicMap("http://psi.example.org/1");
		if ( tm1.getStore().supportRevisionManagement()){
			tm1.getStore().enableRevisionManagement(false);
		}
		CTMTopicMapReader ctmReader = new CTMTopicMapReader(tm1, new File("src/test/resources/equal_identifiers.ctm"));
		ctmReader.read();
		hatanaMap.mergeIn(tm1);
		
		tm2 = (ITopicMap)TopicMapSystemFactoryImpl.newInstance().newTopicMapSystem().createTopicMap("http://psi.example.org/2");
		if ( tm2.getStore().supportRevisionManagement()){
			tm2.getStore().enableRevisionManagement(false);
		}
		XTMTopicMapReader xtmReader = new XTMTopicMapReader(tm2, new File("src/test/resources/opera.xtm"));
		xtmReader.read();
		hatanaMap.mergeIn(tm2);
		
		tm3 = (ITopicMap)TopicMapSystemFactoryImpl.newInstance().newTopicMapSystem().createTopicMap("http://psi.example.org/3");
		if ( tm3.getStore().supportRevisionManagement()){
			tm3.getStore().enableRevisionManagement(false);
		}
		xtmReader = new XTMTopicMapReader(tm3, new File("src/test/resources/norwegianoperas.xtm"));
		xtmReader.read();
		hatanaMap.mergeIn(tm3);		
		
		env = (ITopicMap)TopicMapSystemFactoryImpl.newInstance().newTopicMapSystem().createTopicMap("http://psi.example.org/env");
		if ( env.getStore().supportRevisionManagement()){
			env.getStore().enableRevisionManagement(false);
		}
		hatanaMap.registerTMQLEnvironmentMap(env);
	}
	
	@Test
	public void testQueryInstances() throws Exception {
		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(hatanaMap);
		IQuery q = runtime.run("http://psi.ontopedia.net/Opera >> instances");
		Assert.assertTrue(q.getResults().size() > 0);
		System.out.println(q.getResults());
	}
	
}
