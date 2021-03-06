package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Div;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the production 'numerical-expression' of the new draft
 * 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NumericalExpression extends ExpressionImpl {

	/**
	 * type addition or subtraction
	 */
	public final static int TYPE_ADDITIVE_OPERATION = 0;
	/**
	 * type multiplication, division or modulo
	 */
	public final static int TYPE_MULTIPLICATIVE_OPERATION = 1;
	/**
	 * type unary operation
	 */
	public final static int TYPE_UNARY = 2;

	/**
	 * the operators
	 */
	private final List<Class<? extends IToken>> operators = new LinkedList<Class<? extends IToken>>();

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
	public NumericalExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * contains binary additive-operators + or -
		 */
		List<Integer> indizes = indizesOfAdditiveOperations(tmqlTokens);
		if (!indizes.isEmpty()) {
			int last = 0;
			for (Integer index : indizes) {
				checkForExtensions(NumericalExpression.class, tmqlTokens.subList(last, index), tokens.subList(last, index), runtime);
				operators.add(tmqlTokens.get(index));
				last = index + 1;
			}
			checkForExtensions(NumericalExpression.class, tmqlTokens.subList(last, tmqlTokens.size()), tokens.subList(last, tokens.size()), runtime);
			setGrammarType(TYPE_ADDITIVE_OPERATION);
		}
		/*
		 * multiplicative or unary expression
		 */
		else {
			/*
			 * contains binary multiplicative-operators * or mod or div
			 */
			indizes = indizesOfMultiplicativeOperations(tmqlTokens);
			if (!indizes.isEmpty()) {
				int last = 0;
				for (Integer index : indizes) {
					checkForExtensions(NumericalExpression.class, tmqlTokens.subList(last, index), tokens.subList(last, index), runtime);
					operators.add(tmqlTokens.get(index));
					last = index + 1;
				}
				checkForExtensions(NumericalExpression.class, tmqlTokens.subList(last, tmqlTokens.size()), tokens.subList(last, tokens.size()), runtime);
				setGrammarType(TYPE_MULTIPLICATIVE_OPERATION);
			}
			/*
			 * is unary operator
			 */
			else {
				Class<? extends IToken> token = tmqlTokens.get(0);
				/*
				 * contains sign
				 */
				if (token.equals(Minus.class)) {
					checkForExtensions(Expression.class, tmqlTokens.subList(1, tmqlTokens.size()), tokens.subList(1, tokens.size()), runtime);
					operators.add(token);
				}
				/*
				 * no sign
				 */
				else {
					checkForExtensions(Expression.class, tmqlTokens, tokens, runtime);
				}
				setGrammarType(TYPE_UNARY);
			}
		}

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/**
	 * Extracts the indexes of the given operator classes for additive
	 * operations
	 * 
	 * @param tmqlTokens
	 *            the operator classes
	 * @return a list of all indexes
	 */
	private List<Integer> indizesOfAdditiveOperations(List<Class<? extends IToken>> tmqlTokens) {
		List<Integer> indizes = new LinkedList<Integer>();
		long parenthics = 0;

		Class<? extends IToken> last = null;
		Class<? extends IToken> nextToLast = null;

		for (int index = 0; index < tmqlTokens.size(); index++) {
			Class<? extends IToken> token = tmqlTokens.get(index);
			if (token.equals(BracketSquareOpen.class) || token.equals(BracketRoundOpen.class)) {
				parenthics++;
			} else if (token.equals(BracketSquareOpen.class) || token.equals(BracketRoundClose.class)) {
				parenthics--;
			}
			/*
			 * ignore as signs
			 */
			else if (parenthics == 0 && last != null
			/*
			 * token is additive operator + or -
			 */
			&& (token.equals(Plus.class) || token.equals(Minus.class)
			/*
			 * last token isn't an operator
			 */
			&& !(Plus.class.equals(last) || Minus.class.equals(last) || Div.class.equals(last) || Modulo.class.equals(last)
			/*
			 * last token isn't a * except as place holder after ::
			 */
			|| (Star.class.equals(last) && !DoubleColon.class.equals(nextToLast))))) {
				indizes.add(index);
			}
			/*
			 * store tokens
			 */
			nextToLast = last;
			last = token;
		}
		return indizes;
	}

	/**
	 * Extracts the indexes of the given operator classes for multiplicative
	 * operations
	 * 
	 * @param tmqlTokens
	 *            the operator classes
	 * @return a list of all indexes
	 */
	private List<Integer> indizesOfMultiplicativeOperations(List<Class<? extends IToken>> tmqlTokens) {
		List<Integer> indizes = new LinkedList<Integer>();
		long parenthics = 0;

		Class<? extends IToken> last = null;

		for (int index = 0; index < tmqlTokens.size(); index++) {
			Class<? extends IToken> token = tmqlTokens.get(index);
			if (token.equals(BracketSquareOpen.class) || token.equals(BracketRoundOpen.class)) {
				parenthics++;
			} else if (token.equals(BracketSquareOpen.class) || token.equals(BracketRoundClose.class)) {
				parenthics--;
			}
			/*
			 * ignore as signs
			 */
			else if (parenthics == 0 && last != null
			/*
			 * token is multiplicative operator *, div or mod and * is not a
			 * place holder after :: and /
			 */
			&& (token.equals(Div.class) || token.equals(Modulo.class) || (token.equals(Star.class) && !DoubleColon.class.equals(last)))) {
				indizes.add(index);
			}
			/*
			 * store tokens
			 */
			last = token;
		}
		return indizes;
	}

	/**
	 * Returns all internal stored operators
	 * 
	 * @return the operators
	 */
	public List<Class<? extends IToken>> getOperators() {
		return operators;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void asFlatString(StringBuilder builder) {
		if (getGrammarType() == TYPE_UNARY) {
			if (getTmqlTokens().get(0).equals(Minus.class)) {
				builder.append(Minus.TOKEN);
				builder.append(WHITESPACE);
			}
			getExpressions().get(0).asFlatString(builder);
		} else {
			int index = 0;
			boolean first = true;
			for (NumericalExpression n : getExpressionFilteredByType(NumericalExpression.class)) {
				try {
					if (!first) {
						builder.append(operators.get(index++).newInstance().getLiteral());
						builder.append(WHITESPACE);
					}
					n.asFlatString(builder);
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}
		}
	}
}
