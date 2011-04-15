package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the a part of production 'association-pattern' of the new draft 2011
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPatternRolePart extends ExpressionImpl {

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
	public AssociationPatternRolePart(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		/*
		 * check for filter
		 */
		List<Class<? extends IToken>> protectionStarts = new ArrayList<Class<? extends IToken>>();
		protectionStarts.add(BracketRoundOpen.class);
		protectionStarts.add(BracketSquareOpen.class);

		List<Class<? extends IToken>> protectionEnds = new ArrayList<Class<? extends IToken>>();
		protectionEnds.add(BracketRoundClose.class);
		protectionEnds.add(BracketSquareClose.class);

		List<Class<? extends IToken>> tokensToFound = new ArrayList<Class<? extends IToken>>();
		protectionEnds.add(BracketSquareOpen.class);
		int index = ParserUtils.indexOfTokens(tmqlTokens, tokensToFound, protectionStarts, protectionEnds);
		if (index > 1) {
			checkForExtensions(Anchor.class, tmqlTokens.subList(0, 1), tokens.subList(0, 1), runtime);
			checkForExtensions(FilterPostfix.class, tmqlTokens.subList(1, tmqlTokens.size()), tokens.subList(1, tokens.size()), runtime);
		} else if (index == 1) {
			checkForExtensions(FilterPostfix.class, tmqlTokens, tokens, runtime);
		}

	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public boolean isValid() {
		return true;
	}

}
