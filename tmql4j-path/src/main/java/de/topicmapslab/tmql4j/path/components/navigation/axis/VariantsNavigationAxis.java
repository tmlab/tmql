/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisVariants;

/**
 * Class definition representing the characteristics axis.
 * <p>
 * If the value is a topic item, in forward direction this step computes all
 * names and occurrences of that topic which are subtypes of the optionally
 * specified item. The result is a sequence of name and occurrence items.
 * </p>
 * <p>
 * If the value is a name or an occurrence item, in backward direction this step
 * computes the topic to which the name or the occurrence is attached. The
 * optional item can be used to constrain the type of the items one is
 * interested in.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class VariantsNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public VariantsNavigationAxis() {
		super(AxisVariants.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Name.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Variant.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();

		/*
		 * check if construct is a variant
		 */
		if (construct instanceof Variant) {
			Variant v = (Variant) construct;
			if (optional != null && optional instanceof Topic) {
				if (v.getParent().getType().equals(((Topic) optional))) {
					set.add(v.getParent());
				}
			} else {
				set.add(v.getParent());
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Name) {
			Name name = (Name) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Variant> set = new LinkedList<Variant>(name.getVariants());
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		return construct instanceof Variant;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		return construct instanceof Name;
	}

}
