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
import de.topicmapslab.tmql4j.resultprocessing.core.ctm.CTMResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CTMExpressionTest extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCTMWthLiteral() throws Exception {
		final String prefix = "%prefix o http://psi.ontopia.net/music/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix
				+ " FOR $composer IN // o:composer RETURN \"\"\" <{ $composer >> item >> atomify }> \"\"\"";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}

	public void testCTMWithTopic() throws Exception {
		final String prefix = "%prefix o http://psi.ontopia.net/music/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix + " FOR $composer IN // o:composer RETURN $composer"; // \"\"\" { $composer } \"\"\"";
		set = execute(query);

		Assert.assertEquals(13, set.size());		

		System.out.println(set.getResultType());
		System.out.println();
	}

	public void testCTMWithTopicAndAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopia.net/music/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix
				+ " FOR $composer IN // o:composer RETURN \"\"\" { $composer } { $composer << players } \"\"\"";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}

	public void testCTMWithTopicAndNewContent() throws Exception {
		final String prefix = "%prefix o http://psi.ontopia.net/music/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix
				+ " FOR $composer IN // o:composer RETURN \"\"\" { $composer } <o:doSomething> ( <o:thePerson> : <{ $composer >> indicators >> atomify }> ) \"\"\"";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}

	public void testMaianaBug() throws Exception {
		String query = "%prefix o http://psi.ontopia.net/music/ FOR $composer IN // o:composer RETURN { FOR $type IN $composer >> characteristics >> types RETURN \"\"\"{$composer} {$type}\"\"\"}";
		IResultSet<?> set = null;
		set = execute(query);

		// System.out.println(set);
		System.out.println("MERGED");
		System.out.println(((CTMResult) set).resultsAsMergedCTM());
	}

	public void testMaianaBug2() throws Exception {
		String query = "%prefix o http://psi.ontopia.net/music/ \n FOR $composer IN // o:composer RETURN { \n FOR $type IN $composer >> characteristics >> types \n RETURN \"\"\"{$composer} {$type}\"\"\"}";
		IResultSet<?> set = null;
		set = execute(query);
		System.out.println(set);
	}
}
