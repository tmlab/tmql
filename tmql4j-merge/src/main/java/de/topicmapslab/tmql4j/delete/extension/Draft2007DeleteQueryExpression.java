/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.delete.components.interpreter.DeleteClauseInterpreter;
import de.topicmapslab.tmql4j.delete.components.interpreter.DeleteExpressionInterpreter;
import de.topicmapslab.tmql4j.delete.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.delete.components.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteClause;
import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression;
import de.topicmapslab.tmql4j.delete.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.delete.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.delete.grammar.tokens.Cascade;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007DeleteQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		ITokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(Cascade.class);

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();
		interpreterRegistry.registerInterpreterClass(DeleteClause.class, DeleteClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(DeleteExpression.class, DeleteExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(WhereClause.class, WhereClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
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
