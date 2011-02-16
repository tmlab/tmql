/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.extension;

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
import de.topicmapslab.tmql4j.update.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.update.components.interpreter.TopicDefinitionInterpreter;
import de.topicmapslab.tmql4j.update.components.interpreter.UpdateClauseInterpreter;
import de.topicmapslab.tmql4j.update.components.interpreter.UpdateExpressionInterpreter;
import de.topicmapslab.tmql4j.update.components.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.update.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.update.grammar.productions.TopicDefinition;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateClause;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateExpression;
import de.topicmapslab.tmql4j.update.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.update.grammar.tokens.Add;
import de.topicmapslab.tmql4j.update.grammar.tokens.Associations;
import de.topicmapslab.tmql4j.update.grammar.tokens.Names;
import de.topicmapslab.tmql4j.update.grammar.tokens.Occurrences;
import de.topicmapslab.tmql4j.update.grammar.tokens.Remove;
import de.topicmapslab.tmql4j.update.grammar.tokens.Set;
import de.topicmapslab.tmql4j.update.grammar.tokens.Topics;
import de.topicmapslab.tmql4j.update.grammar.tokens.Update;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007UpdateQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		ITokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(Add.class);
		tokens.register(Associations.class);
		tokens.register(Names.class);
		tokens.register(Occurrences.class);
		tokens.register(Remove.class);
		tokens.register(Set.class);
		tokens.register(Topics.class);
		tokens.register(Update.class);

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();
		interpreterRegistry.registerInterpreterClass(WhereClause.class, WhereClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(TopicDefinition.class, TopicDefinitionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(UpdateClause.class, UpdateClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(UpdateExpression.class, UpdateExpressionInterpreter.class);
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
