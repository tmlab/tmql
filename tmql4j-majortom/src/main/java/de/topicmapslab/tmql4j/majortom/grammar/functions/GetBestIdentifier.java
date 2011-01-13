/**
 * 
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.util.Collection;
import java.util.Map;

import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class GetBestIdentifier extends FunctionImpl {

	/**
	 * constant for function identifier
	 */
	public static final String IDENTIFIER = "fn:best-identifier";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches parameters = getParameters(runtime, context, caller);

		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size()))
			throw new TMQLRuntimeException("Illegal Number Of Arguments for " + IDENTIFIER);
		QueryMatches results = new QueryMatches(runtime);
		for (Map<String, Object> tuple : parameters) {

			Object p1 = tuple.get("$0");
			Object p2 = tuple.get("$1");

			ITopic t = getTopic(runtime, context, p1);

			boolean withPrefix = false;
			try {
				withPrefix = (Boolean) DatatypeAwareUtils.toValue(p2, Boolean.class);
			} catch (Exception e1) {
				// NOTHING TO DO 
			}
			if (t != null) {
				addResult(results, t,  withPrefix);
			} else if (p1 instanceof Collection<?>) {
				for (Object o : (Collection<?>) p1) {
					t = getTopic(runtime, context, o);
					if (t != null)
						addResult(results, t, withPrefix);
				}
			}
		}
		return results;
	}

	/**
	 * Utility method to add the best identifier for the given topic to the
	 * result set if exists
	 * 
	 * @param rqm
	 *            the result set
	 * @param t
	 *            the topic
	 * @param withPrefix
	 *            withPrefix flag for
	 *            {@link ITopic#getBestIdentifier(boolean)}
	 */
	private void addResult(QueryMatches rqm, ITopic t, boolean withPrefix) {
		String r = t.getBestIdentifier(withPrefix);
		addResult(rqm, r);
	}

	/**
	 * Transform the given object to a topic construct. If the object is a
	 * string, the topic map will be asked for the given identifier.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param obj
	 *            the object
	 * @return the topic or <code>null</code>
	 */
	private ITopic getTopic(ITMQLRuntime runtime, IContext context, Object obj) {
		if (obj instanceof ITopic) {
			return (ITopic) obj;
		}
		if (obj instanceof String) {
			return (ITopic) runtime.getConstructResolver().getConstructByIdentifier(context, (String) obj);
		}
		return null;
	}

	/**
	 * Adding the given string to the result set
	 * 
	 * @param rqm
	 *            the query match
	 * @param r
	 *            the result string
	 */
	private void addResult(QueryMatches rqm, String r) {
		if (r != null) {
			Map<String, Object> resultMap = HashUtil.getHashMap();
			resultMap.put(QueryMatches.getNonScopedVariable(), r);
			rqm.add(resultMap);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExpectedNumberOfParameters(long num) {
		return num == 2 ;
	}

}
