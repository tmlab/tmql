/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.RegExpFunction;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.Parameters;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public abstract class FunctionTranslatorImpl extends TmqlSqlTranslatorImpl<FunctionInvocation> implements IFunctionTranslator {

	/**
	 * {@inheritDoc}
	 */
	public List<ISqlDefinition> parametersToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * create result list
		 */
		List<ISqlDefinition> parameters = new ArrayList<ISqlDefinition>();
		/*
		 * contains parameters?
		 */
		if (expression.contains(Parameters.class)) {
			/*
			 * get expression and translator
			 */
			Parameters parameter = expression.getExpressionFilteredByType(Parameters.class).get(0);
			ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(AliasValueExpression.class);
			/*
			 * is tuple expression
			 */
			if (parameter.contains(TupleExpression.class)) {
				IExpression tupleExpression = parameter.getExpressions().get(0);
				/*
				 * iterate over value expressions pairs
				 */
				for (IExpression ex : tupleExpression.getExpressions()) {
					parameters.add(translator.toSql(runtime, context, ex, definition));
				}
			}
			/*
			 * is parameters pair
			 */
			else {
				/*
				 * iterate over parameter pairs
				 */
				for (IExpression paramPair : parameter.getExpressions()) {
					/*
					 * translate value expression
					 */
					AliasValueExpression ex = paramPair.getExpressionFilteredByType(AliasValueExpression.class).get(0);
					parameters.add(translator.toSql(runtime, context, ex, definition));
				}
			}
		}
		return parameters;
	}

	/**
	 * Checks if the number of parameters is valid for the current function
	 * implementation.
	 * 
	 * @param numberOfParameters
	 *            the current number of parameters
	 * @return <code>true</code> if the number of parameters is supported,
	 *         <code>false</code> otherwise.
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		try {
			return getFunction().newInstance().isExpectedNumberOfParameters(numberOfParameters);
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
	}

	private static final Map<Class<? extends IFunction>, IFunctionTranslator> translators = HashUtil.getHashMap();
	static {
		translators.put(RegExpFunction.class, new RegExpFunctionTranslator());
	}

	public static IFunctionTranslator getFunctionTranslator(Class<? extends IFunction> functionType) {
		return translators.get(functionType);
	}

}
