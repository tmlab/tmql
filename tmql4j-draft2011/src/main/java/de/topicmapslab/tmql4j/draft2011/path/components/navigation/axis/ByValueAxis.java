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
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.index.LiteralIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByValue;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the value axis.
 * <p>
 * If the value is a string, this step returns all names, variants and occurrences having this value.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ByValueAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ByValueAxis() {
		super(AxisByValue.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {

		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		/*
		 * check if construct is an occurrence or a variant
		 */
		if (source instanceof String) {
			TopicMap topicMap = context.getQuery().getTopicMap();
			LiteralIndex index = topicMap.getIndex(LiteralIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			set.addAll(index.getNames((String) source));
			set.addAll(index.getOccurrences((String) source));
			if (type == null) {
				set.addAll(index.getVariants((String) source));
			} else {
				Collection<Object> set_ = new LinkedList<Object>();
				for (Object o : set) {
					if (matches(context, (Typed) o, type)) {
						set_.add(o);
					}
				}
				return set_;
			}
			return set;
		}
		throw new InvalidValueException();
	}
}
