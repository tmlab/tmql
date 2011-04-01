package de.topicmapslab.tmql4j.majortom.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.majortom.components.navigation.RatomifyNavigationAxis;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetAssociationTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetBestIdentifier;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetBestLabel;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetCharacteristicTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetCoordinatesInDistance;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDates;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesAfter;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesBefore;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesInRange;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDistance;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetNameTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetNullValue;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetOccurrenceTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetRoleTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetSubtypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetSupertypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByCharacteristicRegExp;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByCharacteristicValue;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByNameRegExp;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByNameValue;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByOccurrenceRegExp;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicsByOccurrenceValue;
import de.topicmapslab.tmql4j.majortom.grammar.functions.RemoveDuplicates;
import de.topicmapslab.tmql4j.majortom.grammar.literals.AxisRatomify;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationRegistry;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;

public class MajortomFunctions implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean extendsExpressionType(Class<? extends IExpression> expressionType) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IExpression> getExpressionType() {
		return FunctionInvocation.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {

		/*
		 * geographical-functions
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier, GetCoordinatesInDistance.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetDistance.GetDistanceIdentifier, GetDistance.class);
		/*
		 * temporal-functions
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetDates.GetDatesIdentifier, GetDates.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetDatesAfter.GetDatesAfterIdentifier, GetDatesAfter.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetDatesBefore.GetDatesBeforeIdentifier, GetDatesBefore.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetDatesInRange.GetDatesInRangeIdentifier, GetDatesInRange.class);
		/*
		 * best label and identifier function
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetBestLabel.GETBESTLABEL, GetBestLabel.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetBestIdentifier.IDENTIFIER, GetBestIdentifier.class);
		/*
		 * index methods
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetCharacteristicTypes.GetCharacteristicTypes, GetCharacteristicTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicTypes.GetTopicTypes, GetTopicTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetAssociationTypes.GetAssociationTypes, GetAssociationTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetRoleTypes.GetRoleTypes, GetRoleTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetNameTypes.GetNameTypes, GetNameTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetOccurrenceTypes.GetOccurrenceTypes, GetOccurrenceTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetNullValue.GetNullValue, GetNullValue.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetSupertypes.GetSupertypes, GetSupertypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetSubtypes.GetSubtypes, GetSubtypes.class);

		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByCharacteristicValue.GetTopicsByCharacteristicValue, GetTopicsByCharacteristicValue.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByCharacteristicRegExp.GetTopicsByCharacteristicRegExp, GetTopicsByCharacteristicRegExp.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByNameValue.GetTopicsByNameValue, GetTopicsByNameValue.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByNameRegExp.GetTopicsByNameRegExp, GetTopicsByNameRegExp.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByOccurrenceValue.GetTopicsByOccurrenceValue, GetTopicsByOccurrenceValue.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicsByOccurrenceRegExp.GetTopicsByOccurrenceRegExp, GetTopicsByOccurrenceRegExp.class);
		
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(RemoveDuplicates.RemoveDuplicates, RemoveDuplicates.class);
		/*
		 * register axes
		 */
		runtime.getLanguageContext().getTokenRegistry().register(AxisRatomify.class);

		try {
			NavigationRegistry.buildHandler().registryAxis(AxisRatomify.class, RatomifyNavigationAxis.class);
		} catch (NavigationException e) {
			throw new TMQLRuntimeException("Cannot register extension", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidProduction(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExpression parse(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller, boolean autoAdd) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		throw new TMQLGeneratorException("Method should never called!");
	}
}
