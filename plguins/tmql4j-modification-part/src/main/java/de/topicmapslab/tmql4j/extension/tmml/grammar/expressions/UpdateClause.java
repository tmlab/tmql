/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.grammar.expressions;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Add;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Associations;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Set;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Topics;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.core.ExpressionImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * update-clause.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * update-clause ::= anchor [parameter] ( SET | ADD ) value-expression
 * update-clause ::= association ADD association-definition
 * update-clause ::= topic ADD topic-definition
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateClause extends ExpressionImpl {

	/**
	 * grammar type of update-clauses containing the keyword SET
	 */
	public static final int TYPE_SET = 0;
	/**
	 * grammar type of update-clauses containing the keyword ADD
	 */
	public static final int TYPE_ADD = 1;
	/**
	 * grammar type of adding a topic
	 */
	public static final int TOPIC_ADD = 2;
	/**
	 * grammar type of adding an association
	 */
	public static final int ASSOCIATION_ADD = 3;
	/**
	 * the language-specific token representing the anchor
	 */
	private final Class<? extends IToken> anchor;
	/**
	 * the string-represented token representing the optional parameter
	 */
	private final String optionalType;

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
	public UpdateClause(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			TMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * read anchor
		 */
		anchor = tmqlTokens.get(0);

		/*
		 * get index of the keyword SET
		 */
		int indexOfSetOrAdd = tmqlTokens.indexOf(Set.class);

		/*
		 * get anchor of the keyword ADD
		 */
		if (indexOfSetOrAdd != -1) {
			setGrammarType(TYPE_SET);
		} else if (tmqlTokens.contains(Add.class)) {
			indexOfSetOrAdd = tmqlTokens.indexOf(Add.class);
			setGrammarType(TYPE_ADD);
		} else {
			throw new TMQLInvalidSyntaxException(getTmqlTokens(), getTokens(),
					getClass(), "Error keyword SET or ADD was expected but "
							+ tmqlTokens.get(2).toString() + " was found.");
		}

		/*
		 * extract optional type parameter
		 */
		if (indexOfSetOrAdd == 2) {
			optionalType = tokens.get(1);
		} else {
			optionalType = null;
		}

		if (anchor.equals(Topics.class)) {
			/*
			 * add topic definition
			 */
			checkForExtensions(TopicDefinition.class, tmqlTokens.subList(
					indexOfSetOrAdd + 1, tmqlTokens.size()), tokens.subList(
					indexOfSetOrAdd + 1, tokens.size()), runtime);
			setGrammarType(TOPIC_ADD);
		} else if (anchor.equals(Associations.class)) {
			/*
			 * add association definition
			 */
			checkForExtensions(PredicateInvocation.class, tmqlTokens.subList(
					indexOfSetOrAdd + 1, tmqlTokens.size()), tokens.subList(
					indexOfSetOrAdd + 1, tokens.size()), runtime);
			setGrammarType(ASSOCIATION_ADD);
		} else {
			/*
			 * add value expression
			 */
			checkForExtensions(ValueExpression.class, tmqlTokens.subList(
					indexOfSetOrAdd + 1, tmqlTokens.size()), tokens.subList(
					indexOfSetOrAdd + 1, tokens.size()), runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (!getTmqlTokens().contains(Set.class)
				&& !getTmqlTokens().contains(Add.class)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the anchor
	 */
	public Class<? extends IToken> getAnchor() {
		return anchor;
	}

	/**
	 * @return the optionalType
	 */
	public String getOptionalType() {
		return optionalType;
	}

}
