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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class VariantsAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for variants
		 */
		IFromPart fromPart;
		fromPart = new FromPart(ISchema.Variants.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);

		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID_PARENT);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.VARIANT);
		result.addSelection(sel);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		ISelection selection = definition.getLastSelection();

		if (optionalType == null) {
			/*
			 * append from clause for characteristics
			 */
			IFromPart fromPart = new FromPart(ISchema.Variants.TABLE, result.getAlias(), true);
			result.addFromPart(fromPart);
			/*
			 * append condition as connection to incoming SQL definition
			 */
			String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID);
			result.add(condition);
			/*
			 * add new selection
			 */
			ISelection sel = new Selection(ISchema.Constructs.ID_PARENT, fromPart.getAlias());
			result.addSelection(sel);
			sel.setCurrentTable(SqlTables.NAME);
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, ISchema.Constructs.ID_PARENT, fromPart.getAlias());
		} else {
			/*
			 * append from clause for characteristics
			 */
			IFromPart fromPart = new FromPart(ISchema.Names.TABLE, result.getAlias(), true);
			result.addFromPart(fromPart);
			/*
			 * append condition as connection to incoming SQL definition
			 */
			ISqlDefinition variants = new SqlDefinition();
			IFromPart variantsFromPart = new FromPart(ISchema.Variants.TABLE, result.getAlias(), true);
			variants.addFromPart(variantsFromPart);
			variants.addSelection(new Selection(ISchema.Constructs.ID_PARENT, variantsFromPart.getAlias()));
			String condition = ConditionalUtils.equal(selection, variantsFromPart.getAlias(), ISchema.Constructs.ID);
			variants.add(condition);
			InCriterion criterion = new InCriterion(ISchema.Constructs.ID, fromPart.getAlias(), variants);
			result.add(criterion);
			/*
			 * add new selection
			 */
			ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
			result.addSelection(sel);
			sel.setCurrentTable(SqlTables.NAME);
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, ISchema.Typeables.ID_TYPE, fromPart.getAlias());
		}
		return result;
	}

}
