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
import java.util.HashSet;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the traverse axis.
 * <p>
 * If the value is a topic item, this step computes first all associations where the topic plays a role. The type of the
 * associations is constrained by the optional item. The overall result of this navigation is then a sequence of all
 * players of these associations, whereby the incoming topic is deducted once from that sequence. In backward direction
 * the result sequence will always be empty.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TraverseAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public TraverseAxis() {
		super(AxisTraverse.class);
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
			Topic topic = (Topic) source;
			TopicMap map = topic.getTopicMap();
			Set<Association> associations = new HashSet<Association>();

			/*
			 * iterate over all associations and extract associations played by given topic
			 */
			for (Association a : map.getAssociations()) {
				if (type == null || matches(context, a, type)) {
					for (Role role : a.getRoles()) {
						if (role.getPlayer().equals(topic)) {
							associations.add(a);
							break;
						}
					}
				}
			}
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = HashUtil.getHashSet();

			/*
			 * iterate over all associations and extract players
			 */
			for (Association association : associations) {
				for (Role role : association.getRoles()) {
					if (!role.getPlayer().equals(topic)) {
						set.add(role.getPlayer());
					}
				}
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
