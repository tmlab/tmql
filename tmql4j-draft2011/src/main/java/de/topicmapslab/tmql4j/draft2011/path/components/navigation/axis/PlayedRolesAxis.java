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

import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayedRoles;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the roles axis.
 * <p>
 * If the value is an topic item, in forward direction this step computes all role items played by this topic.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PlayedRolesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public PlayedRolesAxis() {
		super(AxisPlayedRoles.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Role> set = HashUtil.getHashSet();
			for (Role role : topic.getRolesPlayed()) {
				if (type != null) {
					if (matches(context, role, type)) {
						set.add(role);
					}
				} else {
					set.add(role);
				}
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
