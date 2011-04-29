/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.grammar.functions;

import java.util.List;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Parameters;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;

/**
 * @author Sven Krosse
 * 
 */
public abstract class FunctionImpl implements IFunction {

	private static final String VAR_POSITION_0 = "$0";

	/**
	 * Utility method to extract the parameters
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param caller
	 *            the calling interpreter
	 * @return the result of function execution
	 * @throws TMQLRuntimeException
	 */
	public QueryMatches getParameters(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) throws TMQLRuntimeException {
		List<IExpressionInterpreter<Parameters>> parameters = caller.getInterpretersFilteredByEypressionType(runtime, Parameters.class);
		QueryMatches matches = QueryMatches.emptyMatches();
		/*
		 * call parameters
		 */
		if (!parameters.isEmpty()) {
			matches = parameters.get(0).interpret(runtime, context);
		}
		// /*
		// * check if left hand arguments are empty
		// */
		// if (context.getContextBindings() != null && !context.getContextBindings().isEmpty()) {
		// /*
		// * no parameters given
		// */
		// if (matches.isEmpty()) {
		// return context.getContextBindings().extractAndRenameBindingsForVariable(VAR_POSITION_0);
		// }
		// /*
		// * create new query matches as combination of left side input and parameters
		// */
		// QueryMatches result = new QueryMatches(runtime);
		// for (Map<String, Object> tuple : context.getContextBindings()) {
		// for (Map<String, Object> tuple2 : matches) {
		// Map<String, Object> newTuple = HashUtil.getHashMap();
		// int j = 0;
		// if (tuple.containsKey(QueryMatches.getNonScopedVariable())) {
		// newTuple.put(VAR_POSITION_0, tuple.get(QueryMatches.getNonScopedVariable()));
		// j++;
		// }
		// for (int i = 0; i < tuple2.size(); i++) {
		// if (tuple2.containsKey(Variable.TOKEN + i)) {
		// newTuple.put(Variable.TOKEN + (j++), tuple2.get(Variable.TOKEN + i));
		// }
		// }
		// result.add(newTuple);
		// }
		// }
		// return result;
		// }
		return matches;
	}

}
