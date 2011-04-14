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
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Class definition representing the instances axis.
 * <p>
 * This step computes all instances of the value.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InstancesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public InstancesAxis() {
		super(AxisInstances.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		Topic topic = null;
		/*
		 * check if construct is a topic
		 */

		if (source instanceof Topic) {
			topic = (Topic) source;
		}
		/*
		 * check if it is special string tm:subject
		 */
		else if (TmdmSubjectIdentifier.isTmdmSubject(source)) {
			set.addAll(context.getQuery().getTopicMap().getTopics());
			return set;
		} else {
			throw new InvalidValueException();
		}

		/*
		 * get instances by index
		 */
		TypeInstanceIndex index = context.getQuery().getTopicMap().getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		set.addAll(index.getTopics(topic));

		if (context.isTransitive()) {
			SubtypesAxis axis = new SubtypesAxis();
			Collection<Topic> subtypes = (Collection<Topic>) axis.navigate(context, topic, null);
			for (Topic subtype : subtypes) {
				set.addAll(index.getTopics(subtype));
			}
		}

		return set;
	}
}
