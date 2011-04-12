/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.tests;

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
		_check("DELETE CASCADE ALL");
		_check("DELETE CASCADE topic");
		_check("DELETE topic");
		_check("DELETE CASCADE $c WHERE a","DELETE CASCADE $c WHERE EXISTS a");
	}

	public void _check(String input) {
		_check(input, input);
	}
	public void _check(String input, String output) {
		IParserTree tree = runtime.parse(input);
		assertNotNull(tree);
		String queryString = tree.toQueryString();
		assertEquals(output, queryString);
	}

}
