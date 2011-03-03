/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.extension;

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
import de.topicmapslab.tmql4j.insert.components.interpreter.InsertClauseInterpreter;
import de.topicmapslab.tmql4j.insert.components.interpreter.InsertExpressionInterpreter;
import de.topicmapslab.tmql4j.insert.components.interpreter.NonInterpretedContentInterpreter;
import de.topicmapslab.tmql4j.insert.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.insert.components.interpreter.TMContentInterpreter;
import de.topicmapslab.tmql4j.insert.components.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertClause;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertExpression;
import de.topicmapslab.tmql4j.insert.grammar.productions.NonInterpretedContent;
import de.topicmapslab.tmql4j.insert.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.insert.grammar.productions.TMContent;
import de.topicmapslab.tmql4j.insert.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Insert;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007InsertQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		ITokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(Insert.class);

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();
		interpreterRegistry.registerInterpreterClass(InsertClause.class, InsertClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(InsertExpression.class, InsertExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(NonInterpretedContent.class, NonInterpretedContentInterpreter.class);
		interpreterRegistry.registerInterpreterClass(WhereClause.class, WhereClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(TMContent.class, TMContentInterpreter.class);
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
