package de.topicmapslab.tmql4j.draft2010.interpreter.literals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.IriReference;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

/**
 * Interpreter implementation of production 'IRI-reference' (
 * {@link IriReference} ).
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IriReferenceInterpreter extends
		ExpressionInterpreterImpl<IriReference> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public IriReferenceInterpreter(IriReference ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * convert literal
		 */
		final String literal = getTokens().get(0);

		Object reference;
		/*
		 * try to transform reference to topic map construct
		 */
		try {
			TopicMap topicMap = (TopicMap) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.CURRENT_MAP);
			reference = (Topic) runtime.getDataBridge().getConstructResolver()
					.getConstructByIdentifier(runtime, literal, topicMap);
		} catch (Exception e) {
			/*
			 * try to transform reference as IRI instance
			 */
			try {
				reference = new URI(literal);
			} catch (URISyntaxException ex) {
				throw new TMQLRuntimeException("Literal '" + literal
						+ "' is invalid iri-reference.", ex);
			}
		}

		/*
		 * convert result type to query-match
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(QueryMatches.getNonScopedVariable(), reference);
		QueryMatches results = new QueryMatches(runtime);
		results.add(tuple);

		/*
		 * store result at the variable layer
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

}
