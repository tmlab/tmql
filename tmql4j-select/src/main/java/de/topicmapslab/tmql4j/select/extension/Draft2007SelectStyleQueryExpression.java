/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.select.components.interpreter.FromClauseInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.LimitClauseInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.OffsetClauseInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.OrderByClauseInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.SelectClauseInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.SelectExpressionInterpreter;
import de.topicmapslab.tmql4j.select.components.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.select.grammar.lexical.From;
import de.topicmapslab.tmql4j.select.grammar.lexical.Limit;
import de.topicmapslab.tmql4j.select.grammar.lexical.Offset;
import de.topicmapslab.tmql4j.select.grammar.lexical.Select;
import de.topicmapslab.tmql4j.select.grammar.lexical.Unique;
import de.topicmapslab.tmql4j.select.grammar.productions.FromClause;
import de.topicmapslab.tmql4j.select.grammar.productions.LimitClause;
import de.topicmapslab.tmql4j.select.grammar.productions.OffsetClause;
import de.topicmapslab.tmql4j.select.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.select.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.select.grammar.productions.SelectClause;
import de.topicmapslab.tmql4j.select.grammar.productions.SelectExpression;
import de.topicmapslab.tmql4j.select.grammar.productions.WhereClause;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007SelectStyleQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {

		TokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(From.class);
		tokens.register(Limit.class);
		tokens.register(Offset.class);
		tokens.register(Select.class);
		tokens.register(Unique.class);

		InterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();

		interpreterRegistry.registerInterpreterClass(FromClause.class, FromClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(LimitClause.class, LimitClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(OffsetClause.class, OffsetClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(OrderByClause.class, OrderByClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(SelectClause.class, SelectClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(SelectExpression.class, SelectExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(WhereClause.class, WhereClauseInterpreter.class);
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
