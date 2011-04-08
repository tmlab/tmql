/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistryImpl;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.AKOExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.AliasExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.AliasValueExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.AnchorInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.BindingSetInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.BooleanExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.BooleanPrimitiveInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ContentInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.DirectiveInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.EnvironmentClauseInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ExistsClauseInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ExistsQuantifiersInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.FilterPostfixInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ForAllClauseInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.FunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ISAExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.NavigationInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ParameterPairInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ParametersInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PathExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PostfixInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PostfixedExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PredicateInvocationInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PredicateInvocationRolePlayerExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.PrefixDirectiveInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ProjectionPostfixInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.SimpleContentInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.StepDefinitionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.StepInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.TupleExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.ValueExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.VariableAssignmentInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.components.interpreter.VariableInterpreter;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AKOExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AliasExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.BindingSet;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.BooleanPrimitive;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Directive;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ExistsClause;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ExistsQuantifiers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ForAllClause;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ISAExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ParameterPair;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Parameters;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Postfix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PrefixDirective;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ProjectionPostfix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Variable;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.VariableAssignment;

/**
 * Internal registry for interpreter classes. Provides access to interpreter
 * instances for all TMQL-expression types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InterpreterRegistry extends InterpreterRegistryImpl {

	/**
	 * 
	 * {@inheritDoc}
	 */
	protected void initialize() {
		registerInterpreterClass(AliasExpression.class, AliasExpressionInterpreter.class);
		registerInterpreterClass(AliasValueExpression.class, AliasValueExpressionInterpreter.class);
		registerInterpreterClass(BindingSet.class, BindingSetInterpreter.class);
		registerInterpreterClass(BooleanExpression.class, BooleanExpressionInterpreter.class);
		registerInterpreterClass(BooleanPrimitive.class, BooleanPrimitiveInterpreter.class);
		registerInterpreterClass(Content.class, ContentInterpreter.class);
		registerInterpreterClass(Directive.class, DirectiveInterpreter.class);
		registerInterpreterClass(EnvironmentClause.class, EnvironmentClauseInterpreter.class);
		registerInterpreterClass(ExistsClause.class, ExistsClauseInterpreter.class);
		registerInterpreterClass(ExistsQuantifiers.class, ExistsQuantifiersInterpreter.class);
		registerInterpreterClass(FilterPostfix.class, FilterPostfixInterpreter.class);
		registerInterpreterClass(ForAllClause.class, ForAllClauseInterpreter.class);
		registerInterpreterClass(FunctionInvocation.class, FunctionInvocationInterpreter.class);
		registerInterpreterClass(AKOExpression.class, AKOExpressionInterpreter.class);
		registerInterpreterClass(Anchor.class, AnchorInterpreter.class);
		registerInterpreterClass(ISAExpression.class, ISAExpressionInterpreter.class);
		registerInterpreterClass(Navigation.class, NavigationInterpreter.class);
		registerInterpreterClass(ParameterPair.class, ParameterPairInterpreter.class);
		registerInterpreterClass(Parameters.class, ParametersInterpreter.class);
		registerInterpreterClass(PathExpression.class, PathExpressionInterpreter.class);
		registerInterpreterClass(Postfix.class, PostfixInterpreter.class);
		registerInterpreterClass(PostfixedExpression.class, PostfixedExpressionInterpreter.class);
		registerInterpreterClass(PredicateInvocation.class, PredicateInvocationInterpreter.class);
		registerInterpreterClass(PredicateInvocationRolePlayerExpression.class, PredicateInvocationRolePlayerExpressionInterpreter.class);
		registerInterpreterClass(PrefixDirective.class, PrefixDirectiveInterpreter.class);
		registerInterpreterClass(ProjectionPostfix.class, ProjectionPostfixInterpreter.class);
		registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
		registerInterpreterClass(SimpleContent.class, SimpleContentInterpreter.class);
		registerInterpreterClass(StepDefinition.class, StepDefinitionInterpreter.class);
		registerInterpreterClass(Step.class, StepInterpreter.class);
		registerInterpreterClass(TupleExpression.class, TupleExpressionInterpreter.class);
		registerInterpreterClass(ValueExpression.class, ValueExpressionInterpreter.class);
		registerInterpreterClass(Variable.class, VariableInterpreter.class);
		registerInterpreterClass(VariableAssignment.class, VariableAssignmentInterpreter.class);
		registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
	}
}
