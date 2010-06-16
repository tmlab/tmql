package de.topicmapslab.tmql4j.draft2010.interpreter.literals;

import java.math.BigInteger;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

/**
 * Interpreter implementation of production 'integer' ( {@link IntegerLiteral} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IntegerLiteralInterpreter extends
		ExpressionInterpreterImpl<IntegerLiteral> {

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
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * convert literal
		 */
		final String literal = getTokens().get(0);
		final BigInteger integer;
		try {
			integer = LiteralUtils.asInteger(literal);
		} catch (NumberFormatException e) {
			throw new TMQLRuntimeException("Literal '" + literal
					+ "' is invalid integer.", e);
		}

		/*
		 * convert result type to query-match
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(QueryMatches.getNonScopedVariable(), integer);
		QueryMatches results = new QueryMatches(runtime);
		results.add(tuple);

		/*
		 * store result at the variable layer
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

	}

}
