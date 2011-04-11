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
package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.DoubleDot;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisTypes;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * filter-postfix.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * filter-postfix ::= [ boolean-expression ]
 * </p>
 * <p>
 * filter-postfix ::= // anchor ==> [ ^ anchor ]
 * </p>
 * <p>
 * filter-postfix ::= [ integer ] ==> [ $# == integer ]
 * </p>
 * <p>
 * filter-postfix ::= [ integer-1 .. integer-2 ] ==> [ integer-1 <= $# & $# <
 * integer-2 ]
 * </p>
 * <p>
 * filter-postfix ::= [ @ scope ] | @ scope
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FilterPostfix extends ExpressionImpl {

	/**
	 * 
	 */
	private static final String INDEX_VAR = "$#";

	/**
	 * 
	 */
	private static final String BOUNDS_JOIN = "<= $# AND $# <";

	/**
	 * variable represents the current position during the iteration over a
	 * tuple sequence
	 */
	public static final String CURRENT_POISTION = INDEX_VAR;

	/**
	 * grammar type of filter-postfix containing a boolean-expresseion
	 */
	public static final int TYPE_BOOLEAN_EXPRESSION = 0;
	/**
	 * grammar type of filter-postfix containing a type-filter
	 */
	public static final int TYPE_TYPE_FILTER = 1;
	/**
	 * grammar type of filter-postfix containing non-canonical type-filter
	 */
	public static final int TYPE_SHORTCUT_TYPE_FILTER = 2;
	/**
	 * grammar type of filter-postfix containing an index-filter
	 */
	public static final int TYPE_INDEX_FILTER = 3;
	/**
	 * grammar type of filter-postfix containing a non-canonical index-filter
	 */
	public static final int TYPE_SHORTCUT_INDEX_FILTER = 4;
	/**
	 * grammar type of filter-postfix containing an index-range
	 */
	public static final int TYPE_BOUNDS_FILTER = 5;
	/**
	 * grammar type of filter-postfix containing a non-canonical index-range
	 */
	public static final int TYPE_SHORTCUT_BOUNDS_FILTER = 6;
	/**
	 * grammar type of filter-postfix containing a scoped filter
	 */
	public static final int TYPE_SCOPE_FILTER = 7;
	/**
	 * grammar type of filter-postfix containing a non-canonical scoped filter
	 */
	public static final int TYPE_SHORTCUT_SCOPE_FILTER = 8;

	/**
	 * base constructor to create a new instance.
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
	public FilterPostfix(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		setGrammarType(-1);

		/*
		 * check if it is // anchor
		 */
		if (tmqlTokens.get(0).equals(ShortcutAxisInstances.class)) {
			setGrammarType(TYPE_SHORTCUT_TYPE_FILTER);
			checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
		} else if (tmqlTokens.get(0).equals(Scope.class)) {
			setGrammarType(TYPE_SHORTCUT_SCOPE_FILTER);
			checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
		} else if (tmqlTokens.get(1).equals(Scope.class)) {
			setGrammarType(TYPE_SCOPE_FILTER);
			checkForExtensions(Anchor.class, tmqlTokens.subList(2, 3), tokens.subList(2, 3), runtime);
		} else if (tmqlTokens.get(0).equals(BracketSquareOpen.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(BracketSquareClose.class)) {
			/*
			 * is [#integer]
			 */
			if (tmqlTokens.size() == 3) {
				checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
				setGrammarType(TYPE_SHORTCUT_INDEX_FILTER);
			}
			/*
			 * is [ ^ anchor ]
			 */
			else if (tmqlTokens.get(1).equals(ShortcutAxisTypes.class)) {
				setGrammarType(TYPE_TYPE_FILTER);
				checkForExtensions(Anchor.class, tmqlTokens.subList(2, 3), tokens.subList(2, 3), runtime);
			} else if (tmqlTokens.size() == 5) {
				/*
				 * is [#integer .. #integer]
				 */
				if (tmqlTokens.get(2).equals(DoubleDot.class)) {
					checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
					checkForExtensions(Anchor.class, tmqlTokens.subList(3, 4), tokens.subList(3, 4), runtime);
					setGrammarType(TYPE_SHORTCUT_BOUNDS_FILTER);
				}
				/*
				 * is [$# == #integer]
				 */
				else if (tokens.get(1).equalsIgnoreCase(CURRENT_POISTION) && tmqlTokens.get(2).equals(Equality.class)) {
					checkForExtensions(Anchor.class, tmqlTokens.subList(3, 4), tokens.subList(3, 4), runtime);
					setGrammarType(TYPE_INDEX_FILTER);
				}
			} else if (tmqlTokens.size() == 9) {
				/*
				 * is [#integer <= $# & $# < #integer]
				 */
				if (tmqlTokens.get(2).equals(LowerEquals.class) && tokens.get(3).equalsIgnoreCase(CURRENT_POISTION) && tmqlTokens.get(4).equals(And.class)
						&& tokens.get(5).equalsIgnoreCase(CURRENT_POISTION) && tmqlTokens.get(6).equals(LowerThan.class)) {
					checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
					checkForExtensions(Anchor.class, tmqlTokens.subList(7, 8), tokens.subList(7, 8), runtime);
					setGrammarType(TYPE_BOUNDS_FILTER);
				}
			}

			/*
			 * no other production matches, because of that it has to be a
			 * boolean-expression
			 */
			if (getGrammarType() == -1) {
				checkForExtensions(BooleanExpression.class, tmqlTokens.subList(1, tmqlTokens.size() - 1), tokens.subList(1, tokens.size() - 1), runtime);
				setGrammarType(TYPE_BOOLEAN_EXPRESSION);
			}

		} else {
			throw new TMQLInvalidSyntaxException(getTmqlTokens(), getTokens(), getClass(), "invalid syntax [....] required");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartAfter(StringBuilder builder) {
		builder.append(BracketSquareClose.TOKEN);
		builder.append(WHITESPACE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getJoinToken() {
		if (getGrammarType() == TYPE_BOUNDS_FILTER || getGrammarType() == TYPE_SHORTCUT_BOUNDS_FILTER) {
			return BOUNDS_JOIN;
		}
		return super.getJoinToken();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFlatPartBefore(StringBuilder builder) {
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(WHITESPACE);
		if (getGrammarType() == TYPE_TYPE_FILTER || getGrammarType() == TYPE_SHORTCUT_TYPE_FILTER) {
			builder.append(Dot.TOKEN);
			builder.append(WHITESPACE);
			builder.append(MoveForward.TOKEN);
			builder.append(WHITESPACE);
			builder.append(AxisTypes.TOKEN);
			builder.append(WHITESPACE);
			builder.append(Equality.TOKEN);
			builder.append(WHITESPACE);
		} else if (getGrammarType() == TYPE_SCOPE_FILTER || getGrammarType() == TYPE_SHORTCUT_SCOPE_FILTER) {
			builder.append(Dot.TOKEN);
			builder.append(WHITESPACE);
			builder.append(MoveForward.TOKEN);
			builder.append(WHITESPACE);
			builder.append(AxisScope.TOKEN);
			builder.append(WHITESPACE);
			builder.append(Equality.TOKEN);
			builder.append(WHITESPACE);
		} else if (getGrammarType() == TYPE_INDEX_FILTER || getGrammarType() == TYPE_SHORTCUT_INDEX_FILTER) {
			builder.append(INDEX_VAR);
			builder.append(WHITESPACE);
			builder.append(Equality.TOKEN);
			builder.append(WHITESPACE);
		}
	}

}
