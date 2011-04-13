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
import java.util.Set;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axes;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ISAExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

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
public class ISAExpressionInterpreter extends ExpressionInterpreterImpl<ISAExpression> {

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
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
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
			QueryMatches results = extractArguments(runtime, SimpleContent.class, 0, context, optionalArguments);
			/*
			 * empty content -> return
			 */
			if (results.getPossibleValuesForVariable().isEmpty()) {
				return QueryMatches.emptyMatches();
			}
			anchor = results.getPossibleValuesForVariable();
			variable = QueryMatches.getNonScopedVariable();
		}

		/*
		 * call the second simple-content expression representing the type content
		 */
		QueryMatches simpleContent2 = extractArguments(runtime, SimpleContent.class, 1, context, optionalArguments);

		/*
		 * simple-content-1 is variable
		 */
		try {
			IAxis axis = Axes.buildHandler().lookup(AxisInstances.class);
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

					for (Object obj_ : axis.navigate(context, o, null)) {
						Map<String, Object> map = HashUtil.getHashMap();
						map.put(variable, obj_);
						matches.add(map);
					}
				}
			}
			/*
			 * context is any other query match -> create intersection
			 */
			if (anchor instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) anchor;
				Collection<?> all = matches.getPossibleValuesForVariable();
				col.retainAll(all);
				matches = QueryMatches.asQueryMatchNS(runtime, col.toArray());
				/*
				 * set negation
				 */
				all.remove(col);
				matches.addNegation(QueryMatches.asQueryMatchNS(runtime, all.toArray()));
			}
			/*
			 * context is variable
			 */
			else {
				/*
				 * set negation
				 */
				Set<Topic> topics = HashUtil.getHashSet();
				topics.addAll(topicMap.getTopics());
				for (Object o : matches.getPossibleValuesForVariable(getTokens().get(0))) {
					topics.remove(o);
				}
				QueryMatches negation = new QueryMatches(runtime);
				negation.convertToTuples(topics, getTokens().get(0));
				matches.addNegation(negation);
			}
			return matches;
		} catch (NavigationException e) {
			throw new TMQLRuntimeException("Interpretation fails because of failing navigation.", e);
		}
	}

}
