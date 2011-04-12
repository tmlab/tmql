/*******************************************************************************
 * Copyright 2010, Topic Map Lab ( http://www.topicmapslab.de )
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.topicmapslab.tmql4j.components.parser;

import java.util.List;

import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interface definition of a callback handler of the parser utility methods.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IParserUtilsCallback {

	/**
	 * Method will be called by the
	 * {@link ParserUtils#split(IParserUtilsCallback, List, List, Class, boolean)}
	 * if a new delimer was found.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens of the sub-expression
	 * @param tokens
	 *            the string-represented tokens of the sub-expression
	 * @param foundDelimer
	 *            the founded delimer or <code>null</code> if it is the last
	 *            sub-section
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown if operation fails
	 */
	public void newToken(final List<Class<? extends IToken>> tmqlTokens,
			final List<String> tokens, Class<? extends IToken> foundDelimer)
			throws TMQLGeneratorException, TMQLInvalidSyntaxException;

}