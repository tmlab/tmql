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
package de.topicmapslab.tmql4j.extensions.model;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Interface definition of a language extension entry used to migrate the new
 * expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILanguageExtensionEntry {

	/**
	 * Returns the expression type used as anchor in the parsing tree.
	 * 
	 * @return the expression type as anchor in the parsing tree
	 */
	public Class<? extends IExpression> getExpressionType();

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
	public boolean isValidProduction(final ITMQLRuntime runtime,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, IExpression caller);

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
	public IExpression parse(final ITMQLRuntime runtime,
			final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, IExpression caller, boolean autoAdd)
			throws TMQLInvalidSyntaxException, TMQLGeneratorException;
}
