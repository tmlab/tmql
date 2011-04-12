/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana.tests;

import static junit.framework.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.hatana.query.HatanaQueryProcessor;
import de.topicmapslab.tmql4j.hatana.query.transformer.IPSIRegistry;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestQueryTransformer {

	public HatanaQueryProcessor transformer;
	public IPSIRegistry registry = new IPSIRegistry() {

		public List<Set<String>> getIdentifiersBySubjectIdentifier(String identifier) {
			List<Set<String>> list = new LinkedList<Set<String>>();
			Set<String> set = HashUtil.getHashSet();
			set.add(identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add("sl-" + identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add("ii-" + identifier);
			list.add(set);
			return list;
		}

		public List<Set<String>> getIdentifiersBySubjectLocator(String identifier) {
			List<Set<String>> list = new LinkedList<Set<String>>();
			Set<String> set = HashUtil.getHashSet();
			set.add("si-" + identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add(identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add("ii-" + identifier);
			list.add(set);
			return list;
		}

		public List<Set<String>> getIdentifiersByItemIdentifier(String identifier) {
			List<Set<String>> list = new LinkedList<Set<String>>();
			Set<String> set = HashUtil.getHashSet();
			set.add("si-" + identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add("sl-" + identifier);
			list.add(set);
			set = HashUtil.getHashSet();
			set.add(identifier);
			list.add(set);
			return list;
		}
	};

	@Before
	public void setUp() {
		transformer = new HatanaQueryProcessor();
	}

	@Test
	public void testAxisFilter() throws Exception {

		String query = "/ topic::topic-type";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		assertEquals(
				"/ topic :: * [ . / type :: * = array ( topics-by-subjectidentifier ( \"topic-type\"  ) , topics-by-subjectlocator ( \"sl-topic-type\"  ) , topics-by-itemidentifier ( \"ii-topic-type\"  )  ) ]",
				transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

		query = "/ topic-type";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		assertEquals(
				"/ default :: * [ . / type :: * = array ( topics-by-subjectidentifier ( \"topic-type\"  ) , topics-by-subjectlocator ( \"sl-topic-type\"  ) , topics-by-itemidentifier ( \"ii-topic-type\"  )  ) ]",
				transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}

	@Test
	public void testScopeFilter() throws Exception {

		String query = "/ topic:: * @ topic-type";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(
				"/ topic :: * [ . / scope :: * = array ( topics-by-subjectidentifier ( \"topic-type\"  ) , topics-by-subjectlocator ( \"sl-topic-type\"  ) , topics-by-itemidentifier ( \"ii-topic-type\"  )  ) ]",
				transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}

	@Test
	public void testTopicBySubjectIdentifierFct() throws Exception {

		String query = "topics-by-subjectidentifier( \"identifier\" )";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals("array ( topics-by-subjectidentifier ( \"identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )", transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}

	@Test
	public void testTopicByItemIdentifierFct() throws Exception {

		String query = "topics-by-itemidentifier( \"identifier\" )";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
	
		assertEquals("array ( topics-by-subjectidentifier ( \"si-identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"identifier\"  )  )", transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}

	@Test
	public void testTopicBySubjectLocatorFct() throws Exception {

		String query = "topics-by-subjectlocator( \"identifier\" )";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals("array ( topics-by-subjectidentifier ( \"si-identifier\"  ) , topics-by-subjectlocator ( \"identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )", transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}

	@Test
	public void testAssociationPattern() throws Exception {

		String query = "/ identifier ( identifier -> identifier )";
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();
	
		String array = "array ( topics-by-subjectidentifier ( \"identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )";
		String expected = "/ association-pattern ( " + array + " , " + array + " , " + array + " )";

		assertEquals(expected, transform);

		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);

	}
	
	@Test
	public void testSimpleExpression() throws Exception{
		String query = "identifier  / name :: ";
		
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();		
		String array = "array ( topics-by-subjectidentifier ( \"identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )";
		assertEquals(array + " / name ::", transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
	}
	
	@Test
	public void testFilter() throws Exception{
		String query = "/ topic :: * [ . / subject-identifier :: == \"identifier\" ]";
		String array = "array ( topics-by-subjectidentifier ( \"identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )";
		
		String transform = transformer.getQuery(null, query, registry).getQueryString().trim();		
		assertEquals("/ topic :: * [ . = " + array + " ]", transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / subject-locator :: == \"identifier\" ]";
		
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		array = "array ( topics-by-subjectidentifier ( \"si-identifier\"  ) , topics-by-subjectlocator ( \"identifier\"  ) , topics-by-itemidentifier ( \"ii-identifier\"  )  )";
		assertEquals("/ topic :: * [ . = " + array + " ]", transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / item-identifier :: == \"identifier\" ]";
		
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		array = "array ( topics-by-subjectidentifier ( \"si-identifier\"  ) , topics-by-subjectlocator ( \"sl-identifier\"  ) , topics-by-itemidentifier ( \"identifier\"  )  )";
		assertEquals("/ topic :: * [ . = " + array + " ]", transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / item-identifier :: == . / subject-identifier :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / item-identifier :: == . / subject-locator :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
	
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / subject-identifier :: == . / subject-locator :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / subject-identifier :: == . / item-identifier :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / subject-locator :: == . / subject-identifier :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
		
		query = "/ topic :: * [ . / subject-locator :: == . / item-identifier :: ]";
		transform = transformer.getQuery(null, query, registry).getQueryString().trim();
		
		assertEquals(query, transform);
		
		TMQLRuntimeFactory.newFactory().newRuntime().parse(transform);
	}

}
