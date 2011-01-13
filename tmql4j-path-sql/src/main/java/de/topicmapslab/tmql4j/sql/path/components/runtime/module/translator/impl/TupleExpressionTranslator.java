/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import java.text.MessageFormat;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITmqlSqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext.State;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslaterContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TupleExpressionTranslator extends TmqlSqlTranslatorImpl<TupleExpression> {

	private static final String QUERY = "SELECT {0} FROM {1}";
	private static final String SELECTION_PART = "tuple{0}.{1}";
	private static final String FROM_PART = " ( {0} ) AS tuple{1}";

	/**
	 * {@inheritDoc}
	 */
	public ITranslatorContext transform(ITMQLRuntime runtime, IContext context, IExpression expression, ITranslatorContext state) throws TMQLRuntimeException {
		StringBuilder selectionPart = new StringBuilder();
		StringBuilder fromPart = new StringBuilder();
		List<String> selections = HashUtil.getList();
		ITmqlSqlTranslator<?> translator = TranslatorRegistry.getTranslator(AliasValueExpression.class);
		int i = 0;
		for (IExpression ex : expression.getExpressions()) {
			ITranslatorContext valueContext = translator.transform(runtime, context, ex, state);
			/*
			 * create from part
			 */
			if (!fromPart.toString().isEmpty()) {
				fromPart.append(Comma.TOKEN);
			}
			fromPart.append(MessageFormat.format(FROM_PART, valueContext.getContextOfCurrentNode(), Integer.toString(i)));
			/*
			 * create selection part
			 */
			for (String selection : valueContext.getTopLevelSelections()) {
				if (!selectionPart.toString().isEmpty()) {
					selectionPart.append(Comma.TOKEN);
				}
				selectionPart.append(MessageFormat.format(SELECTION_PART, Integer.toString(i), selection));
			}
			selections.addAll(valueContext.getTopLevelSelections());
			i++;
		}
		String query = MessageFormat.format(QUERY, selectionPart.toString(), fromPart.toString());
		TranslaterContext newContext = new TranslaterContext(State.ANY, selections);
		newContext.setContextOfCurrentNode(query);
		return newContext;
	}

}
