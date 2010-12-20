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
import de.topicmapslab.tmql4j.majortom.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class GroupByTest extends Tmql4JTestCase {

	@Test
	@Ignore
	public void test() throws Exception {
		fromXtm("src/test/resources/toytm.xtm");

		String query = "FOR $t IN // tm:subject " + "GROUP BY $0 " + "RETURN $t >> id, fn:best-label( $t ) , $t >> characteristics tm:name, $t >> characteristics tm:occurrence";

		query = "FOR $t IN // tm:subject GROUP BY $0 RETURN $t >> id, fn:best-label($t), $t >> characteristics tm:name >> atomify, $t >> characteristics tm:occurrence >> atomify ";

		IResultSet<?> resultSet = execute(query);
	}
	
	@Test
	@Ignore
	public void testComplex() throws Exception{
		fromXtm("src/test/resources/toytm.xtm");
		
		final String query 	= "FOR $ot IN // tm:subject >> characteristics >> types "
			+ "GROUP BY $0, $1, $2, $3 "	
			+ "OFFSET 1 "
			+ "LIMIT 10 "
							+ "RETURN {" 
								+ " FOR $t IN // tm:subject "
								+ "RETURN $#, $t >> id, fn:best-label( $t ),  $ot >> id , fn:best-label( $ot ), $t / $ot"
								+ "}"
								;
		
		IResultSet<?> resultSet = execute(query);
	}

	@Test
	@Ignore
	public void testPerformanceProblem() throws Exception {
		for ( int i = 0 ; i < 10000 ; i++ ){
			createTopic();
		}
		
		final String query = " FOR $t IN // tm:subject OFFSET 0 LIMIT 10 RETURN $t , $t >> id , \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\"";
		
		IResultSet<?> rs = execute(query);
		System.out.println(rs);
	}
	
}
