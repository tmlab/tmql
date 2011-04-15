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
package de.topicmapslab.tmql4j.draft2011.path.grammar.productions;

import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a associaton pattern definition.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  association-pattern-definition ::= association-pattern filter*
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPatternDefinition extends ExpressionImpl {

	/**
	 * base constructor to create a new instance.
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
	public AssociationPatternDefinition(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * look for ( .. -> .... ) part of association pattern but protected brackets within filter
		 */
		List<Class<? extends IToken>> protectionStarts = new ArrayList<Class<? extends IToken>>();
		protectionStarts.add(BracketSquareOpen.class);
		List<Class<? extends IToken>> protectionEnds = new ArrayList<Class<? extends IToken>>();
		protectionEnds.add(BracketSquareClose.class);

		List<Class<? extends IToken>> tokensToFound = new ArrayList<Class<? extends IToken>>();
		tokensToFound.add(BracketRoundClose.class);
		int index = ParserUtils.indexOfTokens(tmqlTokens, tokensToFound, protectionStarts, protectionEnds);
		if (index == -1) {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, AssociationPattern.class);
		}
		if (index != tmqlTokens.size() - 1) {
			checkForExtensions(AssociationPattern.class, tmqlTokens.subList(0, index + 1), tokens.subList(0, index + 1), runtime);
			checkForExtensions(FilterPostfix.class, tmqlTokens.subList(index + 2, tmqlTokens.size()), tokens.subList(index + 2, tmqlTokens.size()), runtime);
		} else {
			checkForExtensions(AssociationPattern.class, tmqlTokens, tokens, runtime);
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
