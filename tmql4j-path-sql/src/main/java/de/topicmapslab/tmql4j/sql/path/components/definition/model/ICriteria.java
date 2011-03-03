/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.model;

/**
 * A multiple dimensional {@link ICriterion} like conjunction or disjunctions
 * 
 * @author Sven Krosse
 * 
 */
public interface ICriteria extends ICriterion {

	/**
	 * Adding a new criterion to the criteria
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public void add(ICriterion criterion);
	
	/**
	 * Adding a new criterion to the criteria. The string will be transformed to a criterion object
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public void add(String criterion);
}
