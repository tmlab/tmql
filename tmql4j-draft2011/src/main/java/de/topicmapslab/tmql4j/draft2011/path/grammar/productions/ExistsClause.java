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
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.At;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Exists;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Least;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Most;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Satisfies;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * exists-clause.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * exists-clause ::= exists-quantifier binding-set satisfies boolean-expression
 * </p>
 * <p>
 * exists-quantifier ::= some | at least integer | at most integer
 * </p>
 * <p>
 * exists-clause ::= exists content ==> some $_ in content satisfies not null
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExistsClause extends ExpressionImpl {

	/**
	 * grammar type of canonical exists-clauses
	 */
	public static final int TYPE_CANONICAL_EXPRESSION = 0;
	/**
	 * grammar type of non-canonical exists-clauses with keyword EXISTS
	 */
	public static final int TYPE_NON_CANONICAL_EXPRESSION = 1;

	/**
	 * language-specific token identifies the quantifier of exists-clause
	 */
	private Class<? extends IToken> quantifier;

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
	@SuppressWarnings("unchecked")
	public ExistsClause(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		int index = 0;
		/*
		 * is some binding-set satisfies boolean-expression
		 */
		if (tmqlTokens.get(0).equals(Some.class)) {
			checkForExtensions(ExistsQuantifiers.class, tmqlTokens.subList(0, 1), tokens.subList(0, 1), runtime);
			quantifier = Some.class;
			index = 1;
		}
		/*
		 * is at least integer binding-set satisfies boolean-expression
		 */
		else if (tmqlTokens.get(0).equals(At.class) && tmqlTokens.get(1).equals(Least.class)) {
			checkForExtensions(ExistsQuantifiers.class, tmqlTokens.subList(0, 3), tokens.subList(0, 3), runtime);
			quantifier = Least.class;
			index = 3;
		}
		/*
		 * is at most integer binding-set satisfies boolean-expression
		 */
		else if (tmqlTokens.get(0).equals(At.class) && tmqlTokens.get(1).equals(Most.class)) {
			checkForExtensions(ExistsQuantifiers.class, tmqlTokens.subList(0, 3), tokens.subList(0, 3), runtime);
			quantifier = Most.class;
			index = 3;
		}

		/*
		 * is exists-quantifier binding-set satisfies boolean-expression
		 */
		if (ParserUtils.containsTokens(tmqlTokens, Satisfies.class)) {
			setGrammarType(TYPE_CANONICAL_EXPRESSION);
			/*
			 * call-back instance of parser utility
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				@Override
				public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
					/*
					 * is bindings set
					 */
					if (getExpressions().size() == 1) {
						checkForExtensions(BindingSet.class, tmqlTokens, tokens, runtime);
					}
					/*
					 * is keyword SATISFIES
					 */
					else {
						checkForExtensions(BooleanExpression.class, tmqlTokens, tokens, runtime);
					}
				}
			};

			/*
			 * create set containing all delimers
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(Satisfies.class);

			/*
			 * split expression
			 */
			ParserUtils.split(callback, tmqlTokens.subList(index, tmqlTokens.size()), tokens.subList(index, tokens.size()), delimers, true);
		}
		/*
		 * is content
		 */
		else {
			setGrammarType(TYPE_NON_CANONICAL_EXPRESSION);
			/*
			 * call-back instance of parser utility
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				@Override
				public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
					checkForExtensions(Content.class, tmqlTokens, tokens, runtime);
				}
			};

			/*
			 * create set containing all delimers
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(Exists.class);

			/*
			 * split expression
			 */
			ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);
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
	 * Method return the language-specific token represents the quantifier of
	 * this exists-clause.
	 * 
	 * @return a language-specific token class
	 */
	public Class<? extends IToken> getQuantifier() {
		return quantifier;
	}
}
