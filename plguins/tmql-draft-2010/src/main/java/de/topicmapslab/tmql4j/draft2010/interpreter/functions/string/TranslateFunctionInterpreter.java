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
 * Interpreter implementation of function 'translate'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TranslateFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public TranslateFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "translate";
	}

	/**
	 * {@inheritDoc}
	 */
	
	public long getRequiredVariableCount() {
		return 3;
	}

	/**
	 * {@inheritDoc}
	 */
	
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

			/*
			 * extract arguments
			 */
			final String stringToReplace = LiteralUtils.asString(arguments[1]
					.getPossibleValuesForVariable().get(0));
			final String replacementString = LiteralUtils.asString(arguments[2]
					.getPossibleValuesForVariable().get(0));

			/*
			 * check length of arguments
			 */
			if (stringToReplace.length() < replacementString.length()) {
				throw new TMQLRuntimeException(
						"The second argument has to be a char-sequence containing at least as much as characters like the thrid argument.");
			}
			/*
			 * execute function operation
			 */
			for (Object o : arguments[0].getPossibleValuesForVariable()) {
				String value = LiteralUtils.asString(o);

				/*
				 * replace characters
				 */
				for (int index = 0; index < stringToReplace.length(); index++) {
					value = value.replace(stringToReplace.charAt(index),
							replacementString.charAt(index));
				}

				/*
				 * store result
				 */
				Map<String, Object> result = HashUtil.getHashMap();
				result.put(QueryMatches.getNonScopedVariable(), value);
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
