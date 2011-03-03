/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.template.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.template.components.interpreter.TemplateDefinitionInterpreter;
import de.topicmapslab.tmql4j.template.components.interpreter.UseExpressionInterpreter;
import de.topicmapslab.tmql4j.template.grammar.lexical.CTM;
import de.topicmapslab.tmql4j.template.grammar.lexical.Define;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQR;
import de.topicmapslab.tmql4j.template.grammar.lexical.Redefine;
import de.topicmapslab.tmql4j.template.grammar.lexical.Template;
import de.topicmapslab.tmql4j.template.grammar.lexical.Use;
import de.topicmapslab.tmql4j.template.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.template.grammar.productions.TemplateDefinition;
import de.topicmapslab.tmql4j.template.grammar.productions.UseExpression;

/**
 * @author Sven Krosse
 * 
 */
public class TemplateQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		ITokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(CTM.class);
		tokens.register(Use.class);
		tokens.register(Template.class);
		tokens.register(Define.class);
		tokens.register(JTMQR.class);
		tokens.register(Redefine.class);

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();

		interpreterRegistry.registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(UseExpression.class, UseExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(TemplateDefinition.class, TemplateDefinitionInterpreter.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IExpression> getExpressionType() {
		return de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean extendsExpressionType(Class<? extends IExpression> expressionType) {
		return de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression.class.equals(expressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValidProduction(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller) {
		return QueryExpression.isValid(tmqlTokens);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExpression parse(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller, boolean autoAdd) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		IExpression expression = new QueryExpression(caller, tmqlTokens, tokens, runtime);
		if (autoAdd) {
			caller.addExpression(expression);
		}
		return expression;
	}

}
