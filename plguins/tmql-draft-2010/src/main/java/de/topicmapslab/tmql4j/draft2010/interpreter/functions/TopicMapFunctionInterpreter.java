package de.topicmapslab.tmql4j.draft2010.interpreter.functions;

import java.util.Map;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

/**
 * Interpreter implementation of function 'topicmap'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TopicMapFunctionInterpreter extends
		ExpressionInterpreterImpl<FunctionCall> implements
		IFunctionInvocationInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public TopicMapFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "topicmap";
	}

	/**
	 * {@inheritDoc}
	 */
	
	public long getRequiredVariableCount() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get the current topic map
		 */
		TopicMap topicMap = (TopicMap) runtime.getRuntimeContext().peek()
				.getValue(VariableNames.CURRENT_MAP);

		/*
		 * store topic map instance as function result
		 */
		QueryMatches matches = new QueryMatches(runtime);
		Map<String, Object> result = HashUtil.getHashMap();
		result.put(QueryMatches.getNonScopedVariable(), topicMap);
		matches.add(result);

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);

	}

}
