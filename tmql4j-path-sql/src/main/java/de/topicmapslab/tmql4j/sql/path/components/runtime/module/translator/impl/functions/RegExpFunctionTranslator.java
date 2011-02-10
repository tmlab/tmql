/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.functions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.RegExpFunction;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class RegExpFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IFunction> getFunction() {
		return RegExpFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {

		List<ISqlDefinition> parameters = parametersToSql(runtime, context, expression, definition);

		if (!isExpectedNumberOfParameters(parameters.size())) {
			throw new TMQLRuntimeException("Unexpected number of arguments for function");
		}

		ISqlDefinition string = parameters.get(0);
		ISqlDefinition regExps = parameters.get(1);

		ISqlDefinition result = new SqlDefinition();
		result.setInternalAliasIndex(definition.getInternalAliasIndex());

		IFromPart stringPart = new FromPart(string.toString(), result.getAlias(), false);
		result.addFromPart(stringPart);
		IFromPart regExpPart = new FromPart(regExps.toString(), result.getAlias(), false);
		result.addFromPart(regExpPart);

		StringBuilder builder = new StringBuilder();
		builder.append(stringPart.getAlias());
		builder.append(Dot.TOKEN);
		builder.append(string.getLastSelection().getAlias());
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlOperators.REGEXP);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(regExpPart.getAlias());
		builder.append(Dot.TOKEN);
		builder.append(regExps.getLastSelection().getAlias());
		result.add(builder.toString());

		ISelection selection = new Selection(string.getLastSelection().getAlias(), stringPart.getAlias());
		selection.setCurrentTable(SqlTables.STRING);
		result.addSelection(selection);
		return result;
	}

}
