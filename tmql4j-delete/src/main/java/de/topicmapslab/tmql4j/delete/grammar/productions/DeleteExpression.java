/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.delete.grammar.tokens.Cascade;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.All;
import de.topicmapslab.tmql4j.path.grammar.lexical.Delete;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.Function;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * delete-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * delete-expression ::= DELETE [CASCADE] &lt;value-expression&gt; [ WHERE boolean-expression ]
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteExpression extends ExpressionImpl {

	/**
	 * grammar type of delete-expression which does not contain a where-clause
	 */
	public static final int TYPE_WITHOUT_WHERE_CLAUSE = 0;
	/**
	 * grammar type of delete-expression which contain a where-clause
	 */
	public static final int TYPE_WITH_WHERE_CLAUSE = 1;

	/**
	 * grammar type of delete-expression with keyword ALL
	 */
	public static final int TYPE_ALL = 2;

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
	public DeleteExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		/*
		 * check if keyword ALL is contained
		 */
		if (tmqlTokens.contains(All.class)) {
			setGrammarType(TYPE_ALL);
		}
		/*
		 * check if the keyword WHERE is contained
		 */
		else if (tmqlTokens.contains(Where.class)) {
			/*
			 * get index of the keyword WHERE
			 */
			int whereIndex = tmqlTokens.indexOf(Where.class);
			/*
			 * add delete-clause
			 */
			checkForExtensions(DeleteClause.class, tmqlTokens.subList(0, whereIndex), tokens.subList(0, whereIndex), runtime);
			/*
			 * add where-clause
			 */
			checkForExtensions(WhereClause.class, tmqlTokens.subList(whereIndex, tmqlTokens.size()), tokens.subList(whereIndex, tokens.size()), runtime);
			/*
			 * set grammar type
			 */
			setGrammarType(TYPE_WITH_WHERE_CLAUSE);
		} else {
			/*
			 * add delete-clause
			 */
			checkForExtensions(DeleteClause.class, tmqlTokens, tokens, runtime);
			/*
			 * set grammar type
			 */
			setGrammarType(TYPE_WITHOUT_WHERE_CLAUSE);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid() {
		if (getTmqlTokens().isEmpty()) {
			return false;
		} else if (!getTmqlTokens().get(0).equals(Delete.class)) {
			return false;
		}
		return  getTmqlTokens().get(1).equals(Cascade.class) || getTmqlTokens().get(1).equals(Function.class) || getTmqlTokens().get(1).equals(Element.class) || getTmqlTokens().get(1).equals(Wildcard.class)  || ParserUtils.containsTokens(getTmqlTokens(), Where.class);
	}
	
}
