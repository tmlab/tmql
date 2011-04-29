import org.junit.Test;

import de.topicmapslab.tmql4j.majortom.tests.Tmql4JTestCase;

/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */

/**
 * @author Sven Krosse
 * 
 */
public class TestOOM extends Tmql4JTestCase {

	@Test
	public void testOOM() throws Exception {
		fromXtm("src/test/resources/toytm-and-tmra09-participants.xtm");

		System.out.println(execute("fn:get-topic-types()"));
		System.out.println("1");
		System.out.println(execute("fn:get-supertypes()"));
		System.out.println("2");
		System.out.println(execute("fn:get-supertypes( fn:get-topic-types() )"));
		System.out.println("3");
		System.out.println(execute("fn:get-topic-types() UNION fn:get-supertypes( fn:get-topic-types() )"));
		System.out.println("4");
	}

}
