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
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ZigZagFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants.ISqlFunctions;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants.ISqlKeywords;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class ZigZagFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return ZigZagFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISqlDefinition set = parameters.get(0);

		ISelection array = TranslatorUtils.getFunctionCall(ISqlKeywords.ARRAY, set.toString(), null, null, null);

		ISelection selection = TranslatorUtils.getFunctionCall(ISqlFunctions.ZIGZAG, array, definition.getAlias());
		selection.setCurrentTable(set.getLastSelection().getCurrentTable().recordOf());
		return selection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		// VOID
	}
}
