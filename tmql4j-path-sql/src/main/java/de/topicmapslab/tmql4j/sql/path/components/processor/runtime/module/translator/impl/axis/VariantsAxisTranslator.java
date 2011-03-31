/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

import java.text.MessageFormat;

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
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class VariantsAxisTranslator extends AxisTranslatorImpl {

	static final String COL_ID = "id";
	static final String COL_TYPE = "id_type";
	static final String COL_PARENT = "id_parent";
	static final String BACKWARD_SELECTION = "id_parent";
	static final String CONSTRUCTS = "constructs";
	static final String VARIANTS = "variants";
	static final String NAMES = "names";
	static final String OCCURRENCES = "occurrences";
	static final String FORWARD_CONDITION = "{0} = {1}.id_parent";
	static final String BACKWARD_CONDITION = "{0} = {1}.id";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for variants
		 */
		IFromPart fromPart;
		fromPart = new FromPart(VARIANTS, result.getAlias(), true);		
		result.addFromPart(fromPart);

		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(COL_ID, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.VARIANT);
		result.addSelection(sel);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		ISelection selection = definition.getLastSelection();

		if (optionalType == null) {
			/*
			 * append from clause for characteristics
			 */
			IFromPart fromPart = new FromPart(VARIANTS, result.getAlias(), true);
			result.addFromPart(fromPart);
			/*
			 * append condition as connection to incoming SQL definition
			 */
			result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
			/*
			 * add new selection
			 */
			ISelection sel = new Selection(BACKWARD_SELECTION, fromPart.getAlias());
			result.addSelection(sel);
			sel.setCurrentTable(SqlTables.NAME);
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, COL_PARENT, fromPart.getAlias());
		} else {
			/*
			 * append from clause for characteristics
			 */
			IFromPart fromPart = new FromPart(NAMES, result.getAlias(), true);
			result.addFromPart(fromPart);
			/*
			 * append condition as connection to incoming SQL definition
			 */
			ISqlDefinition variants = new SqlDefinition();
			IFromPart variantsFromPart = new FromPart(VARIANTS, result.getAlias(), true);
			variants.addFromPart(variantsFromPart);
			variants.addSelection(new Selection(COL_PARENT, variantsFromPart.getAlias()));
			variants.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), variantsFromPart.getAlias()));

			InCriterion criterion = new InCriterion(COL_ID, fromPart.getAlias(), variants);
			result.add(criterion);
			/*
			 * add new selection
			 */
			ISelection sel = new Selection(COL_ID, fromPart.getAlias());
			result.addSelection(sel);
			sel.setCurrentTable(SqlTables.NAME);
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, COL_TYPE, fromPart.getAlias());
		}		
		return result;
	}

}
