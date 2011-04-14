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
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisById;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the by-id axis.
 * <p>
 * <p>
 * If the value is a string this step returns the construct with this id.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ByIdAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ByIdAxis() {
		super(AxisById.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * check if anchor is a construct
		 */
		if (source instanceof String) {
			Collection<Object> set = new LinkedList<Object>();
			Construct c = context.getQuery().getTopicMap().getConstructById(source.toString());
			if (c != null) {
				set.add(c);
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
