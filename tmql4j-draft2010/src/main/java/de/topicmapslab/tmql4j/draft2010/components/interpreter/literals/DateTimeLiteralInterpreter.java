package de.topicmapslab.tmql4j.draft2010.components.interpreter.literals;

import java.text.ParseException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DateTimeLiteral;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of production 'dateTime' ( {@link DateTimeLiteral}
 * )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DateTimeLiteralInterpreter extends ExpressionInterpreterImpl<DateTimeLiteral> {
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
	public DateTimeLiteralInterpreter(DateTimeLiteral ex) {
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
		final Calendar c;
		try {
			c = LiteralUtils.asDateTime(literal);
		} catch (ParseException e) {
			logger.warn("Literal '" + literal + "' is invalid dateTime.", e);
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, c);
	}

}
