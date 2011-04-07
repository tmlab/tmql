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

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.SliceFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;

/**
 * @author Sven Krosse
 * 
 */
public class SliceFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return SliceFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISqlDefinition param0 = parameters.get(0);
		IFromPart fromPart = fromParts.get(0);
		/*
		 * get offset and limit
		 */
		int offset, limit;
		try {
			ISqlDefinition param1 = parameters.get(1);
			offset = Integer.parseInt(param1.getLastSelection().getColumn());
			if (offset < 0) {
				offset = 0;
			}
			ISqlDefinition param2 = parameters.get(2);
			limit = Integer.parseInt(param2.getLastSelection().getColumn());
			if (limit < 0) {
				limit = 0;
			}
		} catch (Exception e) {
			throw new TMQLRuntimeException("Offset and limit argument must be an integer!");
		}

		definition.setOffset(offset);
		definition.setLimit(limit);

		ISelection lastSelection = param0.getLastSelection();
		ISelection sel = new Selection(lastSelection.getColumn(), fromPart.getAlias());
		sel.setCurrentTable(lastSelection.getCurrentTable());
		return sel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		definition.addFromPart(fromParts.get(0));
	}

}
