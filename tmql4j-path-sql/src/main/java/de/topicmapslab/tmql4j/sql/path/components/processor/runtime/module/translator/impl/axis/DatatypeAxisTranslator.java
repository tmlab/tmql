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
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

public class DatatypeAxisTranslator extends AxisTranslatorImpl {

	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(ISchema.DatatypeAwares.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.DatatypeAwares.ID_DATATYPE, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.LOCATOR);
		result.addSelection(sel);
		return result;
	}

	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		ISelection lastSel = definition.getLastSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(optionalType == null ? ISchema.DatatypeAwares.TABLE : ISchema.Occurrences.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		if (lastSel.getCurrentTable() == SqlTables.STRING) {
			String alias = TranslatorUtils.addLocatorSelection(result, lastSel.getColumn());
			String condition = ConditionalUtils.equal(alias, ISchema.Constructs.ID, fromPart.getAlias(), ISchema.DatatypeAwares.ID_DATATYPE);
			result.add(condition);
		} else {
			String condition = ConditionalUtils.equal(lastSel, fromPart.getAlias(), ISchema.DatatypeAwares.ID_DATATYPE);
			result.add(condition);
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.ANY);
		result.addSelection(sel);
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, ISchema.Typeables.ID_TYPE, fromPart.getAlias());
		return result;
	}

}
