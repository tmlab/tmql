/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.interpreter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.ProjectionUtils;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.template.components.processor.results.JTMQRResult;
import de.topicmapslab.tmql4j.template.components.processor.results.TemplateResult;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.Template;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.TemplateManager;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQR;
import de.topicmapslab.tmql4j.template.grammar.productions.UseExpression;
import de.topicmapslab.tmql4j.template.util.json.JTMQRWriter;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class UseExpressionInterpreter extends ExpressionInterpreterImpl<UseExpression> {

	/**
	 * 
	 */
	private static final String MISSING_TEMPLATE = "Missing template {0}.";
	/**
	 * 
	 */
	private static final String THE_USE_PART_IS_UNKNOWN = "The use part {0} is unknown!";

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the use-expression
	 */
	public UseExpressionInterpreter(UseExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Class<? extends IToken> token = getTmqlTokens().get(1);
		/*
		 * use template
		 */
		if (de.topicmapslab.tmql4j.template.grammar.lexical.Template.class.equals(token)) {
			return useTemplate(runtime, context, optionalArguments);
		}
		/*
		 * use JTM QR
		 */
		else if (JTMQR.class.equals(token)) {
			return useJTMQR(runtime, context, optionalArguments);
		}

		throw new TMQLRuntimeException(THE_USE_PART_IS_UNKNOWN);

	}

	/**
	 * The method extracts the values from context and create a JTMQR for the
	 * whole sequence
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the JTMQR
	 */
	private QueryMatches useJTMQR(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		runtime.getTmqlProcessor().getResultProcessor().setResultType(JTMQRResult.class);
		/*
		 * fill template
		 */
		if (context.getContextBindings() != null) {			
			String json = JTMQRWriter.write(context.getContextBindings());
			return QueryMatches.asQueryMatchNS(runtime, json);
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * The method extracts the values from context and create one template
	 * snippet for each tuple
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the template snippets
	 */
	private QueryMatches useTemplate(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		runtime.getTmqlProcessor().getResultProcessor().setResultType(TemplateResult.class);
		/*
		 * extract template name
		 */
		final String templateName;
		if (getTmqlTokens().size() == 2) {
			templateName = Template.ANONYMOUS;
		} else {
			templateName = LiteralUtils.asString(getTokens().get(3));
		}
		/*
		 * get template by name
		 */
		Template template = TemplateManager.getTemplateManager().getTemplate(context.getQuery().getTopicMap(), templateName);
		if (template == null) {
			throw new TMQLRuntimeException(MessageFormat.format(MISSING_TEMPLATE, templateName));
		}
		/*
		 * fill template
		 */
		if (context.getContextBindings() != null) {
			List<String> values = new ArrayList<String>();
			for (Map<String, Object> tuple : ProjectionUtils.asTwoDimensionalMap(context.getContextBindings())) {
				Context newContext = new Context(context);
				newContext.setCurrentTuple(tuple);
				values.add(template.use(runtime, newContext, optionalArguments));
			}
			if (!values.isEmpty()) {
				return QueryMatches.asQueryMatchNS(runtime, values);
			}
		}
		return QueryMatches.emptyMatches();
	}

}
