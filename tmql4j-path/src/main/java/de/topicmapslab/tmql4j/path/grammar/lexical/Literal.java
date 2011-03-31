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
package de.topicmapslab.tmql4j.path.grammar.lexical;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.grammar.lexical.Token;
import de.topicmapslab.tmql4j.util.LiteralUtils;

public class Literal extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isToken(final ITMQLRuntime runtime, final String literal) {
		return LiteralUtils.isString(literal) || LiteralUtils.isDecimal(literal) || LiteralUtils.isInteger(literal) || LiteralUtils.isDateTime(literal) || LiteralUtils.isTime(literal)
				|| LiteralUtils.isDate(literal) || LiteralUtils.isBoolean(literal);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return "\"..\"";
	}

}
