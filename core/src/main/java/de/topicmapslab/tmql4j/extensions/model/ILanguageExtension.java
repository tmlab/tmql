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

import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Interface definition of a language extension of the tmql4j engine. A language
 * extension define a sub-set of productions and atoms which should be added to
 * the core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILanguageExtension extends IExtensionPoint {

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
	public boolean extendsExpressionType(
			final Class<? extends IExpression> expressionType);

	/**
	 * Return the language extension entry defining the entry point for using
	 * the extension during the querying process.
	 * 
	 * @return the extension entry
	 */
	public ILanguageExtensionEntry getLanguageExtensionEntry();

}
