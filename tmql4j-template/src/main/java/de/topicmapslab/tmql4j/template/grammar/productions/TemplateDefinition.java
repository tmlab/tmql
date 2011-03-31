/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Delete;
import de.topicmapslab.tmql4j.template.grammar.lexical.Define;
import de.topicmapslab.tmql4j.template.grammar.lexical.Redefine;
import de.topicmapslab.tmql4j.template.grammar.lexical.Template;

/**
 * @author Sven Krosse
 * 
 */
public class TemplateDefinition extends ExpressionImpl {

	/**
	 * constructor
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
	public TemplateDefinition(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		if ( getTmqlTokens().size() == 4 && (getTmqlTokens().get(0).equals(Define.class) || getTmqlTokens().get(0).equals(Redefine.class))
				&& getTmqlTokens().get(1).equals(Template.class)){
			return true;
		}else if ( getTmqlTokens().size() == 3 && getTmqlTokens().get(0).equals(Delete.class) && getTmqlTokens().get(1).equals(Template.class)  ){
			return true;
		}
		return false;
	}
}
