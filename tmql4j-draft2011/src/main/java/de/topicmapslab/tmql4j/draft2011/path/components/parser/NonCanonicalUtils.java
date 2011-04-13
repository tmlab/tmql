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
package de.topicmapslab.tmql4j.draft2011.path.components.parser;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisValue;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisOccurrences;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubjectIdentifiers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisItemIdentifiers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubjectLocators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisAtomifyMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisItem;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayersMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayersMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReifierMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReifierMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Utility class for non-canonical -> canonical transformations.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NonCanonicalUtils {

	/**
	 * map containing all non-canonical patterns an the method to call to
	 * convert them to canonical productions
	 */
	private final static Map<Class<? extends IToken>, String> nonCanonicals = HashUtil.getHashMap();

	static {
		nonCanonicals.put(ShortcutAxisAtomifyMoveForward.class, "toCanonicalForwardAtomifyAxis");
		nonCanonicals.put(ShortcutAxisAtomifyMoveBackward.class, "toCanonicalBackwardAtomifyAxis");
		nonCanonicals.put(ShortcutAxisPlayersMoveForward.class, "toCanonicalForwardPlayersAxis");
		nonCanonicals.put(ShortcutAxisPlayersMoveBackward.class, "toCanonicalBackwardPlayersAxis");
		nonCanonicals.put(ShortcutAxisReifierMoveForward.class, "toCanonicalForwardReifierAxis");
		nonCanonicals.put(ShortcutAxisReifierMoveBackward.class, "toCanonicalBackwardReifierAxis");
		nonCanonicals.put(ShortcutAxisTraverse.class, "toCanonicalTraverseAxis");
		nonCanonicals.put(ShortcutAxisItem.class, "toCanonicalItemAxis");
		nonCanonicals.put(ShortcutAxisIndicators.class, "toCanonicalIndicatorsAxis");
		nonCanonicals.put(ShortcutAxisLocators.class, "toCanonicalLocatorsAxis");
	}

	/**
	 * Checks if the given tokens indicates a non-canonical expression.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @return <code>true</code> if the given tokens are known as non-canonical
	 *         production
	 */
	public static boolean isNonCanonicalProduction(List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		if (tmqlTokens.isEmpty()) {
			throw new IllegalArgumentException("List of language specific tokens cannot be empty!");
		}
		return nonCanonicals.containsKey(tmqlTokens.get(0));
	}

	/**
	 * Transformation method to transform the a non-canonical expression axis to
	 * their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             thrown if the non-canonical expression cannot convert
	 */
	public static void toCanonical(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException {
		/*
		 * check if expression is non-canonical production
		 */
		if (!isNonCanonicalProduction(tmqlTokens, tokens)) {
			throw new TMQLGeneratorException("Non-canonical expression is unknown.");
		}

		/*
		 * translate to canonical production
		 */
		try {
			Method m = NonCanonicalUtils.class.getMethod(nonCanonicals.get(tmqlTokens.get(0)), ITMQLRuntime.class, IExpression.class, List.class, List.class);
			m.invoke(null, runtime, parent, tmqlTokens, tokens);
		} catch (Exception e) {
			throw new TMQLGeneratorException("Cannot translate non-canonical production.", e);
		}

	}

	/**
	 * Transformation method to transform the non-canonical atomify axis in
	 * forward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalForwardAtomifyAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '/ topic-ref' to '>> characteristics topci-ref >> atomify'
		 */

		/*
		 * add characteristics axis
		 */
		tmqlTokens_.add(MoveForward.class);
		tmqlTokens_.add(AxisOccurrences.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new MoveForward().getLiteral());
		tokens_.add(new AxisOccurrences().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

		/*
		 * add atomify axis
		 */
		tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		tokens_ = new LinkedList<String>();
		tmqlTokens_.add(MoveForward.class);
		tmqlTokens_.add(AxisValue.class);
		tokens_.add(new MoveForward().getLiteral());
		tokens_.add(new AxisValue().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical atomify axis in
	 * backward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalBackwardAtomifyAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {

		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '\ topic-ref' to '<< atomify << characteristics topic-ref'
		 */
		/*
		 * add atomify axis
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisValue.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisValue().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

		/*
		 * add characteristics axis
		 */
		tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		tokens_ = new LinkedList<String>();
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisOccurrences.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisOccurrences().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical players axis in
	 * forward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalForwardPlayersAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '-> topic-ref' to '>> players topic-ref'
		 */
		tmqlTokens_.add(MoveForward.class);
		tmqlTokens_.add(AxisPlayers.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new MoveForward().getLiteral());
		tokens_.add(new AxisPlayers().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical players axis in
	 * backward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalBackwardPlayersAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '<- topic-ref' to '<< players topic-ref'
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisPlayers.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisPlayers().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical reifier axis in
	 * forward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalForwardReifierAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '~~>' to '>> reifier'
		 */
		tmqlTokens_.add(MoveForward.class);
		tmqlTokens_.add(AxisReifier.class);
		tokens_.add(new MoveForward().getLiteral());
		tokens_.add(new AxisReifier().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical reifier axis in
	 * backward direction to their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalBackwardReifierAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '<~~' to '<< reifier'
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisReifier.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisReifier().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical traverse axis to
	 * their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalTraverseAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '<-> topic-ref' to '>> traverse topic-ref'
		 */
		tmqlTokens_.add(MoveForward.class);
		tmqlTokens_.add(AxisTraverse.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new MoveForward().getLiteral());
		tokens_.add(new AxisTraverse().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical item axisto their
	 * canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalItemAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '!' to '<< item'
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisItemIdentifiers.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisItemIdentifiers().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical indicators axis to
	 * their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalIndicatorsAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '~' to '<< indicators'
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisSubjectIdentifiers.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisSubjectIdentifiers().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical locators axis to
	 * their canonical corresponding expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param parent
	 *            the calling expression
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @throws TMQLGeneratorException
	 *             , TMQLInvalidSyntaxException thrown by constructor
	 */
	public static void toCanonicalLocatorsAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '=' to '<< locators'
		 */
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisSubjectLocators.class);
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisSubjectLocators().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical instance axis to
	 * their canonical corresponding expression.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @return an array containing the transformed token lists. The
	 *         language-specific tokens at index <code>0</code> and the
	 *         string-represented tokens at index <code>1</code>.
	 */
	public static List<?>[] toCanonicalInstancesAxis(List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();

		/*
		 * transform '// topic-ref' to 'topic-ref << types'
		 */
		tmqlTokens_.add(tmqlTokens.get(1));
		tmqlTokens_.add(MoveBackward.class);
		tmqlTokens_.add(AxisTypes.class);
		tokens_.add(tokens.get(1));
		tokens_.add(new MoveBackward().getLiteral());
		tokens_.add(new AxisTypes().getLiteral());

		/*
		 * add the rest of the given expression
		 */
		if (tmqlTokens.size() > 2) {
			tmqlTokens_.addAll(tmqlTokens.subList(2, tmqlTokens.size()));
			tokens_.addAll(tokens.subList(2, tokens.size()));
		}
		/*
		 * return transformed expression
		 */
		return new List<?>[] { tmqlTokens_, tokens_ };
	}
}
