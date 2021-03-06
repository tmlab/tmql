/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.url;

import java.util.List;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.url.UrlEncodeFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants.ISqlFunctions;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants.ISqlTypes;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class URLEncodeFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return UrlEncodeFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISelection url = parameters.get(0).getLastSelection();
		IFromPart from = fromParts.get(0);
		boolean aliased = url.getAlias() != null;
		ISelection selection = TranslatorUtils.getFunctionCall(ISqlFunctions.URLENCODE, aliased ? url.getAlias() : url.getColumn(), aliased ? from.getAlias() : null, ISqlTypes.VARCHAR,
				definition.getAlias());
		selection.setCurrentTable(SqlTables.STRING);
		return selection;
	}
}
