package de.topicmapslab.tmql4j.draft2010.components.interpreter.literals;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.literals.IriReference;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of production 'IRI-reference' (
 * {@link IriReference} ).
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class IriReferenceInterpreter extends ExpressionInterpreterImpl<IriReference> {
	/**
	 * the logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * convert literal
		 */
		String identifier = LiteralUtils.asString(getTokens().get(0));
		Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, identifier);
		if (construct == null) {
			try {
				return QueryMatches.asQueryMatchNS(runtime, new URI(identifier));
			} catch (URISyntaxException ex) {
				logger.warn("Given string reference cannot be transformed to valid IRI or construct");
				return QueryMatches.emptyMatches();
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, construct);
	}

}
