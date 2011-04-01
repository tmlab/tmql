/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.lexer.TMQLLexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.processor.TmqlProcessorImpl;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.parser.TMQLParser;
import de.topicmapslab.tmql4j.draft2010.components.processor.prepared.PreparedStatement2010;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlProcessor extends TmqlProcessorImpl {

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 */
	public TmqlProcessor(ITMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILexer getTmqlLexer(IQuery query) {
		return new TMQLLexer(getRuntime(), query);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IParser getTmqlParser(ILexer lexer) {
		return new TMQLParser(lexer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<? extends IPreparedStatement> getPreparedStatementClass() {
		return PreparedStatement2010.class;
	}

}
