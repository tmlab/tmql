package de.topicmapslab.tmql4j.draft2010.components.interpreter.literals;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of production 'integer' ( {@link IntegerLiteral} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IntegerLiteralInterpreter extends ExpressionInterpreterImpl<IntegerLiteral> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public IntegerLiteralInterpreter(IntegerLiteral ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * convert literal
		 */
		final String literal = getTokens().get(0);
		final BigInteger integer;
		try {
			integer = LiteralUtils.asInteger(literal);
		} catch (NumberFormatException e) {
			logger.warn("Literal '" + literal + "' is invalid integer.", e);
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, integer);

	}

}
