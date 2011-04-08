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
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.draft2011.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.ITypeHierarchyNavigationAxis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubtypes;

/**
 * Class definition representing the subtypes axis.
 * <p>
 * In forward direction this step computes all subtypes of the value.
 * </p>
 * <p>
 * In backward direction this step produces all supertypes of the value. The
 * optional item has no relevance.
 * </p>
 * <p>
 * <b>Additional Notation:</b> Asking for all subtypes of an item is the inverse
 * of asking for all supertypes
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubtypesNavigationAxis extends BaseNavigationAxisImpl implements ITypeHierarchyNavigationAxis {

	/**
	 * instance of redirected axis
	 */
	private final SupertypesNavigationAxis axis = new SupertypesNavigationAxis();

	/**
	 * base constructor to create an new instance
	 */
	public SubtypesNavigationAxis() {
		super(AxisSubtypes.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		if (optional == null || optional instanceof Construct) {
			return axis.navigateForward(construct, (Construct) optional);
		} else {
			return new LinkedList<Topic>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		if (optional == null || optional instanceof Construct) {
			return axis.navigateBackward(construct, (Construct) optional);
		} else {
			return new LinkedList<Topic>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (optional == null || optional instanceof Construct) {
			return axis.supportsForwardNavigation(construct, (Construct) optional);
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		if (optional == null || optional instanceof Construct) {
			return axis.supportsBackwardNavigation(construct, (Construct) optional);
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTransitivity(boolean transitivity) {
		this.axis.setTransitivity(transitivity);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopicMap(TopicMap topicMap) {
		super.setTopicMap(topicMap);
		axis.setTopicMap(topicMap);
	}

}
