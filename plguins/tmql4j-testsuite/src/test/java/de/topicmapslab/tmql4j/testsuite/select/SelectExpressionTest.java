/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.select;

import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SelectExpressionTest extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testIsaExpression() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " Select $p >> indicators >> atomify "
				+ "where $p isa o:Opera ";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}
		
	public void testSimpleAscOrder() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " Select $p >> indicators >> atomify "
				+ "where $p isa o:Opera "
				+ "order by $p >> indicators >> atomify DESC";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}
	
	public void testRegEx() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " Select $p , $p / tm:name "
				+ "where $p ISA o:Composer  AND $p / tm:name =~ \"Puc.*\" ";
//		query = " // tm:subject / tm:name [ fn:regexp( . , \"/.*la.*/i\" ) ]";
		set = execute(query);

		System.out.println(set);
	}
	
	public void testcount() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;
		query = prefix + "o:Composer >> instances [ 28 == fn:count ( . << players o:Composer >> players o:Work )  ]";
		set = execute(query);

		System.out.println(set);
	}
	
	
	
	

}
