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
 * @author Hannes Niederhausen
 * 
 */
public class GetBestLabel extends FunctionImpl {

	/**
	 * constant for function identifier
	 */
	public static final String GETBESTLABEL = "fn:best-label";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches parameters = getParameters(runtime, context, caller);

		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size()))
			throw new TMQLRuntimeException("Illegal Number Of Arguments for " + GETBESTLABEL);
		QueryMatches results = new QueryMatches(runtime);
		for (Map<String, Object> tuple : parameters) {

			Object p1 = tuple.get("$0");
			Object p2 = tuple.get("$1");
			Object p3 = tuple.get("$2");

			ITopic t = getTopic(runtime, context, p1);

			// only one topic
			ITopic theme = getTopic(runtime, context, p2);
			boolean strict = false;
			try {
				strict = (Boolean) DatatypeAwareUtils.toValue(p3, Boolean.class);
			} catch (Exception e1) {
				// noop
			}
			if (t != null) {
				addResult(results, t, theme, strict);
			} else if (p1 instanceof Collection<?>) {
				for (Object o : (Collection<?>) p1) {
					t = getTopic(runtime, context, o);
					if (t != null)
						addResult(results, t, theme, strict);
				}
			}
		}
		return results;
	}

	/**
	 * Utility method to add the best label for the given topic and theme to the
	 * result set if exists
	 * 
	 * @param rqm
	 *            the result set
	 * @param t
	 *            the topic
	 * @param theme
	 *            the theme
	 * @param strict
	 *            strict mode for
	 *            {@link ITopic#getBestLabel(org.tmapi.core.Topic, boolean)}
	 */
	private void addResult(QueryMatches rqm, ITopic t, ITopic theme, boolean strict) {
		String r;
		if (theme == null) {
			r = t.getBestLabel();
		} else {
			r = t.getBestLabel(theme, strict);
		}
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
		return GETBESTLABEL;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExpectedNumberOfParameters(long num) {
		return (num == 1 || num == 2 || num == 3);
	}

}
