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

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisOccurrences;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the occurrences axis.
 * <p>
 * If the value is a topic item, this step computes all occurrences of that topic which are subtypes of the optionally
 * specified item. The result is a sequence of occurrence items.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class OccurrencesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public OccurrencesAxis() {
		super(AxisOccurrences.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Construct.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object construct, Topic optional) throws TMQLRuntimeException {
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * check if optional type is defined
			 */
			if (optional != null) {
				/*
				 * optional item is a topic
				 */
				if (optional instanceof Topic) {
					Topic type = (optional);
					set.addAll(topic.getOccurrences(type));
				}
			}
			/*
			 * non optional item
			 */
			else {
				set.addAll(topic.getOccurrences());
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
