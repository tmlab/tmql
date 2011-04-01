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
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.DatatypedElement;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.Literal;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.util.LiteralUtils;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

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
	 * 
	 */
	public static final String VARIABLE_TOPIC_MAP = "%_";
	/**
	 * the Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

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
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (Anchor.TYPE_PREPARED == getGrammarTypeOfExpression()) {
			return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
		}

		/*
		 * get first token
		 */
		Class<? extends IToken> anchor = getTmqlTokens().get(0);
		String anchor_ = getTokens().get(0);

		/*
		 * anchor is an item reference (constant)
		 */
		if (anchor.equals(Element.class)) {
			if (TmdmSubjectIdentifier.isTmdmName(anchor_) || TmdmSubjectIdentifier.isTmdmOccurrence(anchor_)) {
				return QueryMatches.asQueryMatchNS(runtime, anchor_);
			}
			try {
				/*
				 * handle special topic undef
				 */
				if ("undef".equalsIgnoreCase(anchor_)) {
					throw new UnsupportedOperationException("Undef is unknown!");
				}
				Construct constructByIdentifier = runtime.getConstructResolver().getConstructByIdentifier(context, anchor_);
				if (constructByIdentifier != null) {
					return QueryMatches.asQueryMatchNS(runtime, constructByIdentifier);
				}
				return QueryMatches.emptyMatches();
			} catch (Exception ex) {
				logger.warn("Cannot found element for given reference '" + anchor_ + "'!");
			}
		}
		/*
		 * anchor is current node
		 */
		else if (anchor.equals(Dot.class)) {
			/*
			 * check if current tuple is on top of the stack
			 */
			if (context.getCurrentNode() != null) {
				return QueryMatches.asQueryMatchNS(runtime, context.getCurrentNode());
			}
			/*
			 * check if context is given by upper-expression
			 */
			else if (context.getContextBindings() != null) {
				return context.getContextBindings();
			}
		}
		/*
		 * anchor is a variable
		 */
		else if (anchor.equals(de.topicmapslab.tmql4j.path.grammar.lexical.Variable.class)) {
			final String variable = getTokens().get(0);
			/*
			 * is current index variable
			 */
			if ("$#".equalsIgnoreCase(variable)) {
				if (context.getCurrentIndexInSequence() > 0) {
					return QueryMatches.asQueryMatchNS(runtime, context.getCurrentIndexInSequence());
				}
				return QueryMatches.emptyMatches();
			}
			/*
			 * check context binding for anchor
			 */
			if (context.getContextBindings() != null) {
				/*
				 * save binding as tuple
				 */
				List<Object> possibleValuesForVariable = context.getContextBindings().getPossibleValuesForVariable(variable);
				if (!possibleValuesForVariable.isEmpty()) {
					return QueryMatches.asQueryMatchNS(runtime, possibleValuesForVariable.toArray());
				}
				/*
				 * check if a mapping exists for the variable used
				 */
				final String origin = context.getContextBindings().getOrigin(variable);
				if (origin != null) {
					possibleValuesForVariable = context.getContextBindings().getPossibleValuesForVariable(origin);
					if (!possibleValuesForVariable.isEmpty()) {
						return QueryMatches.asQueryMatchNS(runtime, possibleValuesForVariable.toArray());
					}
				}
			}
			/*
			 * check current tuple for anchor
			 */
			if (context.getCurrentTuple() != null && context.getCurrentTuple().containsKey(variable)) {
				return QueryMatches.asQueryMatchNS(runtime, context.getCurrentTuple().get(variable));
			}
			/*
			 * check system variables
			 */
			Object value = getSystemReference(runtime, context, variable);
			if (value != null) {
				return QueryMatches.asQueryMatchNS(runtime, value);
			}
		}
		/*
		 * anchor is a string
		 */
		else if (anchor.equals(Literal.class)) {
			try {
				/*
				 * handle as date?
				 */
				if (LiteralUtils.isDate(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, LiteralUtils.asDate(anchor_));
				}
				/*
				 * handle as time?
				 */
				else if (LiteralUtils.isTime(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, LiteralUtils.asTime(anchor_));
				}
				/*
				 * handle as dateTime?
				 */
				else if (LiteralUtils.isDateTime(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, LiteralUtils.asDateTime(anchor_));
				}
				/*
				 * handle as decimal?
				 */
				else if (LiteralUtils.isDecimal(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, LiteralUtils.asDecimal(anchor_));
				}
				/*
				 * handle as integer?
				 */
				else if (LiteralUtils.isInteger(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, LiteralUtils.asInteger(anchor_));
				}
				/*
				 * handle as boolean?
				 */
				else if (LiteralUtils.isBoolean(anchor_)) {
					return QueryMatches.asQueryMatchNS(runtime, Boolean.valueOf(anchor_));
				}
				return QueryMatches.asQueryMatch(runtime, QueryMatches.getNonScopedVariable(), LiteralUtils.asString(anchor_));
			} catch (Exception ex) {
				logger.warn("Cannot found element for given reference '" + anchor_ + "'!");
			}
		}
		/*
		 * anchor is a data-typed element
		 */
		else if (anchor.equals(DatatypedElement.class)) {
			final StringTokenizer tokenizer = new StringTokenizer(anchor_, "^^");
			String value = LiteralUtils.asString(tokenizer.nextToken());
			String datatype = tokenizer.nextToken();
			try {
				Object datatypedValue = LiteralUtils.asLiteral(value, datatype);
				return QueryMatches.asQueryMatchNS(runtime, datatypedValue);
			} catch (Exception ex) {
				logger.warn("Cannot convert element for given reference '" + value + "' to datatype '" + datatype + "'!");
			}
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * Internal method to handle system references.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param variable
	 *            the variable name
	 * @return the value of the system reference or <code>null</code> if the
	 *         variable is not a system variable
	 */
	private Object getSystemReference(ITMQLRuntime runtime, IContext context, final String variable) {
		if (VARIABLE_TOPIC_MAP.equalsIgnoreCase(variable)) {
			return context.getQuery().getTopicMap();
		}
		return null;
	}
}
