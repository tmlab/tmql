/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.tests;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import javax.management.Query;

import junit.framework.Assert;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis.AxisTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis.IndicatorsAxisTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis.InstancesAxisTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.StateImpl;

/**
 * @author Sven Krosse
 *
 */
public class TestAxisTranslation extends Tmql4JTestCase {

	private IState state = new StateImpl(State.TOPIC, "id");
	
	@Test
	public void testInstancesAxisTranslator() throws Exception {
		String expected = MessageFormat.format(IndicatorsAxisTranslator.FORWARD, "id");
		testAxisTranslator(new IndicatorsAxisTranslator(), toExpression(new AxisIndicators(), true), expected);
	}
	
	@Test
	public void testname() throws Exception {
		execute("// tm:subject >> characteristics >> scope");
	}
	
	public IExpression toExpression(IToken token, boolean forward){
		List<Class<? extends IToken>> tmqlTokens = new LinkedList<Class<? extends IToken>>();
		tmqlTokens.add(forward?MoveForward.class: MoveBackward.class);
		tmqlTokens.add(token.getClass());
		
		List<String> tokens = new LinkedList<String>();
		tokens.add(forward?MoveForward.TOKEN: MoveBackward.TOKEN);
		tokens.add(token.getLiteral());
		
		return new Step(null, tmqlTokens, tokens, runtime);
	}
	
	public void testAxisTranslator(AxisTranslatorImpl translator, IExpression expression, String expected) throws Exception{
		IContext context = new Context(runtime.getTmqlProcessor(), new TMQLQuery(topicMap, ""));
		final String actual = translator.transform(runtime, context, expression, state).getInnerContext();
		Assert.assertEquals(expected, actual);
	}
	
}
