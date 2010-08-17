/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.other;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.topicmapslab.tmql4j.resultprocessing.core.reduction.ReductionResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ResultSetImplementationClassesTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testParameterizedType() throws Exception {
		NextStep set = new NextStep();
		Assert.assertEquals(SimpleTupleResult.class, set.getResultClass());
	}

}

class NextStep extends ReductionResultSet {

}
