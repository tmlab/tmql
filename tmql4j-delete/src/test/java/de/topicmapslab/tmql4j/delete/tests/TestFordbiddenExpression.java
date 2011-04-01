/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.tests;

import org.junit.Test;

import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression;
import de.topicmapslab.tmql4j.exception.TMQLParserException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TestFordbiddenExpression extends Tmql4JTestCase {

	private static String QUERY = "DELETE myTopic";
	private static Class<? extends IExpression> forbiddenExpressionType = DeleteExpression.class;

	@Test(expected = TMQLParserException.class)
	public void forbiddenExpression() {
		IQuery query = new TMQLQuery(topicMap, QUERY);
		query.forbidExpression(forbiddenExpressionType);
		execute(query);
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenExpression2() {
		runtime.forbidExpression(forbiddenExpressionType);
		runtime.run(topicMap, QUERY);
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenExpression3() {
		runtime.forbidExpression(forbiddenExpressionType);
		runtime.run(new TMQLQuery(topicMap, QUERY));
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenModificationExpression() {
		IQuery query = new TMQLQuery(topicMap, QUERY);
		query.forbidModificationQueries();
		execute(query);
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenModificationExpression2() {
		runtime.forbidModificationQueries();
		runtime.run(topicMap, QUERY);
	}

	@Test(expected = TMQLParserException.class)
	public void forbiddenModificationExpression3() {
		runtime.forbidModificationQueries();
		runtime.run(new TMQLQuery(topicMap, QUERY));
	}

}
