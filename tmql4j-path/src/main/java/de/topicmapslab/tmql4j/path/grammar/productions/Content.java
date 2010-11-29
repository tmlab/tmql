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

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.Else;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.path.grammar.lexical.If;
import de.topicmapslab.tmql4j.path.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutCondition;
import de.topicmapslab.tmql4j.path.grammar.lexical.Substraction;
import de.topicmapslab.tmql4j.path.grammar.lexical.Then;
import de.topicmapslab.tmql4j.path.grammar.lexical.TripleQuote;
import de.topicmapslab.tmql4j.path.grammar.lexical.Union;
import de.topicmapslab.tmql4j.path.grammar.lexical.XmlEndTag;
import de.topicmapslab.tmql4j.path.grammar.lexical.XmlStartTag;

/**
 * Special implementation of {@link ExpressionImpl} representing a content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * content ::= content ( UNION | MINUS | INTERSECT ) content
 * </p>
 * <p>
 * content ::= { query-expression }
 * </p>
 * <p>
 * content ::= if path-expression then content [ else content ]
 * </p>
 * <p>
 * content ::= tm-content (3)
 * </p>
 * <p>
 * content ::= xml-content (4)
 * </p>
 * <p>
 * content ::= path-expression-1 || path-expression-2 (5)
 * 
 * ==> if path-expression-1 then { path-expression-1 } else { path-expression-2
 * }
 * </p>
 * <p>
 * content ::= path_expression (6) ==> { path_expression }
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Content extends ExpressionImpl {

	/**
	 * grammar type of content containing set operators
	 */
	public static final int TYPE_SET_OPERATION = 0;
	/**
	 * grammar type of content containing a query-expression
	 */
	public static final int TYPE_QUERY_EXPRESSION = 1;
	/**
	 * grammar type of content containing a conditional expression
	 */
	public static final int TYPE_CONDITIONAL_EXPRESSION = 2;
	/**
	 * grammar type of content containing a tm-content
	 */
	public static final int TYPE_CTM_EXPRESSION = 3;
	/**
	 * grammar type of content containing a XML-content
	 */
	public static final int TYPE_XML_EXPRESSION = 4;
	/**
	 * grammar type of content containing a non-canonical conditional expression
	 */
	public static final int TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION = 5;

	/**
	 * index of set operator
	 */
	private int indexOfOperator = -1;

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
	public Content(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is tm-content
		 */
		if (tmqlTokens.get(0).equals(TripleQuote.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(TripleQuote.class)) {
			setGrammarType(TYPE_CTM_EXPRESSION);
			checkForExtensions(TMContent.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is xml-content
		 */
		else if (isXmlContent(parent, tmqlTokens, tokens, runtime)) {
			setGrammarType(TYPE_XML_EXPRESSION);
			checkForExtensions(XMLContent.class, tmqlTokens, tokens, runtime);
		} else {
			/*
			 * create set containing all set-operators ++ , -- , ==
			 */
			Set<Class<? extends IToken>> operators = HashUtil.getHashSet();
			operators.add(Substraction.class);
			operators.add(Intersect.class);
			operators.add(Union.class);
			int index = ParserUtils.indexOfTokens(tmqlTokens, operators);
			/*
			 * is content operator content
			 */
			if (index != -1) {
				setGrammarType(TYPE_SET_OPERATION);
				checkForExtensions(Content.class, getTmqlTokens().subList(0, index), getTokens().subList(0, index), runtime);
				checkForExtensions(Content.class, getTmqlTokens().subList(index + 1, getTmqlTokens().size()), getTokens().subList(index + 1, getTokens().size()), runtime);
				indexOfOperator = index;
			} else {
				/*
				 * create set containing conditional operator ||
				 */
				operators.clear();
				operators.add(ShortcutCondition.class);
				index = ParserUtils.indexOfTokens(tmqlTokens, operators);
				/*
				 * is path-expression || path-expression
				 */
				if (index != -1) {
					setGrammarType(TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION);
					checkForExtensions(PathExpression.class, getTmqlTokens().subList(0, index), getTokens().subList(0, index), runtime);
					checkForExtensions(PathExpression.class, getTmqlTokens().subList(index + 1, getTmqlTokens().size()), getTokens().subList(index + 1, getTokens().size()), runtime);
					indexOfOperator = index;
				}
				/*
				 * if path-expression then content else content
				 */
				else if (tmqlTokens.get(0).equals(If.class)) {
					setGrammarType(TYPE_CONDITIONAL_EXPRESSION);
					/*
					 * get index of keyword THEN
					 */
					operators.clear();
					operators.add(Then.class);
					int iThen = ParserUtils.indexOfTokens(tmqlTokens, operators);
					/*
					 * get index of keyword THEN
					 */
					operators.clear();
					operators.add(Else.class);
					int iElse = ParserUtils.indexOfTokens(tmqlTokens, operators);

					/*
					 * add path-expression as condition
					 */
					checkForExtensions(PathExpression.class, tmqlTokens.subList(1, iThen), tokens.subList(1, iThen), runtime);
					/*
					 * has else expression
					 */
					if (iElse != -1) {
						/*
						 * is then-content
						 */
						checkForExtensions(Content.class, tmqlTokens.subList(iThen + 1, iElse), tokens.subList(iThen + 1, iElse), runtime);
						/*
						 * is else-content
						 */
						checkForExtensions(Content.class, tmqlTokens.subList(iElse + 1, tmqlTokens.size()), tokens.subList(iElse + 1, tokens.size()), runtime);
					} else {
						/*
						 * is then-content
						 */
						checkForExtensions(Content.class, tmqlTokens.subList(iThen + 1, tmqlTokens.size()), tokens.subList(iThen + 1, tokens.size()), runtime);
					}
				}
				/*
				 * is { query-expression }
				 */
				else if (tmqlTokens.size() > 0) {
					if (tmqlTokens.get(0).equals(BracketAngleOpen.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(BracketAngleClose.class)) {
						setGrammarType(TYPE_QUERY_EXPRESSION);
						/*
						 * add query-expression without { and }
						 */
						checkForExtensions(QueryExpression.class, tmqlTokens.subList(1, tmqlTokens.size() - 1), tokens.subList(1, tokens.size() - 1), runtime);
					} else {
						checkForExtensions(QueryExpression.class, tmqlTokens, tokens, runtime);
						setGrammarType(TYPE_QUERY_EXPRESSION);
					}
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

	/**
	 * Method returns the index of the set operator. Method is called by the
	 * interpreter to realize the correct interpretation of the sub-query.
	 * 
	 * @return the index of the set operator relative to the language-specific
	 *         token list of this expression
	 */
	public int getIndexOfOperator() {
		return indexOfOperator;
	}

	private boolean isXmlContent(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) {
		/*
		 * parent should be a RETURN clause
		 */
		if (!(parent instanceof ReturnClause)) {
			return false;
		}
		/*
		 * starts with XML Tag and Ends with XML Tag
		 */
		else if (tmqlTokens.get(0).equals(XmlStartTag.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(XmlEndTag.class)) {
			return true;
		}
		/*
		 * starts with <yml.. > and has END Tag
		 */
		else if (tmqlTokens.get(0).equals(Element.class) && tokens.get(0).startsWith("<") && tmqlTokens.get(tmqlTokens.size() - 1).equals(XmlEndTag.class)) {
			Set<Class<? extends IToken>> tokensToFound = HashUtil.getHashSet();
			tokensToFound.add(GreaterThan.class);
			Set<Class<? extends IToken>> protectionStarts = HashUtil.getHashSet();
			tokensToFound.add(BracketAngleOpen.class);
			Set<Class<? extends IToken>> protectionEnds = HashUtil.getHashSet();
			tokensToFound.add(BracketAngleClose.class);
			int index = ParserUtils.indexOfTokens(tmqlTokens, tokensToFound, protectionStarts, protectionEnds);
			return index != -1;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
}
