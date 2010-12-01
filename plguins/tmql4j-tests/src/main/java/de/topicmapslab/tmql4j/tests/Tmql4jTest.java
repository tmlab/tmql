/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.topicmapslab.tmql4j.path.tests.environment.EnvironmentTests;
import de.topicmapslab.tmql4j.path.tests.path.PathStyleTests;
import de.topicmapslab.tmql4j.path.tests.select.SelectStyleTests;
import de.topicmapslab.tmql4j.tests.delete.DeleteTest;
import de.topicmapslab.tmql4j.tests.draft2010.Draft2010Tests;
import de.topicmapslab.tmql4j.tests.flwr.FlwrStyleTests;
import de.topicmapslab.tmql4j.tests.insert.InsertTest;
import de.topicmapslab.tmql4j.tests.merge.MergeTest;
import de.topicmapslab.tmql4j.tests.update.UpdateTest;

/**
 * @author Sven Krosse
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ PathStyleTests.class, EnvironmentTests.class,
		FlwrStyleTests.class, SelectStyleTests.class, /*Draft2010Tests.class,*/
		DeleteTest.class, UpdateTest.class, MergeTest.class, InsertTest.class })
public class Tmql4jTest {

}
