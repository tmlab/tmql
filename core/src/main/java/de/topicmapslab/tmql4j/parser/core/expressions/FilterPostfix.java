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
package de.topicmapslab.tmql4j.parser.core.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.And;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareClose;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;
import de.topicmapslab.tmql4j.lexer.token.DoubleDot;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.Scope;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisTypes;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

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
	public FilterPostfix(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		setGrammarType(-1);
		
		/*
		 * check if it is // anchor
		 */
		if (tmqlTokens.get(0).equals(ShortcutAxisInstances.class)) {
			setGrammarType(TYPE_SHORTCUT_TYPE_FILTER);
		} else if (tmqlTokens.get(0).equals(Scope.class)) {
			setGrammarType(TYPE_SHORTCUT_SCOPE_FILTER);
		} else if (tmqlTokens.get(1).equals(Scope.class)) {
			setGrammarType(TYPE_SCOPE_FILTER);
		} else if (tmqlTokens.get(0).equals(BracketSquareOpen.class)
				&& tmqlTokens.get(tmqlTokens.size() - 1).equals(
						BracketSquareClose.class)) {
			/*
			 * is [#integer]
			 */
			if (tmqlTokens.size() == 3) {
				try {
					Integer.parseInt(tokens.get(1));
				} catch (Exception ex) {
					throw new TMQLInvalidSyntaxException(getTmqlTokens(),
							getTokens(), getClass(),
							"invalid syntax [integer] required");
				}
				setGrammarType(TYPE_SHORTCUT_INDEX_FILTER);
			}
			/*
			 * is [ ^ anchor ]
			 */
			else if (tmqlTokens.get(1).equals(ShortcutAxisTypes.class)) {
				setGrammarType(TYPE_TYPE_FILTER);
			} else if (tmqlTokens.size() == 5) {
				/*
				 * is [#integer .. #integer]
				 */
				if (tmqlTokens.get(2).equals(DoubleDot.class)) {
					try {
						Integer lower = Integer.parseInt(tokens.get(1));
						Integer upper = Integer.parseInt(tokens.get(3));
						if (lower > upper) {
							throw new TMQLInvalidSyntaxException(
									getTmqlTokens(), getTokens(), getClass(),
									"invalid syntax lower have to be smaller than upper bound");
						}
					} catch (Exception ex) {
						throw new TMQLInvalidSyntaxException(getTmqlTokens(),
								getTokens(), getClass(),
								"invalid syntax [integer .. integer] required");
					}
					setGrammarType(TYPE_SHORTCUT_BOUNDS_FILTER);
				}
				/*
				 * is [$# == #integer]
				 */
				else if (tokens.get(1).equalsIgnoreCase(
						VariableNames.CURRENT_POISTION)
						&& tmqlTokens.get(2).equals(Equality.class)) {					
					setGrammarType(TYPE_INDEX_FILTER);
				}				
			} else if (tmqlTokens.size() == 9) {
				/*
				 * is [#integer <= $# & $# < #integer]
				 */
				if (tmqlTokens.get(2).equals(LowerEquals.class)
						&& tokens.get(3).equalsIgnoreCase(
								VariableNames.CURRENT_POISTION)
						&& tmqlTokens.get(4).equals(And.class)
						&& tokens.get(5).equalsIgnoreCase(
								VariableNames.CURRENT_POISTION)
						&& tmqlTokens.get(6).equals(LowerThan.class)) {
					try {
						Integer lower = Integer.parseInt(tokens.get(1));
						Integer upper = Integer.parseInt(tokens.get(7));
						if (lower > upper) {
							throw new TMQLInvalidSyntaxException(
									getTmqlTokens(), getTokens(), getClass(),
									"invalid syntax lower have to be smaller than upper bound");
						}
					} catch (Exception ex) {
						throw new TMQLInvalidSyntaxException(getTmqlTokens(),
								getTokens(), getClass(),
								"invalid syntax [integer <= $# & $# < integer] required");
					}
					setGrammarType(TYPE_BOUNDS_FILTER);
				}				
			}
			
			/*
			 * no other production matches, because of that it has to be a boolean-expression
			 */
			if ( getGrammarType() == -1 ) {
				checkForExtensions(BooleanExpression.class, tmqlTokens.subList(
						1, tmqlTokens.size() - 1), tokens.subList(1, tokens
						.size() - 1), runtime);
				setGrammarType(TYPE_BOOLEAN_EXPRESSION);
			}

		} else {
			throw new TMQLInvalidSyntaxException(getTmqlTokens(), getTokens(),
					getClass(), "invalid syntax [....] required");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

}
