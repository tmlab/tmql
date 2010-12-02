/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.text.ParseException;
import java.util.Map;

import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.majortom.utils.FunctionUtils;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class GetCoordinatesInDistance extends FunctionImpl {

	public static final String GetCoordinatesInDistanceIdentifier = "fn:coordinates-in-distance";

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
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 2 or 3 parameters.");
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
				Double distance = null;
				Wgs84Coordinate coordinate = FunctionUtils.getWgs84Coordinate(tuple, 1);
				/*
				 * is parameter-list String, double
				 */
				if (tuple.size() == 2) {
					Object oDistance = tuple.get("$1");
					distance = LiteralUtils.asDouble(oDistance);
				} else {
					Object oDistance = tuple.get("$2");
					distance = LiteralUtils.asDouble(oDistance);
				}

				ILiteralIndex index = context.getQuery().getTopicMap().getIndex(ILiteralIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				for (ICharacteristics c : index.getCoordinates(coordinate, distance)) {
					Map<String, Object> result = HashUtil.getHashMap();
					result.put(QueryMatches.getNonScopedVariable(), c);
					results.add(result);
				}
			} catch (NumberFormatException e) {
				throw new TMQLRuntimeException("Given argument has to be a double value", e);
			} catch (ParseException e) {
				throw new TMQLRuntimeException("Given argument has to be a string coordinate value", e);
			} catch (UnsupportedOperationException e) {
				throw new TMQLRuntimeException("Function only supported by MaJorToM Topic Map Engines", e);
			}
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetCoordinatesInDistanceIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2 || numberOfParameters == 3;
	}

}
