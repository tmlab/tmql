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
package de.topicmapslab.tmql4j.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Interface definition of a language extension of the tmql4j engine. A language
 * extension define a sub-set of productions and atoms which should be added to
 * the core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILanguageExtension {

	/**
	 * Method called by the runtime to register the extension end point before
	 * running the query process.
	 * 
	 * @param runtime
	 *            the calling runtime
	 * @throws TMQLExtensionRegistryException
	 *             thrown if an exception caused by the internal runtime
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException;

	/**
	 * Returns the expression class extended by this language extension
	 * 
	 * @return the class extended by this extension
	 */
	public Class<? extends IExpression> getExpressionType();

	/**
	 * Checks if the language extension extends the given expression type. If
	 * the language extension add new productions starting with the given
	 * expression type it has to return <code>true</code>, <code>false</code>
	 * otherwise.
	 * 
	 * @param expressionType
	 *            the extended expression type
	 * @return <code>true</code> if the extension based on the expression type.
	 */
	public boolean extendsExpressionType(final Class<? extends IExpression> expressionType);

	/**
	 * Checks if the given language-specific tokens matching the new production
	 * of the language extension. The method has to check if the extension an be
	 * used for the given sub-query.
	 * 
	 * @param runtime
	 *            the current runtime container
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @param caller
	 *            the calling expression
	 * @return <code>true</code> if the productions can be used,
	 *         <code>false</code> otherwise.
	 */
	public boolean isValidProduction(final ITMQLRuntime runtime, final List<Class<? extends IToken>> tmqlTokens, final List<String> tokens, IExpression caller);

	/**
	 * Called by the parser to add new sub-tree nodes using the extension
	 * anchor.
	 * 
	 * @param runtime
	 *            the runtime container
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @param caller
	 *            the calling expression
	 * @param autoAdd
	 *            flag representing if the sub-tree should add automatically
	 * @return the created expression
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the expression is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the expression can not be created
	 */
	public IExpression parse(final ITMQLRuntime runtime, final List<Class<? extends IToken>> tmqlTokens, final List<String> tokens, IExpression caller, boolean autoAdd)
			throws TMQLInvalidSyntaxException, TMQLGeneratorException;

}
