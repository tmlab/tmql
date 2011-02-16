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
package de.topicmapslab.tmql4j.update.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.update.grammar.tokens.Add;
import de.topicmapslab.tmql4j.update.grammar.tokens.Associations;
import de.topicmapslab.tmql4j.update.grammar.tokens.Remove;
import de.topicmapslab.tmql4j.update.grammar.tokens.Set;
import de.topicmapslab.tmql4j.update.grammar.tokens.Topics;

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
	 * grammar type of update-clauses containing the keyword REMOVE
	 */
	public static final int TYPE_REMOVE = 4;
	/**
	 * the language-specific token representing the anchor
	 */
	private final Class<? extends IToken> anchor;
	/**
	 * the string-represented token representing the optional parameter
	 */
	private final String optionalType;

	/**
	 * the variable defining the context which should be updated
	 */
	private String variableName = QueryMatches.getNonScopedVariable();

	private final Class<? extends IToken> operator;

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
	public UpdateClause(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		int indexOfAnchor = 1;

		/*
		 * check if first token is a variable
		 */
		Class<? extends IToken> token = tmqlTokens.get(0);
		if (token.equals(Variable.class)) {
			variableName = tokens.get(0);
			anchor = tmqlTokens.get(1);
		} else {
			anchor = token;
			indexOfAnchor = 0;
		}
		int indexOfOperator;
		/*
		 * get keyword
		 */
		if (tmqlTokens.contains(Set.class)) {
			operator = Set.class;
			indexOfOperator = tmqlTokens.indexOf(Set.class);
			setGrammarType(TYPE_SET);
		} else if (tmqlTokens.contains(Add.class)) {
			operator = Add.class;
			indexOfOperator = tmqlTokens.indexOf(Add.class);
			setGrammarType(TYPE_ADD);
		} else if (tmqlTokens.contains(Remove.class)) {
			operator = Remove.class;
			indexOfOperator = tmqlTokens.indexOf(Remove.class);
			setGrammarType(TYPE_REMOVE);
		} else {
			throw new TMQLInvalidSyntaxException(getTmqlTokens(), getTokens(), getClass(), "Error keyword SET, ADD or REMOVE was expected but " + tmqlTokens.get(2).toString() + " was found.");
		}

		/*
		 * extract optional type parameter
		 */
		if (indexOfOperator - indexOfAnchor == 2) {
			if (getTmqlTokens().get(indexOfAnchor + 1).equals(Wildcard.class)) {
				checkForExtensions(PreparedExpression.class, tmqlTokens.subList(indexOfAnchor + 1, indexOfAnchor + 2), tokens.subList(indexOfAnchor + 1, indexOfAnchor + 2), runtime);
				optionalType = null;
			} else {
				optionalType = tokens.get(indexOfAnchor + 1);
			}
		} else {
			optionalType = null;
		}

		if (anchor.equals(Topics.class)) {
			/*
			 * add topic definition
			 */
			checkForExtensions(TopicDefinition.class, tmqlTokens.subList(indexOfOperator + 1, tmqlTokens.size()), tokens.subList(indexOfOperator + 1, tokens.size()), runtime);
			setGrammarType(TOPIC_ADD);
		} else if (anchor.equals(Associations.class)) {
			/*
			 * add association definition
			 */
			checkForExtensions(PredicateInvocation.class, tmqlTokens.subList(indexOfOperator + 1, tmqlTokens.size()), tokens.subList(indexOfOperator + 1, tokens.size()), runtime);
			setGrammarType(ASSOCIATION_ADD);
		} else {
			/*
			 * add value expression
			 */
			checkForExtensions(ValueExpression.class, tmqlTokens.subList(indexOfOperator + 1, tmqlTokens.size()), tokens.subList(indexOfOperator + 1, tokens.size()), runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (!getTmqlTokens().contains(Set.class) && !getTmqlTokens().contains(Add.class)&& !getTmqlTokens().contains(Remove.class)) {
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

	/**
	 * Returns the variable defining the context to modifiy
	 * 
	 * @return the variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Returns the operator
	 * 
	 * @return the operator
	 */
	public Class<? extends IToken> getOperator() {
		return operator;
	}

}
