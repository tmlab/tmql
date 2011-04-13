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
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition representing the typed axis.
 * <p>
 * In forward direction this step computes all typed of the topic type.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TypedAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public TypedAxis() {
		super(AxisTyped.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			TypeInstanceIndex index = context.getQuery().getTopicMap().getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			if (!index.isAutoUpdated()) {
				index.reindex();
			}
			Collection<Typed> list = HashUtil.getHashSet();
			list.addAll(index.getAssociations(topic));
			list.addAll(index.getNames(topic));
			list.addAll(index.getRoles(topic));
			list.addAll(index.getOccurrences(topic));
			return list;
		}
		throw new NavigationException("Anchor type '" + source.getClass().getSimpleName() + "' not supported by the typed axis.");
	}
}
