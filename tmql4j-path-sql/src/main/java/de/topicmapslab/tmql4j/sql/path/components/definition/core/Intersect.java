/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 *
 */
public class Intersect extends SqlDefinition {

	private List<ISqlDefinition> intersects;
	
	/**
	 * constructor
	 */
	public Intersect(SqlDefinition def) {
		super(def);
	}
	
	/**
	 * constructor
	 */
	public Intersect() {
		
	}

	/**
	 * Adds a new intersect part to this definition
	 * @param definition the new intersect part
	 */
	public void intersect(ISqlDefinition definition){
		if ( intersects == null ){
			intersects = HashUtil.getList();
		}
		intersects.add(definition);
	}
	
	/**
	 * @return the unions
	 */
	private List<ISqlDefinition> getUnions() {
		if ( intersects == null ){
			return Collections.emptyList();
		}
		return intersects;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		/*
		 * transform this SQL definition to string
		 */
		StringBuilder builder = new StringBuilder(super.toString());
		/*
		 * add union parts
		 */
		for ( ISqlDefinition definition : getUnions()){
			builder.append(WS);
			builder.append(de.topicmapslab.tmql4j.path.grammar.lexical.Intersect.TOKEN);
			builder.append(WS);
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(WS);
			builder.append(definition.toString());
			builder.append(WS);
			builder.append(BracketRoundClose.TOKEN);
		}		
		return builder.toString();
	}
}
