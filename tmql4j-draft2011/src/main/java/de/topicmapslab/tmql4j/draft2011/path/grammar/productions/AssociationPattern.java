package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Slash;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the production 'association-pattern' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPattern extends ExpressionImpl {

	/**
	 * base constructor to create a new expression without sub-nodes
	 * 
	 * @param parent
	 *            the known parent node
	 * @param tmqlTokens
	 *            the list of language-specific tokens contained by this expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	public AssociationPattern(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * look for ( .. -> .... ) part of association pattern but protected brackets within filter
		 */
		List<Class<? extends IToken>> protectionStarts = new ArrayList<Class<? extends IToken>>();
		protectionStarts.add(BracketSquareOpen.class);
		List<Class<? extends IToken>> protectionEnds = new ArrayList<Class<? extends IToken>>();
		protectionEnds.add(BracketSquareClose.class);

		List<Class<? extends IToken>> tokensToFound = new ArrayList<Class<? extends IToken>>();
		tokensToFound.add(BracketRoundOpen.class);
		int index = ParserUtils.indexOfTokens(tmqlTokens, tokensToFound, protectionStarts, protectionEnds);
		if (index == -1) {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, AssociationPattern.class);
		}
		/*
		 * association type
		 */
		checkForExtensions(Anchor.class, tmqlTokens.subList(1, 2), tokens.subList(1, 2), runtime);
		/*
		 * associations have a filter part
		 */
		if (index > 2) {
			checkForExtensions(FilterPostfix.class, tmqlTokens.subList(2, index), tokens.subList(2, index), runtime);
		}

		/*
		 * the callback
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				if (foundDelimer == null || ShortcutAxisPlayers.class.equals(foundDelimer)) {
					checkForExtensions(AssociationPatternRolePart.class, tmqlTokens, tokens, runtime);
				} else {
					throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, AssociationPatternRolePart.class);
				}

			}
		};
		List<Class<? extends IToken>> delimers = new ArrayList<Class<? extends IToken>>();
		delimers.add(ShortcutAxisPlayers.class);
		/*
		 * not part before ->
		 */
		if (tmqlTokens.get(index + 1).equals(ShortcutAxisPlayers.class)) {
			List<Class<? extends IToken>> tmqlTokens_ = Collections.emptyList();
			List<String> tokens_ = Collections.emptyList();
			checkForExtensions(AssociationPatternRolePart.class, tmqlTokens_, tokens_, runtime);
		}
		ParserUtils.split(callback, tmqlTokens.subList(index + 1, tmqlTokens.size() - 1), tokens.subList(index + 1, tokens.size() - 1), delimers, true);
		/*
		 * not part after ->
		 */
		if (getExpressionFilteredByType(AssociationPatternRolePart.class).size() == 1) {
			List<Class<? extends IToken>> tmqlTokens_ = Collections.emptyList();
			List<String> tokens_ = Collections.emptyList();
			checkForExtensions(AssociationPatternRolePart.class, tmqlTokens_, tokens_, runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public boolean isValid() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void asFlatString(StringBuilder builder) {
		builder.append(Slash.TOKEN);
		builder.append(WHITESPACE);
		/*
		 * anchor to flat string
		 */
		getExpressionFilteredByType(Anchor.class).get(0).asFlatString(builder);
		/*
		 * check for filter
		 */
		if (contains(FilterPostfix.class)) {
			getExpressionFilteredByType(FilterPostfix.class).get(0).asFlatString(builder);
		}
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);
		int count = 0;
		for (AssociationPatternRolePart part : getExpressionFilteredByType(AssociationPatternRolePart.class)) {
			if (count == 1) {
				builder.append(ShortcutAxisPlayers.TOKEN);
				builder.append(WHITESPACE);
			} else if (count > 1) {
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			part.asFlatString(builder);
		}
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
	}

}
