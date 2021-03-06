package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.AssociationPatternFct;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the production 'path-specification' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathSpecification extends ExpressionImpl {

	/**
	 * type axis navigation
	 */
	public static final int TYPE_AXIS_SPEC = 0;
	/**
	 * type association pattern
	 */
	public static final int TYPE_ASSOCIATION_PATTERN = 1;
	/**
	 * type association pattern function
	 */
	public static final int TYPE_ASSOCIATION_PATTERN_FCT = 2;

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
	public PathSpecification(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * is new function for association pattern
		 */
		if (tmqlTokens.size() > 1 && AssociationPatternFct.IDENTIFIER.equalsIgnoreCase(tokens.get(0)) ){
			checkForExtensions(FunctionCall.class, tmqlTokens, tokens,
					runtime);
			setGrammarType(TYPE_ASSOCIATION_PATTERN_FCT);
		}
		/*
		 * path specification is an association pattern
		 */
		if (tmqlTokens.size() > 1 && tmqlTokens.get(1).equals(BracketRoundOpen.class)) {
			checkForExtensions(AssociationPattern.class, tmqlTokens, tokens,
					runtime);
			setGrammarType(TYPE_ASSOCIATION_PATTERN);
		}
		/*
		 * path specification is an axis specification
		 */
		else {
			setGrammarType(TYPE_AXIS_SPEC);
		}
	}

	
	public boolean isValid() {
		return true;
	}

}
