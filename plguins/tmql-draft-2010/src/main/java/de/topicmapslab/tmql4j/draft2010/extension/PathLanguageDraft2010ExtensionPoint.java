package de.topicmapslab.tmql4j.draft2010.extension;

import de.topicmapslab.tmql4j.common.context.FunctionRegistry;
import de.topicmapslab.tmql4j.common.context.InterpreterRegistry;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.BooleanFilter;
import de.topicmapslab.tmql4j.draft2010.expressions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.draft2010.expressions.Filter;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.expressions.NumericalExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.PathExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.expressions.PathStep;
import de.topicmapslab.tmql4j.draft2010.expressions.ScopeFilter;
import de.topicmapslab.tmql4j.draft2010.expressions.SetExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.SimpleExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.ValueExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DateLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DateTimeLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DecimalLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IriReference;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.StringLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.TimeLiteral;
import de.topicmapslab.tmql4j.draft2010.internal.PathCanonizer;
import de.topicmapslab.tmql4j.draft2010.internal.PathWhitespacer;
import de.topicmapslab.tmql4j.draft2010.interpreter.AssociationPatternInterpeter;
import de.topicmapslab.tmql4j.draft2010.interpreter.BooleanExpressionInterpeter;
import de.topicmapslab.tmql4j.draft2010.interpreter.BooleanFilterInterpeter;
import de.topicmapslab.tmql4j.draft2010.interpreter.ComparisonExpressionInterpeter;
import de.topicmapslab.tmql4j.draft2010.interpreter.ExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.FilterInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.FunctionCallInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.NumericalExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.PathExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.PathSpecificationInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.PathStepInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.QueryExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.ScopeFilterInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.SetExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.SimpleExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.ValueExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.CountFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.TopicMapFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.literal.BooleanFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.literal.NumberFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.literal.StringFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.maths.CeilingFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.maths.FloorFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.maths.RoundFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.ConcatFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.ContainsFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.EndsWithFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.ExtractRegExpFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.FindFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.MatchesRegExpFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.NormalizeSpaceFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.StartsWithFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.StringLengthFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.SubstringAfterFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.SubstringBeforeFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.SubstringFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.functions.string.TranslateFunctionInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.DateLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.DateTimeLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.DecimalLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.IntegerLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.IriReferenceInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.StringLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.interpreter.literals.TimeLiteralInterpreter;
import de.topicmapslab.tmql4j.draft2010.tokens.Association;
import de.topicmapslab.tmql4j.draft2010.tokens.Datatype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectSubtype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectSupertype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectType;
import de.topicmapslab.tmql4j.draft2010.tokens.Div;
import de.topicmapslab.tmql4j.draft2010.tokens.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.tokens.Equals;
import de.topicmapslab.tmql4j.draft2010.tokens.Instance;
import de.topicmapslab.tmql4j.draft2010.tokens.Intersect;
import de.topicmapslab.tmql4j.draft2010.tokens.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.tokens.Minus;
import de.topicmapslab.tmql4j.draft2010.tokens.Name;
import de.topicmapslab.tmql4j.draft2010.tokens.Occurrence;
import de.topicmapslab.tmql4j.draft2010.tokens.Parent;
import de.topicmapslab.tmql4j.draft2010.tokens.Player;
import de.topicmapslab.tmql4j.draft2010.tokens.Reified;
import de.topicmapslab.tmql4j.draft2010.tokens.Role;
import de.topicmapslab.tmql4j.draft2010.tokens.Scoped;
import de.topicmapslab.tmql4j.draft2010.tokens.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.tokens.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.tokens.Subtype;
import de.topicmapslab.tmql4j.draft2010.tokens.Supertype;
import de.topicmapslab.tmql4j.draft2010.tokens.Topic;
import de.topicmapslab.tmql4j.draft2010.tokens.Type;
import de.topicmapslab.tmql4j.draft2010.tokens.Unequals;
import de.topicmapslab.tmql4j.draft2010.tokens.Union;
import de.topicmapslab.tmql4j.draft2010.tokens.Value;
import de.topicmapslab.tmql4j.draft2010.tokens.Variant;
import de.topicmapslab.tmql4j.extensions.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtension;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.lexer.core.TokenRegistry;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

public class PathLanguageDraft2010ExtensionPoint implements ILanguageExtension {

	
	public String getExtensionPointId() {
		return "path-language-draft-2010";
	}

	
	public void registerExtension(ITMQLRuntime runtime)
			throws TMQLExtensionRegistryException {

		/*
		 * set new canonizer and whitespacer
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		properties.setProperty(
				TMQLRuntimeProperties.CANONIZER_IMPLEMENTATION_CLASS,
				PathCanonizer.class.getCanonicalName());
		properties.setProperty(
				TMQLRuntimeProperties.WHITESPACER_IMPLEMENTATION_CLASS,
				PathWhitespacer.class.getCanonicalName());

		/*
		 * register tokens
		 */
		TokenRegistry registry = runtime.getLanguageContext()
				.getTokenRegistry();
		registry.register(Association.class);
		registry.register(Datatype.class);
		registry.register(DirectInstance.class);
		registry.register(DirectSubtype.class);
		registry.register(DirectSupertype.class);
		registry.register(DirectType.class);
		registry.register(Div.class);
		registry.register(DoubleColon.class);
		registry.register(Equals.class);
		registry.register(Instance.class);
		registry.register(Intersect.class);
		registry.register(ItemIdentifier.class);
		registry.register(Minus.class);
		registry.register(Name.class);
		registry.register(Occurrence.class);
		registry.register(Parent.class);
		registry.register(Player.class);
		registry.register(Reified.class);
		// registry.register(Reifier.class);
		registry.register(Role.class);
		// registry.register(Scope.class);
		registry.register(Scoped.class);
		registry.register(SubjectIdentifier.class);
		registry.register(SubjectLocator.class);
		registry.register(Subtype.class);
		registry.register(Supertype.class);
		registry.register(Topic.class);
		registry.register(Type.class);
		registry.register(Unequals.class);
		registry.register(Union.class);
		registry.register(Value.class);
		registry.register(Variant.class);

		try {

			InterpreterRegistry interpreterRegistry = runtime
					.getLanguageContext().getInterpreterRegistry();
			/*
			 * register interpreter
			 */
			interpreterRegistry.registerInterpreterClass(
					AssociationPattern.class,
					AssociationPatternInterpeter.class);
			interpreterRegistry.registerInterpreterClass(
					BooleanExpression.class, BooleanExpressionInterpeter.class);
			interpreterRegistry.registerInterpreterClass(BooleanFilter.class,
					BooleanFilterInterpeter.class);
			interpreterRegistry.registerInterpreterClass(
					ComparisonExpression.class,
					ComparisonExpressionInterpeter.class);
			interpreterRegistry.registerInterpreterClass(Expression.class,
					ExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(Filter.class,
					FilterInterpreter.class);
			interpreterRegistry.registerInterpreterClass(FunctionCall.class,
					FunctionCallInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					NumericalExpression.class,
					NumericalExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(PathExpression.class,
					PathExpressionInterpreter.class);
			interpreterRegistry
					.registerInterpreterClass(PathSpecification.class,
							PathSpecificationInterpreter.class);
			interpreterRegistry.registerInterpreterClass(PathStep.class,
					PathStepInterpreter.class);
			interpreterRegistry.registerInterpreterClass(QueryExpression.class,
					QueryExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(ScopeFilter.class,
					ScopeFilterInterpreter.class);
			interpreterRegistry.registerInterpreterClass(SetExpression.class,
					SetExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(
					SimpleExpression.class, SimpleExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(ValueExpression.class,
					ValueExpressionInterpreter.class);
			interpreterRegistry.registerInterpreterClass(DateLiteral.class,
					DateLiteralInterpreter.class);
			interpreterRegistry.registerInterpreterClass(DateTimeLiteral.class,
					DateTimeLiteralInterpreter.class);
			interpreterRegistry.registerInterpreterClass(DecimalLiteral.class,
					DecimalLiteralInterpreter.class);
			interpreterRegistry.registerInterpreterClass(IntegerLiteral.class,
					IntegerLiteralInterpreter.class);
			interpreterRegistry.registerInterpreterClass(IriReference.class,
					IriReferenceInterpreter.class);
			interpreterRegistry.registerInterpreterClass(StringLiteral.class,
					StringLiteralInterpreter.class);
			interpreterRegistry.registerInterpreterClass(TimeLiteral.class,
					TimeLiteralInterpreter.class);

			/*
			 * register functions
			 */
			FunctionRegistry functionRegistry = runtime.getLanguageContext()
					.getFunctionRegistry();
			functionRegistry.registerFunction("boolean",
					BooleanFunctionInterpreter.class);
			functionRegistry.registerFunction("ceiling",
					CeilingFunctionInterpreter.class);
			functionRegistry.registerFunction("floor",
					FloorFunctionInterpreter.class);
			functionRegistry.registerFunction("number",
					NumberFunctionInterpreter.class);
			functionRegistry.registerFunction("round",
					RoundFunctionInterpreter.class);
			functionRegistry.registerFunction("string",
					StringFunctionInterpreter.class);
			functionRegistry.registerFunction("concat",
					ConcatFunctionInterpreter.class);
			functionRegistry.registerFunction("contains",
					ContainsFunctionInterpreter.class);
			functionRegistry.registerFunction("ends-with",
					EndsWithFunctionInterpreter.class);
			functionRegistry.registerFunction("extract-regexp",
					ExtractRegExpFunctionInterpreter.class);
			functionRegistry.registerFunction("find",
					FindFunctionInterpreter.class);
			functionRegistry.registerFunction("matches-regexp",
					MatchesRegExpFunctionInterpreter.class);
			functionRegistry.registerFunction("normalize-space",
					NormalizeSpaceFunctionInterpreter.class);
			functionRegistry.registerFunction("starts-with",
					StartsWithFunctionInterpreter.class);
			functionRegistry.registerFunction("string-length",
					StringLengthFunctionInterpreter.class);
			functionRegistry.registerFunction("substring-before",
					SubstringBeforeFunctionInterpreter.class);
			functionRegistry.registerFunction("substring-after",
					SubstringAfterFunctionInterpreter.class);
			functionRegistry.registerFunction("substring",
					SubstringFunctionInterpreter.class);
			functionRegistry.registerFunction("translate",
					TranslateFunctionInterpreter.class);
			functionRegistry.registerFunction("count",
					CountFunctionInterpreter.class);
			functionRegistry.registerFunction("topicmap",
					TopicMapFunctionInterpreter.class);
		} catch (TMQLException e) {
			throw new TMQLExtensionRegistryException(e);
		}
	}

	
	public boolean extendsExpressionType(
			Class<? extends IExpression> expressionType) {
		return expressionType.equals(PathExpression.class);
	}

	
	public ILanguageExtensionEntry getLanguageExtensionEntry() {
		return new PathLanguageDraft2010ExtensionEntry();
	}

}
