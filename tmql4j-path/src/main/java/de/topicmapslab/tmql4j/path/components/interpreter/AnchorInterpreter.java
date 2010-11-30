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

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.topicmapslab.tmql4j.path.grammar.productions.Variable;
import de.topicmapslab.tmql4j.util.LiteralUtils;

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get first token
		 */
		Class<? extends IToken> anchor = getTmqlTokens().get(0);
		String anchor_ = getTokens().get(0);

		/*
		 * anchor is an item reference (constant)
		 */
		if (anchor.equals(Element.class)) {
			try {
				/*
				 * handle as date?
				 */
				if (LiteralUtils.isDate(anchor_)) {
					return QueryMatches.asQueryMatch(runtime, LiteralUtils.asDate(anchor_));
				}
				/*
				 * handle as time?
				 */
				else if (LiteralUtils.isTime(anchor_)) {
					return QueryMatches.asQueryMatch(runtime, LiteralUtils.asTime(anchor_));
				}
				/*
				 * handle as dateTime?
				 */
				else if (LiteralUtils.isDateTime(anchor_)) {
					return QueryMatches.asQueryMatch(runtime, LiteralUtils.asDateTime(anchor_));
				}
				/*
				 * handle as decimal?
				 */
				else if (LiteralUtils.isDecimal(anchor_)) {
					return QueryMatches.asQueryMatch(runtime, LiteralUtils.asDecimal(anchor_));
				}
				/*
				 * handle as integer?
				 */
				else if (LiteralUtils.isInteger(anchor_)) {
					return QueryMatches.asQueryMatch(runtime, LiteralUtils.asInteger(anchor_));
				}
				/*
				 * handle special topic undef
				 */
				else if ("undef".equalsIgnoreCase(anchor_)) {
					throw new UnsupportedOperationException("Undef is unknown!");
				}
				return QueryMatches.asQueryMatch(runtime, runtime.getConstructResolver().getConstructByIdentifier(context, anchor_));
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
			if (context.getCurrentTuple() != null) {
				return QueryMatches.asQueryMatch(runtime, context.getCurrentTuple());
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
		else if (anchor.equals(Variable.class)) {
			final String variable = getTokens().get(0);
			if (context.getContextBindings() != null) {
				QueryMatches match = new QueryMatches(runtime);
				/*
				 * save binding as tuple
				 */
				match.convertToTuples(match.getPossibleValuesForVariable(variable));
				/*
				 * check if variable was mapped by internal operation
				 */
				if (match.isEmpty() && match.getOrigin(variable) != null) {
					match.convertToTuples(match.getPossibleValuesForVariable(match.getOrigin(variable)));
				}
				return match;
			}
		}
		/*
		 * anchor is a string
		 */
		else if (anchor.equals(Literal.class)) {
			return QueryMatches.asQueryMatch(runtime, LiteralUtils.asString(anchor_));
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
				return QueryMatches.asQueryMatch(runtime, datatypedValue);
			} catch (Exception ex) {
				logger.warn("Cannot convert element for given reference '" + value + "' to datatype '" + datatype + "'!");
			}
		}
		return QueryMatches.emptyMatches();
	}
}
