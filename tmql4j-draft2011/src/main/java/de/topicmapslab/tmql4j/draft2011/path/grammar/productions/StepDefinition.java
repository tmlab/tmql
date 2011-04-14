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

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Slash;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a step.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  step-definition ::= step filter*
 * </p>
 * <p>
 * step ::= // anchor
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StepDefinition extends ExpressionImpl {

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
	public StepDefinition(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * the parser call back
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {

			private long numberOfExpressions = 0;

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				/*
				 * is first step
				 */
				if (numberOfExpressions == 0) {
					/*
					 * is association pattern
					 */
					if (tmqlTokens.contains(BracketRoundOpen.class)) {
						checkForExtensions(AssociationPattern.class, tmqlTokens, tokens, runtime);
					} else {
						checkForExtensions(Step.class, tmqlTokens, tokens, runtime);
					}
				}
				/*
				 * parser break because of [ @ ... ]
				 */
				else if (tmqlTokens.size() == 1 && tmqlTokens.get(0).equals(BracketSquareOpen.class)) {
					// IGNORE
				}
				/*
				 * parser break because of @ ... ]
				 */
				else if (tmqlTokens.get(0).equals(Scope.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(BracketSquareClose.class)) {
					checkForExtensions(FilterPostfix.class, tmqlTokens.subList(0, tmqlTokens.size()), tokens.subList(0, tokens.size()), runtime);
				}
				/*
				 * is any filter
				 */
				else {
					checkForExtensions(FilterPostfix.class, tmqlTokens.subList(0, tmqlTokens.size()), tokens.subList(0, tokens.size()), runtime);
				}
				numberOfExpressions++;
			}
		};

		Set<Class<? extends IToken>> protectionStarts = HashUtil.getHashSet();
		protectionStarts.add(BracketAngleOpen.class);
		protectionStarts.add(BracketRoundOpen.class);
		Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
		protectionEnds.add(BracketRoundClose.class);
		protectionEnds.add(BracketAngleClose.class);

		/*
		 * call parser
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(BracketSquareOpen.class); // is boolean-filter [
		delimers.add(Scope.class); // is scope axis @
		delimers.add(ShortcutAxisInstances.class); // is instances axis //
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, false, protectionStarts, protectionEnds);
		setGrammarType(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (!getTmqlTokens().get(0).equals(Slash.class)) {
			return false;
		}
		return true;
	}

}
