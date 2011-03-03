/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.topicmapslab.tmql4j.path.tests.environment.EnvironmentTests;
import de.topicmapslab.tmql4j.path.tests.path.PathStyleTests;

/**
 * @author Sven Krosse
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ PathStyleTests.class, EnvironmentTests.class})
public class Tmql4jTest {

}
