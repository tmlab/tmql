package de.topicmapslab.tmql4j.extension.tmml.core;

import de.topicmapslab.tmql4j.common.context.InterpreterRegistry;
import de.topicmapslab.tmql4j.common.core.exception.TMQLException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteClause;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.InsertClause;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.InsertExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.MergeExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.TopicDefinition;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateClause;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Add;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.All;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Associations;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Cascade;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Delete;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Merge;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Names;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Occurrences;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Set;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Topics;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Update;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.DeleteClauseInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.DeleteExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.ExtendedPredicateInvocationInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.ExtendedPredicateInvocationRolePlayerExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.ExtendedValueExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.InsertClauseInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.InsertExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.MergeExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.TopicDefinitionInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.UpdateClauseInterpreter;
import de.topicmapslab.tmql4j.extension.tmml.interpreter.UpdateExpressionInterpreter;
import de.topicmapslab.tmql4j.extensions.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtension;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.lexer.core.TokenRegistry;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

public class TMQLModifcationPartExtensionPoint implements ILanguageExtension {

	private ModificationPartLanguageExtensionEntry entry;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean extendsExpressionType(
			Class<? extends IExpression> expressionType) {
		return expressionType.equals(QueryExpression.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILanguageExtensionEntry getLanguageExtensionEntry() {
		return entry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtensionPointId() {
		return "extension-modification-part";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerExtension(ITMQLRuntime runtime)
			throws TMQLExtensionRegistryException {
		/*
		 * register tokens
		 */
		TokenRegistry registry = runtime.getLanguageContext()
				.getTokenRegistry();
		registry.register(Add.class);
		registry.register(All.class);
		registry.register(Cascade.class);
		registry.register(Delete.class);
		registry.register(Insert.class);
		registry.register(Merge.class);
		registry.register(Names.class);
		registry.register(Occurrences.class);
		registry.register(Set.class);
		registry.register(Update.class);
		registry.register(Topics.class);
		registry.register(Associations.class);

		/*
		 * register expression interpreter
		 */
		InterpreterRegistry interpreterRegistry = runtime.getLanguageContext()
				.getInterpreterRegistry();
		try {
			interpreterRegistry.registerInterpreterClass(DeleteClause.class,
					DeleteClauseInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					DeleteExpression.class, DeleteExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(InsertClause.class,
					InsertClauseInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					InsertExpression.class, InsertExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(UpdateClause.class,
					UpdateClauseInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					UpdateExpression.class, UpdateExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(MergeExpression.class,
					MergeExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(TopicDefinition.class,
					TopicDefinitionInterpreter.class);
			// interpreterRegistry.registerInterpreterClass(
			// AssociationDefinition.class,
			// AssociationDefinitionInterpreter.class);
			// interpreterRegistry.registerInterpreterClass(
			// AssociationDefinitionPart.class,
			// AssociationDefinitionPartInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					PredicateInvocation.class,
					ExtendedPredicateInvocationInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					PredicateInvocationRolePlayerExpression.class,
					ExtendedPredicateInvocationRolePlayerExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					ValueExpression.class,
					ExtendedValueExpressionInterpreter.class);
			interpreterRegistry
					.registerInterpreterClass(
							de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression.class,
							QueryExpressionInterpreter.class);
		} catch (TMQLException e) {
			throw new TMQLExtensionRegistryException(e);
		}

		entry = new ModificationPartLanguageExtensionEntry();
	}
}
