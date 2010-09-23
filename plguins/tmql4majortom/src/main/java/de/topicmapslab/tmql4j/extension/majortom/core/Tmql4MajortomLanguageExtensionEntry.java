package de.topicmapslab.tmql4j.extension.majortom.core;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * <p>
 * query-expression ::= [ environment-clause ] update-expression
 * </p>
 * <p>
 * query-expression ::= [ environment-clause ] insert-expression
 * </p>
 * <p>
 * query-expression ::= [ environment-clause ] merge-expression
 * </p>
 */
public class Tmql4MajortomLanguageExtensionEntry implements
		ILanguageExtensionEntry {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IExpression> getExpressionType() {
		return de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidProduction(ITMQLRuntime runtime,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			IExpression caller) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExpression parse(ITMQLRuntime runtime,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			IExpression caller, boolean autoAdd)
			throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		throw new TMQLGeneratorException("Method should never called!");
	}
}
