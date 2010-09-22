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
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.Map;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateClause;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ValueExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.token.Element;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;

public class ExtendedValueExpressionInterpreter extends
		ValueExpressionInterpreter {

	public ExtendedValueExpressionInterpreter(ValueExpression ex) {
		super(ex);
	}

	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * redirect
		 */
		super.interpret(runtime);

		/*
		 * check if value-expression is child of update-clause and represent a
		 * topic reference
		 */
		if (getExpression().isChildOf(UpdateClause.class)
				&& getTmqlTokens().size() == 1
				&& getTmqlTokens().get(0).equals(Element.class)) {
			/*
			 * convert to absolute IRI
			 */
			String reference = runtime.getLanguageContext().getPrefixHandler()
					.toAbsoluteIRI(getTokens().get(0));

			IVariableSet set = runtime.getRuntimeContext().peek();
			if (set.contains(VariableNames.QUERYMATCHES)) {
				QueryMatches matches = (QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES);
				if (matches.isEmpty()) {
					try {
						/*
						 * add topic to result
						 */
						Topic t = runtime.getTopicMap()
								.createTopicBySubjectIdentifier(
										runtime.getTopicMap().createLocator(
												reference));
						Map<String, Object> tuple = HashUtil.getHashMap();
						tuple.put(QueryMatches.getNonScopedVariable(), t);
						matches.add(tuple);
						runtime.getRuntimeContext().peek().setValue(
								VariableNames.QUERYMATCHES, matches);
					} catch (Exception e) {
						// NOTHING TO Do
					}
				}
			}
		}
	}

}
