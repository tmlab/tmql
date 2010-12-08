/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.flwr.components.interpreter.FlwrExpressionInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.ForClauseInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.GroupByClauseInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.NonInterpretedContentInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.OrderByClauseInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.ReturnClauseInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.TMContentInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.flwr.components.interpreter.XMLContentInterpreter;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.For;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.Return;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlEndTag;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlStartTag;
import de.topicmapslab.tmql4j.flwr.grammar.productions.FlwrExpression;
import de.topicmapslab.tmql4j.flwr.grammar.productions.ForClause;
import de.topicmapslab.tmql4j.flwr.grammar.productions.GroupByClause;
import de.topicmapslab.tmql4j.flwr.grammar.productions.NonInterpretedContent;
import de.topicmapslab.tmql4j.flwr.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.flwr.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.flwr.grammar.productions.ReturnClause;
import de.topicmapslab.tmql4j.flwr.grammar.productions.TMContent;
import de.topicmapslab.tmql4j.flwr.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.flwr.grammar.productions.XMLContent;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007FlwrStyleQueryExpression implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		ITokenRegistry tokens = runtime.getLanguageContext().getTokenRegistry();
		tokens.register(For.class);
		tokens.register(Return.class);
		tokens.register(XmlEndTag.class);
		tokens.register(XmlStartTag.class);

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();

		interpreterRegistry.registerInterpreterClass(FlwrExpression.class, FlwrExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(ForClause.class, ForClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(GroupByClause.class, GroupByClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(NonInterpretedContent.class, NonInterpretedContentInterpreter.class);
		interpreterRegistry.registerInterpreterClass(OrderByClause.class, OrderByClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
		interpreterRegistry.registerInterpreterClass(ReturnClause.class, ReturnClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(TMContent.class, TMContentInterpreter.class);
		interpreterRegistry.registerInterpreterClass(WhereClause.class, WhereClauseInterpreter.class);
		interpreterRegistry.registerInterpreterClass(XMLContent.class, XMLContentInterpreter.class);
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
