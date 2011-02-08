/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.orderBy;

import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.lexical.Desc;

/**
 * @author Sven Krosse
 * 
 */
public class OrderBy {

	private final String content;
	private boolean asc;

	public OrderBy(final String content) {
		this(content, true);
	}

	public OrderBy(final String content, boolean asc) {
		this.content = content;
		this.asc = asc;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return content + " " + (asc ? Asc.TOKEN : Desc.TOKEN);
	}

}
