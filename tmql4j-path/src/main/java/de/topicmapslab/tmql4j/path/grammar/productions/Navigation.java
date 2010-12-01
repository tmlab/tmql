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
package de.topicmapslab.tmql4j.path.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.NonCanonicalUtils;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisAtomifyMoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisPlayersMoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisPlayersMoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisReifierMoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisReifierMoveForward;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a naviagtion.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * navigation ::= step [ navigation ]
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Navigation extends ExpressionImpl {

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param parent
	 *            the known parent node
	 * @param tmqlTokens
	 *            the list of language-specific tokens contained by this
	 *            expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this
	 *            expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	public Navigation(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				Class<? extends IToken> token = tmqlTokens.get(0);
				/*
				 * is direction token >> or <<
				 */
				if (token.equals(MoveBackward.class) || token.equals(MoveForward.class)) {
					checkForExtensions(Step.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * special handling for non-canonical axis
				 */
				else if (NonCanonicalUtils.isNonCanonicalProduction(tmqlTokens, tokens)) {
					NonCanonicalUtils.toCanonical(runtime, Navigation.this, tmqlTokens, tokens);
				}
			}
		};

		/*
		 * create set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(MoveBackward.class);
		delimers.add(MoveForward.class);
		delimers.add(ShortcutAxisAtomifyMoveBackward.class);
		delimers.add(ShortcutAxisAtomifyMoveForward.class);
		delimers.add(ShortcutAxisItem.class);
		delimers.add(ShortcutAxisIndicators.class);
		delimers.add(ShortcutAxisLocators.class);
		delimers.add(ShortcutAxisReifierMoveForward.class);
		delimers.add(ShortcutAxisReifierMoveBackward.class);
		delimers.add(ShortcutAxisPlayersMoveForward.class);
		delimers.add(ShortcutAxisPlayersMoveBackward.class);

		/*
		 * split expression
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, false);

		setGrammarType(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

}
