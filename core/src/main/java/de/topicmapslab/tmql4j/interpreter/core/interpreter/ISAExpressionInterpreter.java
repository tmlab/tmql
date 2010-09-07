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
import java.util.Set;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;
import de.topicmapslab.tmql4j.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.parser.core.expressions.ISAExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;

/**
 * 
 * Special interpreter class to interpret ISA-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * path-expression ::= simple-content-1 isa simple-content-2 ==>
 * tm:type-instance ( tm:instance : simple-content-1 , tm:type :
 * simple-content-2 )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ISAExpressionInterpreter extends
		ExpressionInterpreterImpl<ISAExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ISAExpressionInterpreter(ISAExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		TopicMap topicMap = getQueriedTopicMap(runtime);

		Object context = null;
		String variable = null;
		/*
		 * context is a variable
		 */
		if (getTmqlTokens().get(0).equals(Variable.class)) {
			context = getTokens().get(0);
			variable = getTokens().get(0);
		}
		/*
		 * context is any other content
		 */
		else {
			QueryMatches m = extractArguments(runtime, SimpleContent.class, 0);
			/*
			 * empty content -> return
			 */
			if (m.getPossibleValuesForVariable().isEmpty()) {
				return;
			}
			context = m.getPossibleValuesForVariable();
			variable = QueryMatches.getNonScopedVariable();
		}

		/*
		 * call the second simple-content expression representing the type
		 * content
		 */
		QueryMatches simpleContent2 = extractArguments(runtime,
				SimpleContent.class, 1);

		/*
		 * simple-content-1 is variable
		 */
		INavigationAxis axis = runtime.getDataBridge()
				.getImplementationOfTMQLAxis(runtime, "instances");
		axis.setTopicMap(topicMap);
		/*
		 * create query-matches containing the results
		 */
		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * iterate over all values of right-hand content
		 */
		for (Object o : simpleContent2.getPossibleValuesForVariable()) {
			/*
			 * get all instances of the current topic
			 */
			if (o instanceof Topic) {
				try {
					for (Object obj_ : axis.navigateForward(o)) {
						Map<String, Object> map = HashUtil.getHashMap();
						map.put(variable, obj_);
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
		 * context is any other query match -> create intersection
		 */
		if (context instanceof Collection<?>) {
			Collection<?> col = (Collection<?>) context;
			Collection<?> all = matches.getPossibleValuesForVariable();
			col.retainAll(all);
			matches = new QueryMatches(runtime);
			matches.convertToTuples(col);
			/*
			 * set negation
			 */
			QueryMatches negation = new QueryMatches(runtime);
			all.remove(col);
			negation.convertToTuples(all);
			matches.addNegation(negation);
		}
		/*
		 * context is variable
		 */
		else {
			/*
			 * } set negation
			 */
			Set<Topic> topics = HashUtil.getHashSet();
			topics.addAll(topicMap.getTopics());
			for (Object o : matches.getPossibleValuesForVariable(getTokens()
					.get(0))) {
				topics.remove(o);
			}
			QueryMatches negation = new QueryMatches(runtime);
			negation.convertToTuples(topics, getTokens().get(0));
			matches.addNegation(negation);
		}

		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, matches);

	}

}
