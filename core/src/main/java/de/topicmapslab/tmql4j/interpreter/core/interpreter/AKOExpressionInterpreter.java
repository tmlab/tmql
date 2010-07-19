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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;
import de.topicmapslab.tmql4j.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.parser.core.expressions.AKOExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;

/**
 * 
 * Special interpreter class to interpret AKO-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * simple-content-1 ako simple-content-2 ==> tm:subclass-of
 * ( tm:subclass : simple-content-1 , tm:superclass : simple-content-2 )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AKOExpressionInterpreter extends
		ExpressionInterpreterImpl<AKOExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public AKOExpressionInterpreter(AKOExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * get queried topic map
		 */
		TopicMap topicMap = getQueriedTopicMap(runtime);

		/*
		 * check if first simple-content is a variable
		 */
		if (!getTmqlTokens().get(0).equals(Variable.class)) {
			throw new TMQLRuntimeException(
					"simple-content-1 has to be a variable");
		}

		/*
		 * call second simple-content expression representing the type
		 */
		QueryMatches simpleContent2 = extractArguments(runtime,
				SimpleContent.class, 1);
		/*
		 * get navigation axis
		 */
		INavigationAxis axis = runtime.getDataBridge()
				.getImplementationOfTMQLAxis(runtime, "subtypes");
		/*
		 * create results container
		 */
		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * iterate over all topics
		 */
		for (Object o : simpleContent2.getPossibleValuesForVariable()) {
			/*
			 * get all sub-types of the current topic
			 */
			if (o instanceof Topic) {
				try {
					for (Object obj_ : axis.navigateForward(o)) {
						Map<String, Object> map = HashUtil.getHashMap();
						map.put(getTokens().get(0), obj_);
						matches.add(map);
					}
				} catch (NavigationException e) {
					throw new TMQLRuntimeException(
							"Interpretation fails because of failing navigation.",
							e);
				}
			}
		}

		/*
		 * set negation to internal query match
		 */
		TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		Collection<Topic> topics = index.getTopicTypes();
		topics.removeAll(matches.getPossibleValuesForVariable(getTokens()
				.get(0)));
		QueryMatches negation = new QueryMatches(runtime);
		negation.convertToTuples(topics, getTokens().get(0));
		matches.addNegation(negation);

		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}
}
