package de.topicmapslab.tmql4j.majortom.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetAssociationTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetBestLabel;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetCharacteristicTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetCoordinatesInDistance;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDates;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesAfter;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesBefore;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDatesInRange;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDistance;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetNameTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetOccurrenceTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetRoleTypes;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetTopicTypes;
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
		 * best label function
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetBestLabel.GETBESTLABEL, GetBestLabel.class);
		/*
		 * index methods
		 */
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetCharacteristicTypes.GetCharacteristicTypes, GetCharacteristicTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetTopicTypes.GetTopicTypes, GetTopicTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetAssociationTypes.GetAssociationTypes, GetAssociationTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetRoleTypes.GetRoleTypes, GetRoleTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetNameTypes.GetNameTypes, GetNameTypes.class);
		runtime.getLanguageContext().getFunctionRegistry().registerFunction(GetOccurrenceTypes.GetOccurrenceTypes, GetOccurrenceTypes.class);
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
