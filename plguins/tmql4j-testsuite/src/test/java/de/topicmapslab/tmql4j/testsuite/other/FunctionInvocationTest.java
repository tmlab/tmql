package de.topicmapslab.tmql4j.testsuite.other;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class FunctionInvocationTest extends BaseTest {

	public void testRegExpFunction() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query;
		SimpleResultSet set = null;

		query = prefix
				+ " SELECT fn:regexp ( o:Puccini >> indicators >> atomify , \".*Pucci.*\" ) ";

		set = execute(query);
		
		Assert.assertEquals(1, set.size());
	}

	public void testSubstringFunction() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query;
		SimpleResultSet set = null;

		query = prefix
				+ " SELECT fn:substring ( $c / o:date_of_birth , 0 , 7 ) WHERE $c ISA o:Composer";
		
		set = execute(query);
		System.out.println(set);
		Assert.assertEquals(16, set.size());

	}

	public void testConcatFunction() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query;
		SimpleResultSet set = null;

		query = prefix
				+ "SELECT fn:string-concat ( \"http://psi.semagia.com/iso8601/\"  , fn:substring ( o:Composer >> instances / o:date_of_birth , 0 , 7 ) ) ";

		System.out.println(query);
		set = execute(query);

		System.out.println(set);

	}
}
