/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.processor.runtime.module;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;
import de.topicmapslab.tmql4j.path.grammar.lexical.Null;
import de.topicmapslab.tmql4j.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class Template {

	public static final String ANONYMOUS = "ANONYMOUS";

	/**
	 * 
	 */
	private static final String CURRENT_NODE = "$0";
	/**
	 * 
	 */
	private static final String MISSING_DATA_FOR_TEMPLATE_PROCESSING = "Missing data for template processing.";
	/**
	 * 
	 */
	private static final String MISSING_BINDING_VALUE_FOR_VARIABLE = "Missing binding value for variable {0}.";
	/**
	 * 
	 */
	private static final String MISSING_ALIAS_FOR_TEMPLATE_PROCESSING = "Missing alias {0} for template processing.";
	private final String name;
	private final String definition;
	private final List<String> wildcards;
	private List<String> parts;

	/**
	 * constructor
	 * 
	 * @param name
	 *            the name
	 * @param definition
	 *            the definition
	 */
	public Template(final String name, final String definition) {
		this.name = name;
		this.definition = definition;
		this.wildcards = new LinkedList<String>();
		this.parts = new LinkedList<String>();

		int index = definition.indexOf(Wildcard.TOKEN);
		int lastIndex = 0;
		while (index != -1) {
			String part = definition.substring(lastIndex, index);
			this.parts.add(part);
			lastIndex = definition.indexOf(Wildcard.TOKEN, index+1);			
			final String wildcard = definition.substring(index + 1, lastIndex);
			this.wildcards.add(wildcard);
			lastIndex ++;
			index = definition.indexOf(Wildcard.TOKEN, lastIndex);
		}
		if (lastIndex < definition.length()) {
			String part = definition.substring(lastIndex);
			this.parts.add(part);
		}
	}

	/**
	 * Uses the template for the given context
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param objects
	 *            optional arguments
	 * @return the query matches and never <code>null</code>
	 * @throws TMQLRuntimeException
	 */
	public String use(ITMQLRuntime runtime, IContext context, Object... objects) throws TMQLRuntimeException {
		Iterator<String> iParts = parts.iterator();
		Iterator<String> iWildcards = wildcards.iterator();
		StringBuilder builder = new StringBuilder();
		/*
		 * iterate over parts
		 */
		while (iParts.hasNext()) {
			String part = iParts.next();
			builder.append(part);
			/*
			 * use next wildcard
			 */
			if (iWildcards.hasNext()) {
				String wildcard = iWildcards.next();
				builder.append(getPart(context, wildcard));
			}
		}
		/*
		 * iterate over wildcards not used yet
		 */
		while (iWildcards.hasNext()) {
			String wildcard = iWildcards.next();
			builder.append(getPart(context, wildcard));
		}
		return builder.toString();
	}

	/**
	 * Internal method to get the value for the wildcard
	 * 
	 * @param context
	 *            the context
	 * @param wildcard
	 *            the wildcard without ?
	 * @return the value and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if wildcard is unknown or context is invalid
	 */
	private String getPart(IContext context, final String wildcard) throws TMQLRuntimeException {
		/*
		 * check alias
		 */
		IResultProcessor processor = context.getTmqlProcessor().getResultProcessor();
		if (!processor.isKnownAlias(wildcard)) {
			throw new TMQLRuntimeException(MessageFormat.format(MISSING_ALIAS_FOR_TEMPLATE_PROCESSING, wildcard));
		}
		/*
		 * get real variable for alias
		 */
		String variable = Variable.TOKEN + processor.getIndexOfAlias(wildcard);
		/*
		 * get value of current tuple or current node
		 */
		Object value;
		if (context.getCurrentTuple() != null) {
			if (context.getCurrentTuple().containsKey(variable)) {
				value = context.getCurrentTuple().get(variable);
			}
			else {
				throw new TMQLRuntimeException(MessageFormat.format(MISSING_BINDING_VALUE_FOR_VARIABLE, wildcard));
			}
		} else if (CURRENT_NODE.equalsIgnoreCase(variable) && context.getCurrentNode() != null) {
			value = context.getCurrentNode();
		} else {
			throw new TMQLRuntimeException(MISSING_DATA_FOR_TEMPLATE_PROCESSING);
		}
		/*
		 * return value
		 */
		if (value == null) {
			return Null.TOKEN;
		}
		return LiteralUtils.asString(value);
	}

	/**
	 * Returns the name of the template
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the definition of the template
	 * 
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}
}
