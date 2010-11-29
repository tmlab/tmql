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
package de.topicmapslab.tmql4j.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interface definition representing one specific production rule of the TMQL
 * language draft. The expression also represent one node of the parsing-tree
 * generated by the parser instance. The instance encapsulates all tokens
 * containing to the production rule, all variables and provide utility
 * functions to access information.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IExpression {

	/**
	 * Method returns a list of all contained expression. Contained expressions
	 * will production rules which are part of the represented rule and
	 * interpreted as children nodes or leafs of the parsing tree.
	 * 
	 * @return the list of contained expression
	 */
	public List<IExpression> getExpressions();

	/**
	 * Method returns a list of string-represented tokens as a sub-set of the
	 * tokens generated by the lexical scanner. The list of tokens represent the
	 * whole sub-query of this production rule.
	 * 
	 * @return a list of string-represented tokens
	 */
	public List<String> getTokens();

	/**
	 * Method returns a list of language-specific tokens as a sub-set of the
	 * tokens generated by the lexical scanner. The list of tokens represent the
	 * whole sub-query of this production rule.
	 * 
	 * @return a list of language-specific tokens
	 */
	public List<Class<? extends IToken>> getTmqlTokens();

	/**
	 * In the current draft some production rules support different syntactical
	 * patterns and can be differ in grammar levels. The method return an
	 * identifier representing the grammar type at the context of the production
	 * rule.
	 * 
	 * @return a numeric value representing the grammar type
	 */
	public int getGrammarType();

	/**
	 * In the current draft some production rules support different syntactical
	 * patterns and can be differ in grammar levels. The method set the grammar
	 * type to the given numeric value.
	 * 
	 * 
	 * @param type
	 *            a numeric value representing the grammar type
	 */
	public void setGrammarType(int type);

	/**
	 * Method add a new sub-tree to the production rule. The method normally
	 * calls during the initialization process of the production itself and
	 * during the parsing process. The method add a new sub-expression to the
	 * internal list.
	 * 
	 * @param ex
	 *            the new expression to add
	 */
	public void addExpression(IExpression ex);

	/**
	 * Method returns a sub-list of contained production rules filtered by a
	 * type. The type symbolize the production rule type the caller is
	 * interested in. Please note that at least an empty list will be returned
	 * and at most the result is equal to the method
	 * {@link IExpression#getExpressions()}.
	 * 
	 * @param <T>
	 *            the type parameter of expressions the caller is interested in
	 * @param type
	 *            the type of expressions the caller is interested in
	 * @return a list of expressions of the given type which are included by the
	 *         current instance
	 */
	public <T extends IExpression> List<T> getExpressionFilteredByType(Class<? extends T> type);

	/**
	 * Method returns the current parent node as part of the parsing tree. The
	 * parent instance will be set by calling the constructor and represent the
	 * production rule containing the expression represented by this instance.
	 * 
	 * @return the parent expression. Please note that an instance of
	 *         query-expression can return <code>null</code> if it is root-node
	 *         of parsing tree.
	 */
	public IExpression getParent();

	/**
	 * Method extract all language-specific tokens which represents a variable
	 * token and transform them to their string-represented pattern.
	 * 
	 * @return a list of all variables contained in the production rule
	 */
	public List<String> getVariables();

	/**
	 * During the optimization process the contained expressions can be reorder
	 * by setting a new list of expression, which has to contain the same
	 * expressions like the old one but can be differ in order. Please note that
	 * this method can cause serious errors, if the re-ordered list contains
	 * absurd content.
	 * 
	 * @param reorder
	 *            the new order of contained expression
	 * @throws TMQLRuntimeException
	 *             thrown if the given list does not contain the same elements
	 *             like the old one
	 */
	public void setExpressions(List<IExpression> reorder) throws TMQLRuntimeException;

	/**
	 * Method checks if there is an extension for the given expression type. If
	 * not the expression of this type will be created.
	 * 
	 * @param clazz
	 *            the expression type
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @param runtime
	 *            the runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown by constructor
	 * @throws TMQLGeneratorException
	 *             thrown by constructor
	 */
	public void checkForExtensions(Class<? extends IExpression> clazz, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException;

	/**
	 * Method called to interpret the current expression.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the matches for this expression
	 * @throws TMQLRuntimeException
	 *             thrown if querying fails
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException;
}
