package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Function;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the production 'expression' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Expression extends
		ExpressionImpl {

	/**
	 * the parent expression
	 */
	private IExpression parent;

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
	public Expression(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
		this.parent = parent;

		/*
		 * is only one token
		 */
		if ( tmqlTokens.size() == 1 && !tmqlTokens.get(0).equals(Dot.class)){
			checkForExtensions(ValueExpression.class, tmqlTokens, tokens,
					runtime);
		}
		/*
		 * contains 'union' | 'intersect' | 'minus'
		 */
		else if ( ValueExpression.isSetExpression(tmqlTokens)){
			checkForExtensions(ValueExpression.class, tmqlTokens, tokens,
					runtime);
		}
		/*
		 * contains numerical operator
		 */
		else if ( ValueExpression.isNumericalExpression(tmqlTokens)){
			checkForExtensions(ValueExpression.class, tmqlTokens, tokens,
					runtime);
		}
		/*
		 * is known function
		 */
		else if ( tmqlTokens.get(0).equals(Function.class)){
			checkForExtensions(ValueExpression.class, tmqlTokens, tokens,
					runtime);
		}
		/*
		 * is path-expression
		 */
		else{
			checkForExtensions(PathExpression.class, tmqlTokens, tokens,
					runtime);
		}
	}

	/**
	 * Method changes the current parent node as part of the parsing tree. The
	 * new parent instance overwrites the old one. The parent represent the
	 * production rule containing the expression represented by this instance.
	 * 
	 * @param parent
	 *            the new parent node
	 */
	public void setParent(IExpression parent) {
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public IExpression getParent() {
		return parent;
	}

	
	public boolean isValid() {
		return true;
	}
	
	

}
