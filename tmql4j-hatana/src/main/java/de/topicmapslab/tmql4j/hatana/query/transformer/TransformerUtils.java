/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana.query.transformer;

import java.util.Arrays;
import java.util.List;

import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Axis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Value;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TransformerUtils {

	/**
	 * Utility method to check if the expression matches the given pattern
	 * 
	 * @param expression
	 *            the expression
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the expression matches the pattern,
	 *         <code>false</code> otherwise
	 */
	public static boolean matchesPattern(IExpression expression, Class<? extends IToken>... tokens) {
		List<Class<? extends IToken>> list = Arrays.asList(tokens);
		return expression.getTmqlTokens().equals(list);
	}

	/**
	 * Utility method to check if the expression matches the given pattern at
	 * the end of this expression
	 * 
	 * @param expression
	 *            the expression
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the expression matches the pattern,
	 *         <code>false</code> otherwise
	 */
	public static boolean matchesPatternAtEnd(IExpression expression, Class<? extends IToken>... tokens) {
		return matchesPatternAtEnd(expression, Arrays.asList(tokens));
	}

	/**
	 * Utility method to check if the expression matches the given pattern at
	 * the end of this expression
	 * 
	 * @param expression
	 *            the expression
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the expression matches the pattern,
	 *         <code>false</code> otherwise
	 */
	public static boolean matchesPatternAtEnd(IExpression expression, List<Class<? extends IToken>> tokens) {
		/*
		 * list is bigger than expression
		 */
		if (tokens.size() > expression.getTmqlTokens().size()) {
			return false;
		}
		/*
		 * same size -> sublist not necessary
		 */
		else if (tokens.size() == expression.getTmqlTokens().size()) {
			return expression.getTmqlTokens().equals(tokens);
		}
		/*
		 * check sublist at the end
		 */
		return expression.getTmqlTokens().subList(expression.getTmqlTokens().size() - (tokens.size() + 1), expression.getTmqlTokens().size()).equals(tokens);
	}

	private static final List<Class<? extends IToken>> subjectIdentifierPattern = HashUtil.getList();
	static {
		subjectIdentifierPattern.add(Axis.class);
		subjectIdentifierPattern.add(SubjectIdentifier.class);
		subjectIdentifierPattern.add(DoubleColon.class);
	}
	private static final List<Class<? extends IToken>> subjectIdentifierWithValuePattern = HashUtil.getList();
	static {
		subjectIdentifierWithValuePattern.add(Axis.class);
		subjectIdentifierWithValuePattern.add(SubjectIdentifier.class);
		subjectIdentifierWithValuePattern.add(DoubleColon.class);
		subjectIdentifierWithValuePattern.add(Axis.class);
		subjectIdentifierWithValuePattern.add(Value.class);
		subjectIdentifierWithValuePattern.add(DoubleColon.class);
	}
	private static final List<Class<? extends IToken>> itemIdentifierPattern = HashUtil.getList();
	static {
		itemIdentifierPattern.add(Axis.class);
		itemIdentifierPattern.add(ItemIdentifier.class);
		itemIdentifierPattern.add(DoubleColon.class);
	}
	private static final List<Class<? extends IToken>> itemIdentifierWithValuePattern = HashUtil.getList();
	static {
		itemIdentifierWithValuePattern.add(Axis.class);
		itemIdentifierWithValuePattern.add(ItemIdentifier.class);
		itemIdentifierWithValuePattern.add(DoubleColon.class);
		itemIdentifierWithValuePattern.add(Axis.class);
		itemIdentifierWithValuePattern.add(Value.class);
		itemIdentifierWithValuePattern.add(DoubleColon.class);
	}
	private static final List<Class<? extends IToken>> subjectLocatorPattern = HashUtil.getList();
	static {
		subjectLocatorPattern.add(Axis.class);
		subjectLocatorPattern.add(SubjectLocator.class);
		subjectLocatorPattern.add(DoubleColon.class);
	}
	private static final List<Class<? extends IToken>> subjectLocatorWithValuePattern = HashUtil.getList();
	static {
		subjectLocatorWithValuePattern.add(Axis.class);
		subjectLocatorWithValuePattern.add(SubjectLocator.class);
		subjectLocatorWithValuePattern.add(DoubleColon.class);
		subjectLocatorWithValuePattern.add(Axis.class);
		subjectLocatorWithValuePattern.add(Value.class);
		subjectLocatorWithValuePattern.add(DoubleColon.class);
	}

	public enum Comparison {
		LEFT_SUBJECT_IDENTIFIER,

		RIGHT_SUBJECT_IDENTIFIER,

		LEFT_SUBJECT_LOCATOR,

		RIGHT_SUBJECT_LOCATOR,

		LEFT_ITEM_IDENTIFIER,

		RIGHT_ITEM_IDENTIFIER,

		ANY
	}

	@SuppressWarnings("unchecked")
	public static Comparison getTypeOfComparison(IExpression expression) {
		IExpression left = expression.getExpressions().get(0);
		IExpression right = expression.getExpressions().get(1);
		/*
		 * left is subject-identifier axis navigation
		 */
		if (ParserUtils.containsTokens(left.getTmqlTokens(), SubjectIdentifier.class)) {
			if (matchesPattern(right, Element.class)) {
				return Comparison.LEFT_SUBJECT_IDENTIFIER;
			}
		}
		/*
		 * right is subject-identifier axis navigation
		 */
		else if (ParserUtils.containsTokens(right.getTmqlTokens(), SubjectIdentifier.class)) {
			if (matchesPattern(left, Element.class)) {
				return Comparison.RIGHT_SUBJECT_IDENTIFIER;
			}
		}
		/*
		 * left is subject-locator axis navigation
		 */
		else if (ParserUtils.containsTokens(left.getTmqlTokens(), SubjectLocator.class)) {
			if (matchesPattern(right, Element.class)) {
				return Comparison.LEFT_SUBJECT_LOCATOR;
			}
		}
		/*
		 * right is subject-locator axis navigation
		 */
		else if (ParserUtils.containsTokens(right.getTmqlTokens(), SubjectLocator.class)) {
			if (matchesPattern(left, Element.class)) {
				return Comparison.RIGHT_SUBJECT_LOCATOR;
			}
		}
		/*
		 * left is item-identifier axis navigation
		 */
		else if (ParserUtils.containsTokens(left.getTmqlTokens(), ItemIdentifier.class)) {
			if (matchesPattern(right, Element.class)) {
				return Comparison.LEFT_ITEM_IDENTIFIER;
			}
		}
		/*
		 * right is item-identifier axis navigation
		 */
		else if (ParserUtils.containsTokens(right.getTmqlTokens(), ItemIdentifier.class)) {
			if (matchesPattern(left, Element.class)) {
				return Comparison.RIGHT_ITEM_IDENTIFIER;
			}
		}
		return Comparison.ANY;

	}

}
