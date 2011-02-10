/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.where;

import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class Union extends SqlDefinition {

	private List<ISqlDefinition> unions;

	/**
	 * constructor
	 * 
	 * @param def
	 *            the definition to clone
	 */
	public Union(SqlDefinition def) {
		super(def);
	}

	/**
	 * constructor
	 */
	public Union() {

	}

	/**
	 * Adds a new union part to this definition
	 * 
	 * @param definition
	 *            the new union part
	 */
	public void union(ISqlDefinition definition) {
		if (unions == null) {
			unions = HashUtil.getList();
		}
		unions.add(definition);
	}

	/**
	 * @return the unions
	 */
	private List<ISqlDefinition> getUnions() {
		if (unions == null) {
			return Collections.emptyList();
		}
		return unions;
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
		for (ISqlDefinition definition : getUnions()) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(de.topicmapslab.tmql4j.path.grammar.lexical.Union.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(definition.toString());
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
		}
		return builder.toString();
	}
}
