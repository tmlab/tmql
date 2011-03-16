package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

public class DatatypeAxisTranslator extends AxisTranslatorImpl {

	private static final String ID = "id";
	private static final String BACKWARD_CONDITION = "{0} = {1}.id_datatype";
	private static final String BACKWARD_CONDITION_ALIAS = "{0}.{1} = {2}.id_datatype";
	private static final String DATATYPEAWARES = "datatypeawares";
	private static final String FORWARD_CONDITION = "{0} = {1}.id";
	private static final String ID_DATATYPE = "id_datatype";

	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context,
			String optionalType, ISqlDefinition definition)
			throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(DATATYPEAWARES, result.getAlias(),
				false);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION,
				selection.getSelection(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ID_DATATYPE, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.LOCATOR);
		result.addSelection(sel);
		return result;
	}

	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context,
			String optionalType, ISqlDefinition definition)
			throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		ISelection lastSel = definition.getLastSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(DATATYPEAWARES, result.getAlias(),
				false);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		if (lastSel.getCurrentTable() == SqlTables.STRING) {
			String alias = TranslatorUtils.addLocatorSelection(result,
					lastSel.getSelection());
			result.add(MessageFormat.format(BACKWARD_CONDITION_ALIAS, alias,
					ID, fromPart.getAlias()));
		} else {
			result.add(MessageFormat.format(BACKWARD_CONDITION,
					lastSel.getSelection(), fromPart.getAlias()));
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ID, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.ANY);
		result.addSelection(sel);
		return result;
	}

}
