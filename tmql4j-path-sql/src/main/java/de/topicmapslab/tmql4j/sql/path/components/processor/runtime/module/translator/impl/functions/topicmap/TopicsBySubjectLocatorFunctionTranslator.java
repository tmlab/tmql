/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.topicmap;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.topicmap.TopicsBySubjectLocator;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public class TopicsBySubjectLocatorFunctionTranslator extends TopicsByFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return TopicsBySubjectLocator.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getRelationColumn() {
		return ISchema.RelSubjectLocators.ID_TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getRelationName() {
		return ISchema.RelSubjectLocators.TABLE;
	}

}
