package de.topicmapslab.tmql4j.draft2010.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DateLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DateTimeLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.DecimalLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IntegerLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IriReference;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.StringLiteral;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.TimeLiteral;
import de.topicmapslab.tmql4j.draft2010.tokens.Div;
import de.topicmapslab.tmql4j.draft2010.tokens.DoubleColon;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundClose;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;
import de.topicmapslab.tmql4j.lexer.token.Function;
import de.topicmapslab.tmql4j.lexer.token.Intersect;
import de.topicmapslab.tmql4j.lexer.token.Modulo;
import de.topicmapslab.tmql4j.lexer.token.Plus;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.lexer.token.Star;
import de.topicmapslab.tmql4j.lexer.token.Substraction;
import de.topicmapslab.tmql4j.lexer.token.Union;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

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
	public ValueExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * only one token
		 */
		if (tmqlTokens.size() == 1) {
			/*
			 * starts with $
			 */
			if (tmqlTokens.get(0).equals(Variable.class)) {
				checkForExtensions(
						de.topicmapslab.tmql4j.parser.core.expressions.Variable.class,
						tmqlTokens, tokens, runtime);
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
					checkForExtensions(DecimalLiteral.class, tmqlTokens,
							tokens, runtime);
				}
				/*
				 * is integer
				 */
				else if (LiteralUtils.isInteger(token)) {
					checkForExtensions(IntegerLiteral.class, tmqlTokens,
							tokens, runtime);
				}
				/*
				 * is date
				 */
				else if (LiteralUtils.isDate(token)) {
					checkForExtensions(DateLiteral.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is time
				 */
				else if (LiteralUtils.isTime(token)) {
					checkForExtensions(TimeLiteral.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * is dateTime
				 */
				else if (LiteralUtils.isDateTime(token)) {
					checkForExtensions(DateTimeLiteral.class, tmqlTokens,
							tokens, runtime);
				}
				/*
				 * is string
				 */
				else if (LiteralUtils.isString(token)) {
					checkForExtensions(StringLiteral.class, tmqlTokens, tokens,
							runtime);
				}
				/*
				 * iri-ref / topic-ref
				 */
				else {
					checkForExtensions(IriReference.class, tmqlTokens, tokens,
							runtime);
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
			checkForExtensions(NumericalExpression.class, tmqlTokens, tokens,
					runtime);
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
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens,
					ValueExpression.class);
		}

	}

	
	public boolean isValid() {
		return true;
	}

	protected static boolean isSetExpression(
			final List<Class<? extends IToken>> tokens) {
		long bracketCount = 0;
		for (Class<? extends IToken> token : tokens) {
			if (token.equals(BracketRoundOpen.class)
					|| token.equals(BracketSquareOpen.class)) {
				bracketCount++;
			} else if (token.equals(BracketRoundClose.class)
					|| token.equals(BracketSquareOpen.class)) {
				bracketCount--;
			} else if (bracketCount == 0
					&& (token.equals(Union.class)
							|| token.equals(Intersect.class) || token
							.equals(Substraction.class))) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isNumericalExpression(
			final List<Class<? extends IToken>> tokens) {
		long bracketCount = 0;
		Class<? extends IToken> last = null;
		for (Class<? extends IToken> token : tokens) {
			if (token.equals(BracketRoundOpen.class)
					|| token.equals(BracketSquareOpen.class)) {
				bracketCount++;
			} else if (token.equals(BracketRoundClose.class)
					|| token.equals(BracketSquareOpen.class)) {
				bracketCount--;
			} else if (bracketCount == 0
					&& (token.equals(Plus.class)
							|| token
									.equals(de.topicmapslab.tmql4j.lexer.token.Minus.class)
							|| token.equals(Modulo.class)
							|| token.equals(Div.class) || (token
							.equals(Star.class)
							&& !DoubleColon.class.equals(last) && !ShortcutAxisAtomifyMoveForward.class
							.equals(last)))) {
				return true;
			}
			last = token;
		}
		return false;
	}

}
