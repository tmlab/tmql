package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

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
 * Interpreter implementation of function 'substring'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubstringFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public SubstringFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getItemIdentifier() {
		return "substring";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getRequiredVariableCount() {
		return 3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * get arguments
		 */
		QueryMatches[] arguments = extractArguments(runtime);

		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * check if second and third arguments are not empty
		 */
		if (!arguments[1].isEmpty()
				&& !arguments[2].isEmpty()
				&& arguments[1].getOrderedKeys().contains(
						QueryMatches.getNonScopedVariable())
				&& arguments[2].getOrderedKeys().contains(
						QueryMatches.getNonScopedVariable())) {
			try {
				/*
				 * get indexes
				 */
				int beginIndex = Integer.parseInt(LiteralUtils
						.asString(arguments[1].getPossibleValuesForVariable()
								.get(0)));
				int count = Integer.parseInt(LiteralUtils.asString(arguments[2]
						.getPossibleValuesForVariable().get(0)));

				/*
				 * execute function operation
				 */
				for (Object o : arguments[0].getPossibleValuesForVariable()) {
					final String value = LiteralUtils.asString(o);

					/*
					 * extract substring
					 */
					final String string;
					if (beginIndex < value.length()) {
						if (beginIndex + count < value.length()) {
							string = value.substring(beginIndex, beginIndex
									+ count);
						} else {
							string = value.substring(beginIndex);
						}
					} else {
						string = "";
					}
					/*
					 * store result
					 */
					Map<String, Object> result = HashUtil.getHashMap();
					result.put(QueryMatches.getNonScopedVariable(), string);
					matches.add(result);

				}
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}
}
