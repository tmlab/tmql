/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.grammar.functions.url;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tmapi.core.Locator;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class UrlEncodeFunction extends FunctionImpl {

	/**
	 * {@inheritDoc}
	 */
	public de.topicmapslab.tmql4j.components.processor.core.QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);

		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 1 parameter.");
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
				List<String> locs = HashUtil.getList();
				/*
				 * add escape IRI of each locator
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
							locs.add(URLEncoder.encode(obj.toString(), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				result.put(QueryMatches.getNonScopedVariable(), locs);
			}
			/*
			 * add escape IRI of the locator
			 */
			else {
				String value = null;
				if (sequence instanceof String) {
					value = sequence.toString();
				} else if (sequence instanceof Locator) {
					value = ((Locator) sequence).getReference();
				} else if (sequence instanceof URI) {
					value = ((URI) sequence).toASCIIString();
				}
				if (value != null) {
					try {
						result.put(QueryMatches.getNonScopedVariable(), URLEncoder.encode(value, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

			}
			results.add(result);
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:url-encode";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}

}
