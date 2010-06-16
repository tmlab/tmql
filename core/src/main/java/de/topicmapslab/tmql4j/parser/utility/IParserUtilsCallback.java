package de.topicmapslab.tmql4j.parser.utility;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.lexer.model.IToken;

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