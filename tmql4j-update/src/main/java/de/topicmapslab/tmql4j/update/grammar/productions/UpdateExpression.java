/**
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.parser.IParserUtilsCallback;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.update.grammar.tokens.Add;
import de.topicmapslab.tmql4j.update.grammar.tokens.Remove;
import de.topicmapslab.tmql4j.update.grammar.tokens.Set;
import de.topicmapslab.tmql4j.update.grammar.tokens.Update;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * update-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 *  * <p>
 * update-expression ::= UPDATE anchor [parameter] ( SET | ADD ) value-expression ( ',' anchor [parameter] ( SET | ADD ) value-expression )? WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateExpression extends ExpressionImpl {

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
	public UpdateExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		final boolean containsWhere = tmqlTokens.contains(Where.class);

		IParserUtilsCallback callback = new IParserUtilsCallback() {

			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				if (foundDelimer == null && containsWhere) {
					/*
					 * add remove where token
					 */
					List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tmqlTokens_.add(Where.class);
					tmqlTokens_.addAll(tmqlTokens);
					List<String> tokens_ = new LinkedList<String>();
					tokens_.add(new Where().getLiteral());
					tokens_.addAll(tokens);
					/*
					 * add new where-clause
					 */
					checkForExtensions(WhereClause.class, tmqlTokens_, tokens_, runtime);
				} else if (foundDelimer == null || foundDelimer.equals(Comma.class) || foundDelimer.equals(Where.class)) {
					/*
					 * add new update-clause
					 */
					checkForExtensions(UpdateClause.class, tmqlTokens, tokens, runtime);
				}
			}
		};

		/*
		 * call parser
		 */
		Collection<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(Where.class);
		delimers.add(Comma.class);
		delimers.add(Update.class);
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		if (getTmqlTokens().isEmpty()) {
			return false;
		} else if (!getTmqlTokens().get(0).equals(Update.class)) {
			return false;
		} else if (!getTmqlTokens().contains(Set.class) && !getTmqlTokens().contains(Add.class)&& !getTmqlTokens().contains(Remove.class)) {
			return false;
		}
		return true;
	}

}
