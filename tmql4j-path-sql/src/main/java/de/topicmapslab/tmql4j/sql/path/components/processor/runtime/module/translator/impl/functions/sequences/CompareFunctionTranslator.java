/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.sequences;

import java.util.List;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.CompareFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class CompareFunctionTranslator extends BinarySetFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getOperator() {
		return ISqlConstants.ISqlOperators.INTERSECT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return CompareFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, String setOperation, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		FromPart fromPart = new FromPart(setOperation, definition.getAlias(), false);
		definition.addFromPart(fromPart);
		/*
		 * add selection clause counting the results and check if them are empty
		 */
		ISelection selection = ConditionalUtils.nonEmpty();
		selection.setCurrentTable(SqlTables.BOOLEAN);
		return selection;
	}

}
