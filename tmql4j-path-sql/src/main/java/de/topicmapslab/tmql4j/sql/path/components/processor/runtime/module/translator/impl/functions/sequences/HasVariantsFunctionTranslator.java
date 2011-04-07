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
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.HasVariantsFunction;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public class HasVariantsFunctionTranslator extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return HasVariantsFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {

		IFromPart fromPart = new FromPart(ISchema.Variants.TABLE, definition.getAlias(), true);
		definition.addFromPart(fromPart);

		ISqlDefinition theme = parameters.get(1);
		ISqlDefinition parent = parameters.get(0);

		ISqlDefinition scopeFilter = new SqlDefinition();

		IFromPart rel_themes = new FromPart(ISchema.RelThemes.TABLE, definition.getAlias(), true);
		scopeFilter.addFromPart(rel_themes);
		scopeFilter.add(new InCriterion(ISchema.RelThemes.ID_THEME, rel_themes.getAlias(), theme));
		scopeFilter.addSelection(new Selection(ISchema.Scopables.ID_SCOPE, rel_themes.getAlias()));

		definition.add(new InCriterion(ISchema.Scopables.ID_SCOPE, fromPart.getAlias(), scopeFilter));
		definition.add(new InCriterion(ISchema.Constructs.ID_PARENT, fromPart.getAlias(), parent));

		ISelection result = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		result.setCurrentTable(SqlTables.VARIANT);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		// VOID
	}
}
