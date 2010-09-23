/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sesametm;

import java.io.File;

import junit.framework.TestCase;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapix.io.LTMTopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestSesameTmQuery extends TestCase{

	private static TopicMapSystem topicMapSystem;
	protected static ITMQLRuntime runtime;

	protected static TopicMap topicMap;
	
	static {
		try {
			topicMapSystem = TopicMapSystemFactory.newInstance()
					.newTopicMapSystem();
			topicMap = topicMapSystem
					.createTopicMap("http://de.topicmapslab/tmql4j/tests/");
			XTMTopicMapReader reader = new XTMTopicMapReader(topicMap,
					new File("src/test/resources/out.xtm"));
			reader.read();

			runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMap);
			runtime.getProperties().enableLanguageExtensionTmqlUl(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private final String template = "%prefix o http://psi.ontopedia.net/ "
			+ "SELECT %subj / tm:name, %pred / tm:name, %obj / tm:name " + "WHERE " +
			// "$subj >> characteristics [ . >> types == %pred ] == $obj " +
			// "OR " +
			"tm:subject ( tm:subject : %subj , %pred : %obj , ...) ";

	public void testSPO() throws Exception {
		Locator subj = topicMap
				.createLocator("http://psi.ontopedia.net/Puccini");
		Locator pred = topicMap.createLocator("http://psi.ontopedia.net/Work");
		Locator obj = topicMap
				.createLocator("http://psi.ontopedia.net/La_Boheme");
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
	}

	public void testSPX() throws Exception {
		Locator subj = topicMap
				.createLocator("http://psi.ontopedia.net/Puccini");
		Locator pred = topicMap.createLocator("http://psi.ontopedia.net/Work");
		Locator obj = null;
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(12, set.size());
	}

	public void testSXX() throws Exception {
		Locator subj = topicMap
				.createLocator("http://www.topicmapslab.de/test/base/bert_0-0");
		Locator pred = null;
		Locator obj = null;
		String query = SparqlTmqlUtils.toPredicateQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(1, set.size());
		
		query = SparqlTmqlUtils.toCharacteristicsQuery(subj, pred, obj);
		System.out.println(query);
		set = execute(query);
		System.out.println(set);
		assertEquals(1, set.size());
	}

	public void testSXO() throws Exception {
		Locator subj = topicMap
				.createLocator("http://psi.ontopedia.net/Puccini");
		Locator pred = null;
		Locator obj = topicMap
				.createLocator("http://psi.ontopedia.net/La_Boheme");
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(1, set.size());
	}

	public void testXPO() throws Exception {
		Locator subj = null;
		Locator pred = topicMap.createLocator("http://psi.ontopedia.net/Work");
		Locator obj = topicMap
				.createLocator("http://psi.ontopedia.net/La_Boheme");
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(13, set.size());
	}

	public void testXPX() throws Exception {
		Locator subj = null;
		Locator pred = topicMap.createLocator("http://psi.ontopedia.net/Style");
		Locator obj = null;
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(6, set.size());
	}

	public void testXXO() throws Exception {
		Locator subj = null;
		Locator pred = null;
		Locator obj = topicMap
				.createLocator("http://psi.ontopedia.net/La_Boheme");
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(6, set.size());
	}

	public void testXXX() throws Exception {
		Locator subj = null;
		Locator pred = null;
		Locator obj = null;
		final String query = toQuery(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
		assertEquals(3662, set.size());
	}

	public String toQuery(Locator subj, Locator pred, Locator obj)
			throws Exception {
		String t = new String(template);

		if (subj != null) {
			t = t.replaceAll("%subj", subj.getReference());
		} else {
			t = t.replaceAll("%subj", "\\$subj");
		}
		if (pred != null) {
			t = t.replaceAll("%pred", pred.getReference());
		} else {
			t = t.replaceAll("%pred", "\\$pred");
		}
		if (obj != null) {
			t = t.replaceAll("%obj", obj.getReference());
		} else {
			t = t.replaceAll("%obj", "\\$obj");
		}
		return t;
	}

	public void test2() throws Exception {
		SimpleResultSet set = execute("%prefix o http://psi.ontopedia.net/ "
				+ "FOR $sub IN // tm:subject " + "RETURN {"
				+ " FOR $pred IN $sub >> characteristics >> types "
				+ " RETURN { "
				+ " FOR $obj IN $sub >> characteristics $pred >> atomify "
				+ " RETURN $sub, $pred, $obj" + " }" + " }");
		System.out.println(set);
	}

	public void testname() throws Exception {
		Locator subj = topicMap
				.createLocator("http://psi.ontopedia.net/Puccini");
		Locator pred = null;
		Locator obj = null;
		final String query = toQueryEx(subj, pred, obj);
		System.out.println(query);
		SimpleResultSet set = execute(query);
		System.out.println(set);
	}

	public String toQueryEx(Locator subj, Locator pred, Locator obj) {
		return SparqlTmqlUtils.toPredicateQuery(subj, pred, obj);

	}

	/*
	 * FOR $subj IN // tm:subject FOR $pred IN %subj >> charcateristics >> types
	 * FOR $obj IN %subj >> charcateristics >> atomify RETURN $subj, $pred, $obj
	 */
	private final String template2 = "FOR $pred IN %subj >> charcateristics >> types "
			+ "FOR $obj IN %subj >> charcateristics >> atomify "
			+ "RETURN $subj, $pred, $obj ";

	public String toQuery3(Locator subj, Locator pred, Locator obj)
			throws Exception {
		String t = new String(template2);

		if (subj != null) {
			t = t.replaceAll("%subj", subj.getReference());
		} else {
			t = t.replaceAll("%subj", "\\$subj");
		}
		if (pred != null) {
			t = t.replaceAll("%pred", pred.getReference());
		} else {
			t = t.replaceAll("%pred", "\\$pred");
		}
		if (obj != null) {
			t = t.replaceAll("%obj", obj.getReference());
		} else {
			t = t.replaceAll("%obj", "\\$obj");
		}
		return t;
	}
	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T execute(String query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		IQuery q = runtime.run(query);
		return (T) q.getResults();
	}

	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T execute(IQuery query)
			throws TMQLRuntimeException {
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				SimpleResultSet.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				SimpleTupleResult.class.getCanonicalName());
		runtime.getProperties().enableMaterializeMetaModel(true);
		runtime.run(query);
		return (T) query.getResults();
	}

}
