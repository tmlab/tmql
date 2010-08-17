/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite;

import de.topicmapslab.tmql4j.testsuite.path.PathExpressionTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Sven Krosse
 * 
 */
public class Tmql4jTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.topicmapslab.tmql4j.suite");
		// $JUnit-BEGIN$
		suite.addTest(PathExpressionTestSuite.suite());
		suite.addTest(OldTestSuite.suite());
		// $JUnit-END$
		return suite;
	}

}
