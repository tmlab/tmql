/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

/**
 * @author Sven Krosse
 * 
 */
public class LocatorsAxisTranslator extends IdentityAxisTranslator {

	static final String COLUMN = "id_topic";
	static final String RELATION = "rel_subject_locators";

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
