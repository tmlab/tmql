/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.tests;

import org.junit.Test;

import de.topicmapslab.tmql4j.exception.TMQLParserException;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateExpression;

/**
 * @author Sven Krosse
 * 
 */
public class TestFordbiddenExpression extends Tmql4JTestCase {

	@Test(expected = TMQLParserException.class)
	public void forbiddenExpression() {
		IQuery query = new TMQLQuery(topicMap, "UPDATE names SET \"1\" WHERE myTopic");
		query.forbidExpression(UpdateExpression.class);
		execute(query);
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenModificationExpression() {
		IQuery query = new TMQLQuery(topicMap, "UPDATE names SET \"1\" WHERE myTopic");
		query.forbidModificationQueries();
		execute(query);
	}

}
