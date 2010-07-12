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
package de.topicmapslab.tmql4j.interpreter.core.predefinition;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.java.tmapi.extension.impl.ExtendedTopicImpl;
import de.topicmapslab.java.tmapi.extension.impl.ExtendedTopicMapImpl;
import de.topicmapslab.java.tmapi.extension.model.base.ExtendedTopic;
import de.topicmapslab.java.tmapi.extension.model.base.ExtendedTopicMap;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;

/**
 * TMQL predefined Environment
 * <p>
 * The environment map contains background knowledge a processor will use in
 * addition to the context map. In many cases additional ontological knowledge
 * can be used to enrich the query processors' understanding of the application
 * domain.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Environment {

	/**
	 * the internal topic map containing all predefined topics
	 */
	private final ExtendedTopicMap map;

	/**
	 * the tmql-concept topic
	 */
	private ExtendedTopic tmql_concept;
	/**
	 * the tmql-ontology topic
	 */
	private ExtendedTopic tmql_ontology;
	/**
	 * the tmql-taxonometry topic
	 */
	private ExtendedTopic tmql_taxonometry;

	/**
	 * the occurrence type specifies the precedence of operators
	 */
	private ExtendedTopic tmql_occurrences_precedence;
	/**
	 * the occurrence type specifies the profile of operators
	 */
	private ExtendedTopic tmql_occurrences_profile;
	/**
	 * the occurrence type specifies the pattern of operators
	 */
	private ExtendedTopic tmql_occurrences_pattern;

	/**
	 * the topic type of all TMQL-functions
	 */
	private ExtendedTopic tmql_function;
	/**
	 * the topic type of all predicates
	 */
	private ExtendedTopic tmql_predicate;

	/**
	 * the topic type of all binary-operators
	 */
	private ExtendedTopic tmql_binary_operator;
	/**
	 * the topic type of all unary operators
	 */
	private ExtendedTopic tmql_unary_operator;
	/**
	 * topic type of all left-hand operators
	 */
	private ExtendedTopic tmql_prefix_operator;
	/**
	 * topic type of all right-hand operators
	 */
	private ExtendedTopic tmql_postfix_operator;

	/**
	 * the topic of the unary minus operator
	 */
	private ExtendedTopic tmql_decimal_unary_minus_prefix_operator;
	/**
	 * the topic of the binary add operator
	 */
	private ExtendedTopic tmql_decimal_binary_add_binary_operator;
	/**
	 * the topic of the binary minus operator
	 */
	private ExtendedTopic tmql_decimal_binary_minus_binary_operator;
	/**
	 * the topic of the binary multiplication operator
	 */
	private ExtendedTopic tmql_decimal_binary_mul_binary_operator;
	/**
	 * the topic of the binary division operator
	 */
	private ExtendedTopic tmql_decimal_binary_div_binary_operator;
	/**
	 * the topic of the binary modulo operator
	 */
	private ExtendedTopic tmql_decimal_binary_mod_binary_operator;
	/**
	 * the topic of the binary lower-than operator
	 */
	private ExtendedTopic tmql_decimal_binary_lt_binary_operator;
	/**
	 * the topic of the binary lower-equals operator
	 */
	private ExtendedTopic tmql_decimal_binary_le_binary_operator;
	/**
	 * the topic of the binary greater-than operator
	 */
	private ExtendedTopic tmql_decimal_binary_gt_binary_operator;
	/**
	 * the topic of the binary greater-equals operator
	 */
	private ExtendedTopic tmql_decimal_binary_ge_binary_operator;
	/**
	 * the topic of the binary string-concat operator
	 */
	private ExtendedTopic tmql_string_concat_binary_operator;
	/**
	 * the topic of the length function
	 */
	private ExtendedTopic tmql_string_length_function;
	/**
	 * the topic of the binary less-than string operator
	 */
	private ExtendedTopic tmql_string_less_than_binary_operator;
	/**
	 * the topic of the binary less-equal string operator
	 */
	private ExtendedTopic tmql_string_less_equal_than_binary_operator;
	/**
	 * the topic of the binary greater-equal string operator
	 */
	private ExtendedTopic tmql_string_greater_equal_than_binary_operator;
	/**
	 * the topic of the binary greater-than string operator
	 */
	private ExtendedTopic tmql_string_greater_than_binary_operator;
	/**
	 * the topic of the binary regular expression string operator
	 */
	private ExtendedTopic tmql_string_regexp_match_binary_operator;
	/**
	 * the topic of the has-data-type function
	 */
	private ExtendedTopic tmql_has_datatype_function;
	/**
	 * the topic of the has-variant function
	 */
	private ExtendedTopic tmql_has_variant_function;
	/**
	 * the topic of the slice function
	 */
	private ExtendedTopic tmql_slice_function;
	/**
	 * the topic of the count function
	 */
	private ExtendedTopic tmql_count_function;
	/**
	 * the topic of the unique function
	 */
	private ExtendedTopic tmql_uniq_function;
	/**
	 * the topic of the combination operator
	 */
	private ExtendedTopic tmql_concat_binary_operator;
	/**
	 * the topic of the except operator
	 */
	private ExtendedTopic tmql_except_binary_operator;
	/**
	 * the topic of the equality operator
	 */
	private ExtendedTopic tmql_compare_binary_operator;
	/**
	 * the topic of the zigzag function
	 */
	private ExtendedTopic tmql_zigzag_function;
	/**
	 * the topic of the zagzig function
	 */
	private ExtendedTopic tmql_zagzig_function;

	private ExtendedTopic tmql_datatype;
	/**
	 * topic type of each primitive data-type
	 */
	private ExtendedTopic tmql_primitive_datatype;
	/**
	 * topic of data-type undefined
	 */
	private ExtendedTopic tmql_datatype_undefined;
	/**
	 * topic of data-type boolean
	 */
	private ExtendedTopic tmql_datatype_boolean;
	/**
	 * topic of data-type integer
	 */
	private ExtendedTopic tmql_datatype_integer;
	/**
	 * topic of data-type decimal
	 */
	private ExtendedTopic tmql_datatype_decimal;
	/**
	 * topic of data-type anyIRI
	 */
	private ExtendedTopic tmql_datatype_iri;
	/**
	 * topic of data-type date
	 */
	private ExtendedTopic tmql_datatype_date;
	/**
	 * topic of data-type dateTime
	 */
	private ExtendedTopic tmql_datatype_dateTime;
	/**
	 * topic of data-type string
	 */
	private ExtendedTopic tmql_datatype_string;
	/**
	 * topic of data-type XML
	 */
	private ExtendedTopic tmql_datatype_xml;
	/**
	 * topic type of complex data-types
	 */
	private ExtendedTopic tmql_complex_datatype;
	/**
	 * topic of data-type tuple
	 */
	private ExtendedTopic tmql_datatype_tuple;
	/**
	 * topic of data-type sequence
	 */
	private ExtendedTopic tmql_datatype_tuple_sequence;

	/**
	 * topic of the only possible value of data-type undefined
	 */
	private ExtendedTopic tmql_topic_undef;

	/**
	 * prefix of each topic locator
	 */
	private final String tmql4j_qname = "http://de.topicmapslab/tmql4j/core/";

	/**
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param system
	 *            the internal {@link TopicMapSystem}
	 * @throws Exception
	 *             thrown if initialization failed
	 */
	public Environment(TMQLRuntime runtime, TopicMap environmentMap)
			throws Exception {
		map = new ExtendedTopicMapImpl(environmentMap);

		/*
		 * tmql-concept - "TMQL core concept"
		 */
		tmql_concept = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_concept"));
		if (tmql_concept == null) {
			tmql_concept = map.createTopic();
			tmql_concept.addSubjectIdentifier(map.createLocator(tmql4j_qname
					+ "tmql_concept"));
			tmql_concept.createName("TMQL core concept", new Topic[0]);
		}

		/*
		 * ontology isa tmql-concept - "Ontology"
		 */
		tmql_ontology = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_ontology"));
		if (tmql_ontology == null) {
			tmql_ontology = map.createTopic();
			tmql_ontology.addSubjectIdentifier(map.createLocator(tmql4j_qname
					+ "tmql_ontology"));
			tmql_ontology.createName("Ontology", new Topic[0]);
			tmql_ontology.isa(tmql_concept);

		}/*
		 * taxonometry isa tmql-concept - "Taxonometry"
		 */
		Topic tmql_taxonometry_ = (Topic) map.getConstructByItemIdentifier(map
				.createLocator(tmql4j_qname + "tmql_taxonometry"));
		if (tmql_taxonometry_ == null) {
			tmql_taxonometry = map.createTopic();
			try {
				tmql_taxonometry.addItemIdentifier(map
						.createLocator("taxonometry"));
			} catch (Exception e) {
				// ONTOPIA WORKAROUND
			}
		} else {
			tmql_taxonometry = new ExtendedTopicImpl(tmql_taxonometry_, runtime
					.getProperties().isTmdmTypeTransitiv());
		}
		tmql_taxonometry.createName("Taxonometry", new Topic[0]);
		tmql_taxonometry.isa(tmql_concept);

		/*
		 * the occurrences type of precedence of functions
		 */
		tmql_occurrences_precedence = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_occurrences_precedence"));
		if (tmql_occurrences_precedence == null) {
			tmql_occurrences_precedence = map.createTopic();
			tmql_occurrences_precedence
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_occurrences_precedence"));
			tmql_occurrences_precedence.createName("Precedence", new Topic[0]);

		}/*
		 * the occurrences type of profile of functions
		 */
		tmql_occurrences_profile = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_occurrences_profile"));
		if (tmql_occurrences_profile == null) {
			tmql_occurrences_profile = map.createTopic();
			tmql_occurrences_profile.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_occurrences_profile"));
			tmql_occurrences_profile.createName("Profile", new Topic[0]);

		}/*
		 * the occurrences type of pattern of functions
		 */
		tmql_occurrences_pattern = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_occurrences_pattern"));
		if (tmql_occurrences_pattern == null) {
			tmql_occurrences_pattern = map.createTopic();
			tmql_occurrences_pattern.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_occurrences_pattern"));
			tmql_occurrences_pattern.createName("Pattern", new Topic[0]);
		}
		createFunctionsAndOperatorsAsTopics();
		createDatatypesAsTopics();

	}

	/**
	 * Internal method to create the concepts of predefined environment which
	 * are "a kind of" or "a instance of" the concept function or its sub-types
	 * 
	 * @throws Exception
	 *             thrown if initialization failed
	 */
	private final void createFunctionsAndOperatorsAsTopics() throws Exception {

		/*
		 * function isa tmql-concept - "Function"
		 */
		tmql_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_function"));
		if (tmql_function == null) {
			tmql_function = map.createTopic();
			tmql_function.addSubjectIdentifier(map.createLocator(tmql4j_qname
					+ "tmql_function"));
			tmql_function.createName("Function", new Topic[0]);
			tmql_function.isa(tmql_concept);

		}/*
		 * predicate iko function - "Predicate"
		 */
		tmql_predicate = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_predicate"));
		if (tmql_predicate == null) {
			tmql_predicate = map.createTopic();
			tmql_predicate.addSubjectIdentifier(map.createLocator(tmql4j_qname
					+ "tmql_predicate"));
			tmql_predicate.createName("Predicate", new Topic[0]);
			tmql_predicate.iko(tmql_function);

		}/*
		 * binary-operator iko function - "Binary Operator"
		 */
		tmql_binary_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_binary_operator"));
		if (tmql_binary_operator == null) {
			tmql_binary_operator = map.createTopic();
			tmql_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_binary_operator"));
			tmql_binary_operator.createName("Binary Operator", new Topic[0]);
			tmql_binary_operator.iko(tmql_function);

		}/*
		 * unary-operator iko function - "Unary Operator"
		 */
		tmql_unary_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_unary_operator"));
		if (tmql_unary_operator == null) {
			tmql_unary_operator = map.createTopic();
			tmql_unary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_unary_operator"));
			tmql_unary_operator.createName("Unary Operator", new Topic[0]);
			tmql_unary_operator.iko(tmql_function);

		}/*
		 * prefix-operator iko unary-operator - "Prefix (unary) operator"
		 */
		tmql_prefix_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_prefix_operator"));
		if (tmql_prefix_operator == null) {
			tmql_prefix_operator = map.createTopic();
			tmql_prefix_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_prefix_operator"));
			tmql_prefix_operator.createName("Prefix (unary) operator",
					new Topic[0]);
			tmql_prefix_operator.iko(tmql_unary_operator);

		}/*
		 * postfix-operator iko unary-operator - "Postfix (unary) operator"
		 */
		tmql_postfix_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_postfix_operator"));
		if (tmql_postfix_operator == null) {
			tmql_postfix_operator = map.createTopic();
			tmql_postfix_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_postfix_operator"));
			tmql_postfix_operator.createName("Postfix (unary) operator",
					new Topic[0]);
			tmql_postfix_operator.iko(tmql_unary_operator);

		}/*
		 * for decimals
		 */
		/*
		 * decimal-unary-minus isa prefix-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-unary-minus - "-"
		 * 
		 * @ prefix-notation profile:
		 * "fn:minus-decimal (a : decimal) return decimal" precedence: 6
		 */
		tmql_decimal_unary_minus_prefix_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_unary_minus_prefix_operator"));
		if (tmql_decimal_unary_minus_prefix_operator == null) {
			tmql_decimal_unary_minus_prefix_operator = map.createTopic();
			tmql_decimal_unary_minus_prefix_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_unary_minus_prefix_operator"));
			tmql_decimal_unary_minus_prefix_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-unary-minus"));
			tmql_decimal_unary_minus_prefix_operator.addItemIdentifier(map
					.createLocator("fn:unary-minus-decimal"));
			tmql_decimal_unary_minus_prefix_operator.createName(
					"Decimal unary minus", new Topic[0]);
			tmql_decimal_unary_minus_prefix_operator.isa(tmql_prefix_operator);
			tmql_decimal_unary_minus_prefix_operator.createOccurrence(
					tmql_occurrences_precedence, "6", new Topic[0]);
			tmql_decimal_unary_minus_prefix_operator.createOccurrence(
					tmql_occurrences_pattern, "-", new Topic[0]);
			tmql_decimal_unary_minus_prefix_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:unary-minus-decimal (a : decimal) return decimal",
					new Topic[0]);

		}/*
		 * decimal-binary-add isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-add - "+"
		 * 
		 * @ infix-notation profile:
		 * "fn:add-decimal (a : decimal, b : decimal) return decimal"
		 * precedence: 2
		 */
		tmql_decimal_binary_add_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_add_binary_operator"));
		if (tmql_decimal_binary_add_binary_operator == null) {
			tmql_decimal_binary_add_binary_operator = map.createTopic();
			tmql_decimal_binary_add_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_add_binary_operator"));
			tmql_decimal_binary_add_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-add"));
			tmql_decimal_binary_add_binary_operator.addItemIdentifier(map
					.createLocator("fn:add-decimal"));
			tmql_decimal_binary_add_binary_operator.createName(
					"Decimal binary add", new Topic[0]);
			tmql_decimal_binary_add_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_add_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "2", new Topic[0]);
			tmql_decimal_binary_add_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "+", new Topic[0]);
			tmql_decimal_binary_add_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:add-decimal (a : decimal, b : decimal) return decimal",
					new Topic[0]);

		}/*
		 * decimal-binary-minus isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-minus -
		 * "-" @ infix-notation profile:
		 * "fn:minus-decimal (a : decimal, b : decimal) return decimal"
		 * precedence: 2
		 */
		tmql_decimal_binary_minus_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_minus_binary_operator"));
		if (tmql_decimal_binary_minus_binary_operator == null) {
			tmql_decimal_binary_minus_binary_operator = map.createTopic();
			tmql_decimal_binary_minus_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_minus_binary_operator"));
			tmql_decimal_binary_minus_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-minus"));
			tmql_decimal_binary_minus_binary_operator.addItemIdentifier(map
					.createLocator("fn:minus-decimal"));
			tmql_decimal_binary_minus_binary_operator.createName(
					"Decimal binary minus", new Topic[0]);
			tmql_decimal_binary_minus_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_minus_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "2", new Topic[0]);
			tmql_decimal_binary_minus_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "-", new Topic[0]);
			tmql_decimal_binary_minus_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:minus-decimal (a : decimal, b : decimal) return decimal",
							new Topic[0]);
		}/*
		 * decimal-binary-mul isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mul - "*"
		 * 
		 * @ infix-notation profile:
		 * "fn:mul-decimal (a : decimal, b : decimal) return decimal"
		 * precedence: 4
		 */
		tmql_decimal_binary_mul_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_mul_binary_operator"));
		if (tmql_decimal_binary_mul_binary_operator == null) {
			tmql_decimal_binary_mul_binary_operator = map.createTopic();
			tmql_decimal_binary_mul_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_mul_binary_operator"));
			tmql_decimal_binary_mul_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mul"));
			tmql_decimal_binary_mul_binary_operator.addItemIdentifier(map
					.createLocator("fn:mul-decimal"));
			tmql_decimal_binary_mul_binary_operator.createName(
					"Decimal binary multiplication", new Topic[0]);
			tmql_decimal_binary_mul_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_mul_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "4", new Topic[0]);
			tmql_decimal_binary_mul_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "*", new Topic[0]);
			tmql_decimal_binary_mul_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:mul-decimal (a : decimal, b : decimal) return decimal",
					new Topic[0]);

		}/*
		 * decimal-binary-div isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-div - "%"
		 * 
		 * @ infix-notation profile:
		 * "fn:div-decimal (a : decimal, b : decimal) return decimal"
		 * precedence: 4
		 */
		tmql_decimal_binary_div_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_div_binary_operator"));
		if (tmql_decimal_binary_div_binary_operator == null) {
			tmql_decimal_binary_div_binary_operator = map.createTopic();
			tmql_decimal_binary_div_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_div_binary_operator"));
			tmql_decimal_binary_div_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-div"));
			tmql_decimal_binary_div_binary_operator.addItemIdentifier(map
					.createLocator("fn:div-decimal"));
			tmql_decimal_binary_div_binary_operator.createName(
					"Decimal binary division", new Topic[0]);
			tmql_decimal_binary_div_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_div_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "4", new Topic[0]);
			tmql_decimal_binary_div_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "%", new Topic[0]);
			tmql_decimal_binary_div_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:div-decimal (a : decimal, b : decimal) return decimal",
					new Topic[0]);

		}/*
		 * decimal-binary-mod isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mod -
		 * "mod" @ infix-notation profile:
		 * "fn:mod-decimal (a : decimal, b : decimal) return decimal"
		 * precedence: 4
		 */
		tmql_decimal_binary_mod_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_mod_binary_operator"));
		if (tmql_decimal_binary_mod_binary_operator == null) {
			tmql_decimal_binary_mod_binary_operator = map.createTopic();
			tmql_decimal_binary_mod_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_mod_binary_operator"));
			tmql_decimal_binary_mod_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mod"));
			tmql_decimal_binary_mod_binary_operator.addItemIdentifier(map
					.createLocator("fn:mod-decimal"));
			tmql_decimal_binary_mod_binary_operator.createName(
					"Decimal binary modulo", new Topic[0]);
			tmql_decimal_binary_mod_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_mod_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "4", new Topic[0]);
			tmql_decimal_binary_mod_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "*", new Topic[0]);
			tmql_decimal_binary_mod_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:mod-decimal (a : decimal, b : decimal) return decimal",
					new Topic[0]);

		}/*
		 * decimal-binary-lt isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-lt - "<" @
		 * infix-notation profile:
		 * "fn:lt-decimal (a : decimal, b : decimal) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_decimal_binary_lt_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_lt_binary_operator"));
		if (tmql_decimal_binary_lt_binary_operator == null) {
			tmql_decimal_binary_lt_binary_operator = map.createTopic();
			tmql_decimal_binary_lt_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_lt_binary_operator"));
			tmql_decimal_binary_lt_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-lt"));
			tmql_decimal_binary_lt_binary_operator.addItemIdentifier(map
					.createLocator("fn:lt-decimal"));
			tmql_decimal_binary_lt_binary_operator.createName(
					"Decimal binary lower than", new Topic[0]);
			tmql_decimal_binary_lt_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_lt_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_decimal_binary_lt_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "<", new Topic[0]);
			tmql_decimal_binary_lt_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:lt-decimal (a : decimal, b : decimal) return tuple-sequence",
							new Topic[0]);

		}/*
		 * decimal-binary-le isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-le - "<="
		 * 
		 * @ infix-notation profile:
		 * "fn:leq-decimal (a : decimal, b : decimal) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_decimal_binary_le_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_le_binary_operator"));
		if (tmql_decimal_binary_le_binary_operator == null) {
			tmql_decimal_binary_le_binary_operator = map.createTopic();
			tmql_decimal_binary_le_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_le_binary_operator"));
			tmql_decimal_binary_le_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-le"));
			tmql_decimal_binary_le_binary_operator.addItemIdentifier(map
					.createLocator("fn:leq-decimal"));
			tmql_decimal_binary_le_binary_operator.createName(
					"Decimal binary lower euquals than", new Topic[0]);
			tmql_decimal_binary_le_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_le_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_decimal_binary_le_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "<=", new Topic[0]);
			tmql_decimal_binary_le_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:leq-decimal (a : decimal, b : decimal) return tuple-sequence",
							new Topic[0]);

		}/*
		 * decimal-binary-gt isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-gt - ">" @
		 * infix-notation profile:
		 * "fn:gt-decimal (a : decimal, b : decimal) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_decimal_binary_gt_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_gt_binary_operator"));
		if (tmql_decimal_binary_gt_binary_operator == null) {
			tmql_decimal_binary_gt_binary_operator = map.createTopic();
			tmql_decimal_binary_gt_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_gt_binary_operator"));
			tmql_decimal_binary_gt_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-gt"));
			tmql_decimal_binary_gt_binary_operator.addItemIdentifier(map
					.createLocator("fn:gt-decimal"));
			tmql_decimal_binary_gt_binary_operator.createName(
					"Decimal binary greater than", new Topic[0]);
			tmql_decimal_binary_gt_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_gt_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_decimal_binary_gt_binary_operator.createOccurrence(
					tmql_occurrences_pattern, ">", new Topic[0]);
			tmql_decimal_binary_gt_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:gt-decimal (a : decimal, b : decimal) return tuple-sequence",
							new Topic[0]);

		}/*
		 * decimal-binary-ge isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-ge - ">="
		 * 
		 * @ infix-notation profile:
		 * "fn:geq-decimal (a : decimal, b : decimal) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_decimal_binary_ge_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_decimal_binary_ge_binary_operator"));
		if (tmql_decimal_binary_ge_binary_operator == null) {
			tmql_decimal_binary_ge_binary_operator = map.createTopic();
			tmql_decimal_binary_ge_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_decimal_binary_ge_binary_operator"));
			tmql_decimal_binary_ge_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-ge"));
			tmql_decimal_binary_ge_binary_operator.addItemIdentifier(map
					.createLocator("fn:geq-decimal"));
			tmql_decimal_binary_ge_binary_operator.createName(
					"Decimal binary greater or quals than", new Topic[0]);
			tmql_decimal_binary_ge_binary_operator.isa(tmql_binary_operator);
			tmql_decimal_binary_ge_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_decimal_binary_ge_binary_operator.createOccurrence(
					tmql_occurrences_pattern, ">=", new Topic[0]);
			tmql_decimal_binary_ge_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:geq-decimal (a : decimal, b : decimal) return tuple-sequence",
							new Topic[0]);

		}/*
		 * for strings / }/* string-concat isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-concat - "+" @
		 * infix-notation profile:
		 * "fn:concat (a : string, b : string) return string" precedence: 2
		 */
		tmql_string_concat_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_concat_binary_operator"));
		if (tmql_string_concat_binary_operator == null) {
			tmql_string_concat_binary_operator = map.createTopic();
			tmql_string_concat_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_string_concat_binary_operator"));
			tmql_string_concat_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-concat"));
			tmql_string_concat_binary_operator.addItemIdentifier(map
					.createLocator("fn:string-concat"));
			tmql_string_concat_binary_operator.createName("String concat",
					new Topic[0]);
			tmql_string_concat_binary_operator.isa(tmql_binary_operator);
			tmql_string_concat_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "2", new Topic[0]);
			tmql_string_concat_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "+", new Topic[0]);
			tmql_string_concat_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:string-concat (a : string, b : string) return string",
					new Topic[0]);

		}/*
		 * string-length isa function
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-length profile:
		 * "fn:length (s : string) return integer"
		 */
		tmql_string_length_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_string_length_function"));
		if (tmql_string_length_function == null) {
			tmql_string_length_function = map.createTopic();
			tmql_string_length_function
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_string_length_function"));
			tmql_string_length_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-length"));
			tmql_string_length_function.addItemIdentifier(map
					.createLocator("fn:length"));
			tmql_string_length_function.createName("String length",
					new Topic[0]);
			tmql_string_length_function.isa(tmql_function);
			tmql_string_length_function.createOccurrence(
					tmql_occurrences_pattern, "+", new Topic[0]);
			tmql_string_length_function.createOccurrence(
					tmql_occurrences_profile,
					"fn:length (s : string) return integer", new Topic[0]);

		}/*
		 * string-less-than isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-less-than - "<" @
		 * infix-notation profile:
		 * "fn:string-lt (a : string, b : string) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_string_less_than_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_less_than_binary_operator"));
		if (tmql_string_less_than_binary_operator == null) {
			tmql_string_less_than_binary_operator = map.createTopic();
			tmql_string_less_than_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_string_less_than_binary_operator"));
			tmql_string_less_than_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-less-than"));
			tmql_string_less_than_binary_operator.addItemIdentifier(map
					.createLocator("fn:string-lt"));
			tmql_string_less_than_binary_operator.createName(
					"String less than", new Topic[0]);
			tmql_string_less_than_binary_operator.isa(tmql_binary_operator);
			tmql_string_less_than_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_string_less_than_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "<", new Topic[0]);
			tmql_string_less_than_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:string-lt (a : string, b : string) return tuple-sequence",
							new Topic[0]);

		}/*
		 * string-less-equal-than isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-less-equal-than -
		 * "<=" @ infix-notation profile:
		 * "fn:string-leq (a : string, b : string) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_string_less_equal_than_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_less_equal_than_binary_operator"));
		if (tmql_string_less_equal_than_binary_operator == null) {
			tmql_string_less_equal_than_binary_operator = map.createTopic();
			tmql_string_less_equal_than_binary_operator
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_string_less_equal_than_binary_operator"));
			tmql_string_less_equal_than_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-less-equal-than"));
			tmql_string_less_equal_than_binary_operator.addItemIdentifier(map
					.createLocator("fn:string-leq"));
			tmql_string_less_equal_than_binary_operator.createName(
					"String less equal than", new Topic[0]);
			tmql_string_less_equal_than_binary_operator
					.isa(tmql_binary_operator);
			tmql_string_less_equal_than_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_string_less_equal_than_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "<=", new Topic[0]);
			tmql_string_less_equal_than_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:string-leq (a : string, b : string) return tuple-sequence",
							new Topic[0]);

		}/*
		 * string-greater-equal-than isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-greater-equal-than
		 * - ">=" @ infix-notation profile:
		 * "fn:string-geq (a : string, b : string) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_string_greater_equal_than_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_greater_equal_than_binary_operator"));
		if (tmql_string_greater_equal_than_binary_operator == null) {
			tmql_string_greater_equal_than_binary_operator = map.createTopic();
			tmql_string_greater_equal_than_binary_operator
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_string_greater_equal_than_binary_operator"));
			tmql_string_greater_equal_than_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-greater-equal-than"));
			tmql_string_greater_equal_than_binary_operator
					.addItemIdentifier(map.createLocator("fn:string-geq"));
			tmql_string_greater_equal_than_binary_operator.createName(
					"String greater equal than", new Topic[0]);
			tmql_string_greater_equal_than_binary_operator
					.isa(tmql_binary_operator);
			tmql_string_greater_equal_than_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_string_greater_equal_than_binary_operator.createOccurrence(
					tmql_occurrences_pattern, ">=", new Topic[0]);
			tmql_string_greater_equal_than_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:string-geq (a : string, b : string) return tuple-sequence",
							new Topic[0]);

		}/*
		 * string-greater-than isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-greater-than - ">"
		 * 
		 * @ infix-notation profile:
		 * "fn:string-gt (a : string, b : string) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_string_greater_than_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_greater_than_binary_operator"));
		if (tmql_string_greater_than_binary_operator == null) {
			tmql_string_greater_than_binary_operator = map.createTopic();
			tmql_string_greater_than_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_string_greater_than_binary_operator"));
			tmql_string_greater_than_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-greater-than"));
			tmql_string_greater_than_binary_operator.addItemIdentifier(map
					.createLocator("fn:string-gt"));
			tmql_string_greater_than_binary_operator.createName(
					"String greater than", new Topic[0]);
			tmql_string_greater_than_binary_operator.isa(tmql_binary_operator);
			tmql_string_greater_than_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_string_greater_than_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "<=", new Topic[0]);
			tmql_string_greater_than_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:string-gt (a : string, b : string) return tuple-sequence",
							new Topic[0]);

		}/*
		 * string-regexp-match isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/string-regexp-match -
		 * "=~" @ infix-notation profile:
		 * "fn:regexp (s : string, re : string) return tuple-sequence"
		 * precedence: 5
		 */
		tmql_string_regexp_match_binary_operator = map
				.getTopicBySubjectIdentifier(map.createLocator(tmql4j_qname
						+ "tmql_string_regexp_match_binary_operator"));
		if (tmql_string_regexp_match_binary_operator == null) {
			tmql_string_regexp_match_binary_operator = map.createTopic();
			tmql_string_regexp_match_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_string_regexp_match_binary_operator"));
			tmql_string_regexp_match_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/string-regexp-match"));
			tmql_string_regexp_match_binary_operator.addItemIdentifier(map
					.createLocator("fn:regexp"));
			tmql_string_regexp_match_binary_operator.createName(
					"String regexp match", new Topic[0]);
			tmql_string_regexp_match_binary_operator.isa(tmql_binary_operator);
			tmql_string_regexp_match_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "5", new Topic[0]);
			tmql_string_regexp_match_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "=~", new Topic[0]);
			tmql_string_regexp_match_binary_operator.createOccurrence(
					tmql_occurrences_profile,
					"fn:regexp (a : string, b : string) return string",
					new Topic[0]);

		}/*
		 * for tuple sequences
		 */
		/*
		 * has-datatype isa function
		 * http://psi.topicmaps.org/tmql/1.0/functions/has-datatype profile:
		 * "fn:has-datatype (s: tuple-sequence) return tuple-sequence"
		 * description: """ Retrieves for each tuple element in each tuple the
		 * data type. For name items this is string, for occurrence item this is
		 * the data type (not the type) of the occurrence value. For atoms this
		 * is the data type of the atom. Each data type is a IRI. """
		 */
		tmql_has_datatype_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_has_datatype_function"));
		if (tmql_has_datatype_function == null) {
			tmql_has_datatype_function = map.createTopic();
			tmql_has_datatype_function
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_has_datatype_function"));
			tmql_has_datatype_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/has-datatype"));
			tmql_has_datatype_function.addItemIdentifier(map
					.createLocator("fn:has-datatype"));
			tmql_has_datatype_function.createName("has datatype", new Topic[0]);
			tmql_has_datatype_function.isa(tmql_function);
			tmql_has_datatype_function
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:has-datatype (s: tuple-sequence) return tuple-sequence",
							new Topic[0]);

		}/*
		 * has-variant isa function
		 * http://psi.topicmaps.org/tmql/1.0/functions/has-variant profile:
		 * "fn:has-variant (s: tuple-sequence, s: item-reference) return tuple-sequence"
		 * description: """ Retrieves for each tuple element in each tuple the
		 * variant name for the given scope. For name items this is the variant
		 * value, if such exists. Otherwise it is undef. For all other things
		 * the function will return undef. """
		 */
		tmql_has_variant_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_has_variant_function"));
		if (tmql_has_variant_function == null) {
			tmql_has_variant_function = map.createTopic();
			tmql_has_variant_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_has_variant_function"));
			tmql_has_variant_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/has-variant"));
			tmql_has_variant_function.addItemIdentifier(map
					.createLocator("fn:has-variant"));
			tmql_has_variant_function.createName("has variant", new Topic[0]);
			tmql_has_variant_function.isa(tmql_function);
			tmql_has_variant_function.createOccurrence(
					tmql_occurrences_profile,
					"fn:has-variant (s: tuple-sequence, s: item-reference",
					new Topic[0]);

		}/*
		 * slice isa function http://psi.topicmaps.org/tmql/1.0/functions/slice
		 * profile:
		 * "fn:slice (s : tuple-sequence, low : integer, high : integer) return tuple-sequence"
		 * description: "selects those tuples with index between low and high-1"
		 */
		tmql_slice_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_slice_function"));
		if (tmql_slice_function == null) {
			tmql_slice_function = map.createTopic();
			tmql_slice_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_slice_function"));
			tmql_slice_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/slice"));
			tmql_slice_function
					.addItemIdentifier(map.createLocator("fn:slice"));
			tmql_slice_function.createName("slice", new Topic[0]);
			tmql_slice_function.isa(tmql_function);
			tmql_slice_function
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:slice (s : tuple-sequence, low : integer, high : integer) return tuple-sequence",
							new Topic[0]);

		}/*
		 * count isa function http://psi.topicmaps.org/tmql/1.0/functions/count
		 * profile: "fn:count (s : tuple-sequence) return integer" description:
		 * "returns the number of tuples in the sequence"
		 */
		tmql_count_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_count_function"));
		if (tmql_count_function == null) {
			tmql_count_function = map.createTopic();
			tmql_count_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_count_function"));
			tmql_count_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/count"));
			tmql_count_function
					.addItemIdentifier(map.createLocator("fn:count"));
			tmql_count_function.createName("count", new Topic[0]);
			tmql_count_function.isa(tmql_function);
			tmql_count_function.createOccurrence(tmql_occurrences_profile,
					"fn:count (s : tuple-sequence) return integer",
					new Topic[0]);
		}/*
		 * uniq isa function http://psi.topicmaps.org/tmql/1.0/functions/uniq
		 * profile: "fn:uniq (s : tuple-sequence) return tuple-sequence"
		 * description:
		 * "returns a new tuple sequence with all duplicate tuples suppressed"
		 */
		tmql_uniq_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_uniq_function"));
		if (tmql_uniq_function == null) {
			tmql_uniq_function = map.createTopic();
			tmql_uniq_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_uniq_function"));
			tmql_uniq_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/uniq"));
			tmql_uniq_function.addItemIdentifier(map.createLocator("fn:uniq"));
			tmql_uniq_function.createName("uniq", new Topic[0]);
			tmql_uniq_function.isa(tmql_function);
			tmql_uniq_function.createOccurrence(tmql_occurrences_profile,
					"fn:uniq (s : tuple-sequence) return tuple-sequence",
					new Topic[0]);

		}/*
		 * concat isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/concat - "++" @
		 * infix-notation profile:
		 * "fn:concat (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
		 * description:
		 * "produces a tuple sequence with all tuples combined -- any ordering is honored"
		 * precedence: 1
		 */
		tmql_concat_binary_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_concat_binary_operator"));
		if (tmql_concat_binary_operator == null) {
			tmql_concat_binary_operator = map.createTopic();
			tmql_concat_binary_operator
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_concat_binary_operator"));
			tmql_concat_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/concat"));
			tmql_concat_binary_operator.addItemIdentifier(map
					.createLocator("fn:concat"));
			tmql_concat_binary_operator.createName("concat", new Topic[0]);
			tmql_concat_binary_operator.isa(tmql_binary_operator);
			tmql_concat_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "1", new Topic[0]);
			tmql_concat_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "++", new Topic[0]);
			tmql_concat_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:concat (s : tuple-sequence, t : tuple-sequence) return tuple-sequence",
							new Topic[0]);

		}/*
		 * except isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/except - "--" @
		 * infix-notation profile:
		 * "fn:except (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
		 * description:
		 * "produces a tuple sequence where all tuple which appear in t are removed from s"
		 * precedence: 1
		 */
		tmql_except_binary_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_except_binary_operator"));
		if (tmql_except_binary_operator == null) {
			tmql_except_binary_operator = map.createTopic();
			tmql_except_binary_operator
					.addSubjectIdentifier(map.createLocator(tmql4j_qname
							+ "tmql_except_binary_operator"));
			tmql_except_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/except"));
			tmql_except_binary_operator.addItemIdentifier(map
					.createLocator("fn:except"));
			tmql_except_binary_operator.createName("except", new Topic[0]);
			tmql_except_binary_operator.isa(tmql_binary_operator);
			tmql_except_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "1", new Topic[0]);
			tmql_except_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "--", new Topic[0]);
			tmql_except_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:except (s : tuple-sequence, t : tuple-sequence) return tuple-sequence",
							new Topic[0]);

		}/*
		 * compare isa binary-operator
		 * http://psi.topicmaps.org/tmql/1.0/functions/compare - "==" @
		 * infix-notation profile:
		 * "fn:compare (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
		 * description:
		 * "produces a tuple sequence of all tuples which appear in s and t"
		 * precedence: 1
		 */
		tmql_compare_binary_operator = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_compare_binary_operator"));
		if (tmql_compare_binary_operator == null) {
			tmql_compare_binary_operator = map.createTopic();
			tmql_compare_binary_operator.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_compare_binary_operator"));
			tmql_compare_binary_operator
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/compare"));
			tmql_compare_binary_operator.addItemIdentifier(map
					.createLocator("fn:compare"));
			tmql_compare_binary_operator.createName("compare", new Topic[0]);
			tmql_compare_binary_operator.isa(tmql_binary_operator);
			tmql_compare_binary_operator.createOccurrence(
					tmql_occurrences_precedence, "1", new Topic[0]);
			tmql_compare_binary_operator.createOccurrence(
					tmql_occurrences_pattern, "==", new Topic[0]);
			tmql_compare_binary_operator
					.createOccurrence(
							tmql_occurrences_profile,
							"fn:compare (s : tuple-sequence, t : tuple-sequence) return tuple-sequence",
							new Topic[0]);

		}/*
		 * zigzag isa function
		 * http://psi.topicmaps.org/tmql/1.0/functions/zigzag profile:
		 * "fn:zigzag (s : tuple-sequence) return tuple-sequence" description:
		 * """ returns a single tuple filled with all values from all tuples
		 * index of tuples run faster than index within the tuple sequence """
		 */
		tmql_zigzag_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_zigzag_function"));
		if (tmql_zigzag_function == null) {
			tmql_zigzag_function = map.createTopic();
			tmql_zigzag_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_zigzag_function"));
			tmql_zigzag_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/zigzag"));
			tmql_zigzag_function.addItemIdentifier(map
					.createLocator("fn:zigzag"));
			tmql_zigzag_function.createName("zigzag", new Topic[0]);
			tmql_zigzag_function.isa(tmql_function);
			tmql_zigzag_function.createOccurrence(tmql_occurrences_profile,
					"fn:zigzag (s : tuple-sequence) return tuple-sequence",
					new Topic[0]);

		}/*
		 * zagzig isa function
		 * http://psi.topicmaps.org/tmql/1.0/functions/zagzig profile:
		 * "fn:zagzig (s : tuple-sequence) return tuple-sequence" description:
		 * """ returns a single tuple filled with all values from all tuples
		 * index within the tuple sequence run faster than index within one
		 * tuple """
		 */
		tmql_zagzig_function = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_zagzig_function"));
		if (tmql_zagzig_function == null) {
			tmql_zagzig_function = map.createTopic();
			tmql_zagzig_function.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_zagzig_function"));
			tmql_zagzig_function
					.addSubjectIdentifier(map
							.createLocator("http://psi.topicmaps.org/tmql/1.0/functions/zagzig"));
			tmql_zagzig_function.addItemIdentifier(map
					.createLocator("fn:zagzig"));
			tmql_zagzig_function.createName("zagzig", new Topic[0]);
			tmql_zagzig_function.isa(tmql_function);
			tmql_zagzig_function.createOccurrence(tmql_occurrences_profile,
					"fn:zagzig (s : tuple-sequence) return tuple-sequence",
					new Topic[0]);
		}
	}

	/**
	 * Internal method to create the concepts of predefined environment which
	 * are "a kind of" or "a instance of" the concept data-type or its sub-types
	 * 
	 * @throws Exception
	 *             thrown if initialization failed
	 */
	private final void createDatatypesAsTopics() throws Exception {

		/**
		 * -- data types--
		 */

		/*
		 * datatype isa tmql-concept - "Data Type"
		 */
		tmql_datatype = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype"));
		if (tmql_datatype == null) {
			tmql_datatype = map.createTopic();
			tmql_datatype.addSubjectIdentifier(map.createLocator(tmql4j_qname
					+ "tmql_datatype"));
			tmql_datatype.createName("Data Type", new Topic[0]);
			tmql_datatype.isa(tmql_concept);

		}/*
		 * primitive-datatype iko datatype - "Primitive Data Type"
		 */
		tmql_primitive_datatype = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_primitive_datatype"));
		if (tmql_primitive_datatype == null) {
			tmql_primitive_datatype = map.createTopic();
			tmql_primitive_datatype.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_primitive_datatype"));
			tmql_primitive_datatype.createName("Primitive Data Type",
					new Topic[0]);
			tmql_primitive_datatype.iko(tmql_datatype);

		}/*
		 * undefined isa primitive-datatype - "Undefined Datatype" description:
		 * "has only a single value"
		 */
		tmql_datatype_undefined = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_undefined"));
		if (tmql_datatype_undefined == null) {
			tmql_datatype_undefined = map.createTopic();
			tmql_datatype_undefined.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_undefined"));
			tmql_datatype_undefined.createName("Undefined Data Type",
					new Topic[0]);
			tmql_datatype_undefined.isa(tmql_primitive_datatype);

			tmql_topic_undef = map.createTopic();
			tmql_topic_undef.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_datatype_undefined/undef"));
			tmql_topic_undef.createName("undef", new Topic[0]);
			tmql_topic_undef.isa(tmql_datatype_undefined);

		}/*
		 * boolean isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#boolean - "Boolean"
		 */
		tmql_datatype_boolean = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_boolean"));
		if (tmql_datatype_boolean == null) {
			tmql_datatype_boolean = map.createTopic();
			tmql_datatype_boolean.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_boolean"));
			tmql_datatype_boolean
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#boolean"));
			tmql_datatype_boolean.createName("Boolean", new Topic[0]);
			tmql_datatype_boolean.isa(tmql_primitive_datatype);

		}/*
		 * integer isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#integer -
		 * "Integer Number"
		 */
		tmql_datatype_integer = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_integer"));
		if (tmql_datatype_integer == null) {
			tmql_datatype_integer = map.createTopic();
			tmql_datatype_integer.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_integer"));
			tmql_datatype_integer
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#integer"));
			tmql_datatype_integer.createName("Integer Number", new Topic[0]);
			tmql_datatype_integer.isa(tmql_primitive_datatype);

		}/*
		 * decimal isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#decimal -
		 * "Decimal Number"
		 */
		tmql_datatype_decimal = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_decimal"));
		if (tmql_datatype_decimal == null) {
			tmql_datatype_decimal = map.createTopic();
			tmql_datatype_decimal.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_decimal"));
			tmql_datatype_decimal
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#decimal"));
			tmql_datatype_decimal.createName("Decimal Number", new Topic[0]);
			tmql_datatype_decimal.isa(tmql_primitive_datatype);

		}/*
		 * iri isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#anyURI - "IRI"
		 * description: "RFC 3987"
		 */
		tmql_datatype_iri = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_iri"));
		if (tmql_datatype_iri == null) {
			tmql_datatype_iri = map.createTopic();
			tmql_datatype_iri.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_iri"));
			tmql_datatype_iri
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#anyURI"));
			tmql_datatype_iri.createName("IRI", new Topic[0]);
			tmql_datatype_iri.isa(tmql_primitive_datatype);
		}/*
		 * date isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#date - "Date"
		 */
		tmql_datatype_date = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_date"));
		if (tmql_datatype_date == null) {
			tmql_datatype_date = map.createTopic();
			tmql_datatype_date.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_date"));
			tmql_datatype_date
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#date"));
			tmql_datatype_date.createName("Date", new Topic[0]);
			tmql_datatype_date.isa(tmql_primitive_datatype);
		}/*
		 * dateTime isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#dateTime - "DateTime"
		 */
		tmql_datatype_dateTime = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_dateTime"));
		if (tmql_datatype_dateTime == null) {
			tmql_datatype_dateTime = map.createTopic();
			tmql_datatype_dateTime.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_dateTime"));
			tmql_datatype_dateTime
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#dateTime"));
			tmql_datatype_dateTime.createName("DateTime", new Topic[0]);
			tmql_datatype_dateTime.isa(tmql_primitive_datatype);
		}/*
		 * string isa primitive-datatype
		 * http://www.w3.org/TR/xmlschema-2/datatypes.html#string - "String"
		 */
		tmql_datatype_string = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_string"));
		if (tmql_datatype_string == null) {
			tmql_datatype_string = map.createTopic();
			tmql_datatype_string.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_string"));
			tmql_datatype_string
					.addSubjectIdentifier(map
							.createLocator("http://www.w3.org/TR/xmlschema-2/datatypes.html#string"));
			tmql_datatype_string.createName("String", new Topic[0]);
			tmql_datatype_string.isa(tmql_primitive_datatype);
		}/*
		 * xml isa primitive-datatype - "XML Content"
		 */
		tmql_datatype_xml = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_xml"));
		if (tmql_datatype_xml == null) {
			tmql_datatype_xml = map.createTopic();
			tmql_datatype_xml.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_xml"));
			tmql_datatype_xml.createName("XML Content", new Topic[0]);
			tmql_datatype_xml.isa(tmql_primitive_datatype);
		}/*
		 * complex-datatype iko datatype - "Complex Data Type"
		 */
		tmql_complex_datatype = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_complex_datatype"));
		if (tmql_complex_datatype == null) {
			tmql_complex_datatype = map.createTopic();
			tmql_complex_datatype.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_complex_datatype"));
			tmql_complex_datatype.createName("Complex Data Type", new Topic[0]);
			tmql_complex_datatype.iko(tmql_datatype);
		}
		/*
		 * tuple isa complex-datatype - "Tuple Content" description:
		 * "ordered collection of primitive values"
		 */
		tmql_datatype_tuple = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_tuple"));
		if (tmql_datatype_tuple == null) {
			tmql_datatype_tuple = map.createTopic();
			tmql_datatype_tuple.addSubjectIdentifier(map
					.createLocator(tmql4j_qname + "tmql_datatype_tuple"));
			tmql_datatype_tuple.createName("Tuple Content", new Topic[0]);
			tmql_datatype_tuple.isa(tmql_complex_datatype);
		}
		/*
		 * tuple-sequence isa complex-datatype - "Tuple Sequence" description:
		 * "a sequence of tuples -- can be ordered, or not"
		 */
		tmql_datatype_tuple_sequence = map.getTopicBySubjectIdentifier(map
				.createLocator(tmql4j_qname + "tmql_datatype_tuple_sequence"));
		if (tmql_datatype_tuple_sequence == null) {
			tmql_datatype_tuple_sequence = map.createTopic();
			tmql_datatype_tuple_sequence.addSubjectIdentifier(map
					.createLocator(tmql4j_qname
							+ "tmql_datatype_tuple_sequence"));
			tmql_datatype_tuple_sequence.createName("Tuple Sequence",
					new Topic[0]);
			tmql_datatype_tuple_sequence.isa(tmql_complex_datatype);
		}
	}

	/**
	 * Method deliver the {@link TopicMap} which contains the predefined
	 * concepts of TMQL
	 * 
	 * @return the {@link TopicMap} which contains the predefined concepts of
	 *         TMQL
	 */
	public TopicMap getTopicMap() {
		return map;
	}

	/**
	 * tmql-concept <br />
	 * - "TMQL core concept"
	 */
	public ExtendedTopic getTmqlConcept() {
		return tmql_concept;
	}

	/**
	 * tmql-tanxonometry <br />
	 * - "TMQL tanxonometry"
	 */
	public ExtendedTopic getTmqlTaxonometry() {
		return tmql_taxonometry;
	}

	/**
	 * function isa tmql-concept <br />
	 * - "Function"
	 */
	public ExtendedTopic getTmqlFunction() {
		return tmql_function;
	}

	/**
	 * ontology isa tmql-concept <br />
	 * - "Ontology"
	 */
	public ExtendedTopic getTmqlOntology() {
		return tmql_ontology;
	}

	/**
	 * type of occurrence precedence of operators
	 */
	public ExtendedTopic getTmqlOccurrencesPrecedence() {
		return tmql_occurrences_precedence;
	}

	/**
	 * type of occurrence profile of functions
	 */
	public ExtendedTopic getTmqlOccurrencesProfile() {
		return tmql_occurrences_profile;
	}

	/**
	 * type of occurrence pattern or symbol of operators
	 */
	public ExtendedTopic getTmqlOccurrencesPattern() {
		return tmql_occurrences_pattern;
	}

	/**
	 * predicate iko function <br />
	 * - "Predicate"
	 */
	public ExtendedTopic getTmqlPredicate() {
		return tmql_predicate;
	}

	/**
	 * binary-operator iko function <br />
	 * - "Binary Operator" <br />
	 * every binary operator has a precedence value <br />
	 * the higher the precedence, the closer it binds the operands <br />
	 * left and right
	 */
	public ExtendedTopic getTmqlBinaryOperator() {
		return tmql_binary_operator;
	}

	/**
	 * unary-operator iko function <br />
	 * - "Unary Operator"
	 * 
	 * <br />
	 * unary prefix operators bind strongest
	 */
	public ExtendedTopic getTmqlUnaryOperator() {
		return tmql_unary_operator;
	}

	/**
	 * prefix-operator iko unary-operator <br />
	 * - "Prefix (unary) operator"
	 */
	public ExtendedTopic getTmqlPrefixOperator() {
		return tmql_prefix_operator;
	}

	/**
	 * postfix-operator iko unary-operator <br />
	 * - "Postfix (unary) operator"
	 */
	public ExtendedTopic getTmqlPostfixOperator() {
		return tmql_postfix_operator;
	}

	/**
	 * decimal-unary-minus isa prefix-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-unary-minus<br />
	 * - "-" @ prefix-notation<br />
	 * profile: "fn:minus-decimal (a : decimal) return decimal"<br />
	 * precedence: 6
	 */
	public ExtendedTopic getTmqlDecimalUnaryMinusPrefixOperator() {
		return tmql_decimal_unary_minus_prefix_operator;
	}

	/**
	 * decimal-binary-add isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-add<br />
	 * - "+" @ infix-notation<br />
	 * profile: "fn:add-decimal (a : decimal, b : decimal) return decimal"<br />
	 * precedence: 2
	 */
	public ExtendedTopic getTmqlDecimalBinaryAddBinaryOperator() {
		return tmql_decimal_binary_add_binary_operator;
	}

	/**
	 * decimal-binary-minus isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-minus<br />
	 * - "-" @ infix-notation<br />
	 * profile: "fn:minus-decimal (a : decimal, b : decimal) return decimal"<br />
	 * precedence: 2
	 */
	public ExtendedTopic getTmqlDecimalBinaryMinusBinaryOperator() {
		return tmql_decimal_binary_minus_binary_operator;
	}

	/**
	 * decimal-binary-mul isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mul<br />
	 * - "*" @ infix-notation<br />
	 * profile: "fn:mul-decimal (a : decimal, b : decimal) return decimal"<br />
	 * precedence: 4
	 */
	public ExtendedTopic getTmqlDecimalBinaryMulBinaryOperator() {
		return tmql_decimal_binary_mul_binary_operator;
	}

	/**
	 * decimal-binary-div isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-div<br />
	 * - "%" @ infix-notation<br />
	 * profile: "fn:div-decimal (a : decimal, b : decimal) return decimal"<br />
	 * precedence: 4
	 */
	public ExtendedTopic getTmqlDecimalBinaryDivBinaryOperator() {
		return tmql_decimal_binary_div_binary_operator;
	}

	/**
	 * decimal-binary-mod isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-mod<br />
	 * - "mod" @ infix-notation<br />
	 * profile: "fn:mod-decimal (a : decimal, b : decimal) return decimal"<br />
	 * precedence: 4
	 */
	public ExtendedTopic getTmqlDecimalBinaryModBinaryOperator() {
		return tmql_decimal_binary_mod_binary_operator;
	}

/**
	 * decimal-binary-lt isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-lt<br />
	 * - "<" @ infix-notation<br />
	 * profile: "fn:lt-decimal (a : decimal, b : decimal) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlDecimalBinaryLtBinaryOperator() {
		return tmql_decimal_binary_lt_binary_operator;
	}

	/**
	 * decimal-binary-le isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-le<br />
	 * - "<=" @ infix-notation<br />
	 * profile:
	 * "fn:leq-decimal (a : decimal, b : decimal) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlDecimalBinaryLeBinaryOperator() {
		return tmql_decimal_binary_le_binary_operator;
	}

	/**
	 * decimal-binary-gt isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-gt<br />
	 * - ">" @ infix-notation<br />
	 * profile: "fn:gt-decimal (a : decimal, b : decimal) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlDecimalBinaryGtBinaryOperator() {
		return tmql_decimal_binary_gt_binary_operator;
	}

	/**
	 * decimal-binary-ge isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/decimal-binary-ge<br />
	 * - ">=" @ infix-notation<br />
	 * profile:
	 * "fn:geq-decimal (a : decimal, b : decimal) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlDecimalBinaryGeBinaryOperator() {
		return tmql_decimal_binary_ge_binary_operator;
	}

	/**
	 * string-concat isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-concat<br />
	 * - "+" @ infix-notation<br />
	 * profile: "fn:concat (a : string, b : string) return string"<br />
	 * precedence: 2
	 */
	public ExtendedTopic getTmqlStringConcatBinaryOperator() {
		return tmql_string_concat_binary_operator;
	}

	/**
	 * string-length isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-length<br />
	 * profile: "fn:length (s : string) return integer"
	 */
	public ExtendedTopic getTmqlStringLengthFunction() {
		return tmql_string_length_function;
	}

/**
	 * string-less-than isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-less-than<br />
	 * - "<" @ infix-notation<br />
	 * profile: "fn:string-lt (a : string, b : string) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlStringLessThanBinaryOperator() {
		return tmql_string_less_than_binary_operator;
	}

	/**
	 * string-less-equal-than isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-less-equal-than<br />
	 * - "<=" @ infix-notation<br />
	 * profile: "fn:string-leq (a : string, b : string) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlStringLessEqualThanBinaryOperator() {
		return tmql_string_less_equal_than_binary_operator;
	}

	/**
	 * string-greater-equal-than isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-greater-equal-than<br />
	 * - ">=" @ infix-notation<br />
	 * profile: "fn:string-geq (a : string, b : string) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlStringGreaterEqualThanBinaryOperator() {
		return tmql_string_greater_equal_than_binary_operator;
	}

	/**
	 * string-greater-than isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-greater-than<br />
	 * - ">" @ infix-notation<br />
	 * profile: "fn:string-gt (a : string, b : string) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlStringGreaterThanBinaryOperator() {
		return tmql_string_greater_than_binary_operator;
	}

	/**
	 * string-regexp-match isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/string-regexp-match<br />
	 * - "=~" @ infix-notation<br />
	 * profile: "fn:regexp (s : string, re : string) return tuple-sequence"<br />
	 * precedence: 5
	 */
	public ExtendedTopic getTmqlStringRegexpMatchBinaryOperator() {
		return tmql_string_regexp_match_binary_operator;
	}

	/**
	 * has-datatype isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/has-datatype<br />
	 * profile: "fn:has-datatype (s: tuple-sequence) return tuple-sequence"<br />
	 * description: """ Retrieves for each tuple element in each tuple the data
	 * type. For name items this is string, for occurrence item this is the data
	 * type (not the type) of the occurrence value. For atoms this is the data
	 * type of the atom. Each data type is a IRI. """
	 */
	public ExtendedTopic getTmqlHasDatatypeFunction() {
		return tmql_has_datatype_function;
	}

	/**
	 * has-variant isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/has-variant<br />
	 * profile:
	 * "fn:has-variant (s: tuple-sequence, s: item-reference) return tuple-sequence"
	 * <br />
	 * description: """ Retrieves for each tuple element in each tuple the
	 * variant name for the given scope. For name items this is the variant
	 * value, if such exists. Otherwise it is undef. For all other things the
	 * function will return undef. """
	 */
	public ExtendedTopic getTmqlHasVariantFunction() {
		return tmql_has_variant_function;
	}

	/**
	 * slice isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/slice<br />
	 * profile:"fn:slice (s : tuple-sequence, low : integer, high : integer) return tuple-sequence"
	 * <br />
	 * description: "selects those tuples with index between low and high-1"
	 */
	public ExtendedTopic getTmqlSliceFunction() {
		return tmql_slice_function;
	}

	/**
	 * count isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/count<br />
	 * profile: "fn:count (s : tuple-sequence) return integer"<br />
	 * description: "returns the number of tuples in the sequence"
	 */
	public ExtendedTopic getTmqlCountFunction() {
		return tmql_count_function;
	}

	/**
	 * uniq isa function<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/uniq<br />
	 * profile: "fn:uniq (s : tuple-sequence) return tuple-sequence"<br />
	 * description:
	 * "returns a new tuple sequence with all duplicate tuples suppressed"
	 */
	public ExtendedTopic getTmqlUniqFunction() {
		return tmql_uniq_function;
	}

	/**
	 * concat isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/concat<br />
	 * - "++" @ infix-notation<br />
	 * profile:
	 * "fn:concat (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
	 * <br />
	 * description:
	 * "produces a tuple sequence with all tuples combined -- any ordering is honored"
	 * <br />
	 * precedence: 1
	 */
	public ExtendedTopic getTmqlConcatBinaryOperator() {
		return tmql_concat_binary_operator;
	}

	/**
	 * except isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/except<br />
	 * - "--" @ infix-notation<br />
	 * profile:
	 * "fn:except (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
	 * <br />
	 * description:
	 * "produces a tuple sequence where all tuple which appear in t are removed from s"
	 * <br />
	 * precedence: 1
	 */
	public ExtendedTopic getTmqlExceptBinaryOperator() {
		return tmql_except_binary_operator;
	}

	/**
	 * compare isa binary-operator<br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/compare<br />
	 * - "==" @ infix-notation<br />
	 * profile:
	 * "fn:compare (s : tuple-sequence, t : tuple-sequence) return tuple-sequence"
	 * <br />
	 * description:
	 * "produces a tuple sequence of all tuples which appear in s and t"<br />
	 * precedence: 1
	 */
	public ExtendedTopic getTmqlCompareBinaryOperator() {
		return tmql_compare_binary_operator;
	}

	/**
	 * zigzag isa function <br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/zigzag <br />
	 * profile: "fn:zigzag (s : tuple-sequence) return tuple-sequence" <br />
	 * description: """ returns a single tuple filled with all values from all
	 * tuples index of tuples run faster than index within the tuple sequence
	 * """
	 */
	public ExtendedTopic getTmqlZigzagFunction() {
		return tmql_zigzag_function;
	}

	/**
	 * zagzig isa function <br />
	 * http://psi.topicmaps.org/tmql/1.0/functions/zagzig <br />
	 * profile: "fn:zagzig (s : tuple-sequence) return tuple-sequence" <br />
	 * description: """ returns a single tuple filled with all values from all
	 * tuples index within the tuple sequence run faster than index within one
	 * tuple """
	 */
	public ExtendedTopic getTmqlZagzigFunction() {
		return tmql_zagzig_function;
	}

	/**
	 * datatype isa tmql-concept <br />
	 * - "Data Type"
	 */
	public ExtendedTopic getTmqlDatatype() {
		return tmql_datatype;
	}

	/**
	 * primitive-datatype iko datatype <br />
	 * - "Primitive Data Type"
	 */
	public ExtendedTopic getTmqlPrimitiveDatatype() {
		return tmql_primitive_datatype;
	}

	/**
	 * undefined isa primitive-datatype <br />
	 * - "Undefined Datatype" <br />
	 * description: "has only a single value"
	 */
	public ExtendedTopic getTmqlDatatypeUndefined() {
		return tmql_datatype_undefined;
	}

	/**
	 * boolean isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#boolean <br />
	 * - "Boolean"
	 */
	public ExtendedTopic getTmqlDatatypeBoolean() {
		return tmql_datatype_boolean;
	}

	/**
	 * integer isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#integer <br />
	 * - "Integer Number"
	 */
	public ExtendedTopic getTmqlDatatypeInteger() {
		return tmql_datatype_integer;
	}

	/**
	 * decimal isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#decimal <br />
	 * - "Decimal Number"
	 */
	public ExtendedTopic getTmqlDatatypeDecimal() {
		return tmql_datatype_decimal;
	}

	/**
	 * iri isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#anyURI <br />
	 * - "IRI" <br />
	 * description: "RFC 3987"
	 */
	public ExtendedTopic getTmqlDatatypeIri() {
		return tmql_datatype_iri;
	}

	/**
	 * date isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#date <br />
	 * - "Date"
	 */
	public ExtendedTopic getTmqlDatatypeDate() {
		return tmql_datatype_date;
	}

	/**
	 * dateTime isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#dateTime <br />
	 * - "DateTime"
	 */
	public ExtendedTopic getTmqlDatatypeDateTime() {
		return tmql_datatype_dateTime;
	}

	/**
	 * string isa primitive-datatype
	 * http://www.w3.org/TR/xmlschema-2/datatypes.html#string <br />
	 * - "String"
	 */
	public ExtendedTopic getTmqlDatatypeString() {
		return tmql_datatype_string;
	}

	/**
	 * xml isa primitive-datatype <br />
	 * - "XML Content"
	 */
	public ExtendedTopic getTmqlDatatypeXml() {
		return tmql_datatype_xml;
	}

	/**
	 * complex-datatype iko datatype <br />
	 * - "Complex Data Type"
	 */
	public ExtendedTopic getTmqlComplexDatatype() {
		return tmql_complex_datatype;
	}

	/**
	 * tuple isa complex-datatype <br />
	 * - "Tuple Content" <br />
	 * description: "ordered collection of primitive values"
	 */
	public ExtendedTopic getTmqlDatatypeTuple() {
		return tmql_datatype_tuple;
	}

	/**
	 * tuple-sequence isa complex-datatype <br />
	 * - "Tuple Sequence" <br />
	 * description: "a sequence of tuples -- can be ordered, or not"
	 */
	public ExtendedTopic getTmqlDatatypeTupleSequence() {
		return tmql_datatype_tuple_sequence;
	}

	/**
	 * Topic representing the only value of datattype undefinded (
	 * {@link #getTmqlDatatypeUndefined()})
	 */
	public ExtendedTopic getTmqlTopicUndef() {
		return tmql_topic_undef;
	}

}
