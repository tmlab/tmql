/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.grammar.pragma;

import de.topicmapslab.tmql4j.components.interpreter.IPragma;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.TemplateManager;
import de.topicmapslab.tmql4j.template.grammar.lexical.Template;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * An extension for the pragma interpreter of the PATH style
 * 
 * @author Sven Krosse
 * 
 */
public class TemplatePragma implements IPragma {

	/**
	 * {@inheritDoc}
	 */
	public String getIdentifier() {
		return Template.TOKEN;
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(ITMQLRuntime runtime, IContext context, String value) throws TMQLRuntimeException {
		TemplateManager.getTemplateManager().setTemplate(context.getQuery().getTopicMap(), de.topicmapslab.tmql4j.template.components.processor.runtime.module.Template.ANONYMOUS,
				LiteralUtils.asString(value));
	}

}
