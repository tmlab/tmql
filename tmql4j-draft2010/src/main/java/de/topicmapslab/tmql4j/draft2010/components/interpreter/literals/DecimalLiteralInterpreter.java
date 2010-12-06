package de.topicmapslab.tmql4j.draft2010.components.interpreter.literals;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DecimalLiteral;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of production 'decimal' ( {@link DecimalLiteral} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DecimalLiteralInterpreter extends ExpressionInterpreterImpl<DecimalLiteral> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public DecimalLiteralInterpreter(DecimalLiteral ex) {
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
		final BigDecimal decimal;
		try {
			decimal = LiteralUtils.asDecimal(literal);
		} catch (NumberFormatException e) {
			logger.warn("Literal '" + literal + "' is invalid decimal.", e);
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, decimal);
	}

}
