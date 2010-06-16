package de.topicmapslab.tmql4j.extension.tmml.core;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Delete;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Merge;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Update;
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
public class ModificationPartLanguageExtensionEntry implements
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
		return !tmqlTokens.isEmpty()
				&& (tmqlTokens.contains(Delete.class)
						|| tmqlTokens.contains(Merge.class)
						|| tmqlTokens.contains(Insert.class) || tmqlTokens
						.contains(Update.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExpression parse(ITMQLRuntime runtime,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			IExpression caller, boolean autoAdd)
			throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		IExpression expression = new QueryExpression(caller, tmqlTokens,
				tokens, (TMQLRuntime) runtime);
		if (autoAdd) {
			caller.addExpression(expression);
		}
		return expression;
	}
}
