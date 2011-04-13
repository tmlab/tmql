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

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the supertypes axis.
 * <p>
 * This step computes all supertypes of the value.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SupertypesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public SupertypesAxis() {
		super(AxisSupertypes.class);
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
			/*
			 * extract all super-types
			 */
			return SubtypesAxis.lookup(context, (Topic) source, true);
		}
		throw new InvalidValueException();
	}
}
