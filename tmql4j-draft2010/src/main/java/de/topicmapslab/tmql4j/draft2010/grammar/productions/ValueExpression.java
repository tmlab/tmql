package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Axis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Div;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Function;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Subtraction;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Union;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DateLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DateTimeLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.DecimalLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IriReference;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.StringLiteral;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.TimeLiteral;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Class representing the production 'value-expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueExpression extends ExpressionImpl {

	/**
	 * base constructor to create a new expression without sub-nodes
	 * 
	 * @param parent
	 *            the known parent node
	 * @param tmqlTokens
	 *            the list of language-specific tokens contained by this
	 *            expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this
	 *            expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	public ValueExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * only one token
		 */
		if (tmqlTokens.size() == 1) {
			/*
			 * starts with $
			 */
			if (tmqlTokens.get(0).equals(Variable.class)) {
				checkForExtensions(de.topicmapslab.tmql4j.draft2010.grammar.productions.Variable.class, tmqlTokens, tokens, runtime);
			}
			/*
			 * literal
			 */
			else {
				String token = tokens.get(0);
				/*
				 * is decimal
				 */
				if (LiteralUtils.isDecimal(token)) {
					checkForExtensions(DecimalLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is integer
				 */
				else if (LiteralUtils.isInteger(token)) {
					checkForExtensions(IntegerLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is date
				 */
				else if (LiteralUtils.isDate(token)) {
					checkForExtensions(DateLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is time
				 */
				else if (LiteralUtils.isTime(token)) {
					checkForExtensions(TimeLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is dateTime
				 */
				else if (LiteralUtils.isDateTime(token)) {
					checkForExtensions(DateTimeLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is string
				 */
				else if (LiteralUtils.isString(token)) {
					checkForExtensions(StringLiteral.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * iri-ref / topic-ref
				 */
				else {
					checkForExtensions(IriReference.class, tmqlTokens, tokens, runtime);
				}
			}
		}
		/*
		 * contains union | intersect | minus
		 */
		else if (isSetExpression(tmqlTokens)) {
			checkForExtensions(SetExpression.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * contains numerical operators
		 */
		else if (isNumericalExpression(tmqlTokens)) {
			checkForExtensions(NumericalExpression.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is known function
		 */
		else if (tmqlTokens.get(0).equals(Function.class)) {
			checkForExtensions(FunctionCall.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * unknown type
		 */
		else {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, ValueExpression.class);
		}

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return true;
	}

	/**
	 * Method checks if the given tokens represent a set operation
	 * 
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the tokens represent a set operation,
	 *         <code>false</code> otherwise
	 */
	@SuppressWarnings("unchecked")
	protected static boolean isSetExpression(final List<Class<? extends IToken>> tokens) {
		return ParserUtils.containsTokens(tokens, Union.class, Intersect.class, Subtraction.class);
		// long bracketCount = 0;
		// for (Class<? extends IToken> token : tokens) {
		// if (token.equals(BracketRoundOpen.class) ||
		// token.equals(BracketSquareOpen.class)) {
		// bracketCount++;
		// } else if (token.equals(BracketRoundClose.class) ||
		// token.equals(BracketSquareOpen.class)) {
		// bracketCount--;
		// } else if (bracketCount == 0 && (token.equals(Union.class) ||
		// token.equals(Intersect.class) || token.equals(Subtraction.class))) {
		// return true;
		// }
		// }
		// return false;
	}

	/**
	 * Method checks if the given tokens represent a numerical operation
	 * 
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the tokens represent a numerical operation,
	 *         <code>false</code> otherwise
	 */
	protected static boolean isNumericalExpression(final List<Class<? extends IToken>> tokens) {
		long bracketCount = 0;
		Class<? extends IToken> last = null;
		for (Class<? extends IToken> token : tokens) {
			if (token.equals(BracketRoundOpen.class) || token.equals(BracketSquareOpen.class)) {
				bracketCount++;
			} else if (token.equals(BracketRoundClose.class) || token.equals(BracketSquareOpen.class)) {
				bracketCount--;
			} else if (bracketCount == 0
					&& (token.equals(Plus.class) || token.equals(Minus.class) || token.equals(Modulo.class) || token.equals(Div.class) || (token.equals(Star.class) && !DoubleColon.class.equals(last)
							&& !Axis.class.equals(last) && !BracketRoundOpen.class.equals(last)))) {
				return true;
			}
			last = token;
		}
		return false;
	}
}
