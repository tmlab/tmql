/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.process;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.process.IInitializationTask;
import de.topicmapslab.tmql4j.common.model.process.IInterpretationTask;
import de.topicmapslab.tmql4j.common.model.process.ILexerTask;
import de.topicmapslab.tmql4j.common.model.process.IProcessingTask;
import de.topicmapslab.tmql4j.common.model.process.IProcessingTaskChain;
import de.topicmapslab.tmql4j.common.model.process.IResultprocessingTask;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.interpreter.core.base.InterpretationTask;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.lexer.base.LexerTask;
import de.topicmapslab.tmql4j.lexer.model.ILexer;
import de.topicmapslab.tmql4j.parser.base.ParsingTask;
import de.topicmapslab.tmql4j.parser.model.IParser;
import de.topicmapslab.tmql4j.preprocessing.base.CanonizerTask;
import de.topicmapslab.tmql4j.preprocessing.base.InitializationTask;
import de.topicmapslab.tmql4j.preprocessing.base.ScreenerTask;
import de.topicmapslab.tmql4j.preprocessing.base.WhiteSpacerTask;
import de.topicmapslab.tmql4j.resultprocessing.core.ResultprocessingTaskImpl;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Base implementation of {@link IProcessingTaskChain} as default processing
 * chain of the TMQL4J engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ProcessingTaskChainImpl implements IProcessingTaskChain {

	/**
	 * the date format using as timestamps for debug information
	 */
	private final SimpleDateFormat format;
	/**
	 * the internal runtime encapsulate the processing chain
	 */
	private final TMQLRuntime runtime;
	/**
	 * the query currently interpreted by the processing chain
	 */
	private final IQuery query;

	/**
	 * base constructor of processing chain
	 * 
	 * @param runtime
	 *            the runtime encapsulate and execute
	 * @param query
	 *            the query to execute
	 */
	public ProcessingTaskChainImpl(final TMQLRuntime runtime, final IQuery query) {
		this.runtime = runtime;
		this.format = new SimpleDateFormat();
		this.query = query;

	}

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws TMQLRuntimeException {

		printlnIfIsVerbose("Init TMQL process chain");
		/*
		 * run initialization task
		 */
		doInitializationTask(runtime);
		/*
		 * run screener process
		 */
		IQuery q = doScreeningTask(runtime, query);
		/*
		 * run whiteSpacer process
		 */
		q = doWhitespacingTask(runtime, q);
		/*
		 * run canonizer process
		 */
		q = doCanonizingTask(runtime, q);		
		/*
		 * created variables layers
		 */
		doLinqOperations();
		/*
		 * run lexical scanning process
		 */
		ILexer lexer = doLexicalScanningTask(runtime, q);
		/*
		 * run parser process
		 */
		IParser parser = doParsingTask(runtime, lexer);
		/*
		 * run interpretation process
		 */
		QueryMatches matches = doInterpretationTask(runtime, parser);

		/*
		 * run result processing
		 */
		doResultProcessingTask(runtime, matches);

		printlnIfIsVerbose("Finishing TMQL process chain");

	}

	/**
	 * Internal method to realize debug output.
	 * 
	 * @param line
	 *            the debug information to print
	 */
	private void printlnIfIsVerbose(String line) {
		if (runtime.isVerbose()) {
			runtime.getPrintStream().println(
					format.format(new Date(System.currentTimeMillis())) + ": "
							+ line);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public TMQLRuntime getTmqlRuntime() {
		return runtime;
	}

	/**
	 * Method initializes an instance of {@link IInitializationTask} and execute
	 * them. The initialization task is used to create the environment of the
	 * topic map querying.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected void doInitializationTask(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		IProcessingTask task = new InitializationTask(runtime, runtime
				.getTopicMapSystem(), runtime.getTopicMap());
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		runtime.setInitialContext(((IInitializationTask) task)
				.getInitialContext());
	}

	/**
	 * Method initializes an instance of {@link ScreenerTask} and execute them.
	 * The screening task is used to clean the given query and remove comments
	 * and white-spaces.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param query
	 *            the query to screen
	 * @return the screened query instance
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected IQuery doScreeningTask(TMQLRuntime runtime, final IQuery query)
			throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		ScreenerTask task = new ScreenerTask(runtime, query);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();

		/*
		 * handle task results
		 */
		IQuery q = task.getResult();
		runtime.getValueStore().setScreenedQuery(q);

		return q;
	}

	/**
	 * Method initializes an instance of {@link WhiteSpacerTask} and execute
	 * them. The white-spacing task is used to clean the given query and remove
	 * multiple existence of white-spaces and add optional white-spaces.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param query
	 *            the query to clean
	 * @return the cleaned query instance
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected IQuery doWhitespacingTask(TMQLRuntime runtime, final IQuery query)
			throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		WhiteSpacerTask task = new WhiteSpacerTask(runtime, query);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		IQuery q = task.getResult();
		runtime.getValueStore().setWhitespacedQuery(q);

		return q;
	}

	/**
	 * Method initializes an instance of {@link CanonizerTask} and execute them.
	 * The canonizer translates the given query and remove all productions of
	 * the non-canonical level by using term-substitutions.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param query
	 *            the query to clean
	 * @return the cleaned query instance
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected IQuery doCanonizingTask(TMQLRuntime runtime, final IQuery query)
			throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		CanonizerTask task = new CanonizerTask(runtime, query);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		IQuery q = task.getResult();
		runtime.getValueStore().setCanonizedQuery(q);

		return q;
	}

	/**
	 * Method initializes the runtime context and replace the variables of the
	 * query with their parameterized arguments.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected void doLinqOperations() throws TMQLRuntimeException {
		/*
		 * create new variable layer
		 */
		runtime.getRuntimeContext().push(
				runtime.getInitialContext().getPredefinedVariableSet()
						.copyWithValues());
		/*
		 * do LINQ operations
		 */
		runtime.setLINQVariables();
	}

	/**
	 * Method initializes an instance of {@link ILexerTask} and execute them.
	 * The task is used for lexical scanning. The given query will be split into
	 * a chain of language-specific tokens and their string representations.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param query
	 *            the query to clean
	 * @return the scanner instance containing all informations of the lexical
	 *         chain
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected ILexer doLexicalScanningTask(TMQLRuntime runtime,
			final IQuery query) throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		LexerTask task = new LexerTask(runtime, query);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		ILexer lexer = task.getResult();
		runtime.getValueStore()
				.setLanguageSpecificTokens(lexer.getTmqlTokens());
		runtime.getValueStore().setStringRepresentedTokens(lexer.getTokens());
		return lexer;
	}

	/**
	 * Method initializes an instance of {@link IProcessingTask} and execute
	 * them. The task is used for grammatical parsing. The given chain of
	 * language-specific tokens will be transformed to a tree structure
	 * representing the query.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param lexer
	 *            the scanner instance containing all informations of the
	 *            lexical chain
	 * @return the parser instance containing the tree structure
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected IParser doParsingTask(TMQLRuntime runtime, final ILexer lexer)
			throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		ParsingTask task = new ParsingTask(runtime, lexer);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		IParser parser = task.getResult();
		runtime.getValueStore().setParserTree(parser.getParserTree());
		return parser;
	}

	/**
	 * Method initializes an instance of {@link IInterpretationTask} and execute
	 * them. The interpreter walk trough the given parsing tree and execute the
	 * tree node in bottom-up direction.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param parser
	 *            the parser instance containing the tree structure
	 * @return an instance of {@link QueryMatches} containing the results of the
	 *         whole interpretation of the parsing tree
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected QueryMatches doInterpretationTask(TMQLRuntime runtime,
			final IParser parser) throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		InterpretationTask task = new InterpretationTask(runtime, parser);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		QueryMatches matches = task.getResult();
		runtime.getValueStore().setInterpretationResults(matches);
		return matches;
	}

	/**
	 * Method initializes an instance of {@link IResultprocessingTask} and
	 * execute them. The interpreter walk trough the given parsing tree and
	 * execute the tree node in bottom-up direction.
	 * 
	 * @param runtime
	 *            the runtime instance
	 * @param parser
	 *            the parser instance containing the tree structure
	 * @return an instance of {@link QueryMatches} containing the results of the
	 *         whole interpretation of the parsing tree
	 * @throws TMQLRuntimeException
	 *             thrown if execution or initialization of the current
	 *             processing task fails
	 */
	protected IResultSet<?> doResultProcessingTask(TMQLRuntime runtime,
			final QueryMatches matches) throws TMQLRuntimeException {
		/*
		 * create new task
		 */
		ResultprocessingTaskImpl task = new ResultprocessingTaskImpl(runtime,
				matches);
		/*
		 * initialize task
		 */
		printlnIfIsVerbose("Init " + task.getClass().getSimpleName());
		task.init();
		/*
		 * execute task
		 */
		printlnIfIsVerbose("Run " + task.getClass().getSimpleName());
		task.run();
		/*
		 * handle task results
		 */
		IResultSet<?> resultSet = task.getResults();
		runtime.getValueStore().setResultSet(resultSet);

		/*
		 * remove last variable layer
		 */
		runtime.getRuntimeContext().pop();

//		/*
//		 * check if binding stack of runtime context is empty
//		 */
//		assert runtime.getRuntimeContext().isStackEmty();

		return resultSet;
	}

	/**
	 * Returns the internal instance of the runtime.
	 * 
	 * @return the runtime
	 */
	protected TMQLRuntime getRuntime() {
		return runtime;
	}
}
