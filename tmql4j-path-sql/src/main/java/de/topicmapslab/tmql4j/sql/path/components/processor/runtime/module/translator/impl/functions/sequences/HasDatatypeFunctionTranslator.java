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

import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.HasDatatypeFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public class HasDatatypeFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return HasDatatypeFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {

		ISelection param = parameters.get(0).getLastSelection();
		IFromPart paramFrom = fromParts.get(0);
		ISelection result;
		if (param.getCurrentTable() == SqlTables.OCCURRENCE || param.getCurrentTable() == SqlTables.VARIANT) {

			IFromPart fromPart = new FromPart(ISchema.DatatypeAwares.TABLE, definition.getAlias(), true);
			definition.addFromPart(fromPart);

			String condition = ConditionalUtils.equal(fromPart.getAlias(), ISchema.Constructs.ID, paramFrom.getAlias(), param.getColumn());
			definition.add(condition);

			result = new Selection(ISchema.DatatypeAwares.ID_DATATYPE, fromPart.getAlias());
			result.setCurrentTable(SqlTables.LOCATOR);
		} else if (param.getCurrentTable() == SqlTables.NAME) {
			result = new Selection("'" + Namespaces.XSD.STRING + "'");
			result.setCurrentTable(SqlTables.STRING);
		} else {
			result = new Selection("'" + Namespaces.XSD.ANY + "'");
			result.setCurrentTable(SqlTables.STRING);
		}
		return result;
	}

}
