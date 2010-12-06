/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Div;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.ComparisonUtils;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.MathematicUtils;

/**
 * Utility class to handle set operations.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryMatchUtils {

	/**
	 * Method executes the operation specified by the given token.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param operators
	 *            the operator token
	 * @param arguments
	 *            an array of arguments
	 * @return the result of operation
	 * @throws TMQLRuntimeException
	 *             thrown if operation fails or arguments are invalid
	 */
	public static QueryMatches operation(final ITMQLRuntime runtime, final Class<? extends IToken> operator, final QueryMatches... arguments) throws TMQLRuntimeException {
		/*
		 * is summation
		 */
		if (operator.equals(Plus.class)) {
			return summation(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is minus
		 */
		else if (operator.equals(Minus.class)) {
			/*
			 * is sign
			 */
			if (arguments.length == 1) {
				return sign(runtime, arguments[0]);
			}
			/*
			 * is subtraction
			 */
			else if (arguments.length == 2) {
				return subtraction(runtime, arguments[0], arguments[1]);
			}
		}
		/*
		 * is multiplication
		 */
		else if (operator.equals(Star.class)) {
			return multiplication(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is division
		 */
		else if (operator.equals(Div.class)) {
			return division(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is modulo
		 */
		else if (operator.equals(Modulo.class)) {
			return modulo(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is lower-than
		 */
		else if (operator.equals(LowerThan.class)) {
			return lowerThan(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is lower-or-equals
		 */
		else if (operator.equals(LowerEquals.class)) {
			return lowerOrEquals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is greater-than
		 */
		else if (operator.equals(GreaterThan.class)) {
			return greaterThan(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is greater-or-equals
		 */
		else if (operator.equals(GreaterEquals.class)) {
			return greaterOrEquals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is regular-expression
		 */
		else if (operator.equals(RegularExpression.class)) {
			return matchesRegExp(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is equality
		 */
		else if (operator.equals(Equality.class)) {
			return equals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * is not same
		 */
		else if (operator.equals(Unequals.class)) {
			return unequals(runtime, arguments[0], arguments[1]);
		}

		throw new TMQLRuntimeException("The operator '" + operator.getSimpleName() + "' is unknown.");
	}

	/**
	 * Method realize set operation 'union'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the result of union
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches union(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);
		result.add(leftHand);
		result.add(rightHand);
		return result;
	}

	/**
	 * Method realize set operation 'subtraction'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the result of subtraction
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches minus(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		for (Map<String, Object> tuple : leftHand) {
			boolean add = true;
			for (Map<String, Object> tupleB : rightHand) {
				if (tupleB.entrySet().containsAll(tuple.entrySet())) {
					add = false;
					break;
				}
			}
			if (add) {
				result.add(tuple);
			}
		}

		return result;
	}

	/**
	 * Method realize set operation 'intersection'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the result of intersection
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches intersect(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);
		for (Map<String, Object> tuple : leftHand) {
			boolean add = false;
			for (Map<String, Object> tupleB : rightHand) {
				if (tupleB.entrySet().containsAll(tuple.entrySet())) {
					add = true;
					break;
				}
			}
			if (add) {
				result.add(tuple);
			}
		}
		return result;
	}

	/**
	 * Method realize set operation 'intersection'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the result of intersection
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches equality(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		Collection<Object> values = rightHand.getPossibleValuesForVariable();
		if (values.isEmpty()) {
			values = rightHand.getPossibleValuesForVariable("$0");
		}
		QueryMatches result = new QueryMatches(runtime);
		for (Map<String, Object> tuple : leftHand) {
			if (tuple.containsKey(QueryMatches.getNonScopedVariable())) {
				if (values.contains(tuple.get(QueryMatches.getNonScopedVariable()))) {
					result.add(tuple);
				}
			} else if (tuple.containsKey("$0")) {
				if (values.contains(tuple.get("$0"))) {
					result.add(tuple);
				}
			} else {
				boolean add = false;
				for (Map<String, Object> tupleB : rightHand) {
					/*
					 * both tuples are of the same size and the right hand part
					 * contains only the key $$$$
					 */
					if (tuple.size() == 1 && tupleB.size() == 1 && tupleB.containsKey(QueryMatches.getNonScopedVariable())) {
						add = tuple.values().containsAll(tupleB.values());
					}
					/*
					 * else compare by key-value-pairs
					 */
					else if (tupleB.entrySet().containsAll(tuple.entrySet())) {
						add = true;
						break;
					}
				}
				if (add) {
					result.add(tuple);
				}
			}
		}
		return result;
	}

	/**
	 * Method realize comparison operation 'contains'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches contains(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		for (Map<String, Object> tuple : leftHand) {
			out: for (Map<String, Object> tupleB : rightHand) {
				for (Entry<String, Object> entry : tupleB.entrySet()) {
					if (!tuple.entrySet().contains(entry)) {
						continue out;
					}
				}
				result.add(tuple);
				break;
			}
		}

		return result;
	}

	/**
	 * Method realize comparison operation 'equals'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches equals(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		for (Map<String, Object> tuple : leftHand) {
			for (Map<String, Object> tupleB : rightHand) {
				if (tuple.entrySet().containsAll(tupleB.entrySet())) {
					result.add(tuple);
				}
				break;
			}
		}

		return result;
	}

	/**
	 * Method realize comparison operation 'unequals'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches unequals(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		for (Map<String, Object> tuple : leftHand) {
			out: for (Map<String, Object> tupleB : rightHand) {
				if (tuple.entrySet().containsAll(tupleB.entrySet())) {
					continue out;
				}
				result.add(tuple);
				break;
			}
		}

		return result;
	}

	/**
	 * Method realize comparison operation 'lowerThan'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches lowerThan(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return comparision(runtime, "isLowerThan", leftHand, rightHand);
	}

	/**
	 * Method realize comparison operation 'lower or equals'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches lowerOrEquals(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return comparision(runtime, "isLowerOrEquals", leftHand, rightHand);
	}

	/**
	 * Method realize comparison operation 'greater than'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches greaterThan(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return comparision(runtime, "isGreaterThan", leftHand, rightHand);

	}

	/**
	 * Method realize comparison operation 'greater or equals'.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches greaterOrEquals(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return comparision(runtime, "isGreaterOrEquals", leftHand, rightHand);
	}

	/**
	 * Method realize comparison operation 'matches regExp' =~.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches matchesRegExp(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return comparision(runtime, "matchesRegExp", leftHand, rightHand);
	}

	/**
	 * Method realize comparison operation.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param method
	 *            the name of the method to call
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches only contains the sub-set of matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	private static QueryMatches comparision(final ITMQLRuntime runtime, final String method, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		if (!rightHand.isEmpty() && rightHand.getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {
			Object obj = rightHand.getPossibleValuesForVariable().get(0);
			for (Map<String, Object> other : leftHand) {
				Iterator<String> variables = other.keySet().iterator();
				try {
					if (other.containsKey(QueryMatches.getNonScopedVariable())) {
						if (ComparisonUtils.compare(method, other.get(QueryMatches.getNonScopedVariable()), obj)) {
							Map<String, Object> tuple = HashUtil.getHashMap();

							if (other.size() > 1) {
								String variable = variables.next();
								while (variable.equalsIgnoreCase(QueryMatches.getNonScopedVariable())) {
									variable = variables.next();
								}
								tuple.put(QueryMatches.getNonScopedVariable(), other.get(variable));
							} else {
								tuple.put(QueryMatches.getNonScopedVariable(), Boolean.valueOf(true));
							}
							result.add(tuple);
						}
					}
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}
		}

		return result;
	}

	/**
	 * Method realize mathematical operation of sign
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @return the query-matches contains the results of operation
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches sign(final ITMQLRuntime runtime, final QueryMatches leftHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "sign", leftHand);
	}

	/**
	 * Method realize mathematical operation of summation '+'
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches contains the sums of all combinations
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches summation(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "summation", leftHand, rightHand);
	}

	/**
	 * Method realize mathematical operation of subtraction '-'
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches contains the differences of all combinations
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches subtraction(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "subtraction", leftHand, rightHand);
	}

	/**
	 * Method realize mathematical operation of multiplication '*'
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches contains the products of all combinations
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches multiplication(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "multiplication", leftHand, rightHand);
	}

	/**
	 * Method realize mathematical operation of division '/'
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches contains the quotients of all combinations
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches division(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "division", leftHand, rightHand);
	}

	/**
	 * Method realize mathematical operation of modulo '%'
	 * 
	 * @param runtime
	 *            the runtime
	 * @param leftHand
	 *            the left hand set
	 * @param rightHand
	 *            the right hand set
	 * @return the query-matches contains the quotients without remainder of all
	 *         combinations
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	public static QueryMatches modulo(final ITMQLRuntime runtime, final QueryMatches leftHand, final QueryMatches rightHand) throws TMQLRuntimeException {
		return mathematicalOperation(runtime, "modulo", leftHand, rightHand);
	}

	/**
	 * Method realize mathematical operation.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param method
	 *            the name of the method to call
	 * @param arguments
	 *            the arguments sets
	 * @return the query-matches containing the results of mathematical
	 *         operation
	 * @throws TMQLRuntimeException
	 *             thrown if result cannot be instantiate
	 */
	private static QueryMatches mathematicalOperation(final ITMQLRuntime runtime, final String method, final QueryMatches... arguments) throws TMQLRuntimeException {
		QueryMatches result = new QueryMatches(runtime);

		/*
		 * unary operation
		 */
		if (arguments.length == 1) {
			/*
			 * iterate over argument values of the first content set
			 */
			for (Map<String, Object> arg : arguments[0]) {
				try {
					/*
					 * check if variable is bound
					 */
					if (arg.containsKey(QueryMatches.getNonScopedVariable())) {
						/*
						 * calculate result
						 */
						Object r = MathematicUtils.calculate(method, arg.get(QueryMatches.getNonScopedVariable()));
						/*
						 * check if result is available
						 */
						if (r != null) {
							Map<String, Object> tuple = HashUtil.getHashMap();
							tuple.put(QueryMatches.getNonScopedVariable(), r);
							result.add(tuple);
						}
					}
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}
		}
		/*
		 * binary operation
		 */
		if (arguments.length == 2) {
			/*
			 * iterate over argument values of the first content set
			 */
			for (Map<String, Object> arg : arguments[0]) {
				/*
				 * iterate over argument values of the second content set
				 */
				for (Map<String, Object> arg2 : arguments[1]) {
					try {
						/*
						 * check if variables are bound
						 */
						if (arg.containsKey(QueryMatches.getNonScopedVariable()) && arg2.containsKey(QueryMatches.getNonScopedVariable())) {
							/*
							 * calculate result
							 */
							Object r = MathematicUtils.calculate(method, arg.get(QueryMatches.getNonScopedVariable()), arg2.get(QueryMatches.getNonScopedVariable()));
							/*
							 * check if result is available
							 */
							if (r != null) {
								Map<String, Object> tuple = HashUtil.getHashMap();
								tuple.put(QueryMatches.getNonScopedVariable(), r);
								result.add(tuple);
							}
						}
					} catch (Exception e) {
						throw new TMQLRuntimeException(e);
					}
				}
			}
		}

		return result;
	}
}
