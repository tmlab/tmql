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
package de.topicmapslab.tmql4j.flwr.grammar.productions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlEndTag;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlStartTag;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.path.grammar.lexical.TripleQuote;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * content ::= tm-content (3)
 * </p>
 * <p>
 * content ::= xml-content (4)
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Content extends ExpressionImpl {
	/**
	 * grammar type of content containing a tm-content
	 */
	public static final int TYPE_CTM_EXPRESSION =3;
	/**
	 * grammar type of content containing a XML-content
	 */
	public static final int TYPE_XML_EXPRESSION = 4;

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
		 * is CTM-content
		 */
		if (tmqlTokens.get(0).equals(TripleQuote.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(TripleQuote.class)) {
			setGrammarType(TYPE_CTM_EXPRESSION);
			checkForExtensions(TMContent.class, tmqlTokens, tokens, runtime);
		}
		/*
		 * is XML-content
		 */
		else if (isXmlContent(runtime, parent, tmqlTokens, tokens)) {
			setGrammarType(TYPE_XML_EXPRESSION);
			checkForExtensions(XMLContent.class, tmqlTokens, tokens, runtime);
		} else {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens, getClass());
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
	 * Utility method to check if the tokens are valid FLWR-content ( CTM or XML
	 * )
	 * 
	 * @param runtime
	 *            the runtime
	 * @param tokens
	 *            the tokens
	 * @param literals
	 *            the literals
	 * @return <code>true</code> if the tokens are valid CTM or XTM content,
	 *         <code>false</code> otherwise
	 */
	public static boolean isValid(ITMQLRuntime runtime, List<Class<? extends IToken>> tokens, List<String> literals) {
		/*
		 * is CTM-content
		 */
		if (tokens.get(0).equals(TripleQuote.class) && tokens.get(tokens.size() - 1).equals(TripleQuote.class)) {
			return true;
		}
		/*
		 * check if content is valid XML content
		 */
		return isXmlContent(runtime, tokens, literals);
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

	/**
	 * Utility method to check if the tokens are valid XML content.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param parent
	 *            the parent of this expression
	 * @param tmqlTokens
	 *            the tokens
	 * @param tokens
	 *            the literals
	 * @return <code>true</code> if the tokens are valid XTM content,
	 *         <code>false</code> otherwise
	 */
	private boolean isXmlContent(ITMQLRuntime runtime, IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		/*
		 * parent should be a RETURN clause
		 */
		if (!(parent instanceof ReturnClause)) {
			return false;
		}
		return isXmlContent(runtime, tmqlTokens, tokens);
	}

	/**
	 * Utility method to check if the tokens are valid XML content.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param tmqlTokens
	 *            the tokens
	 * @param tokens
	 *            the literals
	 * @return <code>true</code> if the tokens are valid XTM content,
	 *         <code>false</code> otherwise
	 */
	private static boolean isXmlContent(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens) {
		/*
		 * starts with XML Tag and Ends with XML Tag
		 */
		if (tmqlTokens.get(0).equals(XmlStartTag.class) && tmqlTokens.get(tmqlTokens.size() - 1).equals(XmlEndTag.class)) {
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
}
