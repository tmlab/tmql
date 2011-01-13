/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TranslaterContext implements ITranslatorContext {

	private final State state;
	private Map<String, String> contexts;
	private List<String> selections;

	/**
	 * constructor
	 * 
	 * @param state
	 *            the state
	 * @param selections
	 *            the selections
	 */
	public TranslaterContext(final State state, final String... selections) {
		this(state,Arrays.asList(selections));
	}
	
	/**
	 * constructor
	 * 
	 * @param state
	 *            the state
	 * @param selections
	 *            the selections
	 */
	public TranslaterContext(final State state, final List<String> selections) {
		this.state = state;
		this.selections = selections;
	}

	/**
	 * {@inheritDoc}
	 */
	public State getState() {
		return state;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContextOfCurrentNode(String context) {
		setContextOfVariable(Dot.TOKEN, context);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getContextOfCurrentNode() {
		return getContextOfVariable(Dot.TOKEN);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getContextOfCurrentNode() + "( " + state.name() + " )";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getContextOfVariable(String variable) {
		if (contexts == null) {
			return null;
		}
		return contexts.get(variable);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContextOfVariable(String variable, String context) {
		if (contexts == null) {
			contexts = HashUtil.getHashMap();
		}
		contexts.put(variable, context);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getTopLevelSelections() {
		return selections;
	}
}
