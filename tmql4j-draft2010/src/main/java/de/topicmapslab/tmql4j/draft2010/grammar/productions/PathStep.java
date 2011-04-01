package de.topicmapslab.tmql4j.draft2010.grammar.productions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Axis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Class representing the production 'path-step' of the new draft 2010
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathStep extends ExpressionImpl {

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
	public PathStep(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * initialize iterators
		 */
		Iterator<Class<? extends IToken>> iteratorTmqlTokens = tmqlTokens.iterator();
		Iterator<String> iteratorTokens = tokens.iterator();

		/*
		 * initialize lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
		List<String> tokens_ = new LinkedList<String>();

		/*
		 * ignore first token /
		 */
		iteratorTmqlTokens.next();
		iteratorTokens.next();

		/*
		 * internal parser flag for association patterns
		 */
		boolean assocPattern = false;
		/*
		 * filter count
		 */
		long filterCount = 0;
		while (iteratorTmqlTokens.hasNext()) {
			Class<? extends IToken> token = iteratorTmqlTokens.next();
			/*
			 * is new boolean filter
			 */
			if (token.equals(BracketSquareOpen.class)) {
				/*
				 * is top-level boolean filter and not part of the association
				 * pattern
				 */
				if (filterCount == 0 && !assocPattern) {
					/*
					 * is first filter part
					 */
					if (getExpressions().isEmpty()) {
						checkForExtensions(PathSpecification.class, tmqlTokens_, tokens_, runtime);
					}
					/*
					 * is next filter part
					 */
					else {
						checkForExtensions(Filter.class, tmqlTokens_, tokens_, runtime);
					}
					/*
					 * reset lists
					 */
					tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tokens_ = new LinkedList<String>();
				}
				filterCount++;
			}
			/*
			 * end of boolean filter
			 */
			else if (token.equals(BracketSquareClose.class)) {
				filterCount--;
			}
			/*
			 * is new scope filter
			 */
			else if (token.equals(Scope.class)) {
				/*
				 * is top-level boolean filter and not part of the association
				 * pattern
				 */
				if (filterCount == 0 && !assocPattern) {
					/*
					 * is first filter part
					 */
					if (getExpressions().isEmpty()) {
						checkForExtensions(PathSpecification.class, tmqlTokens_, tokens_, runtime);
					}
					/*
					 * is next filter part
					 */
					else {
						checkForExtensions(Filter.class, tmqlTokens_, tokens_, runtime);
					}
					/*
					 * reset lists
					 */
					tmqlTokens_ = new LinkedList<Class<? extends IToken>>();
					tokens_ = new LinkedList<String>();
				}
			}
			/*
			 * new part is association pattern
			 */
			else if (token.equals(BracketRoundOpen.class)) {
				assocPattern = true;
			}
			/*
			 * end of association pattern
			 */
			else if (token.equals(BracketRoundClose.class)) {
				assocPattern = false;
			}

			/*
			 * add tokens
			 */
			tmqlTokens_.add(token);
			tokens_.add(iteratorTokens.next());
		}

		/*
		 * add last part
		 */
		if (!tmqlTokens_.isEmpty()) {
			/*
			 * is first filter part
			 */
			if (getExpressions().isEmpty()) {
				checkForExtensions(PathSpecification.class, tmqlTokens_, tokens_, runtime);
			}
			/*
			 * is next filter part
			 */
			else {
				checkForExtensions(Filter.class, tmqlTokens_, tokens_, runtime);
			}
		}
	}

	public boolean isValid() {
		/*
		 * has to contain at least 2 tokens and has to begin with the token /
		 */
		return getTmqlTokens().size() > 1 && getTmqlTokens().get(0).equals(Axis.class);
	}

}
