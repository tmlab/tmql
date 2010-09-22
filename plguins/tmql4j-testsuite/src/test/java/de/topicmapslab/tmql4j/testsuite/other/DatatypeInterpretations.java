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
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DatatypeInterpretations extends BaseTest {

	public void testDoubleValues() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;
		query = prefix + " UPDATE occurrences o:Web ADD \"1\" WHERE o:Puccini";
		set = execute(query);
		System.out.println(set);

		query = prefix + " o:Puccini / o:Web";
		set = execute(query);
		System.out.println(set);

		query = prefix
				+ " SELECT $p / tm:name WHERE ( $p ISA o:Composer AND  $p == o:Puccini AND $p / o:Web  < 500000.0 )";
		set = execute(query);
		System.out.println(set);
	}

	public void testDatatype() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;
		query = prefix + " UPDATE occurrences o:Web ADD \"1\" WHERE o:Puccini";
		set = execute(query);
		System.out.println(set);

		query = prefix + " o:Puccini / o:Web";
		set = execute(query);
		System.out.println(set);

		query = prefix
				+ " SELECT $p [ . / o:Web  < \"3\"^^xsd:integer ] WHERE $p ISA o:Composer ";
		set = execute(query);
		Assert.assertEquals(1, set.size());
	}

	public void test() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;
		query = prefix + " UPDATE occurrences o:Web ADD \"1\" WHERE o:Puccini";
		set = execute(query);
		query = prefix + " UPDATE occurrences o:Web ADD \"2\" WHERE o:Puccini";
		set = execute(query);
		System.out.println(set);

		query = prefix + " SELECT o:Puccini / o:Web ";
		set = execute(query);
		System.out.println(set);
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " SELECT $p [ . / o:Web  <= \"1.5\"^^xsd:float ] WHERE $p ISA o:Composer ";
		set = execute(query);
		Assert.assertEquals(1, set.size());

	}

}
