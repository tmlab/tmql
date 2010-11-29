/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime.module;

import java.util.Collection;
import java.util.Map;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.IValueStore;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Cache implementation to store values created during the runtime process of
 * the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueStore implements IValueStore {

	/**
	 * enumeration containing all keys of the value store
	 * 
	 * @author Sven Krosse
	 * @email krosse@informatik.uni-leipzig.de
	 * 
	 */
	public enum ValueKeys implements IValueStore.IValueKeys {
		/**
		 * representing the stored information of screened query
		 */
		TMQL_RUNTIME_QUERY_SCREENED,
		/**
		 * representing the stored information of the query after white-spacing
		 * process
		 */
		TMQL_RUNTIME_QUERY_WHITESPACED,
		/**
		 * representing the stored information of canonized query
		 */
		TMQL_RUNTIME_QUERY_CANONIZED,
		/**
		 * representing the stored tokens as result of lexical scanning
		 */
		TMQL_RUNTIME_LANGUAGE_SPECIFIC_TOKENS,
		/**
		 * representing the stored tokens as result of lexical scanning
		 */
		TMQL_RUNTIME_STRING_REPRESNETED_TOKENS,
		/**
		 * representing the stored tree as result of parsing process
		 */
		TMQL_RUNTIME_PARSER_TREE,
		/**
		 * representing the stored results of interpretation task
		 */
		TMQL_RUNTIME_INTERPRETER_RESULT,
		/**
		 * representing the stored results of result processing
		 */
		TMQL_RUNTIME_RESULTPROCESSING_RESULT,
		/**
		 * representing the custom information
		 */
		TMQL_RUNTIME_CUSTOM
	}

	private final Map<ValueKeys, Object> store = HashUtil.getHashMap();

	/**
	 * constructor
	 */
	public ValueStore() {
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getScreenedQuery() {
		return (IQuery) get(ValueKeys.TMQL_RUNTIME_QUERY_SCREENED);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setScreenedQuery(IQuery query) {
		put(ValueKeys.TMQL_RUNTIME_QUERY_SCREENED, query);
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getWhitespacedQuery() {
		return (IQuery) get(ValueKeys.TMQL_RUNTIME_QUERY_WHITESPACED);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWhitespacedQuery(IQuery query) {
		put(ValueKeys.TMQL_RUNTIME_QUERY_WHITESPACED, query);
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getCanonizedQuery() {
		return (IQuery) get(ValueKeys.TMQL_RUNTIME_QUERY_CANONIZED);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCanonizedQuery(IQuery query) {
		put(ValueKeys.TMQL_RUNTIME_QUERY_CANONIZED, query);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<Class<? extends IToken>> getLanguageSpecificTokens() {
		return (Collection<Class<? extends IToken>>) get(ValueKeys.TMQL_RUNTIME_LANGUAGE_SPECIFIC_TOKENS);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLanguageSpecificTokens(
			Collection<Class<? extends IToken>> tokens) {
		put(ValueKeys.TMQL_RUNTIME_LANGUAGE_SPECIFIC_TOKENS, tokens);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getStringRepresentedTokens() {
		return (Collection<String>) get(ValueKeys.TMQL_RUNTIME_STRING_REPRESNETED_TOKENS);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStringRepresentedTokens(Collection<String> tokens) {
		put(ValueKeys.TMQL_RUNTIME_STRING_REPRESNETED_TOKENS, tokens);
	}

	/**
	 * {@inheritDoc}
	 */
	public IParserTree getParserTree() {
		return (IParserTree) get(ValueKeys.TMQL_RUNTIME_PARSER_TREE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParserTree(IParserTree tree) {
		put(ValueKeys.TMQL_RUNTIME_PARSER_TREE, tree);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches getInterpretationResults() {
		return (QueryMatches) get(ValueKeys.TMQL_RUNTIME_INTERPRETER_RESULT);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInterpretationResults(QueryMatches matches) {
		put(ValueKeys.TMQL_RUNTIME_INTERPRETER_RESULT, matches);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultSet<?> getResultSet() {
		return (IResultSet<?>) get(ValueKeys.TMQL_RUNTIME_RESULTPROCESSING_RESULT);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setResultSet(IResultSet<?> resutlSet) {
		put(ValueKeys.TMQL_RUNTIME_RESULTPROCESSING_RESULT, resutlSet);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(IValueKeys key) {
		if (key instanceof ValueKeys) {
			return store.get(key);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(IValueKeys key, Object value) {
		if (key instanceof ValueKeys) {
			store.put((ValueKeys) key, value);
		}
	}

}
