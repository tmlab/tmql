package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SimpleExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of the production 'simple-expression'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleExpressionInterpreter extends ExpressionInterpreterImpl<SimpleExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public SimpleExpressionInterpreter(SimpleExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * simple-expression contains variable name
		 */
		if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_VARIABLE) {
			/*
			 * get variable name
			 */
			final String variable = getTokens().get(0);
			/*
			 * value is bind by current tuple
			 */
			if (context.getCurrentTuple() != null && context.getCurrentTuple().containsKey(variable)) {
				Object value = context.getCurrentTuple().get(variable);
				return QueryMatches.asQueryMatchNS(runtime, value);
			}
			/*
			 * iteration context is set
			 */
			else if (context.getContextBindings() != null) {
				return context.getContextBindings().extractAndRenameBindingsForVariable(variable);
			}
			return QueryMatches.emptyMatches();
		}
		/*
		 * simple-expression contains topic-reference
		 */
		else if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_TOPICREF) {
			/*
			 * try to resolve reference
			 */
			Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, LiteralUtils.asString(getTokens().get(0)));
			if (construct instanceof Topic) {
				return QueryMatches.asQueryMatchNS(runtime, construct);
			}
			return QueryMatches.emptyMatches();
		}
		/*
		 * simple-expression contains only simple dot
		 */
		else if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_DOT) {
			if ( context.getCurrentNode() != null ){
				return QueryMatches.asQueryMatchNS(runtime, context.getCurrentNode());
			}else if ( context.getContextBindings() != null ){
				return QueryMatches.asQueryMatchNS(runtime, context.getContextBindings().getPossibleValuesForVariable());
			}else if ( context.getCurrentTuple() != null && context.getCurrentTuple().containsKey(QueryMatches.getNonScopedVariable()) ){
				return QueryMatches.asQueryMatchNS(runtime, context.getCurrentTuple().get(QueryMatches.getNonScopedVariable()));
			}			
		}
		/*
		 * is prepared statement
		 */
		else if ( getGrammarTypeOfExpression() == SimpleExpression.TYPE_PREPARED){
			return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
		}
		/*
		 * is function
		 */
		else if ( getGrammarTypeOfExpression() == SimpleExpression.TYPE_FUNCTION){
			return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
		}
		return QueryMatches.emptyMatches();

	}
}
