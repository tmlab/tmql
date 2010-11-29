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
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.If;
import de.topicmapslab.tmql4j.path.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.path.grammar.lexical.Substraction;
import de.topicmapslab.tmql4j.path.grammar.lexical.Union;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;

/**
 * 
 * Special interpreter class to interpret content.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * content ::= content ( ++ | -- | == ) content
 * </p>
 * <p>
 * content ::= { query-expression }
 * </p>
 * <p>
 * content ::= if path-expression then content [ else content ]
 * </p>
 * <p>
 * content ::= tm-content (3)
 * </p>
 * <p>
 * content ::= xml-content (4)
 * </p>
 * <p>
 * content ::= path-expression-1 || path-expression-2 (5)
 * 
 * ==> if path-expression-1 then { path-expression-1 } else { path-expression-2
 * }
 * </p>
 * <p>
 * content ::= path_expression (6) ==> { path_expression }
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ContentInterpreter extends ExpressionInterpreterImpl<Content> {

	/**
	 * index of set operator
	 */
	private final int indexOfOperator;

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ContentInterpreter(Content ex) {
		super(ex);

		indexOfOperator = getExpression().getIndexOfOperator();
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is content ::= content ( UNION | MINUS | INTERSECT ) content
		 */
		case Content.TYPE_SET_OPERATION: {
			interpretSetOperation(runtime);
		}
			break;
		/*
		 * is { query-expression }
		 */
		case Content.TYPE_QUERY_EXPRESSION: {
			interpretQueryExpression(runtime);
		}
			break;
		/*
		 * is if path-expression then content [ else content ]
		 */
		case Content.TYPE_CONDITIONAL_EXPRESSION: {
			interpretConditionalPathExpression(runtime);
		}
			break;
		/*
		 * is tm-content
		 */
		case Content.TYPE_CTM_EXPRESSION: {
			interpretTMContent(runtime);
		}
			break;
		/*
		 * is xml-content
		 */
		case Content.TYPE_XML_EXPRESSION: {
			interpretXMLContent(runtime);
		}
			break;
		/*
		 * is path-expression-1 || path-expression-2 ==> if path-expression-1
		 * then { path-expression-1 } else { path-expression-2 }
		 */
		case Content.TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION: {
			interpretConditionalPathExpression(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unexpected state!");
		}
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretSetOperation(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches[] content = extractArguments(runtime, Content.class);

		/*
		 * get language-specific token of set-operator
		 */
		Class<? extends IToken> operator = getTmqlTokens().get(indexOfOperator);

		Set<Map<String, Object>> negation = HashUtil.getHashSet();
		negation.addAll(content[0].getMatches());

		QueryMatches result = null;
		/*
		 * is INTERSECT
		 */
		if (operator.equals(Intersect.class)) {
			/*
			 * only tuple contained in both sequences
			 */
			result = QueryMatchUtils.intersect(runtime, content[0], content[1]);
		}
		/*
		 * is UNION
		 */
		else if (operator.equals(Union.class)) {
			/*
			 * combination of both sequences with duplicates
			 */
			result = QueryMatchUtils.union(runtime, content[0], content[1]);
		}
		/*
		 * is MINUS
		 */
		else if (operator.equals(Substraction.class)) {
			/*
			 * only tuples contained in sequence A but not in sequence B
			 */
			result = QueryMatchUtils.minus(runtime, content[0], content[1]);
		}
		/*
		 * is unknown operator
		 */
		else {
			throw new TMQLRuntimeException("Unknown operator '"
					+ operator.getSimpleName() + "'!");
		}

		/*
		 * set non-satisfying bindings
		 */
		negation.removeAll(result.getMatches());
		result.addNegation(negation);
		/*
		 * set overall result to %%%____
		 */
		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, result);

		/*
		 * log it
		 */
		logger.info("Finished! Results: " + result);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretQueryExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretConditionalPathExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		logger.info("Start");

		QueryMatches[] pathContent = extractArguments(runtime,
				PathExpression.class);
		QueryMatches[] content = extractArguments(runtime, Content.class);
		/*
		 * use else-content if exists
		 */
		if (pathContent[0].isEmpty()) {
			/*
			 * else-content exists if first token equals IF and there are three
			 * subexpression or if first token don't equals IF and there are two
			 * subexpression
			 */
			if ((getTmqlTokens().get(0).equals(If.class) && getInterpreters(
					runtime).size() == 3)) {
				runtime.getRuntimeContext().peek()
						.setValue(VariableNames.QUERYMATCHES, content[1]);
				logger.info("Finished! Results: " + content[1]);
			} else if (!getTmqlTokens().get(0).equals(If.class)
					&& getInterpreters(runtime).size() == 2) {
				runtime.getRuntimeContext()
						.peek()
						.setValue(VariableNames.QUERYMATCHES,
								pathContent[pathContent.length - 1]);

				logger.info("Finished! Results: "
						+ pathContent[pathContent.length - 1]);
			}
			/*
			 * no else-content contained returns an empty sequence
			 */
			else {
				runtime.getRuntimeContext()
						.peek()
						.setValue(VariableNames.QUERYMATCHES,
								new QueryMatches(runtime));
				logger.info("Finished! Results are empty");
			}
		}
		/*
		 * is IF ... THEN ... ELSE
		 */
		else {
			/*
			 * there is an else-expression
			 */
			if (getTmqlTokens().contains(If.class)) {
				runtime.getRuntimeContext().peek()
						.setValue(VariableNames.QUERYMATCHES, content[0]);
			}
			/*
			 * shortcut condition
			 */
			else {
				runtime.getRuntimeContext()
						.peek()
						.setValue(
								VariableNames.QUERYMATCHES,
								pathContent[0]
										.extractAndRenameBindingsForVariable(QueryMatches
												.getNonScopedVariable()));
			}
		}
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretXMLContent(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretTMContent(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		ex.interpret(runtime);
	}
}
