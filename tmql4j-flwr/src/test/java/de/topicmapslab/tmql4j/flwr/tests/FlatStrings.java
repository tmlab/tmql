/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

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
		_check("FOR $a IN // myType WHERE c == b AND o ORDER BY $a / tm:name ASC OFFSET 1 LIMIT 1 RETURN $a",
				"FOR $a IN myType << types WHERE EXISTS c == b AND EXISTS o ORDER BY $a >> characteristics tm:name >> atomify ASC OFFSET 1 LIMIT 1 RETURN $a");
		_check("<xml>any</xml>", "RETURN <xml> any </xml>");
		_check("<xml>any{// topics / tm:name}</xml>", "RETURN <xml> any { topics << types >> characteristics tm:name >> atomify } </xml>");
		_check("RETURN '''any'''", "RETURN ''' any '''");
		_check("RETURN '''any{// topics / tm:name}'''", "RETURN ''' any { topics << types >> characteristics tm:name >> atomify } '''");
	}

	public void _check(String input, String output) {
		IParserTree tree = runtime.parse(input);
		assertNotNull(tree);
		String queryString = tree.toQueryString();
		assertEquals(output, queryString);
	}

}
