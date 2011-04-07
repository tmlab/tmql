/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public class ScopeAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(ISchema.RelThemes.TABLE, result.getAlias(), true);
		result.addFromPart(fromPartRel);
		IFromPart fromPart = new FromPart(ISchema.Scopables.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID);
		result.add(condition);
		condition = ConditionalUtils.equal(fromPart.getAlias(), ISchema.Scopables.ID_SCOPE, fromPartRel.getAlias(), ISchema.Scopables.ID_SCOPE);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.RelThemes.ID_THEME, fromPartRel.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(ISchema.RelThemes.TABLE, result.getAlias(), true);
		result.addFromPart(fromPartRel);
		IFromPart fromPart = new FromPart(ISchema.Scopables.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPartRel.getAlias(), ISchema.RelThemes.ID_THEME);
		result.add(condition);
		condition = ConditionalUtils.equal(fromPart.getAlias(), ISchema.Scopables.ID_SCOPE, fromPartRel.getAlias(), ISchema.Scopables.ID_SCOPE);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ANY);
		return result;
	}
}
