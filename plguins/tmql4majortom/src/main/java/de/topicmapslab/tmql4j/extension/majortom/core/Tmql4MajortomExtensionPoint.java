package de.topicmapslab.tmql4j.extension.majortom.core;

import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetBestLabel;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetCoordinatesInDistance;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDates;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesAfter;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesBefore;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesInRange;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDistance;
import de.topicmapslab.tmql4j.extensions.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtension;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.parser.model.IExpression;

public class Tmql4MajortomExtensionPoint implements ILanguageExtension {

	private Tmql4MajortomLanguageExtensionEntry entry;

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
	@Override
	public ILanguageExtensionEntry getLanguageExtensionEntry() {
		return entry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtensionPointId() {
		return "tmql4majortom";
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

		entry = new Tmql4MajortomLanguageExtensionEntry();
	}
}
