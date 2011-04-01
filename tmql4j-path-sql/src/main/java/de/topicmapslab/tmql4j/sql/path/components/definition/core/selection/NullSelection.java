/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.selection;

import de.topicmapslab.tmql4j.path.grammar.lexical.Null;

/**
 * @author Sven Krosse
 *
 */
public class NullSelection extends Selection {

	/**
	 * hidden constructor
	 */
	private NullSelection() {
		super(Null.TOKEN);
	}
	
	private static NullSelection instance;
	
	/**
	 * Returns the singleton instance of the NULL selection
	 * @return the instance
	 */
	public static NullSelection getNullSelection(){
		if ( instance == null ){
			instance = new NullSelection();
		}
		return instance;
	}

}
