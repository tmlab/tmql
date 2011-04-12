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

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Ako;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Colon;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Isa;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * path-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * path-expression ::= postfixed-expression
 * </p>
 * <p>
 * path-expression ::= predicate-invocation
 * </p>
 * <p>
 * path-expression ::= // anchor { postfix }
 * </p>
 * <p>
 * path-expression ::= simple-content-1 AKO simple-content-2 ==> tm:subclass-of
 * ( tm:subclass : simple-content-1 , tm:superclass : simple-content-2 )
 * </p>
 * <p>
 * path-expression ::= simple-content-1 ISA simple-content-2 ==>
 * tm:type-instance ( tm:instance : simple-content-1 , tm:type :
 * simple-content-2 )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathExpression extends ExpressionImpl {

	/**
	 * grammar type of a path-expression containing a postfixed-expression
	 */
	public static final int TYPE_POSTFIXED_EXPRESSION = 0;
	/**
	 * grammar type of a path-expression containing a predicate-invocation
	 */
	public static final int TYPE_PREDICATE_INVOCATION = 1;
	/**
	 * grammar type of a path-expression containing a non-canonical instances
	 * navigation
	 */
	public static final int TYPE_NONCANONICAL_INSTANCE_EXPRESSION = 2;
	/**
	 * grammar type of a path-expression containing an AKO-expression
	 */
	public static final int TYPE_AKO_EXPRESSION = 3;
	/**
	 * grammar type of a path-expression containing an ISA-expression
	 */
	public static final int TYPE_ISA_EXPRESSION = 4;

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
	@SuppressWarnings("unchecked")
	public PathExpression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		int index = ParserUtils.indexOfTokens(tmqlTokens, Ako.class);
		/*
		 * is path-expression ::= simple-content-1 ako simple-content-2
		 */
		if (index != -1) {
			checkForExtensions(AKOExpression.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_AKO_EXPRESSION);
		}
		/*
		 * ISA-expression, postfixed-expression or predicate-invocation
		 */
		else {
			index = ParserUtils.indexOfTokens(tmqlTokens, Isa.class);
			/*
			 * is path-expression ::= simple-content-1 isa simple-content-2
			 */
			if (index != -1) {
				checkForExtensions(ISAExpression.class, tmqlTokens, tokens,
						runtime);
				setGrammarType(TYPE_ISA_EXPRESSION);
			}
			/*
			 * is // anchor { postfix } ==> %_ // anchor { postfix }
			 */
			else if (tmqlTokens.get(0).equals(ShortcutAxisInstances.class)) {
				checkForExtensions(PostfixedExpression.class, tmqlTokens,
						tokens, runtime);
				setGrammarType(TYPE_NONCANONICAL_INSTANCE_EXPRESSION);
			}
			/*
			 * predicate-invocation or postfixed-expression
			 */
			else {
				/*
				 * define opening brackets as beginning of protected section
				 */
				Set<Class<? extends IToken>> protectionStarts = HashUtil.getHashSet();
				protectionStarts.add(BracketAngleOpen.class);
				protectionStarts.add(BracketSquareOpen.class);

				/*
				 * define closing brackets as end of protected section
				 */
				Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
				protectionEnds.add(BracketAngleClose.class);
				protectionEnds.add(BracketSquareClose.class);
				/*
				 * indicators for projection-postfix
				 */
				Set<Class<? extends IToken>> indicators = HashUtil.getHashSet();
				indicators.add(Colon.class);
				/*
				 * look for colon
				 */
				index = ParserUtils.indexOfTokens(tmqlTokens, indicators,
						protectionStarts, protectionEnds);
				/*
				 * is predicate-invocation
				 */
				if (index != -1) {

					checkForExtensions(PredicateInvocation.class, tmqlTokens,
							tokens, runtime);
					setGrammarType(TYPE_PREDICATE_INVOCATION);
				}
				/*
				 * is postfixed-expression
				 */
				else {
					checkForExtensions(PostfixedExpression.class, tmqlTokens,
							tokens, runtime);
					setGrammarType(TYPE_POSTFIXED_EXPRESSION);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

}
