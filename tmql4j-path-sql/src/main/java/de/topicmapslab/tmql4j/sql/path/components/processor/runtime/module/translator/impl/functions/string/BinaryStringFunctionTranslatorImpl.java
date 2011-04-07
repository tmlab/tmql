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

import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;

/**
 * @author Sven Krosse
 * 
 */
public abstract class BinaryStringFunctionTranslatorImpl extends FunctionTranslatorImpl {

	private static final String CRITERION = " CAST ( {0}.{1} AS varchar ) {2}  CAST ( {3}.{4} AS varchar ) ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {

		String criterion = MessageFormat.format(CRITERION, fromParts.get(0).getAlias(), parameters.get(0).getLastSelection().getAlias(), getOperator(), fromParts.get(1).getAlias(), parameters.get(1)
				.getLastSelection().getAlias());
		ISelection selection = new Selection(criterion, definition.getAlias(), false);
		selection.setCurrentTable(SqlTables.STRING);
		return selection;
	}

	protected abstract String getOperator();

}
