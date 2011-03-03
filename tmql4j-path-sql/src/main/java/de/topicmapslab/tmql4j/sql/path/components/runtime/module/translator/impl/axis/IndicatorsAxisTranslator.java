/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

/**
 * @author Sven Krosse
 * 
 */
public class IndicatorsAxisTranslator extends IdentityAxisTranslator {

	static final String RELATION = "rel_subject_identifiers";
	static final String COLUMN = "id_topic";

	/**
	 * {@inheritDoc}
	 */
	protected String getRelationColumn() {
		return COLUMN;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getRelationTable() {
		return RELATION;
	}

}
