/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.tests.error;

import junit.framework.Assert;

import org.junit.Test;

import de.topicmapslab.tmql4j.draft2011.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLParserException;

/**
 * @author Sven Krosse
 * 
 */
public class TestError extends Tmql4JTestCase {

	@Test(expected = TMQLParserException.class)
	public void testUnkownAxis() {
		execute("123 / invalid");
	}

	@Test(expected = TMQLParserException.class)
	public void testUnkownFunction() {
		execute("fn:invalid ( ) ");
	}

	@Test
	public void testInvalid() {
		final String query = " das ist kein valider query";
		try {
			execute(query);
			Assert.fail("Should be failed");
		} catch (TMQLGeneratorException e) {
		}
	}
}
