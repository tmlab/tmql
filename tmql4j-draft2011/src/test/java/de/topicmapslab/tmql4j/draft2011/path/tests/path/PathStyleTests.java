/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.tests.path;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Sven Krosse
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ TestFilterPostfix.class, TestNavigationAxis.class,
		TestNonCanonicalNaviagationAxis.class, TestTupleExpression.class,
		TestPredicateInvocation.class, TestContent.class,
		TestValueExpression.class, TestFunctionInvocation.class })
public class PathStyleTests {

}
