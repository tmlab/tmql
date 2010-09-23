/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.resultprocessing.core.reduction.tree;

import java.util.LinkedList;

public class Path extends LinkedList<Object> {

	private static final long serialVersionUID = 1L;

	protected Path() {

	}

	public Path(Object entry) {
		add(entry);
	}

	public Path(Path path, Object entry) {
		addAll(path);
		add(entry);
	}
}
