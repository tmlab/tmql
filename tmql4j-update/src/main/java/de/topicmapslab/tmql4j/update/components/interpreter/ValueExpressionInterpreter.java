/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.components.interpreter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateClause;
import de.topicmapslab.tmql4j.update.grammar.productions.ValueExpression;

/**
 * Extended interpreter for value-expressions only for update clauses
 * 
 * @author Sven Krosse
 * 
 */
public class ValueExpressionInterpreter extends ExpressionInterpreterImpl<ValueExpression> {

	/**
	 * the logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ValueExpressionInterpreter.class);

	/**
	 * The interpreter called to perform first interpretation task
	 */
	private de.topicmapslab.tmql4j.path.components.interpreter.ValueExpressionInterpreter redirectionInterpreter;

	public ValueExpressionInterpreter(ValueExpression ex) {
		super(ex);
		this.redirectionInterpreter = new de.topicmapslab.tmql4j.path.components.interpreter.ValueExpressionInterpreter(getExpression());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect
		 */
		QueryMatches matches = redirectionInterpreter.interpret(runtime, context, optionalArguments);
		/*
		 * check if value-expression is child of update-clause and represent a
		 * topic reference which has to create because it does not exist
		 */
		if (matches.isEmpty() && getExpression().isChildOf(UpdateClause.class) && getTmqlTokens().size() == 1 && getTmqlTokens().get(0).equals(Element.class)) {
			/*
			 * convert to absolute IRI
			 */
			String reference = runtime.getConstructResolver().toAbsoluteIRI(context, getTokens().get(0));
			try {
				/*
				 * add topic to result
				 */
				TopicMap topicMap = context.getQuery().getTopicMap();
				Topic topic = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(reference));
				return QueryMatches.asQueryMatchNS(runtime, topic);
			} catch (Exception e) {
				logger.warn("Cannot create the given topic reference: " + e.getLocalizedMessage());
			}
		}
		return matches;
	}

}
