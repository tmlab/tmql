/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.functions.string;

import java.util.List;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.SubStringFunction;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.path.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 *
 */
public class SubStringFunctionTranslator extends SqlStringFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IFunction> getFunction() {
		return SubStringFunction.class;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
//
//		List<ISqlDefinition> parameters = parametersToSql(runtime, context, expression, definition);
//
//		if (!isExpectedNumberOfParameters(parameters.size())) {
//			throw new TMQLRuntimeException("Unexpected number of arguments for function");
//		}
//		ISqlDefinition string = parameters.get(0);
//		ISqlDefinition fromIndexes = parameters.get(1);
//		ISqlDefinition toIndexes = parameters.get(2);
//
//		ISqlDefinition result = new SqlDefinition();
//		result.setInternalAliasIndex(definition.getInternalAliasIndex());
//
//		IFromPart stringPart = new FromPart(string.toString(), result.getAlias(), false);
//		result.addFromPart(stringPart);
//		IFromPart fromIndexPart = new FromPart(fromIndexes.toString(), result.getAlias(), false);
//		result.addFromPart(fromIndexPart);
//		IFromPart toIndexPart = new FromPart(toIndexes.toString(), result.getAlias(), false);
//		result.addFromPart(toIndexPart);
//
//		StringBuilder builder = new StringBuilder();
//
//		builder.append(ISqlConstants.ISqlFunctions.SUBSTRING);
//		builder.append(ISqlConstants.WHITESPACE);
//		builder.append(BracketRoundOpen.TOKEN);
//		builder.append(stringPart.getAlias());
//		builder.append(Dot.TOKEN);
//		builder.append(string.getLastSelection().getAlias());		
//		builder.append(Comma.TOKEN);		
//		builder.append(fromIndexPart.getAlias());
//		builder.append(Dot.TOKEN);
//		builder.append(fromIndexes.getLastSelection().getAlias());
//		builder.append(Plus.TOKEN);
//		builder.append(Integer.toString(1));
//		builder.append(Comma.TOKEN);
//		builder.append(toIndexPart.getAlias());
//		builder.append(Dot.TOKEN);
//		builder.append(toIndexes.getLastSelection().getAlias());
//		builder.append(Minus.TOKEN);
//		builder.append(fromIndexPart.getAlias());
//		builder.append(Dot.TOKEN);
//		builder.append(fromIndexes.getLastSelection().getAlias());
//		builder.append(BracketRoundClose.TOKEN);
//
//		ISelection selection = new Selection(builder.toString(), result.getAlias(), false);
//		selection.setCurrentTable(SqlTables.STRING);
//		result.addSelection(selection);
//		return result;
//	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getSqlFunction() {
		return ISqlConstants.ISqlFunctions.SUBSTRING;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String generateSqlParameter(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts, int currentIndex) {
		String parameter = super.generateSqlParameter(definition, parameters, fromParts, currentIndex);
		if (currentIndex == 1) {
			parameter += Plus.TOKEN;
			parameter += Integer.toString(1);
		}else if ( currentIndex == 2 ){
			parameter += Minus.TOKEN;
			parameter += fromParts.get(1).getAlias();
			parameter += Dot.TOKEN;
			parameter += parameters.get(1).getLastSelection().getAlias();
		}		
		return parameter;
	}

}
