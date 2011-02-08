/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.Delete;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.TemplateManager;
import de.topicmapslab.tmql4j.template.grammar.lexical.Define;
import de.topicmapslab.tmql4j.template.grammar.lexical.Redefine;
import de.topicmapslab.tmql4j.template.grammar.productions.TemplateDefinition;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 *
 */
public class TemplateDefinitionInterpreter extends ExpressionInterpreterImpl<TemplateDefinition> {

	/**
	 * 
	 */
	private static final String THE_TEMPLATE_IS_ALREADY_DEFINE_USE_REDEFINE_TO_OVERWRITE = "The template is already define, use REDEFINE to overwrite!";

	/**
	 * the constructor 
	 * @param ex the expression
	 */
	public TemplateDefinitionInterpreter(TemplateDefinition ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		Class<? extends IToken> token = getTmqlTokens().get(0);
		String name = LiteralUtils.asString(getTokens().get(2));
		
		if ( Define.class.equals(token)){
			String definition = LiteralUtils.asString(getTokens().get(3));
			if ( TemplateManager.getTemplateManager().isKnownTemplate(context.getQuery().getTopicMap(), name)){
				throw new TMQLRuntimeException(THE_TEMPLATE_IS_ALREADY_DEFINE_USE_REDEFINE_TO_OVERWRITE); 
			}
			TemplateManager.getTemplateManager().setTemplate(context.getQuery().getTopicMap(), name, definition);
		}else if (Redefine.class.equals(token)){
			String definition = LiteralUtils.asString(getTokens().get(3));
			TemplateManager.getTemplateManager().setTemplate(context.getQuery().getTopicMap(), name, definition);
		}else if (Delete.class.equals(token)){
			TemplateManager.getTemplateManager().removeTemplate(context.getQuery().getTopicMap(), name);
		}
		
		return QueryMatches.emptyMatches();
	}

}
