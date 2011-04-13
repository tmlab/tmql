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
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisBySubjectIdentifier;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the by-subject-identifiers axis.
 * <p>
 * If the value is an IRI this step produces the topic which has this IRI as subject indicator. The optional item has no
 * relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BySubjectIdentifierAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public BySubjectIdentifierAxis() {
		super(AxisBySubjectIdentifier.class);
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
		 * check if construct is a locator
		 */
		if (source instanceof Locator) {
			TopicMap map = context.getQuery().getTopicMap();
			Locator locator = (Locator) source;
			/*
			 * get topic by subject-identifier
			 */
			Construct c = map.getTopicBySubjectIdentifier(locator);
			if (c != null) {
				set.add(c);
			}
			return set;
		}
		/*
		 * check if construct is a topic
		 */
		else if (source instanceof Topic) {
			set.add(source);
			return set;
		}
		/*
		 * check if construct is a string
		 */
		else if (source instanceof String) {
			TopicMap map = context.getQuery().getTopicMap();
			Locator locator = map.createLocator((String) source);
			Construct c = map.getTopicBySubjectIdentifier(locator);
			if (c != null) {
				set.add(c);
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
