/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.parser.IParserTree;

/**
 * @author Sven Krosse
 * 
 */
public class FlatStrings extends Tmql4JTestCase {

	@Test
	public void testToQueryString() {
		_check("UPDATE topics ADD ? !", "UPDATE topics ADD ? !");
		_check("UPDATE topics ADD ? =", "UPDATE topics ADD ? =");
		_check("UPDATE topics ADD ? ~", "UPDATE topics ADD ? ~");
		_check("UPDATE associations ADD argument(argument : argument)", "UPDATE associations ADD argument ( argument : argument )");
		_check("UPDATE occurrences ADD \"value\"^^xsd:string WHERE a", "UPDATE occurrences ADD \"value\"^^xsd:string WHERE EXISTS a");
		_check("UPDATE occurrences ADD \"value\"^^xsd:string , types ADD this WHERE a", "UPDATE occurrences ADD \"value\"^^xsd:string , types ADD this WHERE EXISTS a");
	}

	public void _check(String input, String output) {
		IParserTree tree = runtime.parse(input);
		assertNotNull(tree);
		String queryString = tree.toQueryString();
		assertEquals(output, queryString);
	}

}
