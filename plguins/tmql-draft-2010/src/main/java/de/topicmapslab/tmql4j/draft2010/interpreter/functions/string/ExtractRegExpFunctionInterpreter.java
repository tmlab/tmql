package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseFunctionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

/**
 * Interpreter implementation of function 'extract-regexp'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExtractRegExpFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public ExtractRegExpFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "extract-regexp";
	}

	/**
	 * {@inheritDoc}
	 */
	
	public long getRequiredVariableCount() {
		return 2;
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
				&& arguments[1].getOrderedKeys().contains(
						QueryMatches.getNonScopedVariable())) {

			/*
			 * get regular expression
			 */
			final String regExp = LiteralUtils.asString(arguments[1]
					.getPossibleValuesForVariable().get(0));
			final Pattern pattern = Pattern.compile(".*?(" + regExp + ").*?");

			/*
			 * execute function operation
			 */
			for (Object o : arguments[0].getPossibleValuesForVariable()) {
				final String value = LiteralUtils.asString(o);

				Matcher matcher = pattern.matcher(value);
				String string = "";
				if (matcher.find()) {
					string = matcher.group(1);
				}

				/*
				 * store result
				 */
				Map<String, Object> result = HashUtil.getHashMap();
				result.put(QueryMatches.getNonScopedVariable(), string);
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
