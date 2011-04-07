/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string;

import java.text.MessageFormat;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public abstract class SqlStringFunctionTranslatorImpl extends FunctionTranslatorImpl {

	private static final String PARAM_WITH_CAST = " CAST ( {0}.{1} AS varchar )";
	private static final String PARAM_WITHOUT_CAST = " {0}.{1}";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {

		StringBuilder builder = new StringBuilder();

		builder.append(getSqlFunction());
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);

		/*
		 * iterate over parameters
		 */
		int currentIndex = 0;
		for (currentIndex = 0; currentIndex < parameters.size(); currentIndex++) {
			if (currentIndex != 0) {
				builder.append(Comma.TOKEN);
			}
			/*
			 * translate to SQL
			 */
			String sqlParameter = generateSqlParameter(definition, parameters, fromParts, currentIndex);
			builder.append(sqlParameter);
		}

		/*
		 * get optional arguments
		 */
		String optionalArgument = getAdditionalArgument(definition, parameters, fromParts, currentIndex);
		while (optionalArgument != null) {
			if (currentIndex != 0) {
				builder.append(Comma.TOKEN);
			}
			builder.append(optionalArgument);
			optionalArgument = getAdditionalArgument(definition, parameters, fromParts, ++currentIndex);
		}
		builder.append(BracketRoundClose.TOKEN);

		/*
		 * create selection
		 */
		ISelection selection = new Selection(builder.toString(), definition.getAlias(), false);
		selection.setCurrentTable(getResultType());
		// selection.cast(ISqlConstants.ISqlTypes.VARCHAR);
		return selection;
	}

	/**
	 * Returns the function name
	 * 
	 * @return the function name
	 */
	protected abstract String getSqlFunction();

	/**
	 * Returns the result type of this function. The default is
	 * {@link SqlTables#STRING}
	 * 
	 * @return the result type.
	 */
	protected SqlTables getResultType() {
		return SqlTables.STRING;
	}

	/**
	 * Generates the SQL parameter for the current index
	 * 
	 * @param definition
	 *            the definition
	 * @param parameters
	 *            the origin parameters
	 * @param fromParts
	 *            the from parts
	 * @param currentIndex
	 *            the current index ( zero based )
	 * @return the generated SQL parameter
	 */
	protected String generateSqlParameter(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts, int currentIndex) {
		ISelection selection = parameters.get(currentIndex).getLastSelection();
		if (selection.getCurrentTable() == SqlTables.STRING) {
			return MessageFormat.format(PARAM_WITH_CAST, fromParts.get(currentIndex).getAlias(), selection.getAlias());
		}
		return MessageFormat.format(PARAM_WITHOUT_CAST, fromParts.get(currentIndex).getAlias(), selection.getAlias());
	}

	/**
	 * Returns additional arguments for calling the SQL function if necessary.
	 * The method will be called until <code>null</code> is returned
	 * 
	 * @param definition
	 *            the definition
	 * @param parameters
	 *            the origin parameters
	 * @param fromParts
	 *            the from parts
	 * @param currentIndex
	 *            the current index ( zero based )
	 * @return the additional argument or <code>null</code>
	 */
	protected String getAdditionalArgument(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts, int currentIndex) {
		return null;
	}

}
