package de.topicmapslab.tmql4j.tolog.core.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Utility class for tolog transformation
 * 
 * @author Sven Krosse
 * 
 */
public class TologUtility {

	private final Map<String, String> usings = HashUtil.getHashMap();
	private final Map<String, String> imports = HashUtil.getHashMap();
	private final Map<String, String> selects = HashUtil.getHashMap();
	private final Map<String, TologRuleDefinition> rules = HashUtil
			.getHashMap();
	private static final Map<String, String> predefinde_predicates = HashUtil
			.getHashMap();

	static {
		predefinde_predicates
				.put("association", " tm:association >> instances");
		predefinde_predicates.put("association-role", " VARIABLE >> roles");
		// TODO possible as a part of new draft
		predefinde_predicates.put("base-locator", "");
		predefinde_predicates.put("occurrence",
				" VARIABLE >> characteristics tm:occurrence");
		predefinde_predicates.put("reifies", " VARIABLE >> reifier");
		// TODO possible as a part of new draft
		predefinde_predicates.put("resource", "");
		predefinde_predicates.put("role-player", "VARIABLE >> players");
		predefinde_predicates.put("scope", "VARIABLE >> scope");
		predefinde_predicates.put("source-locator", "VARIABLE >> item");
		predefinde_predicates.put("subject-identifier",
				"VARIABLE >> indicators");
		predefinde_predicates.put("subject-locator", "VARIABLE >> locators");
		predefinde_predicates.put("topic", " tm:subject >> instances");
		predefinde_predicates.put("topic-name",
				"VARIABLE >> characteristics tm:name");
		// TODO possible as a part of new draft
		predefinde_predicates.put("topicmap", "");
		predefinde_predicates.put("type", "VARIABLE >> types");
		predefinde_predicates.put("value", "VARIABLE >> atomify");
		predefinde_predicates.put("variant", "has-variants( VARIABLE )");
		// predefinde_predicates
		// .put("direct-instance-of", "VARIABLE >> instances");
		// predefinde_predicates.put("instance-of", "VARIABLE >> instances");
		predefinde_predicates.put("value-like",
				"VARIABLE >> atomify == %VARIABLE2%");
	}

	public void addNewUsingExpression(final String ident, final String uri) {
		usings.put(ident, uri);
	}

	public String replaceUsings(final String tmql) {
		String result = tmql;
		for (Entry<String, String> entry : usings.entrySet()) {
			result = result.replaceAll("\\" + entry.getKey(), entry.getValue());
		}
		return result;
	}

	public void addNewImportExpression(final String uri, final String ident) {
		imports.put(uri, ident);
	}

	public String replaceImports(final String tmql) {
		String result = tmql;
		for (Entry<String, String> entry : imports.entrySet()) {
			result = result.replaceAll("\\" + entry.getKey(), entry.getValue());
		}
		return result;
	}

	public void addNewSelectExpression(final String variable,
			final String replacement) {
		selects.put(variable, replacement.trim());
	}

	public String replaceVariables(final String tmql) {
		String result = tmql;
		boolean updates = false;
		for (Entry<String, String> entry : selects.entrySet()) {
			while (result.contains(entry.getKey())) {
				result = result.replace(entry.getKey(), entry.getValue());
				updates = true;
			}
		}
		return updates ? replaceVariables(result) : result;
	}

	public void addNewRuleExpression(TologRuleDefinition definition) {
		rules.put(definition.getPredicate(), definition);
	}

	public TologRuleDefinition getRuleDefintion(final String predicate) {
		return rules.get(predicate);
	}

	public boolean containsRuleDefintion(final String predicate) {
		return rules.containsKey(predicate);
	}

	public static final String cleanUriref(String uriref) {
		String uri = uriref;
		if (uri.startsWith("i\"") || uri.startsWith("a\"")
				|| uri.startsWith("s\"")) {
			uri = uri.substring(2, uri.length() - 1);
		}
		return uri;
	}

	public boolean addAsPredefinedPredicate(List<String> tokens) {
		final String predicate = tokens.get(0);
		if (predefinde_predicates.containsKey(predicate)) {

			List<String> variables = new LinkedList<String>();
			StringBuilder builder = new StringBuilder();
			for (String token : tokens) {
				if (token.equalsIgnoreCase("(")) {
					builder = new StringBuilder();
				} else if (token.equalsIgnoreCase(",")
						|| token.equalsIgnoreCase(")")) {
					variables.add(builder.toString().trim());
					builder = new StringBuilder();
				} else {
					builder.append((builder.toString().isEmpty() ? "" : " ")
							+ token);
				}
			}

			String template = predefinde_predicates.get(predicate);
			template = template.replace("VARIABLE", variables.get(0));
			if (variables.size() > 1) {
				template = template.replaceAll("VARIABLE2", variables.get(1));
			}

			if (variables.size() == 1) {
				selects.put(variables.get(0), template);
			} else {
				selects.put(variables.get(1), template);
			}
			return true;
		} else {
			return false;
		}

	}

}
