/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.model;

/**
 * Interface definition of a TMQL axis specified by the current TMQL draft.
 * <p>
 * Each navigation step is interpreted within the effective map . Navigational
 * axes are derived from the structure of a Topic Map instance [TMDM] and can
 * either be followed in forward (>>) or in backward (<<) direction:
 * </p>
 * <p>
 * The optional anchor adds control information which is useful with some axes,
 * but not others. If it is missing tm:subject will be assumed.
 * <p>
 * </p>
 * When the anchor is evaluated, it must evaluate to a topic item and is
 * interpreted as type. Then in all navigation steps the current setting for
 * type transitivity is honored. </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ITypeHierarchyNavigationAxis extends INavigationAxis {

	/**
	 * Set the transitivity handling for the given axis.
	 * 
	 * @param transitivity
	 *            the transitivity to set
	 */
	public void setTransitivity(boolean transitivity);
}
