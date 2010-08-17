/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.users;

import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
public class Test extends BaseTest {

	public void testname() throws Exception {
		SimpleResultSet set = execute("// http://psi.ontopedia.net/Composer [ . / http://psi.ontopedia.net/date_of_birth =~ \"1875.+\" ]");
		System.out.println(set);
		
		set = execute("// http://psi.ontopedia.net/Composer [ fn:regexp ( . / http://psi.ontopedia.net/date_of_birth , \"1875.+\" ) ]");
		System.out.println(set);
	}

	
	/*
	 * // http://psi.ontopia.net/music/composer [ . / http://psi.ontopia.net/biography/date-of-birth =~ \"1875.+\" ]
	 */
}
