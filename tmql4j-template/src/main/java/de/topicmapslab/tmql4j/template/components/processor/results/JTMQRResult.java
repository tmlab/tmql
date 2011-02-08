/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.processor.results;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.Result;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.template.grammar.lexical.JTMQR;

/**
 * @author Sven Krosse
 *
 */
public class JTMQRResult extends ResultSet<JTMQRValue> {
		
	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new JTMQRValue(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return JTMQR.TOKEN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if ( isEmpty()) {
			return "";
		}
		return first().first().toString();
	}

}

class JTMQRValue extends Result{
	/**
	 * @param parent
	 */
	public JTMQRValue(ResultSet<?> parent) {
		super(parent);
	}	
}