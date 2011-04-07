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

import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;

/**
 * @author Sven Krosse
 * 
 */
public abstract class BinarySetFunctionTranslatorImpl extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		/*
		 * create the set operation
		 */
		String criterion = ConditionalUtils.compare(parameters.get(0), getOperator(), parameters.get(1));
		/*
		 * create from part from the set operation
		 */
		return getSelection(definition, criterion, parameters, fromParts);
	}

	/**
	 * Return the selection of the inner SQL definition
	 * 
	 * @param definition
	 *            the definition
	 * @param setOperation
	 *            the SQL definition of the set operation
	 * @param parameters
	 *            the parameters
	 * @param fromParts
	 *            the form parts
	 * @return the inner selection
	 */
	protected abstract ISelection getSelection(ISqlDefinition definition, String setOperation, List<ISqlDefinition> parameters, List<IFromPart> fromParts);

	/**
	 * Returns the set operator ( INTERSECT or EXCEPT )
	 * 
	 * @return the set operator
	 */
	protected abstract String getOperator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		// NOTHING TO DO
	}

}
