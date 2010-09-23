/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.resultprocessing.core.reduction.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Tree {

	private final Root root;

	public Tree() {
		root = new Root();
	}

	public void addNodes(final List<Object> data) {
		root.addNodes(data);
	}
	
	public void addNodes(final Object... data) {
		root.addNodes(Arrays.asList(data));
	}

	public Collection<Path> pathToLeaf() {
		return root.pathToLeaf();
	}

}
