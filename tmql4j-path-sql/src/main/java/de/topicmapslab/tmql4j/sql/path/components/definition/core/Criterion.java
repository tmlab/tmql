/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import de.topicmapslab.tmql4j.sql.path.components.definition.model.ICriterion;

/**
 * @author Sven Krosse
 * 
 */
public class Criterion implements ICriterion {

	private final String criterion;

	/**
	 * constructor
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public Criterion(String criterion) {
		this.criterion = criterion;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return criterion;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if ( obj instanceof Criterion){
			return this.criterion.equalsIgnoreCase(((Criterion) obj).criterion);
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return this.criterion.hashCode();
	}

}
