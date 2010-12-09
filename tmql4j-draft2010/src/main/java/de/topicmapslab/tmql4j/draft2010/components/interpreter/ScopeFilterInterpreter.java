package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ScopeFilter;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of the production 'scope-filter'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ScopeFilterInterpreter extends ExpressionInterpreterImpl<ScopeFilter> {
	/**
	 * the logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Missing content to filter");
			return QueryMatches.emptyMatches();
		}

		/*
		 * get theme to filter by scope
		 */
		final String identifier = LiteralUtils.asString(getTokens().get(1));
		Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, identifier);
		if (!(construct instanceof Topic)) {
			logger.warn("The given theme '" + identifier + "' does not exist or is not a topic!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * filter all constructs as instance of Scoped and containing the given
		 * theme
		 */
		List<Object> results = HashUtil.getList();
		for (Object o : context.getContextBindings().getPossibleValuesForVariable()) {
			if (o instanceof Scoped) {
				if (((Scoped) o).getScope().contains((Topic) construct)) {
					results.add(o);
				}
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, results.toArray());
	}

}
