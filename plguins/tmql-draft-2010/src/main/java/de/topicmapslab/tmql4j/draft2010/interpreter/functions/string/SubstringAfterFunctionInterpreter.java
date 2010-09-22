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
 * Interpreter implementation of function 'substring-after'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubstringAfterFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public SubstringAfterFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "substring-after";
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

		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			final String value = LiteralUtils.asString(o);
			/*
			 * check if second and third arguments are not empty
			 */
			if (!arguments[1].isEmpty()
					&& arguments[1].getOrderedKeys().contains(
							QueryMatches.getNonScopedVariable())) {
				try {
					/*
					 * get indexes
					 */
					final String otherValue = LiteralUtils
							.asString(arguments[1]
									.getPossibleValuesForVariable().get(0));
					int index = value.indexOf(otherValue);
					/*
					 * extract substring
					 */
					final String string;
					if (index != -1) {
						if (index + otherValue.length() < value.length()) {
							string = value.substring(index
									+ otherValue.length());
						} else {
							string = "";
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
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}

		}
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);

	}

}
