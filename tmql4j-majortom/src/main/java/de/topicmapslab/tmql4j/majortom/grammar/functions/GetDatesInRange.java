/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.majortom.utils.FunctionUtils;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

/**
 * @author Sven Krosse
 * 
 */
public class GetDatesInRange extends FunctionImpl {

	public static final String GetDatesInRangeIdentifier = "fn:dates-in-range";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);

		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 2,6 or 12 parameters.");
		}
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			if (!isExpectedNumberOfParameters(tuple.size())) {
				continue;
			}
			try {
				Calendar calendars[] = FunctionUtils.getCalendars(tuple, 2);
				TopicMap topicMap = context.getQuery().getTopicMap();
				ILiteralIndex index = topicMap.getIndex(ILiteralIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				Set<ICharacteristics> set = HashUtil.getHashSet(index.getCharacteristics(topicMap.createLocator(XmlSchemeDatatypes.XSD_DATE)));
				set.addAll(index.getCharacteristics(topicMap.createLocator(XmlSchemeDatatypes.XSD_DATETIME)));
				for (ICharacteristics c : set) {
					Calendar v = ((IOccurrence) c).dateTimeValue();
					if (v.after(calendars[0]) && v.before(calendars[1])) {
						Map<String, Object> result = HashUtil.getHashMap();
						result.put(QueryMatches.getNonScopedVariable(), c);
						results.add(result);
					}
				}
			} catch (NumberFormatException e) {
				throw new TMQLRuntimeException("Given argument has to be a integer value", e);
			} catch (ParseException e) {
				throw new TMQLRuntimeException("Given argument has to be a string date value", e);
			} catch (UnsupportedOperationException e) {
				throw new TMQLRuntimeException("Function only supported by MaJorToM Topic Map Engines", e);
			} catch (URISyntaxException e) {
				throw new TMQLRuntimeException("Given argument has to be a string date value", e);
			}
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetDatesInRangeIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2 || numberOfParameters == 6 || numberOfParameters == 12;
	}

}
