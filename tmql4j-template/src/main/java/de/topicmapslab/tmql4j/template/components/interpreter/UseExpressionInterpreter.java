/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.interpreter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.ctm.writer.core.CTMTopicMapWriter;
import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.ProjectionUtils;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.template.components.processor.results.CTMResult;
import de.topicmapslab.tmql4j.template.components.processor.results.JTMQRResult;
import de.topicmapslab.tmql4j.template.components.processor.results.TemplateResult;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.Template;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.TemplateManager;
import de.topicmapslab.tmql4j.template.grammar.lexical.CTM;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQR;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQROS;
import de.topicmapslab.tmql4j.template.grammar.productions.UseExpression;
import de.topicmapslab.tmql4j.template.util.json.JTMQRWriter;
import de.topicmapslab.tmql4j.util.HashUtil;
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
		/*
		 * use JTMQROS
		 */
		else if (JTMQROS.class.equals(token)) {
			return useJTMQROS(runtime, context, optionalArguments);
		}
		/*
		 * use CTM
		 */
		else if (CTM.class.equals(token)) {
			return useCTM(runtime, context, optionalArguments);
		}
		throw new TMQLRuntimeException(MessageFormat.format(THE_USE_PART_IS_UNKNOWN, getTokens().get(1)));

	}

	/**
	 * The method extracts the values from context and create a CTM for the
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
	private QueryMatches useCTM(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		context.getTmqlProcessor().getResultProcessor().setResultType(CTMResult.class);
		/*
		 * extract values
		 */
		if (context.getContextBindings() != null) {
			List<Construct> results = HashUtil.getList();
			for (Map<String, Object> tuple : context.getContextBindings()) {
				for (Object obj : tuple.values()) {
					if (obj instanceof Topic || obj instanceof Association) {
						if (!results.contains(obj)) {
							results.add((Construct) obj);
						}
					} else {
						throw new TMQLRuntimeException("The results cannot be convert to CTM because of invalid types. Only topics and associations are supported!");
					}
				}
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				new CTMTopicMapWriter(os, context.getQuery().getTopicMap().getLocator().getReference()).write(results);
			} catch (IOException e) {
				throw new TMQLRuntimeException("Transformation to CTM failed!", e);
			}
			return QueryMatches.asQueryMatchNS(runtime, os.toString());
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * The method extracts the values from context and create a JTMQROS for the
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
	private QueryMatches useJTMQROS(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getOutputStream() == null) {
			throw new TMQLRuntimeException("Missing outputstream to use JTMQROS");
		}
		/*
		 * fill result
		 */
		if (context.getContextBindings() != null) {
			JTMQRWriter.write(context.getOutputStream(), context.getContextBindings());
		} else {
			JTMQRWriter.write(context.getOutputStream(), QueryMatches.emptyMatches());
		}
		return QueryMatches.emptyMatches();
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
		context.getTmqlProcessor().getResultProcessor().setResultType(JTMQRResult.class);
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
		context.getTmqlProcessor().getResultProcessor().setResultType(TemplateResult.class);
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
