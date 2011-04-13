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

import org.tmapi.core.Reifiable;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReified;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the reifier axis.
 * <p>
 * If the value is a topic item, this steps finds the association, name or occurrence item which is reified by this
 * topic. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReifiedAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ReifiedAxis() {
		super(AxisReified.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		if (source instanceof Topic) {
			Topic reifier = (Topic) source;
			/*
			 * get reified-construct
			 */
			Reifiable reifiable = reifier.getReified();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * add reified element
			 */
			if (reifiable != null) {
				set.add(reifiable);
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
