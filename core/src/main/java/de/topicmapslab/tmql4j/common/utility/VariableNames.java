/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.utility;

/**
 * Class containing all variable constants which are used in context of a TMQL
 * runtime process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public final class VariableNames {

	/**
	 * private and hidden constructor
	 */
	private VariableNames() {
	}
	
	/**
	 * variable represents a set contain a number of variables
	 */
	public static final String VARIABLES_CONTAINER = "%__variables";
	/**
	 * variable represents the current tuple in context of the interpretation of
	 * a tuple sequence
	 */
	public static final String CURRENT_TUPLE = "@_";
	/**
	 * variable represents the current topic map
	 */
	public static final String CURRENT_MAP = "%_";
	/**
	 * variable represents the predefined environment map
	 */
	public static final String ENVIRONMENT_MAP = "%%";
	/**
	 * variable represents the current position during the iteration over a
	 * tuple sequence
	 */
	public static final String CURRENT_POISTION = "$#";
	/**
	 * variable represents the limit value of a select expression
	 */
	public static final String LIMIT = "$_limit";
	/**
	 * variable represents the offset value of a select expression
	 */
	public static final String OFFSET = "$_lower";
	/**
	 * variable represents the quantifier of a exists clause
	 */
	public static final String QUANTIFIES = "$_quantifies";
	/**
	 * variable represents values used to order the current tuple sequence
	 */
	public static final String ORDER = "%_order";
	/**
	 * variable represents the values which should be filtered or projected
	 */
	public static final String POSTFIXED = "%_postfixed";
	/**
	 * variable represents the possible binding context created by a parallel
	 * boolean-expression
	 */
	public static final String ITERATED_BINDINGS = "%__iteratedBindings";
	/**
	 * variable represents the result of the most expressions
	 */
	public static final String QUERYMATCHES = "%__querymatches";
	/**
	 * variable represents the results not matching the current expression, used
	 * in context of not-expressions
	 */
	public static final String NEGATIVE_MATCHES = "%__negative_querymatches";
	
}
