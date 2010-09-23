/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.context;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import de.topicmapslab.tmql4j.common.core.exception.TMQLException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.AKOExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.AnchorInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.BindingSetInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.BooleanExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.BooleanPrimitiveInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ContentInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.DirectiveInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.EnvironmentClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ExistsClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ExistsQuantifiersInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.FilterPostfixInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.FlwrExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ForAllClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ForClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.FromClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.FunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ISAExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.LimitClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.NavigationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.NonInterpretedContentInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.OffsetClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.OrderByClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ParameterPairInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ParametersInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PathExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PostfixInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PostfixedExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PragmaInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PredicateInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PredicateInvocationRolePlayerExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PrefixDirectiveInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ProjectionPostfixInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ReturnClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.SelectClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.SelectExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.SimpleContentInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.StepInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.TMContentInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.TupleExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.ValueExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.VariableAssignmentInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.VariableInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.WhereClauseInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.XMLContentInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.AKOExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.Anchor;
import de.topicmapslab.tmql4j.parser.core.expressions.BindingSet;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.Directive;
import de.topicmapslab.tmql4j.parser.core.expressions.EnvironmentClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsQuantifiers;
import de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix;
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ForClause;
import de.topicmapslab.tmql4j.parser.core.expressions.FromClause;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.ISAExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.LimitClause;
import de.topicmapslab.tmql4j.parser.core.expressions.Navigation;
import de.topicmapslab.tmql4j.parser.core.expressions.NonInterpretedContent;
import de.topicmapslab.tmql4j.parser.core.expressions.OffsetClause;
import de.topicmapslab.tmql4j.parser.core.expressions.OrderByClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ParameterPair;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.Postfix;
import de.topicmapslab.tmql4j.parser.core.expressions.PostfixedExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.Pragma;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PrefixDirective;
import de.topicmapslab.tmql4j.parser.core.expressions.ProjectionPostfix;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ReturnClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;
import de.topicmapslab.tmql4j.parser.core.expressions.Step;
import de.topicmapslab.tmql4j.parser.core.expressions.TMContent;
import de.topicmapslab.tmql4j.parser.core.expressions.TupleExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.Variable;
import de.topicmapslab.tmql4j.parser.core.expressions.VariableAssignment;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;
import de.topicmapslab.tmql4j.parser.core.expressions.XMLContent;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Internal registry for interpreter classes. Provides access to interpreter
 * instances for all TMQL-expression types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InterpreterRegistry {

	/**
	 * internal registry cache containing all mappings
	 */
	private final Map<Class<? extends IExpression>, Class<? extends IExpressionInterpreter<?>>> registry = HashUtil
			.getHashMap();

	/**
	 * constructor is used to register a unspecified number of new interpreter
	 * modules read from the system property-file (interpreter.properties)
	 * 
	 * @param properties
	 *            the loaded properties from the property-file
	 * @throws TMQLException
	 *             thrown if the specified class not found or the specified
	 *             class have the wrong type
	 */
	@SuppressWarnings("unchecked")
	public <T extends IExpression> InterpreterRegistry()
			throws TMQLInitializationException {

		registry.put(BindingSet.class, BindingSetInterpreter.class);
		registry.put(BooleanExpression.class,
				BooleanExpressionInterpreter.class);
		registry.put(BooleanPrimitive.class, BooleanPrimitiveInterpreter.class);
		registry.put(Content.class, ContentInterpreter.class);
		registry.put(Directive.class, DirectiveInterpreter.class);
		registry.put(EnvironmentClause.class,
				EnvironmentClauseInterpreter.class);
		registry.put(ExistsClause.class, ExistsClauseInterpreter.class);
		registry.put(ExistsQuantifiers.class,
				ExistsQuantifiersInterpreter.class);
		registry.put(FilterPostfix.class, FilterPostfixInterpreter.class);
		registry.put(FlwrExpression.class, FlwrExpressionInterpreter.class);
		registry.put(ForAllClause.class, ForAllClauseInterpreter.class);
		registry.put(ForClause.class, ForClauseInterpreter.class);
		registry.put(FromClause.class, FromClauseInterpreter.class);
		registry.put(FunctionInvocation.class,
				FunctionInvocationInterpreter.class);
		registry.put(AKOExpression.class, AKOExpressionInterpreter.class);
		registry.put(Anchor.class, AnchorInterpreter.class);
		registry.put(ISAExpression.class, ISAExpressionInterpreter.class);
		registry.put(LimitClause.class, LimitClauseInterpreter.class);
		registry.put(Navigation.class, NavigationInterpreter.class);
		registry.put(OffsetClause.class, OffsetClauseInterpreter.class);
		registry.put(OrderByClause.class, OrderByClauseInterpreter.class);
		registry.put(ParameterPair.class, ParameterPairInterpreter.class);
		registry.put(Parameters.class, ParametersInterpreter.class);
		registry.put(PathExpression.class, PathExpressionInterpreter.class);
		registry.put(Postfix.class, PostfixInterpreter.class);
		registry.put(PostfixedExpression.class,
				PostfixedExpressionInterpreter.class);
		registry.put(Pragma.class, PragmaInterpreter.class);
		registry.put(PredicateInvocation.class,
				PredicateInvocationInterpreter.class);
		registry.put(PredicateInvocationRolePlayerExpression.class,
				PredicateInvocationRolePlayerExpressionInterpreter.class);
		registry.put(PrefixDirective.class, PrefixDirectiveInterpreter.class);
		registry.put(ProjectionPostfix.class,
				ProjectionPostfixInterpreter.class);
		registry.put(QueryExpression.class, QueryExpressionInterpreter.class);
		registry.put(ReturnClause.class, ReturnClauseInterpreter.class);
		registry.put(SelectClause.class, SelectClauseInterpreter.class);
		registry.put(SelectExpression.class, SelectExpressionInterpreter.class);
		registry.put(SimpleContent.class, SimpleContentInterpreter.class);
		registry.put(Step.class, StepInterpreter.class);
		registry.put(TMContent.class, TMContentInterpreter.class);
		registry.put(TupleExpression.class, TupleExpressionInterpreter.class);
		registry.put(ValueExpression.class, ValueExpressionInterpreter.class);
		registry.put(Variable.class, VariableInterpreter.class);
		registry.put(VariableAssignment.class,
				VariableAssignmentInterpreter.class);
		registry.put(WhereClause.class, WhereClauseInterpreter.class);
		registry.put(XMLContent.class, XMLContentInterpreter.class);
		registry.put(NonInterpretedContent.class,
				NonInterpretedContentInterpreter.class);

		/*
		 * try to load interpreter mappings
		 */
		try {
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream(
					"interpreter.properties"));
			for (Map.Entry<Object, Object> property : properties.entrySet()) {
				try {
					Class<T> expressionClass = (Class<T>) Class
							.forName(property.getKey().toString());
					Class<? extends IExpressionInterpreter<T>> interpreterClass = (Class<? extends IExpressionInterpreter<T>>) Class
							.forName(property.getValue().toString());
					registerInterpreterClass(expressionClass, interpreterClass);
				} catch (ClassNotFoundException e) {
					throw new TMQLInitializationException("Cannot load class "
							+ property.getKey() + " or " + property.getValue());
				} catch (ClassCastException e) {
					throw new TMQLInitializationException(
							"Invalid types of given classes, may be sub-types of IExpression and IExpressionInterpreter.");
				}
			}
		} catch (Exception ex) {
			throw new TMQLInitializationException(
					"Cannot load interpreter.properties");
		}
	}

	/**
	 * Method try to create a instance of the interpreter, which is used to
	 * interpret the TMQL-expression of the given type
	 * 
	 * @param ex
	 *            the expression which should be interpret by the new
	 *            interpreter
	 * @return a new interpreter instance and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if no interpreter class is registered for the given
	 *             expression type
	 */
	public IExpressionInterpreter<?> interpreterInstance(IExpression ex)
			throws TMQLRuntimeException {
		if (registry.containsKey(ex.getClass())) {
			try {
				return registry.get(ex.getClass())
						.getConstructor(ex.getClass()).newInstance(ex);
			} catch (IllegalArgumentException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			} catch (SecurityException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			} catch (InstantiationException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			} catch (IllegalAccessException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			} catch (InvocationTargetException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			} catch (NoSuchMethodException e) {
				throw new TMQLRuntimeException(
						"Cannot create interpreter instance for expression-type "
								+ ex.getClass().getSimpleName(), e);
			}
		}
		throw new TMQLRuntimeException(
				"Cannot create interpreter instance for expression-type "
						+ ex.getClass().getSimpleName());
	}

	/**
	 * Method is used to register a new interpreter module for a TMQL-expression
	 * type.
	 * 
	 * @param expressionClass
	 *            the TMQL-expression type
	 * @param interpreterClass
	 *            the new interpreter module class
	 * @throws TMQLException
	 *             thrown if at least one parameter is <code>null</code>
	 */
	public <T extends IExpression> void registerInterpreterClass(
			Class<T> expressionClass,
			Class<? extends IExpressionInterpreter<T>> interpreterClass)
			throws TMQLInitializationException {
		if (expressionClass == null || interpreterClass == null) {
			throw new TMQLInitializationException("parameters may not be null.");
		}
		registry.put(expressionClass, interpreterClass);
	}
}
