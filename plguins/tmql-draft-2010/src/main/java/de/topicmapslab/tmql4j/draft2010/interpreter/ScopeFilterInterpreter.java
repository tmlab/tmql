package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Map;

import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.ScopeFilter;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Interpreter implementation of the production 'scope-filter'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ScopeFilterInterpreter extends
		ExpressionInterpreterImpl<ScopeFilter> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public ScopeFilterInterpreter(ScopeFilter ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get context of filter interpretation
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			throw new TMQLRuntimeException(
					"Missing context for filter interpretation");
		}

		QueryMatches context = (QueryMatches) set
				.getValue(VariableNames.ITERATED_BINDINGS);

		/*
		 * get theme to filter by scope
		 */
		Topic theme = null;
		try {
			TopicMap topicMap = (TopicMap) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.CURRENT_MAP);
			theme = (Topic) runtime.getDataBridge().getConstructResolver()
					.getConstructByIdentifier(runtime, getTokens().get(1),
							topicMap);
		} catch (Exception e) {
			throw new TMQLRuntimeException(e);
		}

		/*
		 * filter all constructs as instance of Scoped and containing the given
		 * them
		 */
		QueryMatches results = new QueryMatches(runtime);
		for (Object o : context.getPossibleValuesForVariable()) {
			if (o instanceof Scoped) {
				if (((Scoped) o).getScope().contains(theme)) {
					Map<String, Object> tuple = HashUtil.getHashMap();
					tuple.put(QueryMatches.getNonScopedVariable(), o);
					results.add(tuple);
				}
			}
		}

		/*
		 * set results
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

}
