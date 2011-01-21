/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.components.interpreter;

import java.util.List;

import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.Ellipsis;
import de.topicmapslab.tmql4j.path.util.Restriction;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * 
 * Special interpreter class to interpret association-definition part.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * association-definition-part ::=  ( anchor | variable ) : ( anchor | variable )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationRolePlayerExpressionInterpreter extends ExpressionInterpreterImpl<PredicateInvocationRolePlayerExpression> {
	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PredicateInvocationRolePlayerExpressionInterpreter(PredicateInvocationRolePlayerExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Restriction interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		return interpretAsUpdateStream(runtime, context, optionalArguments);
	}

	/**
	 * Method is called if the expression is child of a update-clause.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalArguments
	 *            any optional arguments
	 * @param r
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation failed
	 */
	private Restriction interpretAsUpdateStream(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (getTmqlTokens().get(0).equals(Ellipsis.class)) {
			throw new TMQLRuntimeException("Ellipsis '...' not allowed as update-definition.");
		}
		List<IExpressionInterpreter<?>> interpreters = getInterpreters(runtime);
		if (interpreters.size() != 2) {
			throw new TMQLRuntimeException("Invalid syntax, number of interpreters '" + interpreters.size() + "' is unexpected!");
		}
		Restriction restriction = new Restriction();
		restriction.setExpression(getExpression());
		restriction.setRoleType(getOrCreateTopic(runtime, context, interpreters.get(0)));
		restriction.setPlayer(getOrCreateTopic(runtime, context, interpreters.get(1)));
		return restriction;
	}

	/**
	 * Method try to find the topic represented by the reference or variable. If
	 * there isn't a topic with this identifier, a new topic will be created
	 * with the reference used as subject-identifier. If the construct isn't a
	 * topic an exception will be thrown. If the reference represent a system
	 * topic, the string will be returned. If the topic does not exits but
	 * should not created, <code>null</code> will be returned.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param interpreter
	 *            the interpreter which should be called to get the topic or
	 *            create a topic by definition
	 * @return the topic
	 * @throws TMQLRuntimeException
	 *             thrown if found construct isn't a topic or the variable is
	 *             not set
	 */
	private final Topic getOrCreateTopic(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> interpreter) throws TMQLRuntimeException {
		TopicMap topicMap = context.getQuery().getTopicMap();
		QueryMatches matches = interpreter.interpret(runtime, context);
		if (!matches.isEmpty()){
			Object val = matches.getFirstValue();
			if ( val instanceof Topic ){
				return (Topic) matches.getFirstValue();
			}else if ( val instanceof String ){
				String ref = runtime.getConstructResolver().toAbsoluteIRI(context, (String)val);
				Locator locator = topicMap.createLocator(ref);
				return TopicDefinitionInterpreter.createTopic(topicMap, AxisIndicators.class, locator);
			}
		}
		
		Class<? extends IToken> identifierType;		

		/*
		 * look for topic creation
		 */
		if (interpreter.getTokens().size() == 1) {
			identifierType = AxisIndicators.class;
		} else if (interpreter.getTokens().size() == 2) {
			identifierType = interpreter.getTmqlTokens().get(1);
		} else if (interpreter.getTokens().size() == 3) {
			identifierType = interpreter.getTmqlTokens().get(2);
		} else {
			throw new TMQLRuntimeException("The given expression does not create or return a topic instance!");
		}

		/*
		 * extract string-represented reference
		 */
		Locator locator = topicMap.createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, LiteralUtils.asString(interpreter.getTokens().get(0))));
		return TopicDefinitionInterpreter.createTopic(topicMap, identifierType, locator);
	}

}
