/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.path;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Sven Krosse
 *
 */
public class PathExpressionTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.topicmapslab.tmql4j.testsuite.path");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestFilterPostfix.class);
		suite.addTestSuite(TestPredicateInvocation.class);
		suite.addTestSuite(TestNavigationAxis.class);
		suite.addTestSuite(TestNonCanonicalNaviagationAxis.class);
		suite.addTestSuite(TestTupleExpression.class);
		//$JUnit-END$
		return suite;
	}

}
