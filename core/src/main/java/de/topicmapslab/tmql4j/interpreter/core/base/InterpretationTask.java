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
package de.topicmapslab.tmql4j.interpreter.core.base;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.process.IInterpretationTask;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.model.IParser;
import de.topicmapslab.tmql4j.parser.model.IParserTree;

/**
 * Base implementation of an {@link IInterpretationTask}. The interpretation
 * task takes place after the parsing process and interprets the current TMQL
 * parser tree to get the results from underlying backend.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InterpretationTask extends ProcessingTaskImpl implements
		IInterpretationTask {

	/**
	 * the internal parser reference
	 */
	private final IParser parser;
	/**
	 * the TMQL4J runtime
	 */
	private final TMQLRuntime runtime;
	/**
	 * the tuple sequence as result of interpretation
	 */
	private QueryMatches matches;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime encapsulating this interpretation task
	 * @param parser
	 *            the parser containing the parser tree
	 */
	public InterpretationTask(TMQLRuntime runtime, IParser parser) {
		this.parser = parser;
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		/*
		 * get parsed tree
		 */
		IParserTree tree = parser.getParserTree();
		/*
		 * get root expression
		 */
		IExpressionInterpreter<?> ex = runtime.getLanguageContext()
				.getInterpreterRegistry().interpreterInstance(tree.root());
		/*
		 * call root expression
		 */
		ex.interpret(runtime);

		/*
		 * get overall results
		 */
		matches = (QueryMatches) runtime.getRuntimeContext().peek()
				.getValue(VariableNames.QUERYMATCHES);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches getResult() throws TMQLRuntimeException {
		if (isFinish()) {
			return matches;
		}
		throw new TMQLRuntimeException(
				"process not finished, please use run() method first.");
	}

}
