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
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByItemIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisBySubjectIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisBySubjectLocator;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayedRoles;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReified;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisByItemIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisBySubjectIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisBySubjectLocator;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayedRoles;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReified;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Slash;
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
	 * map containing all non-canonical patterns an the method to call to convert them to canonical productions
	 */
	private final static Map<Class<? extends IToken>, String> nonCanonicals = HashUtil.getHashMap();

	static {
		nonCanonicals.put(ShortcutAxisPlayers.class, "toCanonicalPlayersAxis");
		nonCanonicals.put(ShortcutAxisPlayedRoles.class, "toCanonicalRolesPlayedAxis");
		nonCanonicals.put(ShortcutAxisReified.class, "toCanonicalReifierAxis");
		nonCanonicals.put(ShortcutAxisReifier.class, "toCanonicalReifiedAxis");
		nonCanonicals.put(ShortcutAxisTraverse.class, "toCanonicalTraverseAxis");
		nonCanonicals.put(ShortcutAxisByItemIdentifier.class, "toCanonicalByItemIdentifierAxis");
		nonCanonicals.put(ShortcutAxisBySubjectIdentifier.class, "toCanonicalBySubjectIdentifierAxis");
		nonCanonicals.put(ShortcutAxisBySubjectLocator.class, "toCanonicalBySubjectLocatorAxis");
	}

	/**
	 * Checks if the given tokens indicates a non-canonical expression.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @return <code>true</code> if the given tokens are known as non-canonical production
	 */
	public static boolean isNonCanonicalProduction(List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		if (tmqlTokens.isEmpty()) {
			throw new IllegalArgumentException("List of language specific tokens cannot be empty!");
		}
		return nonCanonicals.containsKey(tmqlTokens.get(0));
	}

	/**
	 * Transformation method to transform the a non-canonical expression axis to their canonical corresponding
	 * expression.
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
	 * Transformation method to transform the non-canonical players axis to their canonical corresponding expression.
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
	public static void toCanonicalPlayersAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '-> topic-ref' to '\ players topic-ref'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisPlayers.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisPlayers().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical played roles axis to their canonical corresponding
	 * expression.
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
	public static void toCanonicalRolesPlayedAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '<- topic-ref' to '\ played-roles topic-ref'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisPlayedRoles.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisPlayedRoles().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical reifier axis to their canonical corresponding expression.
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
	public static void toCanonicalReifierAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '~~>' to '\ reifier'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisReifier.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisReifier().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical reified axis to their canonical corresponding expression.
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
	public static void toCanonicalReifiedAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '<~~' to '\ reified'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisReified.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisReified().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical traverse axis to their canonical corresponding expression.
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
		 * transform '<-> topic-ref' to '\ traverse topic-ref'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisTraverse.class);
		tmqlTokens_.add(Element.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisTraverse().getLiteral());
		tokens_.add(tokens.get(1));
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);
	}

	/**
	 * Transformation method to transform the non-canonical by-item-identifier axis to their canonical corresponding
	 * expression.
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
	public static void toCanonicalByItemIdentifierAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '!' to '\ by-item-identifier'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisByItemIdentifier.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisByItemIdentifier().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical by-subject-identifier axis to their canonical corresponding
	 * expression.
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
	public static void toCanonicalBySubjectIdentifierAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '~' to '\ by-subject-identifier'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisBySubjectIdentifier.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisBySubjectIdentifier().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical by-subject-locator axis to their canonical corresponding
	 * expression.
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
	public static void toCanonicalBySubjectLocatorAxis(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) throws TMQLGeneratorException,
			TMQLInvalidSyntaxException {
		/*
		 * create temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();
		/*
		 * transform '=' to '\ by-subject-locator'
		 */
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisBySubjectLocator.class);
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisBySubjectLocator().getLiteral());
		parent.checkForExtensions(StepDefinition.class, tmqlTokens_, tokens_, runtime);

	}

	/**
	 * Transformation method to transform the non-canonical instance axis to their canonical corresponding expression.
	 * 
	 * @param tmqlTokens
	 *            the language-specific tokens
	 * @param tokens
	 *            the string-represented tokens
	 * @return an array containing the transformed token lists. The language-specific tokens at index <code>0</code> and
	 *         the string-represented tokens at index <code>1</code>.
	 */
	public static List<?>[] toCanonicalInstancesAxis(List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();

		/*
		 * transform '// topic-ref' to 'topic-ref \ instances'
		 */
		tmqlTokens_.add(tmqlTokens.get(1));
		tmqlTokens_.add(Slash.class);
		tmqlTokens_.add(AxisInstances.class);
		tokens_.add(tokens.get(1));
		tokens_.add(new Slash().getLiteral());
		tokens_.add(new AxisInstances().getLiteral());

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
