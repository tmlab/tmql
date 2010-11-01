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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.DatatypedElement;
import de.topicmapslab.tmql4j.lexer.token.Dot;
import de.topicmapslab.tmql4j.lexer.token.Element;
import de.topicmapslab.tmql4j.lexer.token.Literal;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.parser.core.expressions.Anchor;

/**
 * 
 * Special interpreter class to interpret anchors.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * anchor ::= '.' | variable | topic-ref | literal
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AnchorInterpreter extends ExpressionInterpreterImpl<Anchor> {

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public AnchorInterpreter(Anchor ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get first token
		 */
		Class<? extends IToken> anchor = getTmqlTokens().get(0);
		String anchor_ = getTokens().get(0);

		QueryMatches matches = new QueryMatches(runtime);

		/*
		 * anchor is an item reference (constant)
		 */
		if (anchor.equals(Element.class)) {
			try {
				/*
				 * create tuple
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				/*
				 * handle as date?
				 */
				if (LiteralUtils.isDate(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asDate(anchor_));
					matches.add(tuple);
				}
				/*
				 * handle as time?
				 */
				else if (LiteralUtils.isTime(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asTime(anchor_));
					matches.add(tuple);
				}
				/*
				 * handle as dateTime?
				 */
				else if (LiteralUtils.isDateTime(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asDateTime(anchor_));
					matches.add(tuple);
				}
				/*
				 * handle as decimal?
				 */
				else if (LiteralUtils.isDecimal(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asDecimal(anchor_));
					matches.add(tuple);
				}
				/*
				 * handle as integer?
				 */
				else if (LiteralUtils.isInteger(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asInteger(anchor_));
					matches.add(tuple);
				}
				/*
				 * handle special topic undef
				 */
				else if ("undef".equalsIgnoreCase(anchor_)) {
					tuple.put(QueryMatches.getNonScopedVariable(), runtime.getEnvironment().getTmqlTopicUndef());
					matches.add(tuple);
				}
				/*
				 * handle as topic-reference
				 */
				else {
					try {
						Construct construct = runtime.getDataBridge().getConstructByIdentifier(runtime, anchor_);
						tuple.put(QueryMatches.getNonScopedVariable(), construct);
						matches.add(tuple);
					} catch (DataBridgeException e) {
						logger.warn("Cannot interpret given anchor '" + anchor_ + "'.");
					}
				}
			} catch (Exception ex) {
				throw new TMQLRuntimeException("Cannot interpret given anchor '" + anchor_ + "'.", ex);
			}
		}
		/*
		 * anchor is current node
		 */
		else if (anchor.equals(Dot.class)) {
			/*
			 * check if current tuple is on top of the stack
			 */
			if (runtime.getRuntimeContext().peek().contains(VariableNames.CURRENT_TUPLE)) {
				/*
				 * store current tuple
				 */
				Map<String, Object> currentTuple = HashUtil.getHashMap();
				currentTuple.put(QueryMatches.getNonScopedVariable(), runtime.getRuntimeContext().peek().getValue(VariableNames.CURRENT_TUPLE));
				matches.add(currentTuple);
			}
			/*
			 * check if context is given by upper-expression
			 */
			else if (runtime.getRuntimeContext().peek().contains(VariableNames.ITERATED_BINDINGS)) {
				QueryMatches queryMatches = (QueryMatches) runtime.getRuntimeContext().peek().getValue(VariableNames.ITERATED_BINDINGS);
				matches = queryMatches;
			}
		}
		/*
		 * anchor is a variable
		 */
		else if (anchor.equals(Variable.class)) {
			final String variable = getTokens().get(0);
			IVariableSet set = runtime.getRuntimeContext().peek();
			/*
			 * check if variable is bind on top of the stack
			 */
			if (set.contains(variable)) {
				ITupleSequence<Object> sequence = runtime.getProperties().newSequence();
				sequence.add(set.getValue(variable));
				/*
				 * save binding as tuple
				 */
				matches.convertToTuples(sequence);
			}
			/*
			 * check if context is given by upper-expression
			 */
			else if (set.contains(VariableNames.ITERATED_BINDINGS)) {
				QueryMatches queryMatches = (QueryMatches) runtime.getRuntimeContext().peek().getValue(VariableNames.ITERATED_BINDINGS);
				QueryMatches match = new QueryMatches(runtime);
				/*
				 * save binding as tuple
				 */
				match.convertToTuples(queryMatches.getPossibleValuesForVariable(variable));
				/*
				 * check if variable was mapped by internal operation
				 */
				if (match.isEmpty() && match.getOrigin(variable) != null) {
					match.convertToTuples(queryMatches.getPossibleValuesForVariable(match.getOrigin(variable)));
				}

				matches.add(new QueryMatches(runtime, queryMatches, match));
			}
		}
		/*
		 * anchor is a string
		 */
		else if (anchor.equals(Literal.class)) {
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asString(anchor_));
			matches.add(tuple);
		}
		/*
		 * anchor is a data-typed element
		 */
		else if (anchor.equals(DatatypedElement.class)) {
			final StringTokenizer tokenizer = new StringTokenizer(anchor_, "^^");
			String value = LiteralUtils.asString(tokenizer.nextToken());			
			String datatype = tokenizer.nextToken();
			try {
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asLiteral(value, datatype));
				matches.add(tuple);
			} catch (Exception ex) {
				throw new TMQLRuntimeException("Cannot interpret given literal '" + value + "' with datatype '" + datatype + "'.", ex);
			}
		}

		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES, matches);

	}
}
