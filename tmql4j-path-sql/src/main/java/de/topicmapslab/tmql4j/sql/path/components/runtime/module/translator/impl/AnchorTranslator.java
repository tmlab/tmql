/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.StateImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class AnchorTranslator extends TmqlSqlTranslatorImpl<SimpleContent> {

	private static final String TOPIC_REF = "SELECT id FROM rel_subject_identifiers, locators WHERE id = id_locator AND reference = {0}";
	private static final String TM_SUBJECT = "SELECT id FROM topics WHERE id_topicmap = {0}";

	/**
	 * {@inheritDoc}
	 */
	public IState transform(ITMQLRuntime runtime, IContext context, IExpression expression, IState state) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
		case Anchor.TYPE_TOPICREF: {
			final String token = expression.getTokens().get(0);
			String result;
			if (TmdmSubjectIdentifier.isTmdmSubject(token)) {
				result = MessageFormat.format(TM_SUBJECT, context.getQuery().getTopicMap().getId());
			} else {
				result = MessageFormat.format(TOPIC_REF, "'" + runtime.getConstructResolver().toAbsoluteIRI(context, token) + "'");
			}
			return new StateImpl(State.TOPIC, result);
		}
		case Anchor.TYPE_DOT: {
			return state;
		}
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

}
