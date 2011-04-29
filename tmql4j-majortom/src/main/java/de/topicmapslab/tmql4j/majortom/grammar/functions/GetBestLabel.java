/**
 * 
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Topic;

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
	@Override
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches parameters = getParameters(runtime, context, caller);

		if (parameters.isEmpty()) {
			return QueryMatches.emptyMatches();
		}

		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException("Illegal Number Of Arguments for " + GETBESTLABEL);
		}
		QueryMatches results = new QueryMatches(runtime);
		for (Map<String, Object> tuple : parameters) {

			Object p1 = tuple.get("$0");
			Object p2 = tuple.get("$1");
			Object p3 = tuple.get("$2");
			Topic t = getTopic(runtime, context, p1);

			// only one topic
			Topic theme = getTopic(runtime, context, p2);
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
					if (t != null) {
						addResult(results, t, theme, strict);
					}
				}
			}
		}
		return results;
	}

	/**
	 * Utility method to add the best label for the given topic and theme to the result set if exists
	 * 
	 * @param rqm
	 *            the result set
	 * @param t
	 *            the topic
	 * @param theme
	 *            the theme
	 * @param strict
	 *            strict mode for {@link ITopic#getBestLabel(org.tmapi.core.Topic, boolean)}
	 */
	private void addResult(QueryMatches rqm, Topic t, Topic theme, boolean strict) {
		String r;
		/*
		 * is MajorToM topic instance
		 */
		if (t instanceof ITopic) {
			if (theme == null) {
				r = ((ITopic) t).getBestLabel();
			} else {
				r = ((ITopic) t).getBestLabel(theme, strict);
			}
		}
		/*
		 * check for other TMAPI implementations supporting getBestLabel method
		 */
		else {
			try {
				if (theme == null) {
					Method m = t.getClass().getMethod("getBestLabel");
					r = (String) m.invoke(t);
				} else {
					Method m = t.getClass().getMethod("getBestLabel", Topic.class, boolean.class);
					r = (String) m.invoke(t, theme, strict);
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException("The function '" + GETBESTLABEL + "' is not supported by the current topic map.");
			}
		}
		addResult(rqm, r);
	}

	/**
	 * Transform the given object to a topic construct. If the object is a string, the topic map will be asked for the
	 * given identifier.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param obj
	 *            the object
	 * @return the topic or <code>null</code>
	 */
	private Topic getTopic(ITMQLRuntime runtime, IContext context, Object obj) {
		if (obj instanceof Topic) {
			return (Topic) obj;
		}
		if (obj instanceof String) {
			return (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (String) obj);
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
