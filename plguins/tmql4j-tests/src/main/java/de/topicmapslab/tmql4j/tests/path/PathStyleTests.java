/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.path;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.topicmapslab.tmql4j.path.tests.path.TestContent;
import de.topicmapslab.tmql4j.path.tests.path.TestFilterPostfix;
import de.topicmapslab.tmql4j.path.tests.path.TestFunctionInvocation;
import de.topicmapslab.tmql4j.path.tests.path.TestNavigationAxis;
import de.topicmapslab.tmql4j.path.tests.path.TestNonCanonicalNaviagationAxis;
import de.topicmapslab.tmql4j.path.tests.path.TestPredicateInvocation;
import de.topicmapslab.tmql4j.path.tests.path.TestTupleExpression;
import de.topicmapslab.tmql4j.path.tests.path.TestValueExpression;

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
