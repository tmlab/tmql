/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.components.interpreter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.select.grammar.productions.GroupByClause;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Interpreter of Group By clause - {@link GroupByClause}
 * @author Sven Krosse
 * 
 */
public class GroupByClauseInterpreter extends ExpressionInterpreterImpl<GroupByClause> {

	private static final Logger logger = LoggerFactory.getLogger(GroupByClauseInterpreter.class);

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param ex
	 *            the expression which shall be handled by this instance.
	 */
	public GroupByClauseInterpreter(GroupByClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Missing context bindings to group!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * disable auto reduction
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		/*
		 * call group by of query matches
		 */
		Set<String> variables = HashUtil.getHashSet(getVariables());
		return context.getContextBindings().groupBy(runtime, variables);
	}

}
