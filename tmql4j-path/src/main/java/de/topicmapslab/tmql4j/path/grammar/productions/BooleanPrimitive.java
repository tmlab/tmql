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
package de.topicmapslab.tmql4j.path.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.At;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Every;
import de.topicmapslab.tmql4j.path.grammar.lexical.Exists;
import de.topicmapslab.tmql4j.path.grammar.lexical.Not;
import de.topicmapslab.tmql4j.path.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.path.grammar.lexical.Some;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * boolean-primitive.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * boolean-primitive ::= '(' boolean-expression ')'
 * </p>
 * 
 * <p>
 * boolean-primitive ::= 'not' boolean-primitive
 * </p>
 * 
 * <p>
 * boolean-primitive ::= forall-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= exists-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= '@' anchor
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitive extends ExpressionImpl {

	/**
	 * grammar type of boolean-primitive containing a boolean-expression
	 */
	public static final int TYPE_BOOLEAN_EXPRESSION = 0;
	/**
	 * grammar type of boolean-primitive containing a the keyword NOT
	 */
	public static final int TYPE_NOT_EXPRESSION = 1;
	/**
	 * grammar type of boolean-primitive containing an every-clause
	 */
	public static final int TYPE_EVERY_CLAUSE = 2;
	/**
	 * grammar type of boolean-primitive containing an exists-clause
	 */
	public static final int TYPE_EXISTS_CLAUSE = 3;
	/**
	 * grammar type of boolean-primitive containing a scoped expression
	 */
	public static final int TYPE_SCOPED_EXPRESSION = 4;

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
	public BooleanPrimitive(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * contains the keyword NOT
		 */
		if (tmqlTokens.get(0).equals(Not.class)) {
			setGrammarType(TYPE_NOT_EXPRESSION);
			checkForExtensions(BooleanPrimitive.class, tmqlTokens.subList(1,
					tmqlTokens.size()), tokens.subList(1, tokens.size()),
					runtime);
		}
		/*
		 * is forall-clause
		 */
		else if (tmqlTokens.get(0).equals(Every.class)) {
			setGrammarType(TYPE_EVERY_CLAUSE);
			checkForExtensions(ForAllClause.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is exists-clause
		 */
		else if (tmqlTokens.get(0).equals(Exists.class)
				|| tmqlTokens.get(0).equals(Some.class)
				|| tmqlTokens.get(0).equals(At.class)) {
			setGrammarType(TYPE_EXISTS_CLAUSE);
			checkForExtensions(ExistsClause.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is ( boolean-expression )
		 */
		else if (tmqlTokens.get(0).equals(BracketRoundOpen.class)
				&& tmqlTokens.get(tmqlTokens.size() - 1).equals(
						BracketRoundClose.class)) {
			setGrammarType(TYPE_BOOLEAN_EXPRESSION);
			checkForExtensions(BooleanExpression.class, tmqlTokens.subList(1,
					tmqlTokens.size() - 1), tokens.subList(1,
					tmqlTokens.size() - 1), runtime);
		}
		/*
		 * is @ anchor
		 */
		else if (tmqlTokens.get(0).equals(Scope.class)) {
			setGrammarType(TYPE_SCOPED_EXPRESSION);
		}
		/*
		 * is non-canonical exists-clause
		 * 
		 * content ==> some $_ in content satisfies not null
		 */
		else {
			setGrammarType(TYPE_EXISTS_CLAUSE);
			checkForExtensions(ExistsClause.class, tmqlTokens, tokens, runtime);
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
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
