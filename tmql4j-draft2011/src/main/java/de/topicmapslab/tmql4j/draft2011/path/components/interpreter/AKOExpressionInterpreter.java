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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.NavigationRegistry;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AKOExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

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
public class AKOExpressionInterpreter extends ExpressionInterpreterImpl<AKOExpression> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get queried topic map
		 */
		TopicMap topicMap = context.getQuery().getTopicMap();

		Object anchor = null;
		String variable = null;
		/*
		 * context is a variable
		 */
		if (getTmqlTokens().get(0).equals(Variable.class)) {
			anchor = getTokens().get(0);
			variable = getTokens().get(0);
		}
		/*
		 * context is any other content
		 */
		else {
			QueryMatches m = extractArguments(runtime, SimpleContent.class, 0, context);
			/*
			 * empty content -> return
			 */
			if (m.getPossibleValuesForVariable().isEmpty()) {
				return QueryMatches.emptyMatches();
			}
			anchor = m.getPossibleValuesForVariable();
			variable = QueryMatches.getNonScopedVariable();
		}
		/*
		 * call second simple-content expression representing the type
		 */
		QueryMatches simpleContent2 = extractArguments(runtime, SimpleContent.class, 1, context);

		/*
		 * create results container
		 */
		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * get navigation axis
		 */
		INavigationAxis axis;
		try {
			axis = NavigationRegistry.buildHandler().lookup(AxisSubtypes.class);
			/*
			 * iterate over all topics
			 */
			for (Object o : simpleContent2.getPossibleValuesForVariable()) {
				/*
				 * get all sub-types of the current topic
				 */
				if (o instanceof Topic) {
					for (Object obj_ : axis.navigateForward(o)) {
						Map<String, Object> map = HashUtil.getHashMap();
						map.put(variable, obj_);
						matches.add(map);
					}
				}
			}
		} catch (NavigationException e) {
			throw new TMQLRuntimeException("Interpretation fails because of failing navigation.", e);
		}

		/*
		 * context is any other query match -> create intersection
		 */
		if (anchor instanceof Collection<?>) {
			Collection<?> col = (Collection<?>) anchor;
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
			 * set negation to internal query match
			 */
			TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			Collection<Topic> topics = HashUtil.getHashSet();
			topics.addAll(index.getTopicTypes());
			topics.removeAll(matches.getPossibleValuesForVariable(getTokens().get(0)));
			QueryMatches negation = new QueryMatches(runtime);
			negation.convertToTuples(topics, getTokens().get(0));
			matches.addNegation(negation);
		}

		/*
		 * set to stack
		 */
		return matches;
	}

}
