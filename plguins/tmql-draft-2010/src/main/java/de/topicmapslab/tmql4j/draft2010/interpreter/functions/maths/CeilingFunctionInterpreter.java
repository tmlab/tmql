package de.topicmapslab.tmql4j.draft2010.interpreter.functions.maths;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseFunctionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

/**
 * Interpreter implementation of function 'ceiling'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CeilingFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public CeilingFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "ceiling";
	}

	/**
	 * {@inheritDoc}
	 */
	
	public long getRequiredVariableCount() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * get arguments
		 */
		QueryMatches[] arguments = extractArguments(runtime);

		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * object is a decimal number
			 */
			if (LiteralUtils.isDecimal(o.toString())) {
				BigDecimal d = LiteralUtils.asDecimal(o.toString());
				BigInteger i = BigInteger.valueOf((long) Math.ceil(d
						.doubleValue()));
				result.put(QueryMatches.getNonScopedVariable(), i);
				matches.add(result);
			}
			/*
			 * object is an integer number
			 */
			else if (LiteralUtils.isInteger(o.toString())) {
				BigInteger i = LiteralUtils.asInteger(o.toString());
				result.put(QueryMatches.getNonScopedVariable(), i);
				matches.add(result);
			}
		}
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}

}
