/*
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
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.By;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Group;
import de.topicmapslab.tmql4j.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Production represent the group-by clause.
 * 
 * group-by-clause ::= GROUP BY <variable>
 * 
 * @author Sven Krosse
 * 
 */
public class GroupByClause extends ExpressionImpl {

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
	public GroupByClause(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		/*
		 * create set for all existing tokens
		 */
		Set<Class<? extends IToken>> filtered = HashUtil.getHashSet(getTmqlTokens());
		/*
		 * valid if:
		 * 
		 * - at least 3 tokens existing
		 * 
		 * - first tokens are GROUP BY
		 * 
		 * - other tokens only variables or comma
		 */
		return getTmqlTokens().size() > 2 && getTmqlTokens().get(0).equals(Group.class) && getTmqlTokens().get(1).equals(By.class) && (( filtered.size() == 3 && filtered.contains(Variable.class) ) || (filtered.size() == 4 && filtered.contains(Variable.class)
				&& filtered.contains(Comma.class)));
	}
}
