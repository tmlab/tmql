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
package de.topicmapslab.tmql4j.template.grammar.productions;

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
import de.topicmapslab.tmql4j.path.grammar.lexical.Delete;
import de.topicmapslab.tmql4j.template.grammar.lexical.Define;
import de.topicmapslab.tmql4j.template.grammar.lexical.Redefine;
import de.topicmapslab.tmql4j.template.grammar.lexical.Template;
import de.topicmapslab.tmql4j.template.grammar.lexical.Use;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * query-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * query-expression ::= [ environment-clause ] query-expression use-expression
 * </p>
 * <p>
 * query-expression ::= template-definition
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpression extends ExpressionImpl {

	public static final int TYPE_TEMPLATE_DEFINITION = 0;
	public static final int TYPE_USE_EXPRESSION = 1;

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
	public QueryExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, final ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		if (tmqlTokens.get(0).equals(Delete.class) || tmqlTokens.get(0).equals(Redefine.class) || tmqlTokens.get(0).equals(Define.class)) {
			checkForExtensions(TemplateDefinition.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_TEMPLATE_DEFINITION);
		}
		/*
		 * is query-expression using the use-expression
		 */
		else {
			/*
			 * initialize the parser callback
			 */
			IParserUtilsCallback callback = new IParserUtilsCallback() {
				public void newToken(List<Class<? extends IToken>> tmqlTokens, List<String> tokens, Class<? extends IToken> foundDelimer) throws TMQLGeneratorException, TMQLInvalidSyntaxException {
					if (Use.class.equals(foundDelimer)) {
						checkForExtensions(de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression.class, tmqlTokens, tokens, runtime);
					} else {
						checkForExtensions(UseExpression.class, tmqlTokens, tokens, runtime);
					}
				}
			};
			/*
			 * parse
			 */
			Set<Class<? extends IToken>> delimers = HashUtil.getHashSet();
			delimers.add(Use.class);
			ParserUtils.split(callback, tmqlTokens, tokens, delimers, false);
			setGrammarType(TYPE_USE_EXPRESSION);
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
	 * USE-expression
	 * 
	 * @param tokens
	 *            the tokens
	 * @return <code>true</code> if the tokens represent a valid expression,
	 *         <code>false</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValid(List<Class<? extends IToken>> tokens) {
		/*
		 * first token is use or ( define|redefine|delete template )
		 */
		return ParserUtils.containsTokens(tokens, Use.class)
				|| ((tokens.get(0).equals(Define.class) || tokens.get(0).equals(Delete.class) || tokens.get(0).equals(Redefine.class)) && tokens.get(1).equals(Template.class));
	}

}
