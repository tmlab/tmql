/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.where;

import de.topicmapslab.tmql4j.path.grammar.lexical.Or;

/**
 * @author Sven Krosse
 * 
 */
public class Disjunction extends Criteria {

	/**
	 * {@inheritDoc}
	 */
	protected String getBooleanOperator() {
		return Or.TOKEN;
	}

}
