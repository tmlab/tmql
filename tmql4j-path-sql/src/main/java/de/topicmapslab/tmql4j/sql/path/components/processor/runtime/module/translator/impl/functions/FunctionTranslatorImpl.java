/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.LengthFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.RegExpFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringConcatFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringGeqFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringGtFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLeqFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLtFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.SubStringFunction;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.Parameters;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.RegExpFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringConcatFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringGeqFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringGtFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringLengthFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringLeqFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.StringLtFunctionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string.SubStringFunctionTranslator;

/**
 * @author Sven Krosse
 * 
 */
public abstract class FunctionTranslatorImpl extends TmqlSqlTranslatorImpl<FunctionInvocation> implements IFunctionTranslator {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		List<ISqlDefinition> parameters = parametersToSql(runtime, context, expression, definition);

		if (!isExpectedNumberOfParameters(parameters.size())) {
			throw new TMQLRuntimeException("Unexpected number of arguments for function");
		}

		ISqlDefinition result = new SqlDefinition();
		result.setInternalAliasIndex(definition.getInternalAliasIndex());

		/*
		 * generate from parts
		 */
		List<IFromPart> fromParts = HashUtil.getList();
		for (ISqlDefinition parameter : parameters) {
			IFromPart fromPart = new FromPart(parameter.toString(), result.getAlias(), false);
			fromParts.add(fromPart);
		}

		/*
		 * add from part
		 */
		addFromParts(result, fromParts);
		/*
		 * get selection
		 */
		ISelection selection = getSelection(result,parameters, fromParts);
		result.addSelection(selection);
		/*
		 * get criterion
		 */
		String criterion = getCriterion(result, parameters,fromParts);
		if (criterion != null) {
			result.add(criterion);
		}
		return result;
	}

	/**
	 * Adding all from parts to the selection. Overwrite this method to change
	 * the base handling.
	 * 
	 * @param definition
	 *            the definition
	 * @param fromParts
	 *            the from parts
	 */
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		for (IFromPart fromPart : fromParts) {
			definition.addFromPart(fromPart);
		}
	}

	/**
	 * Generates the selection for this function result. Do not set the
	 * selection, this will be done by the caller.
	 * 
	 * @param definition
	 *            the definition
	 * @param parameters
	 *            the origin parameters
	 * @param fromParts
	 *            all from parts
	 * @return the generated selection
	 */
	protected abstract ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts);

	/**
	 * Generates the criterion for this selection if necessary. Do not set the
	 * criterion, this will be done by the caller.
	 * 
	 * @param definition
	 *            the definition
	 * @param parameters
	 *            the origin parameters
	 * @param fromParts
	 *            the from parts
	 * @return the criterion or <code>null</code>
	 */
	protected String getCriterion(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		return null;
	}

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
		translators.put(SubStringFunction.class, new SubStringFunctionTranslator());
		translators.put(StringConcatFunction.class, new StringConcatFunctionTranslator());
		translators.put(LengthFunction.class, new StringLengthFunctionTranslator());
		translators.put(StringGeqFunction.class, new StringGeqFunctionTranslator());
		translators.put(StringGtFunction.class, new StringGtFunctionTranslator());
		translators.put(StringLeqFunction.class, new StringLeqFunctionTranslator());
		translators.put(StringLtFunction.class, new StringLtFunctionTranslator());
	}

	public static IFunctionTranslator getFunctionTranslator(Class<? extends IFunction> functionType) {
		return translators.get(functionType);
	}

}
