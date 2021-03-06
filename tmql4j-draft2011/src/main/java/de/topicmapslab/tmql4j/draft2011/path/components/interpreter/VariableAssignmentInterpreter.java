/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.VariableAssignment;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret variable-assignments.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * variable-assignment ::= variable in content
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class VariableAssignmentInterpreter extends ExpressionInterpreterImpl<VariableAssignment> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public VariableAssignmentInterpreter(VariableAssignment ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Read variable name
		 */
		final String variable = getTokens().get(0);

		/*
		 * call content
		 */
		QueryMatches content = extractArguments(runtime, Content.class, 0, context, optionalArguments);
		if (content.isEmpty()) {
			return QueryMatches.emptyMatches();
		}

		/*
		 * rename non-scoped variable by variable name of this
		 * variable-assignment
		 */
		if (content.getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {
			if ( context.getCurrentTuple() != null){
				QueryMatches matches = new QueryMatches(runtime);
				for ( Map<String, Object> tuple : content.extractAndRenameBindingsForVariable(variable)){
					tuple.putAll(context.getCurrentTuple());
					matches.add(tuple);
				}
				return matches;
			}
			return content.extractAndRenameBindingsForVariable(variable);
		}
		List<Object> values = HashUtil.getList();
		/*
		 * iterate over all contained variables
		 */
		for (String key : content.getOrderedKeys()) {
			extract(values, content.getPossibleValuesForVariable(key));
		}
		/*
		 * check if current tuple is given
		 */
		if ( context.getCurrentTuple() != null){
			QueryMatches matches = new QueryMatches(runtime);
			for ( Object value : values){
				Map<String, Object> tuple = HashUtil.getHashMap();				
				tuple.putAll(context.getCurrentTuple());
				tuple.put(variable, value);
				matches.add(tuple);
			}
			return matches;
		}
		/*
		 * no current tuple defined
		 */
		return QueryMatches.asQueryMatch(runtime, variable, values.toArray());
	}

	/**
	 * Internal method to extract the value of a variable binding from the
	 * generated variables bindings of the contained variable-assignment.
	 * 
	 * @param results
	 *            the collection to add the extracted values
	 * @param obj
	 *            the object representing the values to extract
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	private void extract(final Collection<Object> results, final Object obj) throws TMQLRuntimeException {
		/*
		 * check if value is a sequence
		 */
		if (obj instanceof Collection<?>) {
			/*
			 * iterate over values and call method iterative
			 */
			for (Object o : (Collection<?>) obj) {
				extract(results, o);
			}
		}
		/*
		 * value is an atom
		 */
		else {
			results.add(obj);
		}
	}
}
