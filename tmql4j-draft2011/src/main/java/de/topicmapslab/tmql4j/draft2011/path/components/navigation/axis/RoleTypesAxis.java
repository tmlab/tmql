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

import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisRoleTypes;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the roles axis.
 * <p>
 * If the value is an association item, this step computes all role-typing topics. Multiple uses of the same role type
 * in one association causes multiple results. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class RoleTypesAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public RoleTypesAxis() {
		super(AxisRoleTypes.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * check if construct is an association
		 */
		if (source instanceof Association) {
			Association association = (Association) source;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Topic> set = new LinkedList<Topic>();
			for (Topic rt : association.getRoleTypes()) {
				if (type != null) {
					if (matches(context, rt, type)) {
						set.add(rt);
					}
				} else {
					set.add(rt);
				}
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
