/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.multithreaded;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MultiThreadExtensionTest extends BaseTest {

	public void testQuantifiedMultiThread() throws Exception {
		String query = null;
		IResultSet<?> set = null;

		query = "%prefix wen http://en.wikipedia.org/wiki/ %prefix psi http://psi.topicmapslab.de/toytm/ FOR $republic IN // wen:Republic WHERE AT LEAST 2 $state IN // wen:State_administrative_division SATISFIES psi:containment ( psi:container : $republic , psi:containee : $state , ... ) RETURN $republic";

		set = execute(query);
		System.out.println(set);
		Assert.assertEquals(3, set.size());
	}

}
