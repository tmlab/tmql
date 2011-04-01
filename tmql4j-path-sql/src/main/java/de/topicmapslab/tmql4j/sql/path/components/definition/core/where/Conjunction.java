/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.where;

import de.topicmapslab.tmql4j.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriterion;

/**
 * @author Sven Krosse
 * 
 */
public class Conjunction extends Criteria {

	/**
	 * {@inheritDoc}
	 */
	protected String getBooleanOperator() {
		return And.TOKEN;
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(ICriterion criterion) {
		if (criterion instanceof Conjunction) {
			for ( ICriterion c : ((Conjunction) criterion).getCriterions()){
				if ( !getCriterions().contains(c)){
					add(c);
				}
			}
		} else {
			super.add(criterion);
		}
	}

}
