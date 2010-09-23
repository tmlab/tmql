/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.resultprocessing.core.reduction.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Node {

	private final Path path;
	private final Set<Node> nodes = new HashSet<Node>();

	protected Node() {
		path = new Path();
	}

	public Node(final Path parent, final Object entry) {
		path = new Path(parent, entry);
	}

	public void addNodes(final List<Object> data) {
		if (!data.isEmpty()) {
			Object d = data.iterator().next();
			List<Object> data_ = new LinkedList<Object>();
			if (data.size() > 1) {
				data_.addAll(data.subList(1, data.size()));
			}
			if (d instanceof Collection<?>) {
				for (Object entry : (Collection<?>) d) {
					Node node = new Node(this.path, entry);
					if (!data_.isEmpty()) {
						node.addNodes(data_);
					}
					nodes.add(node);
				}
			} else {
				Node node = new Node(this.path, d);
				if (!data_.isEmpty()) {
					node.addNodes(data_);
				}
				nodes.add(node);
			}

		}
	}

	public Path getPath() {
		return path;
	}

	public Collection<Path> pathToLeaf() {
		Collection<Path> paths = new LinkedList<Path>();
		if (nodes.isEmpty()) {
			paths.add(path);
		} else {
			for (Node node : nodes) {
				paths.addAll(node.pathToLeaf());
			}
		}
		return paths;
	}

}
