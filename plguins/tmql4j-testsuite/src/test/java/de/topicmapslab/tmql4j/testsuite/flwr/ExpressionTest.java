/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.flwr;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.converter.QueryFactory;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExpressionTest extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSimpleCombination() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix + " FOR $composer IN // o:Composer RETURN $composer";
		set = execute(query);

		Assert.assertEquals(16, set.size());
		Assert.assertTrue(set.getResultType().equalsIgnoreCase(
				ResultType.TMAPI.name()));
	}

	public void testDefaultPrefix() throws Exception {
		runtime.getLanguageContext().getPrefixHandler().setDefaultPrefix(
				"http://psi.ontopedia.net/");
		String query = null;
		IResultSet<?> set = null;

		query = "FOR $composer IN // Composer RETURN $composer";
		set = execute(query);

		Assert.assertEquals(16, set.size());
		Assert.assertTrue(set.getResultType().equalsIgnoreCase(
				ResultType.TMAPI.name()));
	}

	public void testIfThenElse() throws Exception {
		runtime.getLanguageContext().getPrefixHandler().setDefaultPrefix(
				"http://psi.ontopedia.net/");
		String query = null;
		IResultSet<?> set = null;

		query = "FOR $topic IN // tm:subject RETURN ( ( IF $topic [ . >> types == Composer ] THEN \"composer\" ELSE \"no-composer\" ) , $topic )";
		set = execute(query);

		System.out.println(set);
	}

	public void testIFELSE() throws Exception {
		runtime.getLanguageContext().getPrefixHandler().setDefaultPrefix(
				"http://psi.ontopedia.net/");
		String query = null;
		IResultSet<?> set = null;

		// query =
		// "FOR $composer IN // Composer RETURN $composer >> indicators >> atomify";
		// set = execute(query);
		//
		// Assert.assertEquals(14, set.size());
		// System.out.println(set);

		query = "FOR $composer IN // Composer RETURN $composer, ( $composer / tm:name [0] || \"no-name\" ) , ( $composer >> indicators >> atomify || \"unknown\" )";
		set = execute(query);

		System.out.println(set);
		Assert.assertEquals(16, set.size());

		Assert.assertTrue(set.getResultType().equalsIgnoreCase(
				ResultType.TMAPI.name()));
	}

	public void testQuantifiedExpressions() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ "FOR $composer IN // o:Composer WHERE AT LEAST 25 $opera IN // o:Opera SATISFIES o:composed_by ( o:Composer : $composer , o:Work : $opera ) RETURN $composer";

		IResultSet<?> set = execute(query);
		Assert.assertEquals(1, set.size());
	}

	public void testForAlLClause() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ "FOR $composer IN // o:Composer WHERE EVERY $opera IN $composer >> traverse o:composed_by SATISFIES ( $opera ISA o:Opera AND o:composed_by ( o:Composer : $composer , o:Work : $opera ) ) RETURN $composer";

		IResultSet<?> set = execute(QueryFactory.getFactory().getTmqlQuery(
				query));
		Assert.assertEquals(16, set.size());
	}

	public void testMultipleOrder() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		query = "%prefix o http://psi.ontopedia.net/ "
				+ "FOR $composer IN // o:Composer WHERE "
				+ "$opera ISA o:Opera "
				+ "AND o:composed_by ( o:Composer : $composer , o:Work : $opera ) "
				+ "ORDER BY $composer / tm:name [0] DESC , $opera / tm:name [0] ASC "
				+ "RETURN ( $composer / tm:name [0] , $opera / tm:name [0])";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}

	public void testExistsClause() throws Exception {
		String query = null;
		SimpleResultSet set = null;

		// query = "%prefix o http://psi.ontopedia.net/ "
		// + "FOR $composer IN // o:Composer "
		// + "RETURN $composer , $composer >> indicators >> atomify";
		// set = execute(query);
		// assertEquals(16, set.size());
		// System.out.println(set);
		//		
		// query = "%prefix o http://psi.ontopedia.net/ "
		// + "FOR $composer IN // o:Composer "
		// + "WHERE $composer >> indicators "
		// + "RETURN $composer , $composer >> indicators >> atomify";
		// set = execute(query);
		// assertEquals(14, set.size());
		// System.out.println(set);

		query = "%prefix o http://psi.ontopedia.net/ "
				+ "FOR $composer IN // o:Composer "
				+ "WHERE $composer >> indicators >> atomify == \"http://psi.ontopedia.net/Puccini\""
				+ "RETURN $composer , $composer >> indicators >> atomify";
		set = execute(query);
		assertEquals(1, set.size());
		System.out.println(set);
	}

}
