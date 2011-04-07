/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.sequences;

import java.text.MessageFormat;
import java.util.List;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.CompareFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class ConcatFunctionTranslator extends BinarySetFunctionTranslatorImpl {

	/**
	 * 
	 */
	private static final String SELECTION = "unnest ( ARRAY ({0}) )";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getOperator() {
		return ISqlConstants.ISqlOperators.UNION;
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
		/*
		 * add selection clause counting the results and check if them are empty
		 */
		String sel = MessageFormat.format(SELECTION, setOperation);
		ISelection selection = new Selection(sel, definition.getAlias(), false);
		selection.setCurrentTable(parameters.get(0).getLastSelection().getCurrentTable());
		return selection;
	}

}
