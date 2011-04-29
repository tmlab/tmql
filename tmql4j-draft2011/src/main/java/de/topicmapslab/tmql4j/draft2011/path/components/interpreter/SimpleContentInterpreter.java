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

import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * 
 * Special interpreter class to interpret simple-content.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * simple-content ::= anchor [ navigation ]
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleContentInterpreter extends ExpressionInterpreterImpl<SimpleContent> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public SimpleContentInterpreter(SimpleContent ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret anchor
		 */
		boolean isAnchor = getExpression().contains(Anchor.class);
		QueryMatches anchor = extractArguments(runtime, isAnchor ? Anchor.class : FunctionInvocation.class, 0, context, optionalArguments);

		/*
		 * don't contains a subexpression
		 */
		if (getInterpreters(runtime).size() == 1) {
			return anchor;
		}
		/*
		 * contains a subexpression
		 */
		String variable = null;
		if (isAnchor) {
			Anchor a = getExpression().getExpressionFilteredByType(Anchor.class).get(0);
			if (a.getGrammarType() == Anchor.TYPE_VARIABLE) {
				variable = a.getTokens().get(0);
				if (anchor.isEmpty()) {
					anchor = QueryMatches.asQueryMatchNS(runtime, context.getQuery().getTopicMap().getTopics().toArray());
				}
			}
			/*
			 * special interpretation for tm:subject
			 */
			else if (anchor.isEmpty() && TmdmSubjectIdentifier.isTmdmSubject(a.getTokens().get(0))) {
				anchor = QueryMatches.asQueryMatch(runtime, QueryMatches.getNonScopedVariable(), TmdmSubjectIdentifier.TM_SUBJECT);
			}
		}
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * simple-content can only contain one expression of type navigation
		 */
		IExpressionInterpreter<Navigation> ex = getInterpretersFilteredByEypressionType(runtime, Navigation.class).get(0);
		/*
		 * Iterate over all possible bindings
		 */
		for (Object match : anchor.getPossibleValuesForVariable()) {
			Context newContext = new Context(context);
			newContext.setContextBindings(null);
			newContext.setCurrentNode(match);
			/*
			 * call subexpression ( navigation )
			 */
			QueryMatches matches = ex.interpret(runtime, newContext, optionalArguments);

			for (Map<String, Object> object : matches) {
				Map<String, Object> binding = HashUtil.getHashMap();
				binding.putAll(object);
				/*
				 * check if anchor is a variable and add an entry to the tuple containing the binding of this variable
				 */
				if (variable != null) {
					binding.put(variable, match);
				}
				results.add(binding);
			}
		}
		return results;
	}
}
