/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Topic;
import org.tmapi.index.ScopedIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisScoped;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the scoped axis.
 * <p>
 * This navigation leads from a topic to all associations, names, occurrences and variants in that scope. The optional
 * item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ScopedAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ScopedAxis() {
		super(AxisScoped.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * check if construct is a topic
		 */
		if (source instanceof Topic) {
			Topic scope = (Topic) source;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			ScopedIndex index = context.getQuery().getTopicMap().getIndex(ScopedIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * check if topic is theme of an association
			 */
			set.addAll(index.getAssociations(scope));
			/*
			 * check if topic is theme of a name
			 */
			set.addAll(index.getNames(scope));
			/*
			 * check if topic is theme of an occurrence
			 */
			set.addAll(index.getOccurrences(scope));
			/*
			 * check if topic is theme of a variant
			 */
			set.addAll(index.getVariants(scope));

			return set;
		}
		throw new InvalidValueException();
	}

}
