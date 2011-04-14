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
import org.tmapi.core.Typed;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition representing the types axis.
 * <p>
 * This step computes all types of the value.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TypesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public TypesAxis() {
		super(AxisTypes.class);
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
		Collection<Topic> topics = HashUtil.getHashSet();
		/*
		 * check if construct is a topic
		 */
		if (source instanceof Topic) {
			topics.addAll(((Topic) source).getTypes());
		}
		/*
		 * check if construct is a name, occurrence or association
		 */
		else if (source instanceof Typed) {
			topics.add(((Typed) source).getType());
		} else {
			throw new InvalidValueException();
		}

		Collection<Topic> set = HashUtil.getHashSet();
		set.addAll(topics);

		if (context.isTransitive()) {
			SupertypesAxis axis = new SupertypesAxis();
			for (Topic t : topics) {
				set.addAll((Collection<Topic>) axis.navigate(context, t, null));
			}
		}

		return set;

	}

}
