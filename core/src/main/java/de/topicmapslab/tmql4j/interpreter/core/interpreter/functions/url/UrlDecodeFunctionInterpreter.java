/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.url;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Locator;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

/**
 * @author Sven Krosse
 * 
 */
public class UrlDecodeFunctionInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * @param ex
	 */
	public UrlDecodeFunctionInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);

		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres 1 parameter.");
		}

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object sequence = tuple.get("$0");
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * check if value is a sequence
			 */
			if (sequence instanceof Collection<?>) {
				ITupleSequence<String> locs = runtime.getProperties()
						.newSequence();
				/*
				 * decode IRI of each locator
				 */
				for (Object obj : (Collection<?>) sequence) {
					String value = null;
					if (obj instanceof String) {
						value = obj.toString();
					} else if (obj instanceof Locator) {
						value = ((Locator) obj).getReference();
					} else if (obj instanceof URI) {
						value = ((URI) obj).toASCIIString();
					}
					if (value != null) {
						try {
							locs
									.add(URLDecoder.decode(obj.toString(),
											"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				result.put(QueryMatches.getNonScopedVariable(), locs);
			}
			/*
			 * decode IRI of the locator
			 */
			else {
				try {
					String value = null;
					if (sequence instanceof String) {
						value = sequence.toString();
					} else if (sequence instanceof Locator) {
						value = ((Locator) sequence).getReference();
					} else if (sequence instanceof URI) {
						value = ((URI) sequence).toASCIIString();
					}
					if (value != null) {
						result
								.put(QueryMatches.getNonScopedVariable(),
										URLDecoder.decode(value,
												"UTF-8"));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			results.add(result);
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:url-decode";
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}

}
