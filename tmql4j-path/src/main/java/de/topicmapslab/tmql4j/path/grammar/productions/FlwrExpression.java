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
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.For;
import de.topicmapslab.tmql4j.path.grammar.lexical.Order;
import de.topicmapslab.tmql4j.path.grammar.lexical.Return;
import de.topicmapslab.tmql4j.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.path.grammar.lexical.XmlStartTag;
import de.topicmapslab.tmql4j.util.HashUtil;

/*
 * 
 */
/**
 * Special implementation of {@link ExpressionImpl} representing a
 * flwr-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * 	flwr-expression	::=	[  FOR   binding-set ] [  WHERE   boolean-expression ] [  ORDER BY  < value-expression > ] RETURN  content
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FlwrExpression extends ExpressionImpl {

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
	public FlwrExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * call-back instance of parser utility
		 */
		IParserUtilsCallback callback = new IParserUtilsCallback() {
			@Override
			public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
				Class<? extends IToken> token = tmqlTokens.get(0);
				/*
				 * is keyword FOR
				 */
				if (token.equals(For.class)) {
					checkForExtensions(ForClause.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is keyword WHERE
				 */
				else if (token.equals(Where.class)) {
					checkForExtensions(WhereClause.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is keyword ORDER
				 */
				else if (token.equals(Order.class)) {
					checkForExtensions(OrderByClause.class, tmqlTokens, tokens, runtime);
				}
				/*
				 * is keyword RETURN or XML start-tag
				 */
				else if (token.equals(Return.class) || token.equals(XmlStartTag.class)) {
					checkForExtensions(ReturnClause.class, tmqlTokens, tokens, runtime);
				}
			}
		};

		/*
		 * create set containing all delimers
		 */
		Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
		delimers.add(For.class);
		delimers.add(Where.class);
		delimers.add(Order.class);
		delimers.add(Return.class);

		/*
		 * split expression
		 */
		ParserUtils.split(callback, tmqlTokens, tokens, delimers, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean isValid() {
		/*
		 * expects at least two tokens containing the keyword RETURN or an XML
		 * start-tag
		 */
		return getTmqlTokens().size() > 1 && ParserUtils.containsTokens(getTmqlTokens(), Return.class, XmlStartTag.class);
	}
}
