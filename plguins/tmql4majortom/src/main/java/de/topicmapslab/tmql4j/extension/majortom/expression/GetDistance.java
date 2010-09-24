/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.majortom.expression;

import java.text.ParseException;
import java.util.Map;

import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.majortom.utils.FunctionUtils;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

/**
 * @author Sven Krosse
 * 
 */
public class GetDistance extends ExpressionInterpreterImpl<FunctionInvocation> implements IFunctionInvocationInterpreter<FunctionInvocation> {

	public static final String GetDistanceIdentifier = "fn:distance";

	/**
	 * @param ex
	 */
	public GetDistance(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);

		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 2 or 4 parameters.");
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
				Wgs84Coordinate[] coordinates = FunctionUtils.getWgs84Coordinates(tuple,2);
				Map<String, Object> result = HashUtil.getHashMap();
				result.put(QueryMatches.getNonScopedVariable(), coordinates[0].getDistance(coordinates[1]));
				results.add(result);
			} catch (NumberFormatException e) {
				throw new TMQLRuntimeException("Given argument has to be a double value", e);
			} catch (ParseException e) {
				throw new TMQLRuntimeException("Given argument has to be a string coordinate value", e);
			} catch (UnsupportedOperationException e) {
				throw new TMQLRuntimeException("Function only supported by MaJorToM Topic Map Engines", e);
			}
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES, results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetDistanceIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2 || numberOfParameters == 4;
	}

}
