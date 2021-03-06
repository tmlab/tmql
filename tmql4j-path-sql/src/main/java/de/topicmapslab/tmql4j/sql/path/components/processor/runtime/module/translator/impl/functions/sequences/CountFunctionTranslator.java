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
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.CountFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.CountSelection;
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
public class CountFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return CountFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISqlDefinition param = parameters.get(0);
		IFromPart fromPart = fromParts.get(0);
		ISelection sel = new CountSelection(new Selection(param.getLastSelection().getColumn(), fromPart.getAlias()), definition.getAlias());
		sel.setCurrentTable(SqlTables.INTEGER);
		return sel;
	}

}
