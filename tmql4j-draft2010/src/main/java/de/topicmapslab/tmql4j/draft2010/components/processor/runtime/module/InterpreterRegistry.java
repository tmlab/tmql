/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistryImpl;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.AssociationPatternInterpeter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.BooleanExpressionInterpeter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.BooleanFilterInterpeter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.ComparisonExpressionInterpeter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.EnvironmentClauseInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.ExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.FilterInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.FunctionCallInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.NumericalExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.PathExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.PathSpecificationInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.PathStepInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.PragmaInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.PrefixDirectiveInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.ScopeFilterInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.SetExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.SimpleExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.ValueExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.VariableInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.DateLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.DateTimeLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.DecimalLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.IntegerLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.IriReferenceInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.StringLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.literals.TimeLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DateLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DateTimeLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DecimalLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IriReference;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.StringLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.TimeLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.BooleanFilter;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Filter;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.NumericalExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathStep;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Pragma;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PrefixDirective;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ScopeFilter;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SetExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SimpleExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Variable;

/**
 * @author Sven Krosse
 * 
 */
public class InterpreterRegistry extends InterpreterRegistryImpl {

	/**
	 * {@inheritDoc}
	 */
	protected void initialize() {
		/*
		 * literals
		 */
		registerInterpreterClass(DateLiteral.class, DateLiteralInterpreter.class);
		registerInterpreterClass(DateTimeLiteral.class, DateTimeLiteralInterpreter.class);
		registerInterpreterClass(DecimalLiteral.class, DecimalLiteralInterpreter.class);
		registerInterpreterClass(IntegerLiteral.class, IntegerLiteralInterpreter.class);
		registerInterpreterClass(IriReference.class, IriReferenceInterpreter.class);
		registerInterpreterClass(StringLiteral.class, StringLiteralInterpreter.class);
		registerInterpreterClass(TimeLiteral.class, TimeLiteralInterpreter.class);
		/*
		 * productions
		 */
		registerInterpreterClass(AssociationPattern.class, AssociationPatternInterpeter.class);
		registerInterpreterClass(BooleanExpression.class, BooleanExpressionInterpeter.class);
		registerInterpreterClass(BooleanFilter.class, BooleanFilterInterpeter.class);
		registerInterpreterClass(ComparisonExpression.class, ComparisonExpressionInterpeter.class);
		registerInterpreterClass(EnvironmentClause.class, EnvironmentClauseInterpreter.class);
		registerInterpreterClass(Expression.class, ExpressionInterpreter.class);
		registerInterpreterClass(Filter.class, FilterInterpreter.class);
		registerInterpreterClass(FunctionCall.class, FunctionCallInterpreter.class);
		registerInterpreterClass(NumericalExpression.class, NumericalExpressionInterpreter.class);
		registerInterpreterClass(PathExpression.class, PathExpressionInterpreter.class);
		registerInterpreterClass(PathSpecification.class, PathSpecificationInterpreter.class);
		registerInterpreterClass(PathStep.class, PathStepInterpreter.class);
		registerInterpreterClass(Pragma.class, PragmaInterpreter.class);
		registerInterpreterClass(PrefixDirective.class, PrefixDirectiveInterpreter.class);
		registerInterpreterClass(QueryExpression.class, QueryExpressionInterpreter.class);
		registerInterpreterClass(ScopeFilter.class, ScopeFilterInterpreter.class);
		registerInterpreterClass(SetExpression.class, SetExpressionInterpreter.class);
		registerInterpreterClass(SimpleExpression.class, SimpleExpressionInterpreter.class);
		registerInterpreterClass(ValueExpression.class, ValueExpressionInterpreter.class);
		registerInterpreterClass(Variable.class, VariableInterpreter.class);
	}
}
