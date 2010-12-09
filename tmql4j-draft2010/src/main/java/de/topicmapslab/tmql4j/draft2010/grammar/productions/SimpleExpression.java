package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Placeholder;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;

/**
 * Class representing the production 'simple-expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleExpression extends ExpressionImpl {

	/**
	 * type variable
	 */	
	public static final int TYPE_VARIABLE = 0;
	/**
	 * type topic reference
	 */
	public static final int TYPE_TOPICREF = 1;
	/**
	 * type current node
	 */
	public static final int TYPE_DOT = 2;
	/**
	 * type prepared
	 */
	public static final int TYPE_PREPARED = 3;

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
	public SimpleExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		Class<? extends IToken> token = getTmqlTokens().get(0);
		/*
		 * current node
		 */
		if (token.equals(Dot.class)) {
			setGrammarType(TYPE_DOT);
		}
		/*
		 * variable
		 */
		else if (token.equals(Variable.class)) {
			setGrammarType(TYPE_VARIABLE);
		}
		/*
		 * is prepared 
		 */
		else if ( token.equals(Placeholder.class)){
			checkForExtensions(PreparedExpression.class, tmqlTokens, tokens, runtime);
			setGrammarType(TYPE_PREPARED);
		}
		/*
		 * topic-reference
		 */
		else {
			setGrammarType(TYPE_TOPICREF);
		}

	}

	public boolean isValid() {
		/*
		 * expression has to contain only one token
		 */
		if (getTmqlTokens().size() != 1) {
			return false;
		}
		Class<? extends IToken> token = getTmqlTokens().get(0);
		/*
		 * current node
		 */
		if (token.equals(Dot.class)) {
			return true;
		}
		/*
		 * variable
		 */
		else if (token.equals(Variable.class)) {
			return true;
		}
		/*
		 * topic-reference
		 */
		else if (token.equals(Element.class)) {
			return true;
		}
		/*
		 * invalid expression
		 */
		return false;
	}

}
