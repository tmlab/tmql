package de.topicmapslab.tmql4j.draft2010.extension;

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

public class PathLanguageDraft2010ExtensionEntry implements
		ILanguageExtensionEntry {

	private Expression root;

	
	public Class<? extends IExpression> getExpressionType() {
		return PathExpression.class;
	}

	
	public boolean isValidProduction(ITMQLRuntime runtime,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,IExpression caller) {
		/*
		 * allow new path only direct under root
		 */
		if ( caller.getParent() != null ){
			return false;
		}
		try {			
			root = new Expression(caller, tmqlTokens, tokens,
					(TMQLRuntime) runtime);
		} catch (TMQLInvalidSyntaxException e) {
			return false;
		} catch (TMQLGeneratorException e) {
			return false;
		}
		return true;
	}

	
	public IExpression parse(ITMQLRuntime runtime,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			IExpression caller, boolean autoAdd)
			throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		if (root == null || !tmqlTokens.equals(root.getTmqlTokens())) {			
			root = new Expression(caller, tmqlTokens, tokens,
					(TMQLRuntime) runtime);			
		}
		if ( autoAdd){
			caller.addExpression(root);
		}
		return root;
	}

}
