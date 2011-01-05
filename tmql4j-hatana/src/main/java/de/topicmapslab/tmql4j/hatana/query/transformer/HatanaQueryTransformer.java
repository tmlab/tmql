/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana.query.transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.draft2010.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.ArrayFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.AssociationPatternFct;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicsByItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicsBySubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicsBySubjectLocator;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Arrow;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Axis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisDefault;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Type;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ScopeFilter;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SimpleExpression;
import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.hatana.query.transformer.TransformerUtils.Comparison;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Hatana TMQL Query Transformer
 * 
 * @author Sven Krosse
 * 
 */
public class HatanaQueryTransformer {

	/**
	 * 
	 */
	private static final String QUOTE = "\"";
	private static final String WHITESPACE = " ";

	/**
	 * Transform the given tree to a query based on array syntax to handle
	 * multiple identities
	 * 
	 * @param tree
	 *            the tree
	 * @param registry
	 *            the PSI registry to extract multiple identities
	 * @return the transformed query
	 * @throws TMQLConverterException
	 */
	public static String transform(final IParserTree tree,
			final IPSIRegistry registry) throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		transform(tree.root(), registry, builder);
		return builder.toString();
	}

	/**
	 * Transform the given expression to a query based on array syntax to handle
	 * multiple identities
	 * 
	 * @param expression
	 *            the expression
	 * @param registry
	 *            the PSI registry to extract multiple identities
	 * @param builder
	 *            the builder to write in
	 * @throws TMQLConverterException
	 */
	private static void transform(final IExpression expression,
			final IPSIRegistry registry, final StringBuilder builder)
			throws TMQLConverterException {
		try {
			/*
			 * is path step
			 */
			if (expression instanceof PathSpecification) {
				/*
				 * is navigation
				 */
				if (expression.getGrammarType() == PathSpecification.TYPE_AXIS_SPEC) {
					transformPathSpecification(expression, registry, builder);
				}
				/*
				 * is association pattern
				 */
				else if (expression.getGrammarType() == PathSpecification.TYPE_ASSOCIATION_PATTERN) {
					transformAssociationPattern(expression.getExpressions()
							.get(0), registry, builder);
				}
				/*
				 * is association pattern function
				 */
				else {
					transformFunctionCall(expression.getExpressions().get(0),
							registry, builder);
				}
			}
			/*
			 * is scope filter
			 */
			else if (expression instanceof ScopeFilter) {
				transformScopeFilter(expression, registry, builder);
			}
			/*
			 * is comparison
			 */
			else if (expression instanceof ComparisonExpression) {
				transformComparisonExpression(
						(ComparisonExpression) expression, registry, builder);
			}
			/*
			 * is function call
			 */
			else if (expression instanceof FunctionCall) {
				transformFunctionCall(expression, registry, builder);
			}
			/*
			 * is simple expression
			 */
			else if (expression instanceof SimpleExpression) {
				/*
				 * is function
				 */
				if (expression.getGrammarType() == SimpleExpression.TYPE_FUNCTION) {
					transformFunctionCall(expression.getExpressions().get(0),
							registry, builder);
				}
				/*
				 * is topic reference
				 */
				else if (expression.getGrammarType() == SimpleExpression.TYPE_TOPICREF) {
					buildArrayBySubjectIdentifier(builder, registry, expression
							.getTokens().get(0));
				}
				/*
				 * is anything else
				 */
				else {
					tokensToQuery(builder, expression);
				}
			}
			/*
			 * has children
			 */
			else if (!expression.getExpressions().isEmpty()) {
				Iterator<IExpression> iterator = expression.getExpressions()
						.iterator();
				IExpression ex = iterator.next();
				int index = 0;
				/*
				 * move over expression
				 */
				for (; index < expression.getTmqlTokens().size(); index++) {
					Class<? extends IToken> token = expression.getTmqlTokens()
							.get(index);
					/*
					 * is first token of child
					 */
					if (token.equals(ex.getTmqlTokens().get(0))) {
						transform(ex, registry, builder);
						index += ex.getTmqlTokens().size();
						/*
						 * was last children?
						 */
						if (iterator.hasNext()) {
							ex = iterator.next();
							index -= 1;
						} else {
							tokensToQuery(builder, expression.getTokens()
									.subList(index,
											expression.getTokens().size()));
							break;
						}
					} else {
						builder.append(expression.getTokens().get(index));
						builder.append(WHITESPACE);
					}
				}
			}
			/*
			 * no children
			 */
			else {
				tokensToQuery(builder, expression);
			}
		} catch (Exception e) {
			throw new TMQLConverterException(e);
		}
	}

	/**
	 * Transform an association patternto an array-based TMQL part.
	 * <p>
	 * <code> type ( type -> type ) ==> association-pattern ( array(...) , array(...), array (...))</code>
	 * <br />
	 * </p>
	 * 
	 * @param expression
	 *            the comparison expression
	 * @param registry
	 *            the PSI registry
	 * @param builder
	 *            the builder
	 * @return <code>true</code> if the next expression should be ignored,
	 *         <code>false</code> otherwise
	 */
	private static void transformAssociationPattern(
			final IExpression expression, final IPSIRegistry registry,
			final StringBuilder builder) {
		builder.append(AssociationPatternFct.IDENTIFIER);
		builder.append(WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);

		/*
		 * get association type by identifier if it isn't *
		 */
		if (expression.getTmqlTokens().get(0).equals(Element.class)) {
			buildArrayBySubjectIdentifier(builder, registry, LiteralUtils
					.asString(expression.getTokens().get(0)));
		} else {
			buildEmptyArray(builder);
		}
		builder.append(Comma.TOKEN);
		builder.append(WHITESPACE);

		/*
		 * read left-hand role by identifier if exists and isn't *
		 */
		int index = expression.getTmqlTokens().indexOf(BracketRoundOpen.class) + 1;
		if (expression.getTmqlTokens().get(index).equals(Element.class)) {
			buildArrayBySubjectIdentifier(builder, registry, LiteralUtils
					.asString(expression.getTokens().get(index)));
		} else {
			buildEmptyArray(builder);
		}
		builder.append(Comma.TOKEN);
		builder.append(WHITESPACE);

		/*
		 * read right-hand role by identifier if exists and isn't *
		 */
		index = expression.getTmqlTokens().indexOf(Arrow.class) + 1;
		if (expression.getTmqlTokens().get(index).equals(Element.class)) {
			buildArrayBySubjectIdentifier(builder, registry, LiteralUtils
					.asString(expression.getTokens().get(index)));
		} else {
			buildEmptyArray(builder);
		}

		builder.append(BracketRoundClose.TOKEN); // round bracket
		builder.append(WHITESPACE); // white space
	}

	/**
	 * Transform a function expression to an array-based TMQL part.
	 * <p>
	 * <code> topics-by-subjectidentifier(...) ==> array(...) </code> <br />
	 * <code> topics-by-subjectlocator(...) ==> array(...) </code> <br />
	 * <code> topics-by-itemidentifier(...) ==> array(...) </code> <br />
	 * </p>
	 * 
	 * @param expression
	 *            the comparison expression
	 * @param registry
	 *            the PSI registry
	 * @param builder
	 *            the builder
	 * @return <code>true</code> if the next expression should be ignored,
	 *         <code>false</code> otherwise
	 */
	private static void transformFunctionCall(final IExpression expression,
			final IPSIRegistry registry, final StringBuilder builder) {
		final String functionName = expression.getTokens().get(0);
		/*
		 * temporary sets
		 */
		Set<String> subjectIdentifiers = HashUtil.getHashSet();
		Set<String> subjectLocators = HashUtil.getHashSet();
		Set<String> itemIdentifiers = HashUtil.getHashSet();
		Set<String> otherExpressions = HashUtil.getHashSet();

		boolean isSubjectIdentifierFct = TopicsBySubjectIdentifier.IDENTIFIER
				.equalsIgnoreCase(functionName);
		boolean isSubjectLocatorFct = TopicsBySubjectLocator.IDENTIFIER
				.equalsIgnoreCase(functionName);
		boolean isItemIdentifierFct = TopicsByItemIdentifier.IDENTIFIER
				.equalsIgnoreCase(functionName);

		/*
		 * is any other function
		 */
		if (!isSubjectIdentifierFct && !isSubjectLocatorFct
				&& !isItemIdentifierFct) {
			builder.append(functionName);
			builder.append(WHITESPACE);
			builder.append(BracketRoundOpen.TOKEN);
			boolean first = true;
			for (IExpression ex : expression.getExpressions()) {
				if (!first) {
					builder.append(Comma.TOKEN);
					builder.append(WHITESPACE);
				}
				transform(ex, registry, builder);
				first = false;
			}
			builder.append(WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
			builder.append(WHITESPACE);
			return;
		}

		/*
		 * looking for parameters
		 */
		for (IExpression ex : expression.getExpressions()) {
			/*
			 * is string literal -> extract equal identifiers
			 */
			if (ex.getTmqlTokens().get(0).equals(Element.class)) {
				String identifier = LiteralUtils
						.asString(ex.getTokens().get(0));
				List<Set<String>> identifiers = null;
				/*
				 * extract by subject-identifier
				 */
				if (isSubjectIdentifierFct) {
					subjectIdentifiers.add(identifier);
					identifiers = registry
							.getIdentifiersBySubjectIdentifier(identifier);
				}
				/*
				 * extract by subject-locator
				 */
				else if (isSubjectLocatorFct) {
					subjectLocators.add(identifier);
					identifiers = registry
							.getIdentifiersBySubjectLocator(identifier);
				}
				/*
				 * extract by item-identifier
				 */
				else {
					itemIdentifiers.add(identifier);
					identifiers = registry
							.getIdentifiersByItemIdentifier(identifier);
				}
				subjectIdentifiers.addAll(identifiers.get(0));
				subjectLocators.addAll(identifiers.get(1));
				itemIdentifiers.addAll(identifiers.get(2));
			}
			/*
			 * transform other parameters
			 */
			else {
				StringBuilder expressionBuilder = new StringBuilder();
				transform(ex, registry, expressionBuilder);
				otherExpressions.add(expressionBuilder.toString());
			}
		}

		boolean first = true;
		StringBuilder tempBuilder = new StringBuilder();
		for (String token : otherExpressions) {
			if (!first) {
				tempBuilder.append(Comma.TOKEN);
				tempBuilder.append(WHITESPACE);
			} else {
				tempBuilder.append(functionName);
				tempBuilder.append(WHITESPACE); // white space
				tempBuilder.append(BracketRoundOpen.TOKEN); // round bracket
				tempBuilder.append(WHITESPACE);
			}
			tempBuilder.append(token);
			tempBuilder.append(WHITESPACE);
			first = false;
		}

		buildArray(builder, subjectIdentifiers, subjectLocators,
				itemIdentifiers, first ? null : tempBuilder.toString());
	}

	/**
	 * Transform a comparison expression to an array-based TMQL part.
	 * <p>
	 * <code> . / subject-identifier == " " ==> . / subject-identifier = array(...) </code>
	 * <br />
	 * <code> . / subject-locator == " " ==> . / subject-locator = array(...) </code>
	 * <br />
	 * <code> . / item-identifier == " " ==> . / item-identifier = array(...) </code>
	 * <br />
	 * </p>
	 * 
	 * @param expression
	 *            the comparison expression
	 * @param registry
	 *            the PSI registry
	 * @param builder
	 *            the builder
	 */
	@SuppressWarnings("unchecked")
	private static void transformComparisonExpression(
			final ComparisonExpression expression, final IPSIRegistry registry,
			final StringBuilder builder) throws Exception {
		Comparison type = TransformerUtils.getTypeOfComparison(expression);
		/*
		 * is any but no identity expression
		 */
		if (type == Comparison.ANY) {
			transform(expression.getExpressions().get(0), registry, builder);
			builder.append(expression.getOperator().newInstance().getLiteral());
			builder.append(WHITESPACE);
			transform(expression.getExpressions().get(1), registry, builder);
			return;
		}
		IExpression axisExpression = null;
		int index = -1;
		StringBuilder arrayBuilder = new StringBuilder();
		switch (type) {
		case LEFT_SUBJECT_IDENTIFIER: {
			axisExpression = expression.getExpressions().get(0);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					SubjectIdentifier.class);
			buildArrayBySubjectIdentifier(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(1).getTokens()
							.get(0)));
		}
			break;
		case RIGHT_SUBJECT_IDENTIFIER: {
			axisExpression = expression.getExpressions().get(1);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					SubjectIdentifier.class);
			buildArrayBySubjectIdentifier(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(0).getTokens()
							.get(0)));
		}
			break;
		case LEFT_SUBJECT_LOCATOR: {
			axisExpression = expression.getExpressions().get(0);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					SubjectLocator.class);
			buildArrayBySubjectLocator(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(1).getTokens()
							.get(0)));
		}
			break;
		case RIGHT_SUBJECT_LOCATOR: {
			axisExpression = expression.getExpressions().get(1);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					SubjectLocator.class);
			buildArrayBySubjectLocator(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(0).getTokens()
							.get(0)));
		}
			break;
		case LEFT_ITEM_IDENTIFIER: {
			axisExpression = expression.getExpressions().get(0);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					ItemIdentifier.class);
			buildArrayByItemIdentifier(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(1).getTokens()
							.get(0)));
		}
			break;
		case RIGHT_ITEM_IDENTIFIER: {
			axisExpression = expression.getExpressions().get(1);
			index = ParserUtils.indexOfTokens(axisExpression.getTmqlTokens(),
					ItemIdentifier.class);
			buildArrayByItemIdentifier(arrayBuilder, registry, LiteralUtils
					.asString(expression.getExpressions().get(0).getTokens()
							.get(0)));
		}
			break;
		}
		/*
		 * move before given index
		 */
		index -= 1;
		/*
		 * move to previous token
		 */
		if (index > 0) {
			tokensToQuery(builder, axisExpression.getTokens().subList(0,
					index));
		}
		builder.append(Equals.TOKEN);
		builder.append(WHITESPACE);
		builder.append(arrayBuilder.toString());
	}

	/**
	 * Transform a scope filter to an array-based TMQL part.
	 * <p>
	 * <code> @ theme ==> [ . / scope = theme ] </code> <br />
	 * </p>
	 * 
	 * @param expression
	 *            the scope filter
	 * @param registry
	 *            the PSI registry
	 * @param builder
	 *            the builder
	 */
	private static void transformScopeFilter(final IExpression expression,
			final IPSIRegistry registry, final StringBuilder builder) {
		List<String> tokens = HashUtil.getList();
		tokens.add(BracketSquareOpen.TOKEN); // filter start
		tokens.add(Dot.TOKEN); // dot
		tokens.add(Axis.TOKEN); // slash
		tokens.add(AxisScope.TOKEN); // scope axis
		tokens.add(DoubleColon.TOKEN); // the double colon
		tokens.add(Star.TOKEN); // the star
		tokens.add(Equals.TOKEN); // equals token
		tokensToQuery(builder, tokens);
		buildArrayBySubjectIdentifier(builder, registry, expression.getTokens()
				.get(1));
		builder.append(BracketSquareClose.TOKEN); // filter end
		builder.append(WHITESPACE); // white space
	}

	/**
	 * Transform a path specification to an array-based TMQL part.
	 * <p>
	 * <code> / axis :: filter ==> / axis :: * [ . / type = array(...) ] </code>
	 * <br />
	 * <code> / filter ==> / default :: * [ . / type = array(...) ] </code> <br />
	 * </p>
	 * 
	 * @param expression
	 *            the path specification
	 * @param registry
	 *            the PSI registry
	 * @param builder
	 *            the builder
	 */
	private static void transformPathSpecification(
			final IExpression expression, final IPSIRegistry registry,
			final StringBuilder builder) {
		/*
		 * has filter pattern
		 */
		if (expression.getTmqlTokens().contains(DoubleColon.class)) {
			/*
			 * has type filter reference
			 */
			if (expression.getTmqlTokens().size() == 3
					&& !expression.getTmqlTokens().get(2).equals(Star.class)) {
				List<String> tokens = HashUtil.getList();
				tokens.add(expression.getTokens().get(0)); // axis
				tokens.add(expression.getTokens().get(1)); // double colon
				tokens.add(Star.TOKEN); // star
				tokens.add(BracketSquareOpen.TOKEN); // filter start
				tokens.add(Dot.TOKEN); // dot
				tokens.add(Axis.TOKEN); // slash
				tokens.add(Type.TOKEN); // type axis
				tokens.add(DoubleColon.TOKEN); // the double colon
				tokens.add(Star.TOKEN); // the star
				tokens.add(Equals.TOKEN); // equals token
				tokensToQuery(builder, tokens);
				buildArrayBySubjectIdentifier(builder, registry, expression
						.getTokens().get(2));
				builder.append(BracketSquareClose.TOKEN); // filter end
				builder.append(WHITESPACE); // white space
				return;
			}
			tokensToQuery(builder, expression);
			return;
		}
		/*
		 * is unspecified filter
		 */
		if (expression.getTmqlTokens().get(0).equals(Star.class)) {
			tokensToQuery(builder, expression);
			return;
		}

		List<String> tokens = HashUtil.getList();
		tokens.add(AxisDefault.TOKEN); // default axis
		tokens.add(DoubleColon.TOKEN); // double colon
		tokens.add(Star.TOKEN); // star
		tokens.add(BracketSquareOpen.TOKEN); // filter start
		tokens.add(Dot.TOKEN); // dot
		tokens.add(Axis.TOKEN); // slash
		tokens.add(Type.TOKEN); // type axis
		tokens.add(DoubleColon.TOKEN); // the double colon
		tokens.add(Star.TOKEN); // the star
		tokens.add(Equals.TOKEN); // equals token
		tokensToQuery(builder, tokens);
		buildArrayBySubjectIdentifier(builder, registry, expression.getTokens()
				.get(0));
		builder.append(BracketSquareClose.TOKEN); // filter end
		builder.append(WHITESPACE); // white space
	}

	/**
	 * Build an TMQL array pattern for the topic by subject-identifier
	 * 
	 * @param builder
	 *            the builder to write in
	 * @param registry
	 *            the PSI registry to extract multiple identities
	 * @param subjectIdentifier
	 *            the subject identifier to check
	 */
	private static void buildArrayBySubjectIdentifier(
			final StringBuilder builder, final IPSIRegistry registry,
			final String subjectIdentifier) {
		List<Set<String>> identifiers = registry
				.getIdentifiersBySubjectIdentifier(subjectIdentifier);
		buildArray(builder, identifiers.get(0), identifiers.get(1), identifiers
				.get(2), null);
	}

	/**
	 * Build an TMQL array pattern for the topic by subject-locator
	 * 
	 * @param builder
	 *            the builder to write in
	 * @param registry
	 *            the PSI registry to extract multiple identities
	 * @param subjectLocator
	 *            the subject locator to check
	 */
	private static void buildArrayBySubjectLocator(final StringBuilder builder,
			final IPSIRegistry registry, final String subjectLocator) {
		List<Set<String>> identifiers = registry
				.getIdentifiersBySubjectLocator(subjectLocator);
		buildArray(builder, identifiers.get(0), identifiers.get(1), identifiers
				.get(2), null);
	}

	/**
	 * Build an TMQL array pattern for the topic by item-identifier
	 * 
	 * @param builder
	 *            the builder to write in
	 * @param registry
	 *            the PSI registry to extract multiple identities
	 * @param itemIdentifier
	 *            the item identifier to check
	 */
	private static void buildArrayByItemIdentifier(final StringBuilder builder,
			final IPSIRegistry registry, final String itemIdentifier) {
		List<Set<String>> identifiers = registry
				.getIdentifiersByItemIdentifier(itemIdentifier);
		buildArray(builder, identifiers.get(0), identifiers.get(1), identifiers
				.get(2), null);
	}

	/**
	 * Utility method to build an TMQL array with four parts of optional content
	 * and one for each identity type
	 * 
	 * @param builder
	 *            the builder
	 * @param subjectIdentifiers
	 *            the subject identifiers
	 * @param subjectLocators
	 *            the subject locators
	 * @param itemIdentifiers
	 *            the item identifiers
	 * @param optional
	 *            the optional part or <code>null</code>
	 */
	private static void buildArray(final StringBuilder builder,
			final Set<String> subjectIdentifiers,
			final Set<String> subjectLocators,
			final Set<String> itemIdentifiers, final String optional) {
		builder.append(ArrayFunction.IDENTIFIER); // array-function
		builder.append(WHITESPACE); // white space
		builder.append(BracketRoundOpen.TOKEN); // round bracket
		builder.append(WHITESPACE); // white space
		boolean first = optional == null;
		/*
		 * add optional content at first position
		 */
		if (optional != null) {
			builder.append(optional);
			builder.append(WHITESPACE);
		}
		/*
		 * add subject-identifier content
		 */
		if (!subjectIdentifiers.isEmpty()) {
			boolean first_ = true;
			/*
			 * add comma to array arguments list
			 */
			if (!first) {
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(TopicsBySubjectIdentifier.IDENTIFIER);
			builder.append(WHITESPACE); // white space
			builder.append(BracketRoundOpen.TOKEN); // round bracket
			builder.append(WHITESPACE); // white space
			for (String subjectIdentifier : subjectIdentifiers) {
				if (!first_) {
					builder.append(Comma.TOKEN);
					builder.append(WHITESPACE);
				}
				builder.append(QUOTE);
				builder.append(subjectIdentifier); // subject-identifier
				builder.append(QUOTE);
				builder.append(WHITESPACE); // white space
				first_ = false;
			}
			builder.append(WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
			builder.append(WHITESPACE);
			first = false;
		}

		/*
		 * add subject-locator content
		 */
		if (!subjectLocators.isEmpty()) {
			boolean first_ = true;
			/*
			 * add comma to array arguments list
			 */
			if (!first) {
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(TopicsBySubjectLocator.IDENTIFIER);
			builder.append(WHITESPACE); // white space
			builder.append(BracketRoundOpen.TOKEN); // round bracket
			builder.append(WHITESPACE); // white space
			for (String subjectLocator : subjectLocators) {
				if (!first_) {
					builder.append(Comma.TOKEN);
					builder.append(WHITESPACE);
				}
				builder.append(QUOTE);
				builder.append(subjectLocator); // subject-locator
				builder.append(QUOTE);
				builder.append(WHITESPACE); // white space
				first_ = false;
			}
			builder.append(WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
			builder.append(WHITESPACE);
			first = false;
		}

		/*
		 * add item-identifier content
		 */
		if (!itemIdentifiers.isEmpty()) {
			boolean first_ = true;
			/*
			 * add comma to array arguments list
			 */
			if (!first) {
				builder.append(Comma.TOKEN);
				builder.append(WHITESPACE);
			}
			builder.append(TopicsByItemIdentifier.IDENTIFIER);
			builder.append(WHITESPACE); // white space
			builder.append(BracketRoundOpen.TOKEN); // round bracket
			builder.append(WHITESPACE); // white space
			for (String itemIdentifier : itemIdentifiers) {
				if (!first_) {
					builder.append(Comma.TOKEN);
					builder.append(WHITESPACE);
				}
				builder.append(QUOTE);
				builder.append(itemIdentifier); // item-identifier
				builder.append(QUOTE);
				builder.append(WHITESPACE); // white space
				first_ = false;
			}
			builder.append(WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
			builder.append(WHITESPACE);
			first = false;
		}

		builder.append(WHITESPACE);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
		return;
	}

	/**
	 * Build an empty TMQL array
	 * 
	 * @param builder
	 *            the builder to write in
	 */
	private static void buildEmptyArray(final StringBuilder builder) {
		builder.append(ArrayFunction.IDENTIFIER);
		builder.append(WHITESPACE);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WHITESPACE);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WHITESPACE);
	}

	/**
	 * Transform the tokens of the given expression to a query part
	 * 
	 * @param builder
	 *            the builder to write in
	 * @param expression
	 *            the expression
	 */
	private static void tokensToQuery(final StringBuilder builder,
			IExpression expression) {
		tokensToQuery(builder, expression.getTokens());
	}

	/**
	 * Transform the given tokens to a query part
	 * 
	 * @param builder
	 *            the builder to write in
	 * @param tokens
	 *            the tokens
	 */
	private static void tokensToQuery(final StringBuilder builder,
			List<String> tokens) {
		for (String token : tokens) {
			builder.append(token);
			builder.append(WHITESPACE);
		}
	}

}
