/**
 * 
 */
package de.topicmapslab.tmql4j.extension.majortom.expression;

import java.util.Collection;
import java.util.Map;

import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

/**
 * @author Hannes Niederhausen
 * 
 */
public class GetBestLabel extends ExpressionInterpreterImpl<FunctionInvocation> implements IFunctionInvocationInterpreter<FunctionInvocation> {

	public GetBestLabel(FunctionInvocation ex) {
		super(ex);
	}

	public static final String GETBESTLABEL = "fn:best-label";

	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		QueryMatches qm = extractArguments(runtime, Parameters.class, 0);

		if (!isExpectedNumberOfParameters(qm.getOrderedKeys().size()))
			throw new TMQLRuntimeException("Illegal Number Of Arguments for " + GETBESTLABEL);
		QueryMatches rqm = new QueryMatches(runtime);
		for (Map<String, Object> tuple : qm) {

			Object p1 = tuple.get("$0");
			Object p2 = tuple.get("$1");
			Object p3 = tuple.get("$2");

			ITopic t = getTopic(p1, runtime);
			
			// only one topic
			ITopic theme = getTopic(p2, runtime);
			boolean strict = false;
			try {
				strict = (Boolean) DatatypeAwareUtils.toValue(p3, Boolean.class);
			} catch (Exception e1) {
				// noop
			}
			if (t!=null) {
				addResult(rqm, t, theme, strict);
			} else if (p1 instanceof Collection<?>) {
				for (Object o : (Collection<?>) p1) {
					t = getTopic(o, runtime);
					if (t!=null)
						addResult(rqm, t, theme, strict);
				}
			}
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES, rqm);
	}

	private void addResult(QueryMatches rqm, ITopic t, ITopic theme, boolean strict) {
		String r;
		if (theme == null) {
			r = t.getBestLabel();
		} else { 
			r = t.getBestLabel(theme, strict);
		}
		addResult(rqm, r);
	}

	private ITopic getTopic(Object obj, TMQLRuntime runtime) {
		if (obj instanceof ITopic)
			return (ITopic) obj;
		
		if (obj instanceof String) {
			return (ITopic) runtime.getDataBridge().getConstructByIdentifier(runtime, (String) obj);
		}
		
		return null;
	}

	private void addResult(QueryMatches rqm, String r) {
		if (r != null) {
			Map<String, Object> resultMap = HashUtil.getHashMap();
			resultMap.put(QueryMatches.getNonScopedVariable(), r);
			rqm.add(resultMap);
		}
	}

	@Override
	public String getItemIdentifier() {
		return GETBESTLABEL;
	}

	@Override
	public boolean isExpectedNumberOfParameters(long num) {
		return (num == 1 || num == 2 || num == 3);
	}

}
