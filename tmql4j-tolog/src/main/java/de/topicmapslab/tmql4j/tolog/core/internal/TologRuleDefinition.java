package de.topicmapslab.tmql4j.tolog.core.internal;

import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.tolog.exception.IncompatibleTologRuleException;

/**
 * Class definition of a tolog rule.
 * 
 * @author Sven Krosse
 * 
 */
public class TologRuleDefinition {

	private final String predicate;
	private final List<String> predicateVariables;
	private final String definition;
	private static final int MAX_ITERATIONS = 5;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the parsed tokens
	 * @param interpretTokens
	 *            the origin tokens
	 */
	public TologRuleDefinition(List<String> tokens, List<String> interpretTokens) {
		predicate = tokens.get(0);
		predicateVariables = new LinkedList<String>();
		int index = tokens.indexOf(":-");
		List<String> rulePart = tokens.subList(0, index);
		List<String> definitionPart = interpretTokens;

		for (String token : rulePart) {
			if (token.startsWith("$")) {
				predicateVariables.add(token);
			}
		}

		List<String> variables = new LinkedList<String>();

		StringBuilder builder = new StringBuilder();
		for (String token : definitionPart) {
			if (token.startsWith("$") && predicateVariables.contains(token)) {
				String variable = "$tmp_" + System.currentTimeMillis();
				variables.add(variable);
				builder.append(variable + " ");
			} else {
				builder.append(token + " ");
			}
		}

		String definition = builder.toString();
		/*
		 * is recursive
		 */
		if (definition.contains(predicate)) {
			String tmp = definition;
			for (int count = 0; count < MAX_ITERATIONS; count++) {
				index = tmp.indexOf(predicate);
				String isolate = tmp.substring(index, tmp.indexOf(")", index));
				String replacement = definition;
				for (String variable : variables) {
					replacement = replacement.replaceAll(variable, variable
							+ count);
				}
				tmp = tmp.replace(isolate, replacement);
			}
			this.definition = tmp;
		}
		/*
		 * is not recursive
		 */
		else {
			this.definition = definition;
		}
	}

	/**
	 * Replace the contained variables by their sub-queries and create a TMQL
	 * query by append the snippets.
	 * 
	 * @param tokens
	 *            the TMQL tokens
	 * @return the TMQL query
	 * @throws IncompatibleTologRuleException thrown if token rule cannot be used for given tokens
	 */
	public String clean(List<String> tokens)
			throws IncompatibleTologRuleException {
		List<String> variables = new LinkedList<String>();
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			if (token.equalsIgnoreCase("(")) {
				sb = new StringBuilder();
			} else if (token.equalsIgnoreCase(",")
					|| token.equalsIgnoreCase(")")) {
				variables.add(sb.toString().trim());
				sb = new StringBuilder();
			} else {
				sb.append((sb.toString().isEmpty() ? "" : " ") + token);
			}
		}

		if (variables.size() != predicateVariables.size()) {
			throw new IncompatibleTologRuleException(
					"Transformation of tolog rule fails because of invalid variable count!");
		}

		String replacement = definition;
		for (int index = 0; index < variables.size(); index++) {
			replacement = replacement.replaceAll(predicateVariables.get(index),
					variables.get(index));
		}
		return replacement;
	}

	public String getPredicate() {
		return predicate;
	}

}
