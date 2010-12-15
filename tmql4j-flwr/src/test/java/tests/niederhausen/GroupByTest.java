/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package tests.niederhausen;


import org.junit.Ignore;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.flwr.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class GroupByTest extends Tmql4JTestCase {

	@Test
	@Ignore
	public void test() throws Exception {
		fromXtm("src/test/resources/toytm.xtm");

		final String query 	= "FOR $t IN // tm:subject " 
							+ "GROUP BY $0 " 
							+ "RETURN $t >> id, $t >> characteristics tm:name, $t >> characteristics tm:occurrence";
		
		IResultSet<?> resultSet = execute(query);
		System.out.println(resultSet);
	}
	@Test
	public void testComplex() throws Exception{
		fromXtm("src/test/resources/toytm.xtm");

		System.out.println(topicMap.getTopics());
		
		final String query 	= "FOR $ot IN // tm:subject >> characteristics >> types "
			+ "GROUP BY $0 "			
			+ "LIMIT 10 "
							+ "RETURN {" 
								+ " FOR $t IN // tm:subject "
								+ "RETURN $t >> id,  $t / $ot"
								+ "}"
								;
		
		IResultSet<?> resultSet = execute(query);
		System.out.println(resultSet);
	}

}
