package de.topicmapslab.tmql4j.insert.grammar.productions;

import java.util.Iterator;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.path.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.path.grammar.productions.EnvironmentClause;

/**
 * Query-Expression of the insert-style of TMQL
 * 
 * @author Sven Krosse
 * 
 */
public class QueryExpression extends ExpressionImpl {

	/**
	 * base constructor to create a new expression without sub-nodes
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
	public QueryExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * initialize token iterator
		 */
		Iterator<Class<? extends IToken>> chain = tmqlTokens.iterator();
		/*
		 * get key token
		 */
		Class<? extends IToken> key = chain.next();

		/*
		 * create temporary lists of tokens
		 */
		List<Class<? extends IToken>> tmqlTokens_ = tmqlTokens;
		List<String> tokens_ = tokens;

		/*
		 * check if first token is %prefix or %pragma
		 */
		if (key.equals(Pragma.class) || key.equals(Prefix.class)) {
			/*
			 * get last index of %pragma
			 */
			int pragmaIndex = tmqlTokens.lastIndexOf(Pragma.class);
			/*
			 * get last index of %prefix
			 */
			int directiveIndex = tmqlTokens.lastIndexOf(Prefix.class);

			/*
			 * get real index
			 */
			int index = pragmaIndex > directiveIndex ? pragmaIndex + 3 : directiveIndex + 3;

			/*
			 * add environment-clause
			 */
			tmqlTokens_ = tmqlTokens.subList(0, index);
			tokens_ = tokens.subList(0, index);
			checkForExtensions(EnvironmentClause.class, tmqlTokens_, tokens_, runtime);

			tmqlTokens_ = tmqlTokens.subList(index, tmqlTokens.size());
			tokens_ = tokens.subList(index, tokens.size());

			/*
			 * get next key
			 */
			key = tmqlTokens_.iterator().next();
		}

		/*
		 * check if token is keyword INSERT
		 */
		if (tmqlTokens.contains(Insert.class)) {
			checkForExtensions(InsertExpression.class, tmqlTokens_, tokens_, runtime);
		} else {
			throw new TMQLInvalidSyntaxException(tmqlTokens_, tokens_, this.getClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return isValid(getTmqlTokens());
	}

	/**
	 * Utility method to check if the given tokens represent a valid
	 * insert-expression
	 * 
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the expression is valid, <code>false</code>
	 *         otherwise
	 */
	public static boolean isValid(List<Class<? extends IToken>> tokens) {
		return !tokens.isEmpty() && (tokens.contains(Insert.class));
	}

}
